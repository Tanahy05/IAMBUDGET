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


public class SignUp{

    @FXML
    private TextField nameField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button signupButton;

    @FXML
    private Hyperlink loginLink;

    @FXML
    private Button backButton;

    @FXML
    private void handleSignup(ActionEvent event) {
        String name = nameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Basic validation
        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Sign Up Error");
                alert.setContentText("Cannot leave empty fields");
                alert.showAndWait();


            System.out.println("Please fill in all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Sign Up Error");
                alert.setContentText("Passwords do not match");
                alert.showAndWait();

            System.out.println("Passwords don't match");
            return;
        }
        User user= new User(UserDatabase.getNextUserId(),name,username,password);
        // TODO: Implement actual account creation logic
        System.out.println("Account creation for: " + username);
        boolean check= Auth.registerUser(user);
        // After successful signup, navigate to login page
        if (check) {
            switchToLogin(event);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sign Up Error");
            alert.setContentText("Username already exists");
            alert.showAndWait();
        }

    }

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