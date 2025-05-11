package controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.User;
import storage.UserDatabase;

/**
 * Controller class for the Sign Up page in a JavaFX application.
 * Handles user input validation and account creation logic.
 */
public class SignUp {

    /** Field for entering the user's full name. */
    @FXML
    private TextField nameField;

    /** Field for entering the desired username. */
    @FXML
    private TextField usernameField;

    /** Field for entering the password. */
    @FXML
    private PasswordField passwordField;

    /** Field for confirming the entered password. */
    @FXML
    private PasswordField confirmPasswordField;

    /** Button that triggers the sign-up process. */
    @FXML
    private Button signupButton;

    /** Hyperlink to navigate to the login screen. */
    @FXML
    private Hyperlink loginLink;

    /** Button to go back to the previous screen (Home Page). */
    @FXML
    private Button backButton;

    /**
     * Handles the sign-up process, including input validation and
     * user registration via the Auth class.
     *
     * @param event The action event triggered by clicking the sign-up button.
     */
    @FXML
    private void handleSignup(ActionEvent event) {
        String name = nameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Basic input validation
        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Sign Up Error");
            alert.setContentText("Cannot leave empty fields");
            alert.showAndWait();
            System.out.println("Please fill in all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Sign Up Error");
            alert.setContentText("Passwords do not match");
            alert.showAndWait();
            System.out.println("Passwords don't match");
            return;
        }

        // Create a new User object
        User user = new User(UserDatabase.getNextUserId(), name, username, password);

        // Register the user using the Auth class
        boolean check = Auth.registerUser(user);

        if (check) {
            switchToLogin(event);
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Sign Up Error");
            alert.setContentText("Username already exists");
            alert.showAndWait();
        }
    }

    /**
     * Navigates the user to the login screen.
     *
     * @param event The action event triggered by the user (e.g., clicking login link or after signup).
     */
    @FXML
    private void switchToLogin(ActionEvent event) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/ui/Login.fxml"));
            Scene loginScene = new Scene(loginPage);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(loginScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates the user back to the home page.
     *
     * @param event The action event triggered by the back button.
     */
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Parent homePage = FXMLLoader.load(getClass().getResource("/ui/HomePage.fxml"));
            Scene homeScene = new Scene(homePage);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(homeScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
