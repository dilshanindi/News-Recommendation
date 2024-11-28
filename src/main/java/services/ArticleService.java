package services;

import models.Article;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ArticleService {

    private static final String API_URL = "https://newsapi.org/v2/everything?q=keyword&apiKey=b52f49e93b08454f889bf1b6e08ead16";

    public List<Article> fetchArticlesFromAPI() {
        List<Article> articles = new ArrayList<>();

        try {
            // Connect to the API
            URL url = new URL(API_URL);
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
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }
                scanner.close();

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
                    articles.add(new Article(id, title, description, publishedDate));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return articles;
    }

    public List<Article> fetchArticlesByCategory(String category) {
        List<Article> filteredArticles = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("categorized_articles.csv"))) {
            String line;
            br.readLine(); // Skip header

            int idCounter = 0; // To generate unique IDs for each article
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String id = "CSV_" + idCounter++; // Generate unique ID for CSV articles
                String title = values[0];
                String description = values[1];
                String articleCategory = values[2];
                LocalDateTime publishedDate = LocalDateTime.now(); // Placeholder for published date

                if (articleCategory.equalsIgnoreCase(category)) {
                    filteredArticles.add(new Article(id, title, description, publishedDate));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filteredArticles;
    }
}
