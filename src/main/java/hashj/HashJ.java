package hashj;

import hashj.engines.BruteForceEngine;
import java.util.Scanner;

public class HashJMain {
    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)) { // validating user input
            System.out.print("Target hash: ");
            String targetHash = scanner.nextLine(); // can be any string, no restrictions

            System.out.print("Hash algorithm (MD5, SHA=1, SHA-256): ");
            String algorithm = scanner.nextLine(); // should be one from a predefined list (enum)

            System.out.print("Wordlist path: ");
            String wordlistPath = scanner.nextLine(); // can be any string, no restrictions

            BruteForceEngine bruteForceEngine = new BruteForceEngine(targetHash, algorithm, wordlistPath);
            bruteForceEngine.crack();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    } // end main method
} // end class
