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
        mainStage = stage; // Ensure this is called before showing any scenes
    }

    // Display the login page
    public void showLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecsys/login.fxml"));
            Scene scene = new Scene(loader.load());
            mainStage.setTitle("News Recommendation System - Login");
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            System.out.println("Error loading login view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Show the user's home dashboard
    public void showUserHomeDashboard (Reg user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecsys/user-dashboard.fxml"));
            Scene scene = new Scene(loader.load());

            UserHomeControl controller = loader.getController();
            controller.setAccountDetails(user.getRole(), user.getUserName()); // Set account details for the user

            mainStage.setScene(scene);
            mainStage.setTitle("User Dashboard");
            mainStage.show();
        } catch (IOException e) {
            System.out.println("Error loading user dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Display the page for managing the admin profile
    public void showAdminProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecsys/admin-profile-manage.fxml"));
            Parent root = loader.load();
            mainStage.setScene(new Scene(root));
            mainStage.setTitle("Admin Profile Management");
            mainStage.show();
        } catch (IOException e) {
            System.out.println("Error loading admin profile page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Show the admin's home dashboard
    public void showAdminDashboard(Admin admin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecsys/admin-dashboard.fxml"));
            Scene scene = new Scene(loader.load());

            AdminHomeControl controller = loader.getController();
            controller.setAdminDetails(admin); // Correct method to set admin details

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
            e.printStackTrace();
        }
    }

    // Display the page for creating a new account
    public void showAccountCreation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/newsrecsys/create-account.fxml"));
            Scene scene = new Scene(loader.load());
            mainStage.setTitle("News Recommendation System - Create Account");
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            System.out.println("Error loading account creation page: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
