package controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller class for the application's home page.
 * <p>
 * This controller handles navigation from the home page to other pages
 * in the application, specifically the login and signup pages.
 * It serves as the entry point for users to either log in to an existing
 * account or create a new account.
 * </p>
 */
public class HomePage {

    /**
     * Navigates to the login page
     * @param event The ActionEvent from the button click
     */
    @FXML
    private void navigateToLogin(ActionEvent event) {
        try {
            // Load the login page
            Parent loginPage = FXMLLoader.load(getClass().getClassLoader().getResource("ui/Login.fxml"));
            Scene loginScene = new Scene(loginPage);

            // Get the stage from the event source and change the scene
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(loginScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates to the signup page
     * @param event The ActionEvent from the button click
     */
    @FXML
    private void navigateToSignup(ActionEvent event) {
        try {
            // Load the signup page
            Parent signupPage = FXMLLoader.load(getClass().getClassLoader().getResource("ui/SignUp.fxml"));
            Scene signupScene = new Scene(signupPage);

            // Get the stage from the event source and change the scene
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(signupScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}