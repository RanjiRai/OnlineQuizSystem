package BackEnd.User;

import Utils.EncryptionUtil;
import Utils.SessionManager;

public class AuthController {

    /**
     * Authenticates a user by verifying the provided username and password.
     *
     * @param username The username to authenticate.
     * @param password The password to verify.
     * @return True if authentication is successful, otherwise false.
     */
    public static boolean authenticateUser(String username, String password) {
        // Fetch stored password hash from DB
        String storedHash = UserDAO.getUserPassword(username);

        if (storedHash == null) {
            System.err.println("⚠ User not found: " + username);
            return false;
        }

        // Debugging: Print retrieved hash (Remove in production)
        System.out.println("🔹 Stored Hash: " + storedHash);

        // Verify the password
        boolean isValid = EncryptionUtil.verifyPassword(password, storedHash);

        if (isValid) {
            SessionManager.loginUser(username);  // Store user session
            System.out.println("✅ User authenticated successfully: " + username);
            return true;
        } else {
            System.err.println("❌ Authentication failed for user: " + username);
            return false;
        }
    }

    /**
     * Registers a new user by hashing the password and storing it in the database.
     *
     * @param username The username to register.
     * @param password The password to hash and store.
     * @return True if registration is successful, otherwise false.
     */
    public static boolean registerUser(String username, String password) {
        // Ensure the username is unique
        if (UserDAO.getUserPassword(username) != null) {
            System.err.println("⚠ Username already exists: " + username);
            return false;
        }

        // Hash the password
        String hashedPassword = EncryptionUtil.hashPassword(password);
        if (hashedPassword == null) {
            System.err.println("❌ Password hashing failed for user: " + username);
            return false;
        }

        // Debugging: Print hashed password (Remove in production)
        System.out.println("🔹 Hashed Password: " + hashedPassword);

        // Insert user into the database
        boolean success = UserDAO.createUser(username, hashedPassword);
        if (success) {
            System.out.println("✅ User registered successfully: " + username);
        } else {
            System.err.println("❌ User registration failed: " + username);
        }
        return success;
    }
}
