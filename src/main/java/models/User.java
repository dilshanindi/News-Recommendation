package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {
    private final String userId; // Unique identifier for the user
    private String name;
    private String email;
    private String password; // Add password field

    private final List<String> preferences; // Preferred categories
    private final List<Article> readingHistory; // Reading history

    // Constructor
    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public List<String> getPreferences() {
        return Collections.unmodifiableList(preferences);
    }

    public List<Article> getReadingHistory() {
        return Collections.unmodifiableList(readingHistory);
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
