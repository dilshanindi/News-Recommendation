package com.example.newsrec;

import javafx.application.Application;
import javafx.stage.Stage;
import services.ArticleService;
import services.Categorize;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize the primary stage for NewsRec
        NewsRec.initializeStage(primaryStage); // Corrected method name

        // Create instance of NewsRec to navigate to the login page
        NewsRec newsRec = new NewsRec();
        newsRec.showLoginPage(); // Corrected method name to match NewsRec
    }

    public static void main(String[] args) {
        launch(args); // Launch JavaFX

        // Example of fetching and saving news (currently commented out)
        ArticleService newsLoader = new ArticleService();
        // newsLoader.fetchAndSaveNews(); // Uncomment to fetch and save news

        // Example of categorizing news (currently commented out)
        Categorize categorizer = new Categorize();
        // categorizer.categorizeNews(); // Uncomment to categorize news
    }
}
