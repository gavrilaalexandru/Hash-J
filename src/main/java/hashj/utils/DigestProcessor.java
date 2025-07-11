package hashj.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestProcessor {
    private static final ThreadLocal<MessageDigest> MD5_DIGEST =
            ThreadLocal.withInitial(() -> {
                try {
                    return MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            });

    private static final ThreadLocal<MessageDigest> SHA1_DIGEST =
            ThreadLocal.withInitial(() -> {
                try {
                    return MessageDigest.getInstance("SHA-1");
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            });

    private static final ThreadLocal<MessageDigest> SHA256_DIGEST =
            ThreadLocal.withInitial(() -> {
                try {
                    return MessageDigest.getInstance("SHA-256");
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            });

    public static String hash(String password, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest md = switch (algorithm.toUpperCase()) {
            case "MD5" -> MD5_DIGEST.get();
            case "SHA-1" -> SHA1_DIGEST.get();
            case "SHA-256" -> SHA256_DIGEST.get();
            default -> MessageDigest.getInstance(algorithm);
        };

        md.reset();

        byte[] digest = md.digest(password.getBytes());

        StringBuilder sb = new StringBuilder(digest.length * 2);
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
