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

    /**
     * Initializes the controller
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupComboBoxes();
        setupTableView();
        setupCharts();
        loadBudgetData();
    }

    /**
     * Sets up the combo boxes with default values
     */
    private void setupComboBoxes() {
        categoryComboBox.setItems(FXCollections.observableArrayList(defaultCategories));
        timePeriodComboBox.setItems(FXCollections.observableArrayList(timePeriods));
        timePeriodComboBox.setValue("Monthly"); // Default value

        analysisPeriodComboBox.setItems(FXCollections.observableArrayList(analysisPeriods));
        analysisPeriodComboBox.setValue("This Month"); // Default value
    }

    /**
     * Sets up the budget table view
     */
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

    /**
     * Sets up the charts with initial empty data
     */
    private void setupCharts() {
        // Initial empty data for pie chart
        spendingPieChart.setData(FXCollections.observableArrayList());
        spendingPieChart.setTitle("Spending by Category");

        // Clear any existing data in charts
        budgetVsActualChart.getData().clear();
        spendingTrendChart.getData().clear();
    }

    /**
     * Loads budget data from the system
     */
    private void loadBudgetData() {
        // This would typically fetch data from your model/database
        // For now, we'll add some sample data for demonstration
        // In a real app, you would get this from SystemManager or a budgetTracker

        if (SystemManager.isUserLoggedIn()) {
            // TODO: Get actual budget data from the system
            // For now, add sample data
            budgets.add(new Budget("1", "Food", new BigDecimal("300.00"), "Monthly"));
            budgets.add(new Budget("2", "Housing", new BigDecimal("1200.00"), "Monthly"));
            budgets.add(new Budget("3", "Transportation", new BigDecimal("150.00"), "Monthly"));

            // Update charts with the loaded data
            updateBudgetAnalysis();
        }
    }

    /**
     * Handles saving a new budget
     */
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
                    // In real app, you would call your budget tracker's edit method
                    existingBudget.setLimit(amount);
                    existingBudget.setPeriod(period);
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

            // In real app, save to the system via your tracker
            // budgetTracker.add(newBudget);

            // Update analysis and clear form
            updateBudgetAnalysis();
            clearBudgetForm();

            showAlert("Budget saved successfully");

        } catch (NumberFormatException e) {
            showAlert("Please enter a valid numeric amount");
        }
    }

    /**
     * Handles deleting a budget
     */
    private void handleDeleteBudget(Budget budget) {
        budgets.remove(budget);

        // In real app, delete from your system/tracker
        // budgetTracker.delete(budget.getBudgetId());

        updateBudgetAnalysis();
        showAlert("Budget deleted successfully");
    }

    /**
     * Clears the budget form fields
     */
    private void clearBudgetForm() {
        categoryComboBox.setValue(null);
        budgetAmountField.clear();
        timePeriodComboBox.setValue("Monthly");
    }

    /**
     * Handles changing the analysis period
     */
    @FXML
    private void handleAnalysisPeriodChange(ActionEvent event) {
        // Update the analysis based on selected period
        updateBudgetAnalysis();
    }

    /**
     * Handles refreshing the analysis
     */
    @FXML
    private void handleRefreshAnalysis(ActionEvent event) {
        updateBudgetAnalysis();
        showAlert("Analysis refreshed");
    }

    /**
     * Updates the budget analysis data and charts
     */
    private void updateBudgetAnalysis() {
        // Get selected analysis period
        String period = analysisPeriodComboBox.getValue();

        // In a real app, you would fetch spending data for this period
        // For now, we'll generate sample data

        // Update pie chart with sample data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Budget budget : budgets) {
            // Simulate actual spending as some percentage of budget
            double actualPercent = Math.random() * 1.2; // 0% to 120% of budget
            BigDecimal actual = budget.getLimit().multiply(new BigDecimal(actualPercent));
            pieChartData.add(new PieChart.Data(budget.getCategory(), actual.doubleValue()));
        }
        spendingPieChart.setData(pieChartData);

        // Update budget vs. actual bar chart
        // In a real app, this would show budget amount vs actual spending for each category

        // Update spending trend line chart
        // In a real app, this would show spending over time

        // Update progress bar - ratio of total spending to total budget
        // For demo, set to random percentage
        budgetProgressBar.setProgress(Math.random());
    }

    /**
     * Handles exporting the report
     */
    @FXML
    private void handleExportReport(ActionEvent event) {
        // In a real app, this would generate and save a PDF or Excel report
        showAlert("Report export functionality not yet implemented");
    }

    /**
     * Handles navigating back to dashboard
     */
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


    /**
     * Shows an alert dialog with the given message
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}