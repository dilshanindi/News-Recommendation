package services;

import utils.DatabaseHandler;

public class UserService {
    private final DatabaseHandler dbHandler;

    public UserService() {
        this.dbHandler = new DatabaseHandler();
    }

    // Save interaction
    public void saveInteraction(int userId, int newsId, String interactionType, String category) {
        String query = "INSERT INTO userinteractions (user_id, idNews, interaction_type, interaction_timestamp, category) " +
                "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?)";
        dbHandler.executeUpdate(query, userId, newsId, interactionType, category);
    }
}
