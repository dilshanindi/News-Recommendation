package com.example.newsrec;

import javafx.application.Application;
import javafx.stage.Stage;
import services.ArticleService;
import services.Categorize;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        NewsRec.setPrimaryStage(primaryStage); // set as primary stage NewsRecommendationSystem
        NewsRec newsRecSys = new NewsRec();
        newsRecSys.showLoginPage(); // show login page
    }

    public static void main(String[] args) {
        ArticleService newsLoader = new ArticleService();
//        newsLoader.fetchAndSaveNews();

        launch(args); // Launch JavaFX

        Categorize categorizer = new Categorize();
//        categorizer.categorizeNews();
    }
}
