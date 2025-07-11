package hashj;

import hashj.engines.BruteForceEngine;
import hashj.models.AnsiColor;

import java.util.Scanner;

public class HashJ {
    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)) { // validating user input
            AnsiColor.CLEAR_SCREEN.execute();
            printBanner();

            System.out.print(AnsiColor.YELLOW + "Target hash: " + AnsiColor.RESET);
            String targetHash = scanner.nextLine(); // can be any string, no restrictions

            System.out.print(AnsiColor.YELLOW + "Hash algorithm (MD5, SHA=1, SHA-256): " + AnsiColor.RESET);
            String algorithm = scanner.nextLine(); // should be one from a predefined list (enum)

            System.out.print(AnsiColor.YELLOW + "Wordlist path: " + AnsiColor.RESET);
            String wordlistPath = scanner.nextLine(); // can be any string, no restrictions

            System.out.print(AnsiColor.YELLOW + "Number of threads (default: " + Runtime.getRuntime().availableProcessors() + "): " + AnsiColor.RESET);
            String threadsInput = scanner.nextLine();
            int noThreads = threadsInput.isEmpty() ? Runtime.getRuntime().availableProcessors() : Integer.parseInt(threadsInput);
            BruteForceEngine bruteForceEngine = new BruteForceEngine(targetHash, algorithm, wordlistPath, noThreads);
            bruteForceEngine.crack();
        } catch (Exception e) {
            System.out.println(AnsiColor.RED + "Error: " + e.getMessage() + AnsiColor.RESET);;
        }
    } // end main method

    private static void printBanner() {
        System.out.println(AnsiColor.CYAN + """
                ╔══════════════════════════════════════╗
                ║                HashJ                 ║
                ║       Multithreaded Hash Cracker     ║
                ╚══════════════════════════════════════╝
                """ + AnsiColor.RESET);
    }
} // end class
