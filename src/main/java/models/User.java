package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {
    private final String userId; // Unique identifier for the user
    private String name;
    private String email;
    private final List<String> preferences; // Preferred categories (immutable list to prevent external modification)
    private final List<Article> readingHistory; // Reading history (immutable list for encapsulation)

    // Constructor
    public User(String userId, String name, String email) {
        this.userId = userId; // Ensures each user has a unique ID
        setName(name); // Validate name through setter
        setEmail(email); // Validate email through setter
        this.preferences = new ArrayList<>();
        this.readingHistory = new ArrayList<>();
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

    public List<String> getPreferences() {
        return Collections.unmodifiableList(preferences); // Return unmodifiable view for better encapsulation
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

    // Utility method to display user info
    public String getUserDetails() {
        return String.format("User: %s (%s)%nPreferences: %s%nReading History: %d articles",
                name, email, preferences, readingHistory.size());
    }
}
