package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {
    private final String userId; // Unique identifier for the user
    private String name;         // User's name
    private String email;        // User's email
    private String password;     // User's password
    private String role;         // User's role (e.g., "admin" or "user")

    private final List<String> preferences; // Preferred categories
    private final List<Article> readingHistory; // Reading history

    // Constructor
    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        setName(name); // Validate and set name
        setEmail(email); // Validate and set email
        setPassword(password); // Validate and set password
        this.preferences = new ArrayList<>();
        this.readingHistory = new ArrayList<>();
        this.role = "user"; // Default role
    }

    // Overloaded constructor for role-based user creation
    public User(String userId, String name, String email, String password, String role) {
        this(userId, name, email, password); // Call the primary constructor
        setRole(role); // Validate and set role
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public List<String> getPreferences() {
        return Collections.unmodifiableList(preferences);
    }

    public List<Article> getReadingHistory() {
        return Collections.unmodifiableList(readingHistory);
    }

    // Setters
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        this.email = email;
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long.");
        }
        this.password = password;
    }

    public void setRole(String role) {
        if (role == null || (!role.equalsIgnoreCase("admin") && !role.equalsIgnoreCase("user"))) {
            throw new IllegalArgumentException("Invalid role. Role must be 'admin' or 'user'.");
        }
        this.role = role;
    }

    // Business Methods
    public boolean addPreference(String category) {
        if (category != null && !preferences.contains(category)) {
            return preferences.add(category);
        }
        return false;
    }

    public boolean removePreference(String category) {
        return preferences.remove(category);
    }

    public void addToReadingHistory(Article article) {
        if (article != null) {
            readingHistory.add(article);
        }
    }

    public boolean hasReadArticle(String articleId) {
        return readingHistory.stream()
                .anyMatch(article -> article.getId().equals(articleId));
    }

    // Utility Methods
    public String getUserDetails() {
        return String.format("User: %s (%s)%nRole: %s%nPreferences: %s%nReading History: %d articles",
                name, email, role, preferences, readingHistory.size());
    }

    // Optional: Method to display the role directly
    public void displayRole() {
        System.out.println("Role: " + getRole());
    }
}
