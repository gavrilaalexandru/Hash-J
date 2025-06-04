package cracker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
                String hashedPassword;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

} // end class
