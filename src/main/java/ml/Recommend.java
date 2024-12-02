package ml;


import models.UserInteraction;
import models.Article;
import services.ArticleService;
import utils.DatabaseHandler;
import utils.NoInteractionsException;

import java.util.*;

public class RecommendationEngine {
    private final DatabaseHandler databaseHandler;
    private final ArticleService articleService;
    private final CategoryScoreCalculator scoreCalculator;

    public RecommendationEngine() {
        this.databaseHandler = new DatabaseHandler();
        this.articleService = new ArticleService();
        this.scoreCalculator = new CategoryScoreCalculator();
    }

    // Fetch user interaction data
    public List<Interact> fetchUserInteractions(int userId) throws NoInteractionsException {
        String query = "SELECT * FROM userinteractions WHERE user_id = ?";
        List<UserInteraction> interactions = databaseHandler.executeQueryList(query, resultSet -> {
            return new UserInteraction(
                    resultSet.getInt("user_id"),
                    resultSet.getInt("idNews"),
                    resultSet.getString("interaction_type"),
                    resultSet.getString("category")
        }, userId);

        if (interactions.isEmpty()) {
            throw new NoInteractionsException("No interactions found for user ID " + userId);
        }
        return interactions;
    }

    // Get recommended articles based on interaction history
    public List<Article> getRecommendations(int userId) {
        try {
            List<UserInteraction> interactions = fetchUserInteractions(userId);
            Map<String, Integer> categoryScores = scoreCalculator.calculateCategoryScores(interactions);

            // Identify top categories
            List<String> preferredCategories = getTopCategories(categoryScores);

            // Fetch articles related to top categories
            return articleService.fetchArticlesByCategories(preferredCategories, userId);
        } catch (NoInteractionsException e) {
            System.err.println(e.getMessage());
            return Collections.emptyList();  // Return empty list if no interactions
        }
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
        public Map<String, Integer> calculateCategoryScores(List<UserInteraction> interactions) {
            Map<String, Integer> scores = new HashMap<>();

            for (UserInteraction interaction : interactions) {
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
