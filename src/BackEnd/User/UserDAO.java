package BackEnd.User;

import BackEnd.Database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    /**
     * Creates a new user in the database with a plain-text password.
     *
     * @param username The username to register.
     * @param password The plain-text password to be stored directly.
     * @return True if the user is successfully created, otherwise false.
     */
    public static boolean createUser(String username, String password) {
        String checkUserQuery = "SELECT 1 FROM users WHERE username = ?";
        String insertUserQuery = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkUserQuery)) {

            // Check if the username already exists
            checkStmt.setString(1, username);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    System.err.println("❌ User already exists: " + username);
                    return false; // Username already taken
                }
            }

            // Insert the user into the database (without hashing)
            try (PreparedStatement insertStmt = conn.prepareStatement(insertUserQuery)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, password); // Store password as plain text
                int rowsAffected = insertStmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("✅ User registered successfully: " + username);
                    return true;
                }
            }

        } catch (SQLException e) {
            System.err.println("🚨 SQL Error in createUser: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves the plain-text password for a given username.
     *
     * @param username The username to fetch the password for.
     * @return The plain-text password, or null if the user does not exist or an error occurs.
     */
    public static String getUserPassword(String username) {
        String query = "SELECT password FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password"); // Return plain-text password
                } else {
                    System.err.println("⚠ User not found: " + username);
                }
            }

        } catch (SQLException e) {
            System.err.println("🚨 SQL Error in getUserPassword: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // Return null if user not found or error occurs
    }

    /**
     * Validates a user's login by checking the entered password against the stored plain-text password.
     *
     * @param username The username to authenticate.
     * @param enteredPassword The plain-text password entered by the user.
     * @return True if the password matches, otherwise false.
     */
    public static boolean validateUser(String username, String enteredPassword) {
        String storedPassword = getUserPassword(username);

        if (storedPassword == null) {
            System.out.println("❌ User authentication failed: " + username);
            return false;
        }

        boolean isMatch = storedPassword.equals(enteredPassword); // Direct comparison
        System.out.println("🔹 Password match for " + username + ": " + isMatch);
        return isMatch;
    }
}
