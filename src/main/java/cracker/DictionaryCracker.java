package cracker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class DictionaryCracker {
    private String targetHash;
    private String algorithm;
    private String wordlistPath;

    public DictionaryCracker(String targetHash, String algorithm, String wordlistPath) {
        this.targetHash = targetHash;
        this.algorithm = algorithm;
        this.wordlistPath = wordlistPath;
    }

    public void crack() {
        try(BufferedReader br = new BufferedReader(new FileReader(wordlistPath))) {
            String password;
            while ((password = br.readLine()) != null) {
                String hashedPassword = HashUtils.hash(password, algorithm);
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
