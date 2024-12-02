package models;

import java.time.LocalDateTime;

public class Article {
    private final String id; // Unique identifier for the article
    private final String title;
    private final String description;
    private final LocalDateTime publishedDate; // Additional attribute for publication date
    private String category; // Added category for classification

    // Constructor
    public Article(String id, String title, String description, LocalDateTime publishedDate, String category) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Article ID cannot be null or empty.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        if (description == null) {
            description = "No description available."; // Default value if description is missing
        }
        this.id = id;
        this.title = title;
        this.description = description;
        this.publishedDate = publishedDate != null ? publishedDate : LocalDateTime.now(); // Default to current time
        this.category = category != null ? category : "Uncategorized"; // Default to "Uncategorized"
    }

    // Overloaded constructor for simpler instantiation
    public Article(String id, String title, String description, LocalDateTime publishedDate) {
        this(id, title, description, publishedDate, "Uncategorized");
    }

    public Article(String id, String title, String description) {
        this(id, title, description, LocalDateTime.now());
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public String getCategory() {
        return category;
    }

    // Setter for category
    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty.");
        }
        this.category = category;
    }

    // Utility methods
    public boolean isRecentArticle(int daysThreshold) {
        return LocalDateTime.now().minusDays(daysThreshold).isBefore(publishedDate);
    }

    public String getArticleSummary() {
        return String.format("Article: %s%nPublished on: %s%nSummary: %s",
                title, publishedDate.toString(), description.length() > 100
                        ? description.substring(0, 100) + "..."
                        : description);
    }

    // Overriding toString to display detailed information
    @Override
    public String toString() {
        return String.format("Title: %s%nPublished: %s%nCategory: %s%nDescription: %s",
                title, publishedDate.toString(), category, description.length() > 50
                        ? description.substring(0, 50) + "..."
                        : description);
    }

    // Static factory method for easier object creation
    public static Article create(String id, String title, String description) {
        return new Article(id, title, description, LocalDateTime.now(), "Uncategorized");
    }
}
