package hashj.engines;

import hashj.models.AnsiColor;
import hashj.utils.DigestProcessor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class BruteForceEngine {
    private final String targetHash;
    private final String algorithm;
    private final String wordlistPath;
    private final int noThreads;

    // thread safe
    private final AtomicBoolean found = new AtomicBoolean(false);
    private final AtomicLong checkedPasswords = new AtomicLong(0);
    private final AtomicLong totalPasswords = new AtomicLong(0);
    private volatile String foundPassword = null;
    private volatile long startTime;

    // UI
    private volatile String[] currentPasswords;
    private final Object displayLock = new Object();

    public BruteForceEngine(String targetHash, String algorithm, String wordlistPath, int noThreads) {
        this.targetHash = targetHash;
        this.algorithm = algorithm;
        this.wordlistPath = wordlistPath;
        this.noThreads = Math.max(1, noThreads);
        this.currentPasswords = new String[noThreads];
    }

    public long countLines() {
        try (Stream<String> lines = Files.lines(Paths.get(wordlistPath), StandardCharsets.UTF_8)) {
            return lines.count();
        } catch (IOException e) {
            throw new RuntimeException("Failed to count wordlist lines: " + e.getMessage());
        }
    }

    public void crack() {
        AnsiColor.CLEAR_SCREEN.execute();
        printHeader();

        this.startTime = System.currentTimeMillis();
        this.totalPasswords.set(countLines());

        ExecutorService executor = Executors.newFixedThreadPool(noThreads + 2); // +2 for UI and reader
        BlockingQueue<String> passwordQueue = new LinkedBlockingQueue<>(noThreads * 10);

        Future<?> readerTask = executor.submit(() -> readPasswords(passwordQueue));
        Future<?> uiTask = executor.submit(this::updateUI);

        CompletableFuture<?>[] workers = new CompletableFuture[noThreads];
        for (int i = 0; i < noThreads; i++) {
            final int threadId = i;
            workers[i] = CompletableFuture.runAsync(() -> processPasswords(passwordQueue, threadId), executor);
        }

        try {
            readerTask.get();
            for (int i = 0; i < noThreads; i++) {
                passwordQueue.offer("__POISON_PILL__");
            }
            CompletableFuture.allOf(workers).get();
        } catch (ExecutionException | InterruptedException e) {
            System.err.println(AnsiColor.RED + "Thread execution error: " + e.getMessage() + AnsiColor.RESET);
        } finally {
            uiTask.cancel(true);
            while (!uiTask.isDone() && !executor.isShutdown()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            executor.shutdown();
            try {
                if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        printResults();
    }

    private void printHeader() {
        System.out.print("\033[2J\033[1;1H"); // Clear screen and move cursor to top
        System.out.println(AnsiColor.CYAN + "╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    HashJ - Brute Force Attack                 ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Target Hash: %-48s ║%n", targetHash.length() > 48 ? targetHash.substring(0, 45) + "..." : targetHash);
        System.out.printf("║ Algorithm:   %-48s ║%n", algorithm);
        System.out.printf("║ Threads:     %-48d ║%n", noThreads);
        System.out.printf("║ Wordlist:    %-48s ║%n", Paths.get(wordlistPath).getFileName().toString());
        System.out.println("╚═══════════════════════════════════════════════════════════════╝" + AnsiColor.RESET);
        System.out.println();
    }

    private void readPasswords(BlockingQueue<String> queue) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(wordlistPath), StandardCharsets.UTF_8))) {
            String password;
            while ((password = br.readLine()) != null && !found.get()) {
                try {
                    queue.put(password);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println(AnsiColor.RED + "File error: " + e.getMessage() + AnsiColor.RESET);
        }
    }

    private void processPasswords(BlockingQueue<String> queue, int threadId) {
        try {
            while (!found.get()) {
                String password = queue.take();
                if ("__POISON_PILL__".equals(password)) {
                    break;
                }

                currentPasswords[threadId] = password;

                try {
                    String hashedPassword = DigestProcessor.hash(password, algorithm);
                    checkedPasswords.incrementAndGet();

                    if (hashedPassword.equalsIgnoreCase(targetHash)) {
                        if (found.compareAndSet(false, true)) {
                            foundPassword = password;
                        }
                        break;
                    }
                } catch (NoSuchAlgorithmException e) {
                    System.err.println(AnsiColor.RED + "Invalid algorithm: " + algorithm + AnsiColor.RESET);
                    break;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            currentPasswords[threadId] = null;
        }
    }

    private void updateUI() {
        try {
            while (!found.get()) {
                synchronized (displayLock) {
                    System.out.print("\033[2J\033[1;1H"); // Clear screen and reset cursor
                    long checked = checkedPasswords.get();
                    long total = totalPasswords.get();
                    double progress = total > 0 ? (double) checked / total * 100 : 0;
                    long elapsed = System.currentTimeMillis() - startTime;
                    double rate = elapsed > 0 ? checked / (elapsed / 1000.0) : 0;

                    printProgressBar(progress, checked, total, rate);
                    printThreadStatus();
                    printStats(elapsed, rate);

                    System.out.flush();
                }
                Thread.sleep(300); // Reduced update frequency for stability
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void printProgressBar(double progress, long checked, long total, double rate) {
        int barWidth = 50;
        int filled = (int) (progress / 100 * barWidth);

        System.out.print("\033[2K"); // Clear current line
        System.out.print(AnsiColor.YELLOW + "Progress: [");
        System.out.print(AnsiColor.GREEN + "█".repeat(filled));
        System.out.print(AnsiColor.DARK_GRAY + "░".repeat(barWidth - filled));
        System.out.printf(AnsiColor.YELLOW + "] %.1f%%", progress);
        System.out.printf(" (%,d/%,d)%n", checked, total);
        System.out.print("\033[2K"); // Clear current line
        System.out.printf("Speed: %s%.0f passwords/sec%s%n%n", AnsiColor.CYAN, rate, AnsiColor.RESET);
    }

    private void printThreadStatus() {
        System.out.print("\033[2K"); // Clear current line
        System.out.println(AnsiColor.BLUE + "Thread Status:" + AnsiColor.RESET);
        for (int i = 0; i < noThreads; i++) {
            String current = currentPasswords[i];
            System.out.print("\033[2K"); // Clear current line
            if (current != null) {
                System.out.printf("  Thread %d: %s%-20s%s%n",
                        i + 1, AnsiColor.WHITE,
                        current.length() > 20 ? current.substring(0, 17) + "..." : current,
                        AnsiColor.RESET);
            } else {
                System.out.printf("  Thread %d: %s%-20s%s%n",
                        i + 1, AnsiColor.DARK_GRAY, "idle", AnsiColor.RESET);
            }
        }
        System.out.println();
    }

    private void printStats(long elapsed, double rate) {
        System.out.print("\033[2K"); // Clear current line
        System.out.println(AnsiColor.MAGENTA + "Statistics:" + AnsiColor.RESET);
        System.out.print("\033[2K"); // Clear current line
        System.out.printf("  Elapsed: %s%.2f seconds%s%n", AnsiColor.WHITE, elapsed / 1000.0, AnsiColor.RESET);
        System.out.print("\033[2K"); // Clear current line
        System.out.printf("  Average: %s%.0f passwords/sec%s%n", AnsiColor.WHITE, rate, AnsiColor.RESET);

        if (rate > 0 && totalPasswords.get() > checkedPasswords.get()) {
            long remaining = totalPasswords.get() - checkedPasswords.get();
            double eta = remaining / rate;
            System.out.print("\033[2K"); // Clear current line
            System.out.printf("  ETA: %s%s%s%n", AnsiColor.WHITE, formatTime(eta), AnsiColor.RESET);
        } else {
            System.out.print("\033[2K"); // Clear current line
            System.out.printf("  ETA: %s%s%s%n", AnsiColor.WHITE, "N/A", AnsiColor.RESET);
        }
    }

    private void printResults() {
        System.out.print("\033[2J\033[1;1H"); // Clear screen and move cursor to top
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        long checked = checkedPasswords.get();
        double rate = duration > 0 ? checked / (duration / 1000.0) : 0;

        if (found.get()) {
            System.out.println(AnsiColor.GREEN + """
            ╔══════════════════════════════════════╗
            ║            SUCCESS!                  ║
            ╚══════════════════════════════════════╝
            """ + AnsiColor.RESET);
            System.out.printf("%s Password found: %s%s%s%n%n",
                    AnsiColor.GREEN, AnsiColor.BOLD, foundPassword, AnsiColor.RESET);
        } else {
            System.out.println(AnsiColor.RED + """
            ╔══════════════════════════════════════╗
            ║          NOT FOUND                   ║
            ╚══════════════════════════════════════╝
            """ + AnsiColor.RESET);
            System.out.printf("%s Password not found in wordlist%s%n%n",
                    AnsiColor.RED, AnsiColor.RESET);
        }

        System.out.println(AnsiColor.CYAN + "Final Statistics:" + AnsiColor.RESET);
        System.out.printf("  Passwords tested: %s%,d%s%n", AnsiColor.WHITE, checked, AnsiColor.RESET);
        System.out.printf("  Total time: %s%.2f seconds%s%n", AnsiColor.WHITE, duration / 1000.0, AnsiColor.RESET);
        System.out.printf("  Average speed: %s%.0f passwords/sec%s%n", AnsiColor.WHITE, rate, AnsiColor.RESET);
        System.out.printf("  Threads used: %s%d%s%n", AnsiColor.WHITE, noThreads, AnsiColor.RESET);
    }

    private String formatTime(double seconds) {
        if (Double.isNaN(seconds) || seconds < 0) {
            return "calculating...";
        }

        if (seconds < 60) {
            return String.format("%.0fs", seconds);
        } else {
            int minutes = (int) (seconds / 60);
            return String.format("%dm", minutes);
        }
    }
}