package utils;

import java.sql.*;

/**
 * Handles database operations for the News Recommendation System.
 * Encapsulates database access logic and provides reusable methods for user management.
 */
public class DatabaseHandler {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/NewsRecommendationDB"; // Update with your DB name
    private static final String DATABASE_USER = "root"; // Update with your MySQL username
    private static final String DATABASE_PASSWORD = "2627"; // Update with your MySQL password

    private Connection connection;

    /**
     * Constructor initializes the database connection.
     */
    public DatabaseHandler() {
        initializeConnection();
    }

    /**
     * Establishes the connection to the database.
     */
    private void initializeConnection() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            System.out.println("Database connection established successfully.");
        } catch (SQLException e) {
            System.err.println("Error establishing database connection: " + e.getMessage());
        }
    }

    /**
     * Checks if the database connection is active.
     *
     * @return true if connected, false otherwise.
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            System.err.println("Error checking connection status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if a NIC already exists in the database.
     *
     * @param nic the user's NIC.
     * @return true if the NIC exists, false otherwise.
     */
    public boolean checkNICExists(String nic) {
        String query = "SELECT 1 FROM users WHERE nic = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nic);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.err.println("Error checking NIC existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inserts a new user into the database.
     *
     * @param nic the user's NIC.
     * @param name the user's name.
     * @param age the user's age.
     * @param email the user's email.
     * @param password the user's password.
     * @return true if the insertion is successful, false otherwise.
     */
    public boolean addUser(String nic, String name, int age, String email, String password) {
        String query = "INSERT INTO users (nic, name, age, email, password, role) VALUES (?, ?, ?, ?, ?, 'user')";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nic);
            statement.setString(2, name);
            statement.setInt(3, age);
            statement.setString(4, email);
            statement.setString(5, password);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifies a user's email and password for login.
     *
     * @param email the user's email.
     * @param password the user's password.
     * @return the user's role ("user" or "admin"), or "none" if invalid credentials.
     */
    public String verifyUser(String email, String password) {
        String query = "SELECT role FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("role");
            } else {
                return "none";
            }
        } catch (SQLException e) {
            System.err.println("Error verifying user: " + e.getMessage());
            return "none";
        }
    }

    /**
     * Retrieves a user's name based on their email.
     *
     * @param email the user's email.
     * @return the user's name, or null if not found.
     */
    public String getUserName(String email) {
        String query = "SELECT name FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name");
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user name: " + e.getMessage());
            return null;
        }
    }

    /**
     * Closes the database connection.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}
