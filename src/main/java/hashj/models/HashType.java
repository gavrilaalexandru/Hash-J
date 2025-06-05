package hashj.models;

public enum HashType {
    MD5("MD5"),
    SHA1("SHA-1"),
    SHA256("SHA-256");

    private final String value;

    HashType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static boolean isValid(String userInput) {
        for (HashType ht : HashType.values()) {
            if (ht.name().equalsIgnoreCase(userInput)) {
                return true;
            }
        }
        return false;
    }

    public static HashType fromString(String algorithm) {
        for (HashType ht : HashType.values()) {
            if (ht.value.equalsIgnoreCase(algorithm)) {
                return ht;
            }
        }
        throw new IllegalArgumentException("Invalid algorithm: " + algorithm);
    }
}
