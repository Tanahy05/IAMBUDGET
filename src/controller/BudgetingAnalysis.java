package controller;

import interfaces.Tracker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Budget;
import model.SystemManager;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for Budget Analysis View
 * Handles budget setting, tracking, and visualization
 */
public class BudgetingAnalysis implements Initializable {

    // Form fields for budget setting
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField budgetAmountField;
    @FXML private ComboBox<String> timePeriodComboBox;
    @FXML private Button saveBudgetButton;

    // Budget table
    @FXML private TableView<Budget> budgetCategoriesTable;
    @FXML private TableColumn<Budget, String> categoryNameColumn;
    @FXML private TableColumn<Budget, BigDecimal> budgetAmountColumn;
    @FXML private TableColumn<Budget, String> timePeriodColumn;
    @FXML private TableColumn<Budget, Button> actionsColumn;

    // Analysis controls
    @FXML private ComboBox<String> analysisPeriodComboBox;
    @FXML private Button refreshAnalysisButton;
    @FXML private ProgressBar budgetProgressBar;

    // Charts
    @FXML private PieChart spendingPieChart;
    @FXML private BarChart<String, Number> budgetVsActualChart;
    @FXML private LineChart<String, Number> spendingTrendChart;

    // Navigation buttons
    @FXML private Button exportButton;
    @FXML private Button dashboardButton;

    // Model data
    private ObservableList<Budget> budgets;
    private List<String> defaultCategories = List.of("Food", "Housing", "Transportation", "Entertainment", "Utilities", "Healthcare", "Other");
    private List<String> timePeriods = List.of("Daily", "Weekly", "Monthly", "Yearly");
    private List<String> analysisPeriods = List.of("This Week", "This Month", "Last Month", "Last 3 Months", "This Year");

    // Budget Tracker reference
    private BudgetTracker budgetTracker;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Get the budget tracker from SystemManager
        budgetTracker = SystemManager.getBudgetTracker();

        setupComboBoxes();
        setupTableView();
        setupCharts();
        loadBudgetData();
    }


    private void setupComboBoxes() {
        categoryComboBox.setItems(FXCollections.observableArrayList(defaultCategories));
        timePeriodComboBox.setItems(FXCollections.observableArrayList(timePeriods));
        timePeriodComboBox.setValue("Monthly"); // Default value

        analysisPeriodComboBox.setItems(FXCollections.observableArrayList(analysisPeriods));
        analysisPeriodComboBox.setValue("This Month"); // Default value
    }


    private void setupTableView() {
        // Initialize the budgets list
        budgets = FXCollections.observableArrayList();

        // Configure table columns
        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        budgetAmountColumn.setCellValueFactory(new PropertyValueFactory<>("limit"));
        timePeriodColumn.setCellValueFactory(new PropertyValueFactory<>("period"));

        // Custom cell factory for the actions column
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    deleteButton.setOnAction(event -> {
                        Budget budget = getTableView().getItems().get(getIndex());
                        handleDeleteBudget(budget);
                    });
                    setGraphic(deleteButton);
                }
            }
        });

        budgetCategoriesTable.setItems(budgets);
    }


    private void setupCharts() {
        // Initial empty data for pie chart
        spendingPieChart.setData(FXCollections.observableArrayList());
        spendingPieChart.setTitle("Spending by Category");

        // Clear any existing data in charts
        budgetVsActualChart.getData().clear();
        spendingTrendChart.getData().clear();
    }


    private void loadBudgetData() {
        if (SystemManager.isUserLoggedIn()) {
            // Clear existing budgets
            budgets.clear();

            // Load budgets from the tracker
            List<Budget> loadedBudgets = budgetTracker.getBudgets();
            budgets.addAll(loadedBudgets);

            // Update charts with the loaded data
            updateBudgetAnalysis();
        }
    }

    @FXML
    private void handleSaveBudget(ActionEvent event) {
        String category = categoryComboBox.getValue();
        String amountText = budgetAmountField.getText();
        String period = timePeriodComboBox.getValue();

        // Validate inputs
        if (category == null || category.isEmpty()) {
            showAlert("Please select a category");
            return;
        }

        if (amountText == null || amountText.isEmpty()) {
            showAlert("Please enter a budget amount");
            return;
        }

        try {
            BigDecimal amount = new BigDecimal(amountText);

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                showAlert("Budget amount must be greater than zero");
                return;
            }

            // Check if budget for this category already exists
            for (Budget existingBudget : budgets) {
                if (existingBudget.getCategory().equals(category)) {
                    // Update existing budget
                    existingBudget.setLimit(amount);
                    existingBudget.setPeriod(period);

                    // Update in the tracker
                    budgetTracker.edit(existingBudget);

                    budgetCategoriesTable.refresh();
                    showAlert("Budget updated successfully");
                    updateBudgetAnalysis();
                    clearBudgetForm();
                    return;
                }
            }

            // Create new budget
            String budgetId = String.valueOf(System.currentTimeMillis()); // Simple ID generation
            Budget newBudget = new Budget(budgetId, category, amount, period);

            // Add to UI list
            budgets.add(newBudget);

            // Add to the tracker
            budgetTracker.add(newBudget);

            // Save to persistence
            SystemManager.saveUserData();

            // Update analysis and clear form
            updateBudgetAnalysis();
            clearBudgetForm();

            showAlert("Budget saved successfully");

        } catch (NumberFormatException e) {
            showAlert("Please enter a valid numeric amount");
        }
    }


    private void handleDeleteBudget(Budget budget) {
        budgets.remove(budget);

        // Delete from the tracker
        budgetTracker.delete(budget.getBudgetId());

        // Save changes
        SystemManager.saveUserData();

        updateBudgetAnalysis();
        showAlert("Budget deleted successfully");
    }


    private void clearBudgetForm() {
        categoryComboBox.setValue(null);
        budgetAmountField.clear();
        timePeriodComboBox.setValue("Monthly");
    }


    @FXML
    private void handleAnalysisPeriodChange(ActionEvent event) {

        updateBudgetAnalysis();
    }


    @FXML
    private void handleRefreshAnalysis(ActionEvent event) {
        updateBudgetAnalysis();
        showAlert("Analysis refreshed");
    }


    private void updateBudgetAnalysis() {

        String period = analysisPeriodComboBox.getValue();



        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Budget budget : budgets) {

            double actualPercent = Math.random() * 1.2;
            BigDecimal actual = budget.getLimit().multiply(new BigDecimal(actualPercent));
            pieChartData.add(new PieChart.Data(budget.getCategory(), actual.doubleValue()));
        }
        spendingPieChart.setData(pieChartData);


        budgetProgressBar.setProgress(Math.random());
    }


    @FXML
    private void handleExportReport(ActionEvent event) {
        // In a real app, this would generate and save a PDF or Excel report
        showAlert("Report export functionality not yet implemented");
    }


    @FXML
    private void handleBackToDashboard(ActionEvent event) {
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


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}