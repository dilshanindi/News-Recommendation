package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import utils.DatabaseHandler;


public class CreateAccountControl {

    @FXML
    private TextField nicField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private final DatabaseHandler databaseHandler;

    // Constructor initializes DatabaseHandler
    public CreateAccountControl(DatabaseHandler databaseHandler) {
        this.databaseHandler = new DatabaseHandler();
    }

    // Method to handle account creation
    @FXML
    private void handleCreateAccount(ActionEvent event) {
        UserInput userInput = collectUserInput();

        if (!validateUserInput(userInput)) {
            return; // Stop processing if validation fails
        }

        if (databaseHandler.checkNICExists(userInput.getNic())) {
            showAlert("Error", "NIC already exists. Please enter a unique NIC.", Alert.AlertType.ERROR);
            return;
        }

        // Attempt to insert user into database
        if (databaseHandler.addUser(userInput.getNic(), userInput.getName(), userInput.getAge(),
                userInput.getEmail(), userInput.getPassword())) {
            showAlert("Success", "Account created successfully! You can now log in.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Failed to create account. Please try again.", Alert.AlertType.ERROR);
        }
    }

    // Method to collect user input into a data structure
    private UserInput collectUserInput() {
        return new UserInput(
                nicField.getText().trim(),
                nameField.getText().trim(),
                ageField.getText().trim(),
                emailField.getText().trim(),
                passwordField.getText()
        );
    }

    // Method to validate user input
    private boolean validateUserInput(UserInput input) {
        if (input.hasEmptyFields()) {
            showAlert("Error", "All fields are required!", Alert.AlertType.ERROR);
            return false;
        }

        if (!input.isNicValid()) {
            showAlert("Error", "NIC should be a unique integer value.", Alert.AlertType.ERROR);
            return false;
        }

        if (!input.isNameValid()) {
            showAlert("Error", "Name should only contain letters.", Alert.AlertType.ERROR);
            return false;
        }

        if (!input.isAgeValid()) {
            showAlert("Error", "Age should be an integer between 1 and 100.", Alert.AlertType.ERROR);
            return false;
        }

        if (!input.isEmailValid()) {
            showAlert("Error", "Invalid email format.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    // Method to handle clearing of input fields
    @FXML
    private void handleClear(ActionEvent event) {
        nicField.clear();
        nameField.clear();
        ageField.clear();
        emailField.clear();
        passwordField.clear();
    }

    // Utility method to show alerts
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Inner class to encapsulate user input data
    private static class UserInput {
        private final String nic;
        private final String name;
        private final String ageText;
        private final String email;
        private final String password;

        public UserInput(String nic, String name, String ageText, String email, String password) {
            this.nic = nic;
            this.name = name;
            this.ageText = ageText;
            this.email = email;
            this.password = password;
        }

        public String getNic() {
            return nic;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            try {
                return Integer.parseInt(ageText);
            } catch (NumberFormatException e) {
                return -1; // Invalid age
            }
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public boolean hasEmptyFields() {
            return nic.isEmpty() || name.isEmpty() || ageText.isEmpty() || email.isEmpty() || password.isEmpty();
        }

        public boolean isNicValid() {
            return nic.matches("\\d+");
        }

        public boolean isNameValid() {
            return name.matches("[a-zA-Z\\s]+");
        }

        public boolean isAgeValid() {
            int age = getAge();
            return age > 0 && age <= 100;
        }

        public boolean isEmailValid() {
            return email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
        }
    }
}
