package utils;

import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    private static final String DB_URL = "http://localhost/phpmyadmin/index.php?route=/sql&db=newsrecommendation&table=users&pos=0"; // Correct JDBC URL


    // Establish a database connection
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public boolean executeUpdate(String query, Object... params) {
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set query parameters
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }

            // Execute the update query
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Query executed successfully.");
                return true; // Return true if the update was successful
            } else {
                System.out.println("No rows affected.");
                return false; // Return false if no rows were updated
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to execute query: " + query);
            return false; // Return false if an error occurred
        }
    }

    public String executeQuerySingleResult(String query, String param) {
        try (Connection connection = connect(); PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, param);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1); // Return the first column of the first result
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUserId(String email) {
        String query = "SELECT userId FROM users WHERE email = ?";

        try (Connection connection = connect(); PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("userId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // If no user is found
    }


    // Verify user credentials and return their role
    public String verifyUser(String email, String password) {
        String sql = "SELECT role FROM users WHERE email = ? AND password = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role"); // Return the role if the user exists
                }
            }
        } catch (SQLException e) {
            System.out.println("Error verifying user: " + e.getMessage());
        }
        return null; // Return null if verification fails
    }

    // Retrieve user name by email
    public String getUserName(String email) {
        String sql = "SELECT name FROM users WHERE email = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name"); // Return the name if found
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user name: " + e.getMessage());
        }
        return null; // Return null if no user is found
    }

    // Update user details for admin
    public boolean updateUserDetailsForAdmin(String nic, String name, int age, String email, String role) {
        String sql = "UPDATE users SET name = ?, age = ?, email = ?, role = ? WHERE nic = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, email);
            pstmt.setString(4, role);
            pstmt.setString(5, nic);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating user details: " + e.getMessage());
            return false;
        }
    }

    // Delete user by NIC
    public boolean deleteUserByNIC(String nic) {
        String sql = "DELETE FROM users WHERE nic = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nic);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting user by NIC: " + e.getMessage());
            return false;
        }
    }

    // Clear reading history for a user by NIC
    public boolean clearHistory(String nic) {
        String sql = "DELETE FROM reading_history WHERE user_nic = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nic);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error clearing reading history: " + e.getMessage());
            return false;
        }
    }
    // Add this method to the DatabaseHandler class
    public ResultSet executeQuery(String query, int userId) throws SQLException {
        Connection connection = connect(); // Ensure this method establishes a DB connection
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        return statement.executeQuery();
    }


    // Fetch all users from the database
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String email = rs.getString("email");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String nic = rs.getString("nic");
                String role = rs.getString("role");

                // Assuming a User constructor or setter that accepts role
                User user = new User(email, name, nic, role);
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all users: " + e.getMessage());
        }

        return users;
    }

    // Fetch user details by NIC
    public User getUserByNIC(String nic) {
        String sql = "SELECT * FROM users WHERE nic = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nic);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String email = rs.getString("email");
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    String role = rs.getString("role");

                    return new User(email, name, nic, role);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user by NIC: " + e.getMessage());
        }

        return null;
    }

    // Check if NIC exists
    public boolean checkNICExists(String nic) {
        String sql = "SELECT nic FROM users WHERE nic = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nic);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Returns true if NIC exists
            }
        } catch (SQLException e) {
            System.out.println("Error checking NIC: " + e.getMessage());
            return false;
        }
    }

    // Insert a new user
    public boolean addUser(String nic, String name, int age, String email, String password) {
        String sql = "INSERT INTO users (nic, name, age, email, password, role) VALUES (?, ?, ?, ?, ?, 'user')";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nic);
            pstmt.setString(2, name);
            pstmt.setInt(3, age);
            pstmt.setString(4, email);
            pstmt.setString(5, password);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
            return false;
        }
    }
}
