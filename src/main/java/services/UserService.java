package services;

import models.User;
import utils.DatabaseHandler;

public class UserService {
    private final DatabaseHandler dbHandler;

    public UserService() {
        this.dbHandler = new DatabaseHandler();
    }

    // Method to clear user's history by NIC
    public boolean clearHistory(String nic) {
        return dbHandler.clearHistory(nic);
    }

    // Method to delete a user's account by NIC
    public boolean deleteAccount(String nic) {
        return dbHandler.deleteUserByNIC(nic);
    }

    // Method to change a user's password
    public boolean changePassword(String nic, String currentPassword, String newPassword) {
        // Verify current password before changing
        String sql = "SELECT password FROM users WHERE nic = ?";
        try {
            String existingPassword = dbHandler.executeQuerySingleResult(sql, nic);
            if (existingPassword != null && existingPassword.equals(currentPassword)) {
                String updateQuery = "UPDATE users SET password = ? WHERE nic = ?";
                dbHandler.executeUpdate(updateQuery, newPassword, nic);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to update user details by NIC
    public boolean updateUserDetails(String nic, String name, String email) {
        String sql = "UPDATE users SET name = ?, email = ? WHERE nic = ?";
        try {
            return dbHandler.executeUpdate(sql, name, email, nic);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to fetch a user by NIC
    public User getUserByNIC(String nic) {
        return dbHandler.getUserByNIC(nic);
    }
}
