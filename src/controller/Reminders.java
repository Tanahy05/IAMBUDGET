
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
 * Controller for the Reminders view.
 * Handles user interactions for creating and managing reminders.
 * This class manages the UI components and business logic for the reminder creation form.
 */
public class Reminders implements Initializable {

    /**
     * Text field for entering the reminder name/description
     */
    @FXML
    private TextField reminderNameField;

    /**
     * Text field for entering the amount associated with the reminder
     */
    @FXML
    private TextField amountField;

    /**
     * Date picker for selecting the due date of the reminder
     */
    @FXML
    private DatePicker dueDatePicker;

    /**
     * Date picker for selecting when the user should be reminded
     */
    @FXML
    private DatePicker reminderDatePicker;

    /**
     * Button for saving a new reminder
     */
    @FXML
    private Button saveReminderButton;

    /**
     * Button for clearing the reminder form
     */
    @FXML
    private Button clearFormButton;

    /**
     * Reference to the reminder tracker service
     */
    private ReminderTracker reminderTracker;

    /**
     * Initializes the controller.
     * Sets up initial values for UI components and checks user login status.
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
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
     * Handles the save reminder button action.
     * Validates form input, creates a new reminder, and saves it to storage.
     *
     * @param event The action event triggered by clicking the save button
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
     * Handles the clear form button action.
     * Resets all form fields to their default values.
     *
     * @param event The action event triggered by clicking the clear button, can be null if called programmatically
     */
    @FXML
    void handleClearForm(ActionEvent event) {
        reminderNameField.clear();
        amountField.clear();
        dueDatePicker.setValue(LocalDate.now());
        reminderDatePicker.setValue(LocalDate.now());
    }

    /**
     * Validates all form input fields.
     * Checks for required fields, valid numeric values, and logical date relationships.
     *
     * @return true if all form inputs are valid, false otherwise
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
     * Displays an alert dialog with the specified type, title, and message.
     *
     * @param alertType The type of alert to display (e.g., ERROR, INFORMATION)
     * @param title The title of the alert dialog
     * @param message The content message to display in the alert
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays a login required alert dialog.
     * Informs the user that they must log in to create reminders.
     */
    private void showLoginAlert() {
        showAlert(Alert.AlertType.WARNING, "Login Required",
                "You must log in to create reminders.");
    }

    /**
     * Enables or disables all form components.
     * Used to restrict access when no user is logged in.
     *
     * @param disabled true to disable all form components, false to enable them
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