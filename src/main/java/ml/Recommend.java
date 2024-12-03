package ml;

import models.Interact;
import models.Article;
import services.ArticleService;
import utils.DatabaseHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Recommend {
    private final DatabaseHandler databaseHandler;
    private final ArticleService articleService;
    private final CategoryScoreCalculator scoreCalculator;

    public Recommend() {
        this.databaseHandler = new DatabaseHandler();
        this.articleService = new ArticleService();
        this.scoreCalculator = new CategoryScoreCalculator();
    }

    // Fetch user interaction data
    public List<Interact> fetchUserInteractions(int userId) {
        String query = "SELECT * FROM userinteractions WHERE user_id = ?";
        List<Interact> interactions = new ArrayList<>();

        try (ResultSet resultSet = databaseHandler.executeQuery(query, userId)) {
            while (resultSet.next()) {
                interactions.add(new Interact(
                        resultSet.getInt("user_id"),
                        resultSet.getInt("idNews"),
                        resultSet.getString("interaction_type"),
                        resultSet.getString("category")

                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return interactions;
    }

    // Get recommended articles based on interaction history
    public List<Article> getRecommendations(int userId) {
        List<Interact> interactions = fetchUserInteractions(userId);

        if (interactions.isEmpty()) {
            System.err.println("No interactions found for user ID " + userId);
            return Collections.emptyList();  // Return empty list if no interactions
        }

        // Calculate category scores
        Map<String, Integer> categoryScores = scoreCalculator.calculateCategoryScores(interactions);

        // Identify top categories
        List<String> preferredCategories = getTopCategories(categoryScores);


        // Fetch articles related to top categories
        return articleService.fetchArticlesByCategories(preferredCategories, userId);
    }

    // Helper method to sort categories by score and get the top ones
    private List<String> getTopCategories(Map<String, Integer> categoryScores) {
        return categoryScores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(3)
                .toList();
    }

    // Inner class responsible for calculating category scores
    private static class CategoryScoreCalculator {
        // Calculate category scores based on interactions
        public Map<String, Integer> calculateCategoryScores(List<Interact> interactions) {
            Map<String, Integer> scores = new HashMap<>();

            for (Interact interaction : interactions) {
                int score = determineInteractionScore(interaction.getInteractionType());
                scores.merge(interaction.getCategory(), score, Integer::sum);
            }

            return scores;
        }

        // Determine score based on interaction type
        private int determineInteractionScore(String interactionType) {
            return switch (interactionType.toLowerCase()) {
                case "liked" -> 2;  // Positive score
                case "read" -> 1;   // Neutral score
                case "skipped" -> -1; // Negative score
                default -> 0;
            };
        }
    }
}
