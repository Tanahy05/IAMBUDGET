package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Reminder;
import controller.ReminderTracker;
import model.SystemManager;
import model.User;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controller for the Reminders view
 */
public class Reminders implements Initializable {

    @FXML
    private TextField reminderNameField;

    @FXML
    private TextField amountField;

    @FXML
    private DatePicker dueDatePicker;

    @FXML
    private DatePicker reminderDatePicker;

    @FXML
    private Button saveReminderButton;

    @FXML
    private Button clearFormButton;

    private ReminderTracker reminderTracker;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize with today's date
        dueDatePicker.setValue(LocalDate.now());
        reminderDatePicker.setValue(LocalDate.now());

        // Get the reminder tracker instance
        reminderTracker = SystemManager.getReminderTracker();

        // Disable the form if no user is logged in
        boolean userLoggedIn = SystemManager.isUserLoggedIn();
        setFormDisabled(!userLoggedIn);

        if (!userLoggedIn) {
            showLoginAlert();
        }
    }

    /**
     * Handle saving a new reminder
     */
    @FXML
    void handleSaveReminder(ActionEvent event) {
        if (!validateForm()) {
            return;
        }

        User currentUser = SystemManager.getCurrentUser();
        if (currentUser == null) {
            showLoginAlert();
            return;
        }

        try {
            String name = reminderNameField.getText().trim();
            double amount = Double.parseDouble(amountField.getText().trim());
            LocalDate dueDate = dueDatePicker.getValue();
            LocalDate reminderDate = reminderDatePicker.getValue();

            // Create and add the reminder
            Reminder reminder = reminderTracker.addReminder(
                    name, amount, dueDate, reminderDate, currentUser.getUserID());

            // Save the reminder to storage
            SystemManager.saveUserData();

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Reminder Created",
                    "Reminder '" + name + "' has been created successfully!");

            // Clear the form
            handleClearForm(null);

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Amount",
                    "Please enter a valid number for the amount.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handle clearing the form
     */
    @FXML
    void handleClearForm(ActionEvent event) {
        reminderNameField.clear();
        amountField.clear();
        dueDatePicker.setValue(LocalDate.now());
        reminderDatePicker.setValue(LocalDate.now());
    }

    /**
     * Validate the form fields
     */
    private boolean validateForm() {
        StringBuilder errorMessage = new StringBuilder();

        if (reminderNameField.getText().trim().isEmpty()) {
            errorMessage.append("- Reminder name cannot be empty\n");
        }

        if (amountField.getText().trim().isEmpty()) {
            errorMessage.append("- Amount cannot be empty\n");
        } else {
            try {
                double amount = Double.parseDouble(amountField.getText().trim());
                if (amount < 0) {
                    errorMessage.append("- Amount cannot be negative\n");
                }
            } catch (NumberFormatException e) {
                errorMessage.append("- Amount must be a valid number\n");
            }
        }

        if (dueDatePicker.getValue() == null) {
            errorMessage.append("- Due date must be selected\n");
        }

        if (reminderDatePicker.getValue() == null) {
            errorMessage.append("- Reminder date must be selected\n");
        } else if (dueDatePicker.getValue() != null &&
                reminderDatePicker.getValue().isAfter(dueDatePicker.getValue())) {
            errorMessage.append("- Reminder date cannot be after due date\n");
        }

        if (errorMessage.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Form Validation Error",
                    "Please correct the following errors:\n" + errorMessage.toString());
            return false;
        }

        return true;
    }

    /**
     * Show an alert dialog
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Show login alert
     */
    private void showLoginAlert() {
        showAlert(Alert.AlertType.WARNING, "Login Required",
                "You must log in to create reminders.");
    }

    /**
     * Enable or disable form elements
     */
    private void setFormDisabled(boolean disabled) {
        reminderNameField.setDisable(disabled);
        amountField.setDisable(disabled);
        dueDatePicker.setDisable(disabled);
        reminderDatePicker.setDisable(disabled);
        saveReminderButton.setDisable(disabled);
        clearFormButton.setDisable(disabled);
    }
}