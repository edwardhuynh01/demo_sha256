import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileHelper {
    public static final String FILE_PATH = "./logs/pwd.txt";
    public static boolean addPasswordHashIfNotExists(String passwordHash, String name, String filePath) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        boolean nameExists = false;
        boolean passwordMatched = false;
        for (String line : lines) {
            String[] parts = line.split(":", 2);
            if (parts[0].equals(name)) {
                nameExists = true;
                if (parts[1].equals(passwordHash)) {
                    passwordMatched = true;
                }
                break;
            }
        }
        if (nameExists && !passwordMatched) {
            System.out.println("Wrong password......: " + PasswordHashRetriever(name));
            return false;
        } else if (!nameExists) {
            Files.write(Paths.get(filePath), (name + ":" + passwordHash + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("Hashed password was added to the file.");
            return true;
        } else {
            System.out.println("Database hashed pass: " + PasswordHashRetriever(name));
            return true;
        }
    }
    private static String PasswordHashRetriever(String name) throws Exception {
        String passwordHash = findPasswordHashByName(name, FILE_PATH);
        if (passwordHash != null) {
            return passwordHash;
        } else {
            return null;
        }
    }
    private static String findPasswordHashByName(String name, String filePath) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        for (String line : lines) {
            if (line.startsWith(name + ":")) {
                return line.split(":", 2)[1];
            }
        }
        return null;
    }
}

