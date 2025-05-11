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

/**
 * Controller class for the navigation bar component in the application.
 * <p>
 * This controller handles navigation between different screens of the financial management
 * application, including income tracking, budgeting, reminders, dashboard, expense tracking,
 * and logout functionality. Each method responds to button actions within the navigation bar
 * UI component to load the corresponding view.
 * </p>
 */
public class NavBar {

    /**
     * Navigates to the Income Tracking screen.
     * <p>
     * This method loads the TrackingIncome.fxml file and sets it as the current scene.
     * </p>
     *
     * @param event The ActionEvent triggered by clicking the Income navigation button
     */
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

    /**
     * Navigates to the Budget Analysis screen.
     * <p>
     * This method loads the BudgetingAnalysis.fxml file and sets it as the current scene.
     * </p>
     *
     * @param event The ActionEvent triggered by clicking the Budgets navigation button
     */
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

    /**
     * Navigates to the Reminders screen.
     * <p>
     * This method loads the Reminders.fxml file and sets it as the current scene.
     * </p>
     *
     * @param event The ActionEvent triggered by clicking the Reminders navigation button
     */
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

    /**
     * Navigates to the Dashboard screen.
     * <p>
     * This method loads the Dashboard.fxml file and sets it as the current scene.
     * The dashboard provides an overview of the user's financial status.
     * </p>
     *
     * @param event The ActionEvent triggered by clicking the Dashboard navigation button
     */
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

    /**
     * Navigates to the Expense Tracking screen.
     * <p>
     * This method loads the ExpenseTracker.fxml file and sets it as the current scene.
     * </p>
     *
     * @param event The ActionEvent triggered by clicking the Expense navigation button
     */
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

    /**
     * Logs out the current user and navigates to the Home Page.
     * <p>
     * This method calls the SystemManager's logout functionality to clear current user data,
     * then loads the HomePage.fxml file and sets it as the current scene.
     * </p>
     *
     * @param event The ActionEvent triggered by clicking the Logout button
     */
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