package models;


public class News {
    private final int articleId;  // Unique identifier for each news article
    private String headline;      // Title of the article
    private String summary;       // Brief description of the article
    private String body;          // Full content of the article
    private String category;      // Category (e.g., Sports, Politics)

    // Constructor for creating a news article (for database insertion, no ID)
    public News(String headline, String summary, String body) {
        this.articleId = 0;
        this.headline = headline;
        this.summary = summary;
        this.body = body;
    }

    // Constructor for fetching a news article (with ID and category)
    public News(int articleId, String headline, String summary, String body, String category) {
        this.articleId = articleId;
        this.headline = headline;
        this.summary = summary;
        this.body = body;
        this.category = category;
    }

    // Getter for article ID
    public int getArticleId() {
        return articleId;
    }

    // Getter for headline (title of the article)
    public String getHeadline() {
        return headline;
    }

    // Getter for summary (brief description)
    public String getArticleSummary() {
        return summary;
    }

    // Getter for full article content
    public String getBody() {
        return body;
    }

    // Getter for category of the article
    public String getCategory() {
        return category;
    }

    // Setter for article category (e.g., update to a new category)
    public void setCategory(String category) {
        this.category = category;
    }

    // Utility method to display article details
    public String getArticleDetails() {
        return String.format("Article ID: %d%nHeadline: %s%nCategory: %s%nSummary: %s%nContent: %s",
                articleId, headline, category, summary, body);
    }
}
