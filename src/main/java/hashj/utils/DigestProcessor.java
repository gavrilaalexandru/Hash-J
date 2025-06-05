package hashj.utils;

import hashj.models.HashType;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestProcessor {
    public static String hash(String password, String algorithm) throws NoSuchAlgorithmException {
        HashType algo = HashType.fromString(algorithm);
        MessageDigest md = MessageDigest.getInstance(algo.getValue());
        byte[] digest = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
