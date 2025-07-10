package hashj.engines;

import hashj.models.AnsiColor;
import hashj.utils.DigestProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Stream;

public class BruteForceEngine {
    private final String targetHash;
    private final String algorithm;
    private final String wordlistPath;
    private final int noThreads;

    public BruteForceEngine(String targetHash, String algorithm, String wordlistPath, int noThreads) {
        this.targetHash = targetHash;
        this.algorithm = algorithm;
        this.wordlistPath = wordlistPath;
        this.noThreads = noThreads;
    }

    public long countLines() {
        try(Stream<String> lines = Files.lines(Paths.get(wordlistPath))) {
            return lines.count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void crack() {
        try(BufferedReader br = new BufferedReader(new FileReader(wordlistPath))) {
            AnsiColor.CLEAR_SCREEN.execute();
            System.out.println(AnsiColor.YELLOW + "Starting brute force...");
            long startTime = System.currentTimeMillis();

            String password;
            long lines = countLines();
            long currentLine = 0;

            while ((password = br.readLine()) != null) {
                currentLine++;
                double progress = (double) (currentLine * 100) / lines;
                System.out.printf(AnsiColor.CYAN + "[*] Trying: %s (%.2f%%)\n" + AnsiColor.RESET, password, progress);

                String hashedPassword = DigestProcessor.hash(password, algorithm);
                if (hashedPassword.equalsIgnoreCase(targetHash)) {
                    long endTime = System.currentTimeMillis();
                    System.out.println(AnsiColor.GREEN + "Password found: " + password);
                    System.out.printf("Elapsed time: %.2f seconds\n" + AnsiColor.RESET, (endTime - startTime) / 1000.0);
                    return;
                }
            }
            long endTime = System.currentTimeMillis();
            System.out.println(AnsiColor.RED + "Password not found");
            System.out.printf("Elapsed time: %.2f seconds\n" + AnsiColor.RESET, (endTime - startTime) / 1000.0);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
} // end class
