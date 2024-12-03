package models;

public class Admin extends User {

    // Constructor aligned with the User class
    public Admin(String userId, String name, String email, String password) {
        super(userId, name, email, password, "admin"); // Call the appropriate User constructor
    }

    // Override getRole() as an instance method
    @Override
    public String getRole() {
        return "Admin";
    }

    // Admin-specific functionality
    public void manageUsers() {
        System.out.println("Managing user accounts...");
    }
}
