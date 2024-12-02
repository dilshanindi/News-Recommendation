package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Article;
import services.ArticleService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class  UserHomeControl {

    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private Label accountTypeLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private Button profileManageButton;
    @FXML
    private Button logOutButton;
    @FXML
    private ListView<String> newlyAddedArticlesList;

    private final ArticleService articleService;
    private final ObservableList<String> articlesObservableList;

    public UserHomeControl() {
        this.articleService = new ArticleService();
        this.articlesObservableList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        // Populate ComboBox with predefined categories
        categoryComboBox.getItems().addAll("Technology", "Health", "Sports", "AI", "Business", "Science");
        newlyAddedArticlesList.setItems(articlesObservableList);

        // Load articles based on selected category
        categoryComboBox.setOnAction(event -> loadArticles(categoryComboBox.getValue()));

        // Set click event for ListView items
        newlyAddedArticlesList.setOnMouseClicked(event -> {
            String selectedItem = newlyAddedArticlesList.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                findArticleByTitle(selectedItem).ifPresent(this::showArticlePopup);
            }
        });

        // Profile and logout actions
        profileManageButton.setOnAction(event -> handleProfileManage());
        logOutButton.setOnAction(event -> handleLogOut());
    }

    // Set account type and user's name
    public void setAccountDetails(String role, String userName) {
        accountTypeLabel.setText(role.equalsIgnoreCase("admin") ? "Admin Account" : "User Account");
        userNameLabel.setText(userName);
    }

    private void loadArticles(String category) {
        System.out.println("Selected Category: " + category);
        List<Article> articles = articleService.fetchArticlesByCategory(category);

        articlesObservableList.clear();
        if (articles != null && !articles.isEmpty()) {
            articles.forEach(article -> articlesObservableList.add(article.getTitle()));
        } else {
            articlesObservableList.add("No articles available for this category.");
        }
        System.out.println("Loaded articles: " + articles.size());
    }

    // Find an article by title using Optional
    private Optional<Article> findArticleByTitle(String title) {
        List<Article> articles = articleService.fetchArticlesByCategory(categoryComboBox.getValue());
        return articles == null ? Optional.empty() : articles.stream().filter(article -> article.getTitle().equals(title)).findFirst();
    }

    // Show article details in a popup
    private void showArticlePopup(Article article) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecsys/news-popup.fxml"));
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle(article.getTitle());

            Scene scene = new Scene(loader.load());
            popupStage.setScene(scene);

            // Set data and stage in popup controller
            NewsPopupControl controller = loader.getController();
            controller.initializePopup(article.getTitle(), article.getDescription());
            controller.setPopupStage(popupStage);

            popupStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Error loading article popup: " + e.getMessage());
        }
    }

    @FXML
    private void handleProfileManage() {
        try {
            // Load the profile management FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecsys/user-profile-manage.fxml"));
            Scene scene = new Scene(loader.load());

            // Create a new stage for the profile management page
            Stage profileStage = new Stage();
            profileStage.setTitle("Profile Management - News Recommendation System");
            profileStage.setScene(scene);
            profileStage.initModality(Modality.WINDOW_MODAL); // Makes it modal to the parent window
            profileStage.initOwner(profileManageButton.getScene().getWindow()); // Set owner as the current stage



            // Show the profile management stage
            profileStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Error loading profile management page: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogOut() {
        System.out.println("Logging out...");
        // Redirect to login page if implemented
    }
}
