package models;



public class Interact {
    private int userId;
    private int newsId;
    private String interactionType;
    private String category;

    // Constructor with validation for interactionType and category
    public Interact(int userId, int newsId, String interactionType, String category) {
        if (interactionType == null || interactionType.trim().isEmpty()) {
            throw new IllegalArgumentException("Interaction type cannot be null or empty");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        this.userId = userId;
        this.newsId = newsId;
        this.interactionType = interactionType;
        this.category = category;
    }

    // Getters
    public int getUserId() {
        return userId;
    }

    public int getNewsId() {
        return newsId;
    }

    public String getInteractionType() {
        return interactionType;
    }

    public String getCategory() {
        return category;
    }

    // toString() method for better readability in logs and debugging
    @Override
    public String toString() {
        return "UserInteraction{" +
                "userId=" + userId +
                ", newsId=" + newsId +
                ", interactionType='" + interactionType + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    // Optionally, you can add setters if you need to modify these values after the object is created
    // However, if these values should be immutable, it might be better to leave out setters entirely.
}
