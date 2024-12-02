package utils;

import models.Admin;
import models.Reg;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    private static final String DB_URL = "http://localhost/phpmyadmin/index.php?route=/database/structure&db=newsrecommendationdb"; // Replace with your database name


    // Establish a database connection
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public void executeUpdate(String query, Object... params) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set query parameters
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }

            // Debug log
            System.out.println("Executing query: " + query);
            System.out.println("With parameters:");
            for (Object param : params) {
                System.out.println(param);
            }

            // Execute the update query
            preparedStatement.executeUpdate();
            System.out.println("Query executed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to execute query: " + query);
        }
    }

    // Check if NIC is unique for a user (excluding their own record based on email)
    public boolean isNICUnique(String nic, String email) {
        String sql = "SELECT 1 FROM users WHERE nic = ? AND email != ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nic);
            pstmt.setString(2, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return !rs.next(); // True if no other user has this NIC
            }
        } catch (SQLException e) {
            System.out.println("Error checking NIC uniqueness: " + e.getMessage());
            return false;
        }
    }

    // Check if NIC exists in the database
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

    // Insert a new user into the database
    public boolean insertUser(String nic, String name, int age, String email, String password) {
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

    // Verify user credentials and return their role
    public String verifyUser(String email, String password) {
        String sql = "SELECT role FROM users WHERE email = ? AND password = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role"); // Return the user's role ("user" or "admin")
                }
            }
        } catch (SQLException e) {
            System.out.println("Error verifying user: " + e.getMessage());
        }
        return "none"; // Return "none" if no match is found
    }

    // Verify if the password matches for the given email
    public boolean verifyPassword(String email, String password) {
        String sql = "SELECT 1 FROM users WHERE email = ? AND password = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Returns true if a record is found
            }
        } catch (SQLException e) {
            System.out.println("Error verifying password: " + e.getMessage());
            return false;
        }
    }

    // Update a user's password
    public boolean updatePassword(String email, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, email);
            return pstmt.executeUpdate() > 0; // Return true if a row is updated
        } catch (SQLException e) {
            System.out.println("Error updating password: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUserDetailsForAdmin(String email, String name, int age, String nic, String role) {
        // Validate NIC uniqueness
        if (!isNICUnique(nic, email)) {
            System.out.println("Error: NIC already exists for another user.");
            return false;
        }

        String sql = "UPDATE users SET name = ?, age = ?, nic = ?, role = ? WHERE email = ?";

        if (!emailExists(email)) {
            System.out.println("Error: Email does not exist in the database.");
            return false;
        }

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);  // Set name
            pstmt.setInt(2, age);     // Set age
            pstmt.setString(3, nic);  // Set NIC
            pstmt.setString(4, role); // Set role
            pstmt.setString(5, email); // Set email

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected (Admin): " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating user details (Admin): " + e.getMessage());
            return false;
        }
    }

    public boolean updateUserDetailsForUser(String email, String name, int age, String nic) {
        // Validate NIC uniqueness
        if (!isNICUnique(nic, email)) {
            System.out.println("Error: NIC already exists for another user.");
            return false;
        }

        String sql = "UPDATE users SET name = ?, age = ?, nic = ? WHERE email = ?";

        if (!emailExists(email)) {
            System.out.println("Error: Email does not exist in the database.");
            return false;
        }

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);  // Set name
            pstmt.setInt(2, age);     // Set age
            pstmt.setString(3, nic);  // Set NIC
            pstmt.setString(4, email); // Set email

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected (User): " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating user details (User): " + e.getMessage());
            return false;
        }
    }

    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error checking email existence: " + e.getMessage());
            return false;
        }
    }

    // Delete a user from the database
    public boolean deleteUser(String email) {
        String sql = "DELETE FROM users WHERE email = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email); // Use email to identify the user
            return pstmt.executeUpdate() > 0; // Return true if a row is deleted
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }

    // Clear user's history
    public boolean clearHistory(String email) {
        String sql = "DELETE FROM user_history WHERE email = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error clearing history: " + e.getMessage());
            return false;
        }
    }

    // Fetch a user's NIC based on their email
    public String getNIC(String email) {
        String sql = "SELECT nic FROM users WHERE email = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nic");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching NIC: " + e.getMessage());
        }
        return null;
    }

    // Get user details by email for a regular user
    public Reg getRegularUserDetails(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String nic = rs.getString("nic");
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    return new Reg(email, name, age, nic);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching regular user details: " + e.getMessage());
        }
        return null;
    }

    // Get admin details by email
    public Admin getAdminDetails(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String nic = rs.getString("nic");
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    return new Admin(email, name, age, nic);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching admin details: " + e.getMessage());
        }
        return null;
    }
}
