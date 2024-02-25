import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordManager {
    private static PasswordManager passwordManager;
    private PasswordManager()
    {

    }
    public static PasswordManager getInstance()
    {
        if(passwordManager == null)
            passwordManager = new PasswordManager();

        return passwordManager;
    }

    public String generateSalt() {
        byte[] saltBytes = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(saltBytes);

        StringBuilder salt = new StringBuilder();
        for (byte b : saltBytes) {
            salt.append(String.format("%02x", b));
        }

        return salt.toString();
    }

    public String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        String passwordWithSalt = password + salt;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = digest.digest(passwordWithSalt.getBytes());

        StringBuilder hashedPassword = new StringBuilder();
        for (byte b : hashedBytes) {
            hashedPassword.append(String.format("%02x", b));
        }

        return hashedPassword.toString();
    }

    public boolean verifyPassword(String enteredPassword, String storedHash, String salt) throws NoSuchAlgorithmException {
        String enteredPasswordHash = hashPassword(enteredPassword, salt);
        return enteredPasswordHash.equals(storedHash);
    }
}