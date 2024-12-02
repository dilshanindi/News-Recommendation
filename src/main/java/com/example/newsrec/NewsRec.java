package com.example.newsrec;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controllers.AdminHomeControl;
import controllers.UserHomeControl;
import models.Admin;
import models.Reg;

import java.io.IOException;

public class NewsRec {

    private static Stage mainStage;

    // Set the primary stage for the application
    public static void initializeStage(Stage stage) {
        mainStage = stage;
    }

    // Display the login page
    public void showLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecsys/login.fxml"));
            Scene scene = new Scene(loader.load());
            mainStage.setTitle("News Recommendation System - Login");
            mainStage.setScene(scene);
            mainStage.show();
        } catch (Exception e) {
            System.out.println("Error loading login view: " + e.getMessage());
        }
    }

    // Show the user's home dashboard
    public void showUserDashboard(Reg user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecsys/user-dashboard.fxml"));
            Scene scene = new Scene(loader.load());

            UserHomeControl controller = loader.getController();
            controller.initializeUserDetails(user);  // Pass user details to the controller

            mainStage.setScene(scene);
            mainStage.setTitle("User Dashboard");
            mainStage.show();
        } catch (IOException e) {
            System.out.println("Error loading user dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Display the user profile management page
    public void showUserProfileManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecsys/user-profile-manage.fxml"));
            Parent root = loader.load();

            mainStage.setTitle("User Profile Management");
            mainStage.setScene(new Scene(root));
            mainStage.show();
        } catch (IOException e) {
            System.out.println("Error loading user profile management: " + e.getMessage());
        }
    }

    // Show the admin's home dashboard
    public void showAdminDashboard(Admin admin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecsys/admin-dashboard.fxml"));
            Scene scene = new Scene(loader.load());

            AdminDashboardController controller = loader.getController();
            controller.initializeAdminDetails(admin);  // Pass admin details to the controller

            mainStage.setScene(scene);
            mainStage.setTitle("Admin Dashboard");
            mainStage.show();
        } catch (IOException e) {
            System.out.println("Error loading admin dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Show the admin profile management page
    public void showAdminProfileManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecsys/admin-profile-manage.fxml"));
            Parent root = loader.load();
            mainStage.setScene(new Scene(root));
            mainStage.setTitle("Admin Profile Management");
            mainStage.show();
        } catch (IOException e) {
            System.out.println("Error loading admin profile management: " + e.getMessage());
        }
    }

    // Display the page for creating a new account
    public void showAccountCreationPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecsys/create-account.fxml"));
            Scene scene = new Scene(loader.load());
            mainStage.setTitle("News Recommendation System - Create Account");
            mainStage.setScene(scene);
            mainStage.show();
        } catch (Exception e) {
            System.out.println("Error loading account creation page: " + e.getMessage());
        }
    }
}
