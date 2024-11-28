package models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Category {

    private final Map<String, List<String>> categoryKeywords; // Instance-based for flexibility

    // Constructor for initialization
    public Category() {
        this.categoryKeywords = initializeCategoryKeywords();
    }

    // Initializes keywords for each category
    private Map<String, List<String>> initializeCategoryKeywords() {
        Map<String, List<String>> keywords = new HashMap<>();
        keywords.put("Technology", List.of("software", "Technology", "computer", "internet", "gadget", "cybersecurity", "robotics", "innovation", "blockchain", "AI", "data science", "programming", "hardware", "cloud computing"));
        keywords.put("Health", List.of("medicine", "Health", "fitness", "disease", "health", "nutrition", "wellness", "mental health", "public health", "pandemic", "exercise", "diet", "therapy", "vaccination"));
        keywords.put("Sports", List.of("soccer", "basketball", "Sports", "cricket", "tennis", "olympics", "golf", "football", "baseball", "hockey", "racing", "athletics", "rugby", "boxing", "swimming"));
        keywords.put("AI", List.of("artificial intelligence", "neural network", "machine learning", "deep learning", "automation", "NLP", "computer vision", "AI ethics", "predictive modeling", "AI research", "generative AI", "big data", "AI"));
        keywords.put("Business", List.of("economy", "Business", "market", "investment", "finance", "entrepreneurship", "startup", "corporate", "trade", "stocks", "real estate", "banking", "insurance", "cryptocurrency"));
        keywords.put("Science", List.of("biology", "Science", "physics", "research", "chemistry", "genetics", "astronomy", "ecology", "geology", "space", "climate change", "nanotechnology", "astrophysics", "marine biology"));
        return keywords;
    }

    // Inner Article class
    public static class Article {
        private final String title;
        private final String description;
        private String category;

        public Article(String title, String description) {
            this.title = title;
            this.description = description == null ? "No description available" : description;
            this.category = "Uncategorized";
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }

    // Reads articles from a CSV file
    public List<Article> readArticlesFromCSV(String filePath) {
        List<Article> articles = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.lines().skip(1).forEach(line -> { // Skip header and process lines
                String[] values = line.split(",");
                String title = values[0].trim();
                String description = values.length > 1 ? values[1].trim() : "";
                articles.add(new Article(title, description));
            });
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
        return articles;
    }

    // Categorizes articles based on keywords
    public void categorizeArticles(List<Article> articles) {
        for (Article article : articles) {
            article.setCategory(determineCategory(article));
        }
    }

    // Determines the best category for an article
    private String determineCategory(Article article) {
        String content = (article.getTitle() + " " + article.getDescription()).toLowerCase();
        String bestCategory = "Uncategorized";
        int highestMatchCount = 0;

        for (Map.Entry<String, List<String>> entry : categoryKeywords.entrySet()) {
            String category = entry.getKey();
            int matchCount = (int) entry.getValue().stream()
                    .filter(keyword -> content.contains(keyword.toLowerCase()))
                    .count();

            if (matchCount > highestMatchCount) {
                highestMatchCount = matchCount;
                bestCategory = category;
            }
        }
        return bestCategory;
    }

    // Writes categorized articles to a CSV file
    public void writeCategorizedArticlesToCSV(List<Article> articles, String outputFilePath) {
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            writer.write("Title,Description,Category\n");
            for (Article article : articles) {
                writer.write(formatArticleForCSV(article));
            }
        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
    }

    // Formats an article for CSV output
    private String formatArticleForCSV(Article article) {
        return String.format("%s,%s,%s\n",
                sanitizeForCSV(article.getTitle()),
                sanitizeForCSV(article.getDescription()),
                article.getCategory());
    }

    // Sanitizes text for CSV output
    private String sanitizeForCSV(String text) {
        return text.replace(",", " ").trim(); // Replace commas and trim whitespace
    }

    public static void main(String[] args) {
        Category categorizer = new Category();

        // Step 1: Read articles from CSV
        List<Article> articles = categorizer.readArticlesFromCSV("articles.csv");

        // Step 2: Categorize each article
        categorizer.categorizeArticles(articles);

        // Step 3: Write categorized articles to a new CSV file
        categorizer.writeCategorizedArticlesToCSV(articles, "categorized_articles.csv");

        System.out.println("Categorization complete. Results saved to categorized_articles.csv.");
    }
}
