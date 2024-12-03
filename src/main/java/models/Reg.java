package models;

public class Reg extends User {

    // Constructor for Regular User with explicit role
    public Reg(String userId, String name, String email, String password) {
        super(userId, name, email, password, "Regular User"); // Pass role explicitly
    }

    // Custom method to display the role (not overriding any method in the User class)
    public void displayRole() {
        System.out.println("Role: Regular User");
    }

    // Regular user-specific functionality
    public void viewArticles() {
        System.out.println("Viewing recommended articles...");
    }

    // Retrieve role (uses the method from the parent class)
    public String getRole() {
        return super.getRole(); // Retrieve role from the parent class
    }

    public String getUserName() {
        return super.getName(); // Retrieve name from the parent class
    }
}
