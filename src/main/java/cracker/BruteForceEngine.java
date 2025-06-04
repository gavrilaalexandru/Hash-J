package cracker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class BruteForceEngine {
    private String targetHash;
    private String algorithm;
    private String wordlistPath;

    public BruteForceEngine(String targetHash, String algorithm, String wordlistPath) {
        this.targetHash = targetHash;
        this.algorithm = algorithm;
        this.wordlistPath = wordlistPath;
    }

    public void crack() {
        try(BufferedReader br = new BufferedReader(new FileReader(wordlistPath))) {
            String password;
            while ((password = br.readLine()) != null) {
                String hashedPassword = DigestEngine.hash(password, algorithm);
                if (hashedPassword.equalsIgnoreCase(targetHash)) {
                    System.out.print("Password found: " + password);
                    return;
                }
            }
            System.out.print("Password not found");
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

} // end class
