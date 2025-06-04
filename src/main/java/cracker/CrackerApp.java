package cracker;

import java.util.Scanner;

public class CrackerApp {
    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)) {
            System.out.print("Target hash: ");
            String targetHash = scanner.nextLine();

            System.out.print("Hash algorithm: ");
            String algorithm = scanner.nextLine();

            System.out.print("Wordlist path: ");
            String wordlistPath = scanner.nextLine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    } // end main method
} // end class
