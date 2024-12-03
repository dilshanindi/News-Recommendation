package services;


import models.News;
import utils.DatabaseHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Categorize {

    private static final DatabaseHandler dbHandler = new DatabaseHandler();

    // Keywords for categories (Encapsulating categories in a Map could be a better approach for scalability)
    private static final String[][] CATEGORY_KEYWORDS = {
            {"Technology", "tech", "innovation", "software", "hardware", "gadget"},
            {"Health", "health", "medicine", "hospital", "doctor", "fitness"},
            {"Sports", "sports", "football", "cricket", "basketball", "team"},
            {"AI", "AI", "artificial intelligence", "machine learning", "neural network"},
            {"Business", "business", "stock", "finance", "market", "economy"},
            {"Science", "science", "research", "experiment", "space", "discovery"}
    };

    public void categorizeNews() {
        try (Connection conn = dbHandler.connect()) {
            if (conn == null) return;

            // Step 1: Fetch news from the `news` table
            List<News> newsList = fetchNewsFromDatabase(conn);

            // Step 2: Categorize each news
            List<News> categorizedNewsList = categorize(newsList);

            // Step 3: Save categorized news to the `news_categorized` table
            saveCategorizedNews(conn, categorizedNewsList);

        } catch (SQLException e) {
            System.err.println("Error during categorization process: " + e.getMessage());
        }
    }

    // Fetches news from the database and maps it to News objects
    private List<News> fetchNewsFromDatabase(Connection conn) throws SQLException {
        String selectSQL = "SELECT idNews, title, description, content FROM news";
        List<News> newsList = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                News news = new News(
                        rs.getInt("idNews"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("content"),
                        "Uncategorized" // Default category
                );
                newsList.add(news);
            }
        }
        System.out.println("Fetched " + newsList.size() + " news items.");
        return newsList;
    }

    // Categorizes the list of news based on keywords
    private List<News> categorize(List<News> newsList) {
        for (News news : newsList) {
            boolean categorized = false;

            for (String[] categoryData : CATEGORY_KEYWORDS) {
                String category = categoryData[0];
                String[] keywords = categoryData;

                for (int i = 1; i < keywords.length; i++) {
                    String keyword = keywords[i];
                    if (isKeywordInNews(news, keyword)) {
                        news.setCategory(category);
                        categorized = true;
                        break;
                    }
                }

                if (categorized) break;
            }

            if (!categorized) {
                news.setCategory("Uncategorized"); // Default category
            }
        }
        return newsList;
    }

    // Checks if a keyword is present in the news title, description, or content
    private boolean isKeywordInNews(News news, String keyword) {
        return news.getHeadline().toLowerCase().contains(keyword.toLowerCase()) ||
                news.getArticleDetails().toLowerCase().contains(keyword.toLowerCase()) ||
                news.getBody().toLowerCase().contains(keyword.toLowerCase());
    }

    // Saves the categorized news into the `news_categorized` table
    private void saveCategorizedNews(Connection conn, List<News> categorizedNewsList) throws SQLException {
        String insertSQL = """
            INSERT INTO news_categorized (idNews, title, description, content, category)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            for (News news : categorizedNewsList) {
                pstmt.setInt(1, news.getArticleId());
                pstmt.setString(2, news.getHeadline());
                pstmt.setString(3, news.getArticleDetails());
                pstmt.setString(4, news.getBody());
                pstmt.setString(5, news.getCategory());
                pstmt.addBatch();
            }

            // Execute batch insert for better performance
            int[] rowsAffected = pstmt.executeBatch();
            System.out.println("Rows inserted into `news_categorized`: " + rowsAffected.length);
        } catch (SQLException e) {
            System.err.println("Error during saving categorized news: " + e.getMessage());
        }
    }
}
