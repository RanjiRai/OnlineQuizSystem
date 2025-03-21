package BackEnd.Database;
/*
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/online_quiz_db";
    private static final String USER = "root@localhost:3306";  // Change this to your MySQL username
    private static final String PASSWORD = "dfamily13245";  // Change this to your MySQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.sql.Driver;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/online_quiz_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "dfamily13245"; // Replace with your MySQL password

    public static Connection getConnection() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Debug: List available JDBC drivers
            System.out.println("Checking available JDBC drivers:");
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                System.out.println("Available Driver: " + drivers.nextElement().getClass().getName());
            }

            // Establish Connection
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
            return conn;
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found! Make sure it's in the classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database connection failed! Check URL, username, password, and server status.");
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        // Test the connection
        getConnection();
    }
}

