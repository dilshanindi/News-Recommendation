package services;

import models.User;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private final Map<String, User> users; // A temporary in-memory "database"

    public UserService() {
        this.users = new HashMap<>();
    }

    public User getUserByNIC(String nic) {
        return users.get(nic);
    }

    public boolean updateUserDetails(String nic, String name, String email) {
        User user = users.get(nic);
        if (user != null) {
            user.setName(name);
            user.setEmail(email);
            return true; // Update successful
        }
        return false; // User not found
    }

    public boolean changePassword(String nic, String currentPassword, String newPassword) {
        User user = users.get(nic);
        if (user != null && user.getPassword().equals(currentPassword)) {
            user.setPassword(newPassword);
            return true; // Password change successful
        }
        return false; // Current password does not match or user not found
    }

    public boolean deleteAccount(String nic) {
        return users.remove(nic) != null; // Returns true if the user was successfully removed
    }

    public boolean clearHistory(String nic) {
        User user = users.get(nic);
        if (user != null) {
            user.getReadingHistory().clear();
            return true; // History cleared
        }
        return false; // User not found
    }
}
