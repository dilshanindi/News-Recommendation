package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.newsrec.NewsRec;
import models.Admin;
import models.Manager;
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

        // Fetch Admin details using the singleton Manager instance
        Manager manager = Manager.getInstance();
        Admin admin = new Admin(
                manager.getLoggedInUserName(),  // Admin's name
                manager.getLoggedInUserEmail(), // Admin's email
                manager.getLoggedInUserNIC(),   // Admin's NIC
                String.valueOf(0)                              // Default age (fetch if needed)
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
        // Existing search functionality
    }

    @FXML
    private void handleUpdate() {
        // Existing update functionality
    }

    @FXML
    private void handleDelete() {
        // Existing delete functionality
    }

    private void clearFields() {
        // Existing clear fields functionality
    }

    @FXML
    private void handleClearHistory() {
        // Existing clear history functionality
    }

    @FXML
    private void handleLogOut() {
        try {
            Manager.getInstance().clearSession(); // Clear session using the singleton instance
            NewsRec newsRecSys = new NewsRec();
            newsRecSys.showLoginPage(); // Navigate to the login page
        } catch (Exception e) {
            Alerts.showAlert("Error", "Failed to log out: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void handleHomePage() {
        // Navigate to Admin Home Page
        Manager manager = Manager.getInstance();
        Admin admin = new Admin(
                manager.getLoggedInUserName(), // Name
                manager.getLoggedInUserEmail(), // Email
                manager.getLoggedInUserNIC(),
                String.valueOf(0)// NIC
                 // Age (default value if not needed here, replace with actual if required)
        );

        new com.example.newsrec.NewsRec().showAdminDashboard(admin);
    }
}
