package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.example.newsrec.NewsRec;
import utils.DatabaseHandler;

public class LoginControl {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private final NewsRec newsRecSystem;
    private final DatabaseHandler databaseHandler;

    // Constructor - Use dependency injection to manage dependencies
    public LoginControl() {
        this.newsRecSystem = new NewsRec();
        this.databaseHandler = new DatabaseHandler();
    }

    @FXML
    private void initialize() {
        // Initialization logic if needed
    }

    @FXML
    private void handleLogin() {
        String email = getEmailInput();
        String password = getPasswordInput();

        if (isInputValid(email, password)) {
            processLogin(email, password);
        }
    }

    @FXML
    private void handleClear() {
        clearInputs();
    }

    @FXML
    private void handleCreateAccount() {
        navigateToCreateAccount();
    }

    // Helper Methods for Encapsulation and Reusability

    private String getEmailInput() {
        return emailField.getText();
    }

    private String getPasswordInput() {
        return passwordField.getText();
    }

    private boolean isInputValid(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            showAlert("Error", "Email and Password fields cannot be empty!");
            return false;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            showAlert("Error", "Please provide a valid email address.");
            return false;
        }

        return true;
    }

    private void processLogin(String email, String password) {
        String userType = databaseHandler.verifyUser(email, password); // Returns "user", "admin", or "none"
        if ("none".equalsIgnoreCase(userType)) {
            showAlert("Login Failed", "Invalid email or password!");
            return;
        }

        String userName = databaseHandler.getUserName(email); // Fetch the user's name
        if (userName == null) {
            showAlert("Error", "User name could not be retrieved. Please try again.");
            return;
        }

        navigateToHomePage(userName, userType);
    }

    private void navigateToHomePage(String userName, String userType) {
        if ("user".equalsIgnoreCase(userType)) {
            newsRecSystem.showUserHomePage(userName, userType);

        } else {
            showAlert("Error", "Unexpected user type: " + userType);
        }
    }

    private void clearInputs() {
        emailField.clear();
        passwordField.clear();
    }

    private void navigateToCreateAccount() {
        newsRecSystem.showCreateAccountPage();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
