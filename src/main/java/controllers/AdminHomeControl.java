package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.example.newsrec.NewsRec;
import models.Admin;
import models.Manager;
import utils.Alerts;

/**
 * Controller for managing the Admin's home page, including displaying user information and handling logout.
 */
public class AdminHomeControl {

    @FXML
    private Label accountTypeLabel; // Label to show account type, e.g., "Admin Account"

    @FXML
    private Label userNameLabel; // Label to show logged-in user's name

    @FXML
    private Button profileManageButton; // Button to navigate to profile management

    @FXML
    private Button logOutButton; // Button to log out from the application

    private final NewsRec newsRecommendationSystem;

    private String loggedInUserName;
    private String loggedInUserType;

    // Constructor to initialize NewsRecommendationSystem and session handling
    public AdminHomeControl() {
        this.newsRecommendationSystem = new NewsRec();
    }

    /**
     * Initialize method that sets up user details based on the current session.
     */
    @FXML
    public void initialize() {
        // Fetch logged-in user details from the session
        Manager session = Manager.getSessionInstance();
        loggedInUserName = session.getLoggedInUserName();
        loggedInUserType = session.getUserType();

        if (loggedInUserName == null || loggedInUserType == null) {
            // Show error if session details are missing and log the user out
            Alerts.showAlert("Error", "Logged-in user details are missing. Redirecting to login.", Alert.AlertType.ERROR);
            handleLogOut();
        } else {
            displayUserDetails(); // Display account type and user name
        }
    }

    /**
     * Sets the admin details and updates the UI labels.
     *
     * @param admin The admin object containing user details.
     */
    public void setAdminDetails(Admin admin) {
        accountTypeLabel.setText("Admin Account");
        userNameLabel.setText(admin.getName());
    }

    /**
     * Display the account type and logged-in user's name on the home page.
     */
    private void displayUserDetails() {
        accountTypeLabel.setText("Admin Account");
        userNameLabel.setText(loggedInUserName);
    }

    /**
     * Handles the action to navigate to the profile management page.
     */
    @FXML
    private void handleProfileManage() {
        // Navigate to Admin Profile Management page
        try {
            newsRecommendationSystem.showAdminProfile();
        } catch (Exception e) {
            e.printStackTrace();
            Alerts.showAlert("Error", "Failed to load Admin Profile Manage page.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles logging out the admin and navigating to the login page.
     */
    private void handleLogOut() {
        try {
            // Clear session and show the login page
            Manager.clearSession();
            newsRecommendationSystem.showLoginPage();
        } catch (Exception e) {
            Alerts.showAlert("Error", "Failed to log out: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
