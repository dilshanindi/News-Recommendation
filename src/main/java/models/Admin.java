package models;


public class Admin extends User {
    public Admin(String name, String email, String nic, int age) {
        super(name, email, nic, age);
    }

    public static String getRole() {
        return "Admin";
    }

    @Override
    public void displayRole() {
        System.out.println("Role: Admin");
    }

    // Admin-specific functionality
    public void manageUsers() {
        System.out.println("Managing user accounts...");
    }
}
