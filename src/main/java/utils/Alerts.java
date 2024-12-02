package utils;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.regex.Pattern;

public class Alerts {

    // Show alert messages with an option to add custom buttons
    public static void showAlert(String title, String message, Alert.AlertType alertType, ButtonType... buttons) {
        Alert alert = new Alert(alertType, message, buttons.length > 0 ? buttons : ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);  // Header can be customized if needed
        alert.showAndWait();
    }

    // Validate email format (Regular Expression adjusted for broader matches)
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(regex);
    }

    // Validate NIC format (Assumes a valid NIC is a numeric value)
    public static boolean isValidNIC(String nic) {
        return nic != null && nic.matches("\\d{9,12}"); // NIC is numeric and should be 9 to 12 digits long
    }

    // Validate name format (letters, spaces, and limited special characters for names)
    public static boolean isValidName(String name) {
        return name != null && name.matches("^[a-zA-Z\\s]+$"); // Allow only letters and spaces
    }

    // Validate age: Must be an integer between 1 and 100
    public static boolean isValidAge(String ageText) {
        try {
            int age = Integer.parseInt(ageText);
            return age >= 1 && age <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Check for non-empty fields
    public static boolean areFieldsNonEmpty(String... fields) {
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                return false; // If any field is empty, return false
            }
        }
        return true; // All fields are non-empty
    }

    // Validate phone number (Accepts only digits and must be 10 digits long)
    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("^\\d{10}$");
    }

    // Validate password strength (at least 8 characters, includes one number and one special character)
    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        return password.length() >= 8 &&
                Pattern.compile("[0-9]").matcher(password).find() &&  // At least one digit
                Pattern.compile("[^a-zA-Z0-9]").matcher(password).find(); // At least one special character
    }
}
