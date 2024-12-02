package controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.newsrec.NewsRec;
import models.Admin;
import models.SessionManager;
import models.User;
import utils.Alerts;
import utils.DatabaseHandler;

import java.util.List;

public class AdminProfile {

    @FXML
    private TextField nicField, nameField, ageField, emailField;

    @FXML
    private TextField roleField;

    @FXML
    private Button searchButton, updateButton, deleteButton;

    @FXML
    private Label accountTypeLabel, userNameLabel;

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> nicColumn, nameColumn, emailColumn, roleColumn;

    @FXML
    private TableColumn<User, Integer> ageColumn;

    private final DatabaseHandler dbHandler = new DatabaseHandler();
    private ObservableList<User> users;

    public void initialize() {
        initializeTableColumns();
        loadUsers();
        setupEventHandlers();

        Admin admin = new Admin(
                SessionManager.getLoggedInUserName(),  // Admin's name
                SessionManager.getLoggedInUserEmail(), // Admin's email
                SessionManager.getLoggedInUserNIC(),   // Admin's NIC
                0                                      // Default age (fetch if needed)
        );

        if (admin.getName() == null || admin.getEmail() == null) {
            // Redirect to login if user details are missing
            Alerts.showAlert("Error", "Logged-in user details not found. Redirecting to login.", Alert.AlertType.ERROR);
            handleLogOut();
        } else {
            setAdminDetails(admin); // Display account type and user name
        }
    }

    // Helper method to initialize table columns
    private void initializeTableColumns() {
        nicColumn.setCellValueFactory(new PropertyValueFactory<>("nic"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
    }

    // Helper method to set up event handlers for buttons
    private void setupEventHandlers() {
        searchButton.setOnAction(event -> handleSearch());
        updateButton.setOnAction(event -> handleUpdate());
        deleteButton.setOnAction(event -> handleDelete());
    }

    // Method to set admin details dynamically
    public void setAdminDetails(Admin admin) {
        accountTypeLabel.setText("Admin Account");
        userNameLabel.setText(admin.getName());
    }

    // Load all users into the table
    private void loadUsers() {
        List<User> userList = dbHandler.getAllUsers();
        if (userList != null) {
            users = FXCollections.observableArrayList(userList);
            userTable.setItems(users);
        }
    }

    private void handleSearch() {
        String nic = nicField.getText();

        if (nic.isEmpty()) {
            Alerts.showAlert("Error", "NIC is required to search.", Alert.AlertType.ERROR);
            return;
        }

        // Fetch user details by NIC
        User user = dbHandler.getUserByNIC(nic);

        if (user != null) {
            // Populate text fields with user details
            nameField.setText(user.getName());
            ageField.setText(String.valueOf(user.getAge()));
            emailField.setText(user.getEmail());
            roleField.setText(user.getRole());
        } else {
            // Show error if user not found
            Alerts.showAlert("Error", "No user found with the given NIC.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleUpdate() {
        // Fetch data from input fields
        String nic = nicField.getText();
        String name = nameField.getText();
        String ageText = ageField.getText();
        String email = emailField.getText();
        String role = roleField.getText();

        // Validate input fields
        if (!Alerts.isNonEmpty(nic, name, ageText, email, role)) {
            Alerts.showAlert("Error", "All fields are required.", Alert.AlertType.ERROR);
            return;
        }

        if (!Alerts.isValidAge(ageText)) {
            Alerts.showAlert("Error", "Invalid age format.", Alert.AlertType.ERROR);
            return;
        }

        // Validate role input
        if (!(role.equalsIgnoreCase("user") || role.equalsIgnoreCase("admin"))) {
            Alerts.showAlert("Error", "Role must be either 'user' or 'admin'.", Alert.AlertType.ERROR);
            return;
        }

        // Parse age as integer
        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            Alerts.showAlert("Error", "Age must be a number.", Alert.AlertType.ERROR);
            return;
        }

        boolean isUpdated = dbHandler.updateUserDetailsForAdmin(email, name, Integer.parseInt(ageText), nic, role); // Include role
        if (isUpdated) {
            Alerts.showAlert("Success", "Details updated successfully!", Alert.AlertType.INFORMATION);
        } else {
            Alerts.showAlert("Error", "NIC already exists or failed to update details. Please try again.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDelete() {
        String nic = nicField.getText(); // Get the NIC from the input field

        // Validate NIC
        if (nic.isEmpty()) {
            Alerts.showAlert("Error", "NIC is required to delete a user.", Alert.AlertType.ERROR);
            return;
        }

        // Confirm the deletion with the admin
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Confirmation");
        confirmationAlert.setHeaderText("Are you sure?");
        confirmationAlert.setContentText("This will permanently delete the user account.");

        // Wait for admin confirmation
        ButtonType result = confirmationAlert.showAndWait().orElse(ButtonType.CANCEL);
        if (result != ButtonType.OK) {
            return; // Cancel the deletion if not confirmed
        }

        // Call the database handler to delete the user
        boolean isDeleted = dbHandler.deleteUserByNIC(nic);
        if (isDeleted) {
            Alerts.showAlert("Success", "User account deleted successfully!", Alert.AlertType.INFORMATION);
            loadUsers(); // Refresh the table view after deletion
            clearFields(); // Clear the input fields
        } else {
            Alerts.showAlert("Error", "Failed to delete the user account. Please try again.", Alert.AlertType.ERROR);
        }
    }

    // Clear input fields after deletion
    private void clearFields() {
        nicField.clear();
        nameField.clear();
        ageField.clear();
        emailField.clear();
        roleField.clear();
    }

    @FXML
    // Handle clear history functionality
    private void handleClearHistory() {
        String email = emailField.getText();
        if (email.isEmpty()) {
            Alerts.showAlert("Error", "Email is required to clear history.", Alert.AlertType.ERROR);
            return;
        }
        boolean isCleared = dbHandler.clearHistory(email);
        if (isCleared) {
            Alerts.showAlert("Success", "User history cleared successfully.", Alert.AlertType.INFORMATION);
        } else {
            Alerts.showAlert("Error", "Failed to clear user history.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleLogOut() {
        try {
            SessionManager.clearSession();
            NewsRecommendationSystem newsRecSys = new NewsRecommendationSystem();
            newsRecSys.showLoginPage(); // Navigate to the login page
        } catch (Exception e) {
            Alerts.showAlert("Error", "Failed to log out: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Handle navigation to home page
    private void handleHomePage() {
        // Create an Admin object using session data
        Admin admin = new Admin(
                SessionManager.getLoggedInUserName(), // Name
                SessionManager.getLoggedInUserEmail(), // Email
                SessionManager.getLoggedInUserNIC(), // NIC
                0 // Age (default value if not needed here, replace with actual if required)
        );

        // Navigate to Admin Home Page
        new org.example.newsrecsys.NewsRecommendationSystem().showAdminHomePage(admin);
    }
}
