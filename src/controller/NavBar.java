package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.SystemManager;

import java.io.IOException;

public class NavBar {
    @FXML
    private void showIncome(ActionEvent event){
        try {
            Parent homePage = FXMLLoader.load(getClass().getResource("/ui/TrackingIncome.fxml"));
            Scene homeScene = new Scene(homePage);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(homeScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showBudgets(ActionEvent event) {
        try {
            Parent homePage = FXMLLoader.load(getClass().getResource("/ui/BudgetingAnalysis.fxml"));
            Scene homeScene = new Scene(homePage);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(homeScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showReminders(ActionEvent event) {
        try {
            Parent homePage = FXMLLoader.load(getClass().getResource("/ui/Reminders.fxml"));
            Scene homeScene = new Scene(homePage);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(homeScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showDashboard(ActionEvent event) {
        try {
            Parent homePage = FXMLLoader.load(getClass().getResource("/ui/Dashboard.fxml"));
            Scene homeScene = new Scene(homePage);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(homeScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showExpense(ActionEvent event) {
        try {
            Parent homePage = FXMLLoader.load(getClass().getResource("/ui/ExpenseTracker.fxml"));
            Scene homeScene = new Scene(homePage);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(homeScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        SystemManager.logoutUser();
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
