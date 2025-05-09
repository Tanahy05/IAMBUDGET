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


public class Login {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink signupLink;

    @FXML
    private Button backButton;
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // TODO: Implement actual authentication logic
        int accepted=Auth.login(username, password);
        // For demonstration, just print entered credentials
        System.out.println("Login attempt with username: " + username);
        if(accepted==0){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setContentText("Username doesn't exist");
            alert.showAndWait();


        }
        else if(accepted==1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setContentText("Password is incorrect");
            alert.showAndWait();
        }
        else{
            navigateToDashboard(event);
        }

    }

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

    // For future implementation
    private void navigateToDashboard(ActionEvent event) {
        try {
            // Replace with your actual dashboard FXML path
            Parent dashboardPage = FXMLLoader.load(getClass().getResource("ui/dashboard.fxml"));
            Scene dashboardScene = new Scene(dashboardPage);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(dashboardScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}