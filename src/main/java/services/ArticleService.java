package services;

import models.Article;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ArticleService {

    private static final String API_URL = "https://newsapi.org/v2/everything?q=keyword&apiKey=b52f49e93b08454f889bf1b6e08ead16";
    private final List<Article> articles; // Local storage for articles (could be fetched from API or file)

    // Constructor to initialize the articles list
    public ArticleService() {
        this.articles = new ArrayList<>();
    }

    /**
     * Fetch articles from the API.
     *
     * @return A list of articles fetched from the API.
     */
    public List<Article> fetchArticlesFromAPI() {
        List<Article> fetchedArticles = new ArrayList<>();

        try {
            // Convert API_URL to URI and then to URL
            URI uri = URI.create(API_URL);
            URL url = uri.toURL();

            // Connect to the API
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            // Check if connection is successful
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                // Read the API response
                StringBuilder inline = new StringBuilder();
                try (Scanner scanner = new Scanner(url.openStream())) {
                    while (scanner.hasNext()) {
                        inline.append(scanner.nextLine());
                    }
                }

                // Parse the JSON data
                JSONObject jsonResponse = new JSONObject(inline.toString());
                JSONArray articlesArray = jsonResponse.getJSONArray("articles");

                // Extract data for each article
                for (int i = 0; i < articlesArray.length(); i++) {
                    JSONObject articleJson = articlesArray.getJSONObject(i);
                    String id = "API_" + i; // Generate unique ID for API articles
                    String title = articleJson.optString("title", "No title available");
                    String description = articleJson.optString("description", "No description available");
                    LocalDateTime publishedDate = LocalDateTime.now(); // Placeholder for published date

                    // Create and add an Article object to the list
                    fetchedArticles.add(new Article(id, title, description, publishedDate, "Uncategorized"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add fetched articles to the local storage
        this.articles.addAll(fetchedArticles);
        return fetchedArticles;
    }

    /**
     * Fetch articles by category.
     *
     * @param category The category to filter articles by.
     * @return A list of articles matching the specified category.
     */
    public List<Article> fetchArticlesByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty.");
        }

        // Filter articles based on the category
        return articles.stream()
                .filter(article -> category.equalsIgnoreCase(article.getCategory()))
                .collect(Collectors.toList());
    }

    /**
     * Load articles from a CSV file.
     *
     * @param filePath The file path to the CSV file.
     * @return A list of articles loaded from the file.
     */
    public List<Article> loadArticlesFromCSV(String filePath) {
        List<Article> loadedArticles = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String id = values[0].trim();
                String title = values[1].trim();
                String description = values.length > 2 ? values[2].trim() : "No description available";
                LocalDateTime publishedDate = LocalDateTime.now(); // Placeholder for published date

                loadedArticles.add(new Article(id, title, description, publishedDate, "Uncategorized"));
            }
        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }

        // Add loaded articles to the local storage
        this.articles.addAll(loadedArticles);
        return loadedArticles;
    }

    /**
     * Get all articles.
     *
     * @return A list of all articles in the service.
     */
    public List<Article> getAllArticles() {
        return new ArrayList<>(articles); // Return a copy to prevent modification
    }

    /**
     * Add an article to the service.
     *
     * @param article The article to add.
     */
    public void addArticle(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("Article cannot be null.");
        }
        articles.add(article);
    }

    /**
     * Clear all articles (useful for testing or resetting).
     */
    public void clearArticles() {
        articles.clear();
    }
}
