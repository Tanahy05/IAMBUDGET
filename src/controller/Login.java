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
import model.SystemManager;

/**
 * Controller class for the application login view.
 * Manages user authentication and navigation between login-related screens.
 */
public class Login {

    /**
     * Text field for entering username.
     */
    @FXML
    private TextField usernameField;

    /**
     * Password field for entering user password securely.
     */
    @FXML
    private PasswordField passwordField;

    /**
     * Button to trigger the login process.
     */
    @FXML
    private Button loginButton;

    /**
     * Hyperlink to navigate to the signup page.
     */
    @FXML
    private Hyperlink signupLink;

    /**
     * Button to navigate back to the previous screen.
     */
    @FXML
    private Button backButton;

    /**
     * Handles the login button action.
     * Validates user credentials and navigates to dashboard on success.
     * Shows appropriate error messages on authentication failure.
     *
     * @param event The action event triggered by clicking the login button
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        int accepted = Auth.login(username, password);
        System.out.println("Login attempt with username: " + username);
        if(accepted == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setContentText("Username doesn't exist");
            alert.showAndWait();
        }
        else if(accepted == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setContentText("Password is incorrect");
            alert.showAndWait();
        }
        else {
            SystemManager.loginUser(Auth.findUser(username));
            navigateToDashboard(event);
        }
    }

    /**
     * Switches from the login view to the signup view.
     * Loads the signup FXML and sets it to the current stage.
     *
     * @param event The action event triggered by clicking the signup link
     */
    @FXML
    private void switchToSignup(ActionEvent event) {
        try {
            Parent signupPage = FXMLLoader.load(getClass().getResource("/ui/Signup.fxml"));
            Scene signupScene = new Scene(signupPage);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(signupScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the back button action to return to the home page.
     * Loads the home page FXML and sets it to the current stage.
     *
     * @param event The action event triggered by clicking the back button
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

    /**
     * Navigates to the dashboard view after successful login.
     * Loads the dashboard FXML and sets it to the current stage.
     *
     * @param event The action event from the login button
     */
    private void navigateToDashboard(ActionEvent event) {
        try {
            // Replace with your actual dashboard FXML path
            Parent dashboardPage = FXMLLoader.load(getClass().getResource("/ui/dashboard.fxml"));
            Scene dashboardScene = new Scene(dashboardPage);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(dashboardScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}