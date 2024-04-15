import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SHA256Hasher {
    public static String toHexString(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    public static String hashPassword(String password) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return toHexString(encodedhash);
    }
    public static void savePasswordHash(String passwordHash) throws Exception {
        Files.write(Paths.get(FileHelper.FILE_PATH), passwordHash.getBytes());
    }
}

