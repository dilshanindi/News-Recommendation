package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import services.UserService;

public class UserProfile {

    @FXML
    private TextField nicTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private Button searchButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button changePasswordButton;

    @FXML
    private Button deleteAccountButton;

    @FXML
    private Button clearHistoryButton;

    private final UserService userService;

    public UserProfile() {
        this.userService = new UserService(); // Handles database operations
    }

    @FXML
    public void initialize() {
        // Set action for Search button
        searchButton.setOnAction(event -> searchUser());

        // Set action for Update button
        updateButton.setOnAction(event -> updateUserDetails());

        // Set action for Change Password button
        changePasswordButton.setOnAction(event -> changePassword());

        // Set action for Delete Account button
        deleteAccountButton.setOnAction(event -> deleteAccount());

        // Set action for Clear History button
        clearHistoryButton.setOnAction(event -> clearHistory());
    }

    private void searchUser() {
        String nic = nicTextField.getText();
        if (nic.isEmpty()) {
            showAlert(AlertType.WARNING, "NIC Field is Empty", "Please enter your NIC.");
            return;
        }

        // Fetch user details from the database
        var user = userService.getUserByNIC(nic);

        if (user != null) {
            nameTextField.setText(user.getName());
            emailTextField.setText(user.getEmail());
        } else {
            showAlert(AlertType.ERROR, "User Not Found", "No user found with the provided NIC.");
        }
    }

    private void updateUserDetails() {
        String nic = nicTextField.getText();
        String name = nameTextField.getText();
        String email = emailTextField.getText();

        if (nic.isEmpty() || name.isEmpty() || email.isEmpty()) {
            showAlert(AlertType.WARNING, "Incomplete Data", "Please fill in all fields.");
            return;
        }

        // Update user details in the database
        boolean success = userService.updateUserDetails(nic, name, email);

        if (success) {
            showAlert(AlertType.INFORMATION, "Update Successful", "Your details have been updated.");
        } else {
            showAlert(AlertType.ERROR, "Update Failed", "Failed to update your details.");
        }
    }

    private void changePassword() {
        String nic = nicTextField.getText();
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();

        if (nic.isEmpty() || currentPassword.isEmpty() || newPassword.isEmpty()) {
            showAlert(AlertType.WARNING, "Incomplete Data", "Please fill in all fields.");
            return;
        }

        // Validate current password and update the password
        boolean success = userService.changePassword(nic, currentPassword, newPassword);

        if (success) {
            showAlert(AlertType.INFORMATION, "Password Changed", "Your password has been successfully updated.");
        } else {
            showAlert(AlertType.ERROR, "Password Change Failed", "Current password is incorrect.");
        }
    }

    private void deleteAccount() {
        String nic = nicTextField.getText();

        if (nic.isEmpty()) {
            showAlert(AlertType.WARNING, "NIC Field is Empty", "Please enter your NIC.");
            return;
        }

        // Confirm deletion
        boolean confirmed = showConfirmation("Delete Account", "Are you sure you want to delete your account?");
        if (!confirmed) return;

        // Delete the user's account from the database
        boolean success = userService.deleteAccount(nic);

        if (success) {
            showAlert(AlertType.INFORMATION, "Account Deleted", "Your account has been successfully deleted.");
            clearFields();
        } else {
            showAlert(AlertType.ERROR, "Deletion Failed", "Failed to delete your account.");
        }
    }

    private void clearHistory() {
        String nic = nicTextField.getText();

        if (nic.isEmpty()) {
            showAlert(AlertType.WARNING, "NIC Field is Empty", "Please enter your NIC.");
            return;
        }

        // Clear user's history in the database
        boolean success = userService.clearHistory(nic);

        if (success) {
            showAlert(AlertType.INFORMATION, "History Cleared", "Your news history has been cleared.");
        } else {
            showAlert(AlertType.ERROR, "Clear History Failed", "Failed to clear your news history.");
        }
    }

    private void clearFields() {
        nicTextField.clear();
        nameTextField.clear();
        emailTextField.clear();
        currentPasswordField.clear();
        newPasswordField.clear();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        var result = alert.showAndWait();
        return result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK;
    }
}
