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
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Budget;
import model.SystemManager;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BudgetingAnalysis implements Initializable {

    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField budgetAmountField;
    @FXML private ComboBox<String> timePeriodComboBox;
    @FXML private Button saveBudgetButton;

    @FXML private TableView<Budget> budgetCategoriesTable;
    @FXML private TableColumn<Budget, String> categoryNameColumn;
    @FXML private TableColumn<Budget, BigDecimal> budgetAmountColumn;
    @FXML private TableColumn<Budget, String> timePeriodColumn;
    @FXML private TableColumn<Budget, Button> actionsColumn;

    @FXML private ComboBox<String> analysisPeriodComboBox;

    @FXML private PieChart spendingPieChart;

    @FXML private Button exportButton;
    @FXML private Button dashboardButton;

    private ObservableList<Budget> budgets;
    private final List<String> defaultCategories = List.of("Food", "Housing", "Transportation", "Entertainment", "Utilities", "Healthcare", "Other");
    private final List<String> timePeriods = List.of("Daily", "Weekly", "Monthly", "Yearly");
    private final List<String> analysisPeriods = List.of("This Week", "This Month", "Last Month", "Last 3 Months", "This Year");

    private BudgetTracker budgetTracker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        budgetTracker = SystemManager.getBudgetTracker();
        setupComboBoxes();
        setupTableView();
        setupCharts();
        loadBudgetData();
    }

    private void setupComboBoxes() {
        categoryComboBox.setItems(FXCollections.observableArrayList(defaultCategories));
        timePeriodComboBox.setItems(FXCollections.observableArrayList(timePeriods));
        timePeriodComboBox.setValue("Monthly");

        analysisPeriodComboBox.setItems(FXCollections.observableArrayList(analysisPeriods));
        analysisPeriodComboBox.setValue("This Month");
    }

    private void setupTableView() {
        budgets = FXCollections.observableArrayList();

        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        budgetAmountColumn.setCellValueFactory(new PropertyValueFactory<>("limit"));
        timePeriodColumn.setCellValueFactory(new PropertyValueFactory<>("period"));

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
        spendingPieChart.setData(FXCollections.observableArrayList());
        spendingPieChart.setTitle("Spending by Category");
    }

    private void loadBudgetData() {
        if (SystemManager.isUserLoggedIn()) {
            budgets.clear();
            List<Budget> loadedBudgets = budgetTracker.getBudgets();
            budgets.addAll(loadedBudgets);
            updateBudgetAnalysis();
        }
    }

    @FXML
    private void handleSaveBudget(ActionEvent event) {
        String category = categoryComboBox.getValue();
        String amountText = budgetAmountField.getText();
        String period = timePeriodComboBox.getValue();

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

            for (Budget existingBudget : budgets) {
                if (existingBudget.getCategory().equals(category)) {
                    existingBudget.setLimit(amount);
                    existingBudget.setPeriod(period);
                    budgetTracker.edit(existingBudget);
                    budgetCategoriesTable.refresh();
                    showAlert("Budget updated successfully");
                    updateBudgetAnalysis();
                    clearBudgetForm();
                    return;
                }
            }

            String budgetId = String.valueOf(System.currentTimeMillis());
            Budget newBudget = new Budget(budgetId, category, amount, period);
            budgets.add(newBudget);
            budgetTracker.add(newBudget);
            SystemManager.saveUserData();
            updateBudgetAnalysis();
            clearBudgetForm();
            showAlert("Budget saved successfully");

        } catch (NumberFormatException e) {
            showAlert("Please enter a valid numeric amount");
        }
    }

    private void handleDeleteBudget(Budget budget) {
        budgets.remove(budget);
        budgetTracker.delete(budget.getBudgetId());
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

    private void updateBudgetAnalysis() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Budget budget : budgets) {
            double actualPercent = Math.random() * 1.2;
            BigDecimal actual = budget.getLimit().multiply(new BigDecimal(actualPercent));
            pieChartData.add(new PieChart.Data(budget.getCategory(), actual.doubleValue()));
        }
        spendingPieChart.setData(pieChartData);
    }

    @FXML
    private void handleExportReport(ActionEvent event) {
        showAlert("Report export functionality not yet implemented");
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
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
