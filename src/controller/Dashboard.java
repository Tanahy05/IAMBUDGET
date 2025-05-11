package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Dashboard {

    // Add @FXML annotations to all UI components
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, String> typeColumn;
    @FXML private TableColumn<Transaction, String> categoryColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;
    @FXML private TableColumn<Transaction, LocalDate> dateColumn;

    @FXML private TableView<BudgetViewModel> budgetTable;
    @FXML private TableColumn<BudgetViewModel, String> budgetCategoryColumn;
    @FXML private TableColumn<BudgetViewModel, BigDecimal> limitColumn;
    @FXML private TableColumn<BudgetViewModel, String> periodColumn;

    @FXML private ListView<DisplayReminder> remindersList;
    @FXML private PieChart spendingChart;
    @FXML private Label totalIncomeLabel;
    @FXML private Label totalExpenseLabel;
    @FXML private Label balanceLabel;

    private BudgetTracker budgetTracker;

    @FXML
    public void initialize() {
        // Get the budget tracker instance
        budgetTracker = SystemManager.getBudgetTracker();

        // Initialize only if components are not null
        if (transactionTable != null) {
            initializeTransactionTable();
        }
        if (budgetTable != null) {
            initializeBudgetTable();
        }

        // Load sample transaction data (since you didn't provide transaction tracker code)
        loadSampleTransactionData();

        // Load actual budget data
        loadActualBudgetData();

        // Load actual reminders
        loadReminders();

        // Only update summary if labels exist
        if (totalIncomeLabel != null && totalExpenseLabel != null && balanceLabel != null) {
            updateSummary();
        }

        // Update spending chart with actual budget data
        updateSpendingChart();
    }

    private void initializeTransactionTable() {
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private void initializeBudgetTable() {
        budgetCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        limitColumn.setCellValueFactory(new PropertyValueFactory<>("limit"));
        periodColumn.setCellValueFactory(new PropertyValueFactory<>("period"));
    }

    private void loadSampleTransactionData() {
        if (transactionTable == null || !SystemManager.isUserLoggedIn()) {
            return;
        }

        // Get the actual expenses and incomes
        List<Expense> actualExpenses = ExpenseTracker.getInstance().getExpenses();
        List<Income> actualIncomes = IncomeTracker.getInstance().getUserIncome(SystemManager.getCurrentUser().getUserID());

        ObservableList<Transaction> transactionList = FXCollections.observableArrayList();

        // Add last 5 incomes to the transaction list
        int incomeSize = actualIncomes.size();
        int incomeStart = Math.max(0, incomeSize - 5);
        for (int i = incomeStart; i < incomeSize; i++) {
            Income income = actualIncomes.get(i);
            transactionList.add(new Transaction(
                    "Income", // Type of transaction
                    income.getSource(), // Category
                    income.getAmount().doubleValue(), // Amount (convert to double if it's BigDecimal)
                    income.getDate() // Date
            ));
        }

        // Add last 5 expenses to the transaction list
        int expenseSize = actualExpenses.size();
        int expenseStart = Math.max(0, expenseSize - 5);
        for (int i = expenseStart; i < expenseSize; i++) {
            Expense expense = actualExpenses.get(i);
            transactionList.add(new Transaction(
                    "Expense", // Type of transaction
                    expense.getCategory(), // Category
                    expense.getAmount(), // Amount (convert to double if it's BigDecimal)
                    expense.getDate() // Date
            ));
        }

        // Set the transaction data into the table
        transactionTable.setItems(transactionList);
    }

    private void loadActualBudgetData() {
        if (budgetTable == null || !SystemManager.isUserLoggedIn()) {
            return;
        }

        // Convert the model Budget objects to BudgetViewModel objects for display
        List<model.Budget> actualBudgets = budgetTracker.getBudgets();

        ObservableList<BudgetViewModel> budgetViewModels = FXCollections.observableArrayList();

        for (model.Budget budget : actualBudgets) {
            budgetViewModels.add(new BudgetViewModel(
                    budget.getCategory(),
                    budget.getLimit(),
                    budget.getPeriod()
            ));
        }

        budgetTable.setItems(budgetViewModels);

        // Log the count of budgets loaded
        System.out.println("Loaded " + budgetViewModels.size() + " budgets for dashboard display");
    }

    private void updateSpendingChart() {
        if (spendingChart == null || !SystemManager.isUserLoggedIn()) {
            return;
        }

        // Get actual budgets
        List<model.Budget> actualBudgets = budgetTracker.getBudgets();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (model.Budget budget : actualBudgets) {
            // You might want to use actual expense data instead of the budget limit
            // This is just a placeholder to show the categories
            pieChartData.add(new PieChart.Data(
                    budget.getCategory(),
                    budget.getLimit().doubleValue()
            ));
        }

        spendingChart.setData(pieChartData);
    }

    /**
     * Load reminders from the ReminderTracker for the current user
     */
    private void loadReminders() {
        if (remindersList == null || !SystemManager.isUserLoggedIn()) {
            return;
        }

        int userId = SystemManager.getCurrentUser().getUserID();
        ReminderTracker reminderTracker = SystemManager.getReminderTracker();
        List<Reminder> userReminders = reminderTracker.getUserReminders(userId);

        // Convert Reminder model objects to DisplayReminder for UI display
        ObservableList<DisplayReminder> displayReminders = FXCollections.observableArrayList(
                userReminders.stream()
                        .filter(r -> !r.isCompleted()) // Only show incomplete reminders
                        .map(r -> new DisplayReminder(
                                r.getName(),
                                r.getDueDate(),
                                r.getAmount(),
                                r.getReminderId()))
                        .collect(Collectors.toList())
        );

        remindersList.setItems(displayReminders);

        // Add double-click handler to mark reminders as completed
        remindersList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double click
                DisplayReminder selectedReminder = remindersList.getSelectionModel().getSelectedItem();
                if (selectedReminder != null) {
                    // Ask for confirmation
                    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmation.setTitle("Complete Reminder");
                    confirmation.setHeaderText("Mark as Complete");
                    confirmation.setContentText("Do you want to mark this reminder as completed?");

                    confirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            if (reminderTracker.markReminderCompleted(selectedReminder.getId(), userId)) {
                                // Refresh the list
                                loadReminders();
                            }
                        }
                    });
                }
            }
        });
    }

    private void updateSummary() {
        double totalIncome = transactionTable.getItems().stream()
                .filter(t -> t.getType().equals("Income"))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpense = transactionTable.getItems().stream()
                .filter(t -> t.getType().equals("Expense"))
                .mapToDouble(Transaction::getAmount)
                .sum();

        totalIncomeLabel.setText(String.format("$%.2f", totalIncome));
        totalExpenseLabel.setText(String.format("$%.2f", totalExpense));
        balanceLabel.setText(String.format("$%.2f", totalIncome - totalExpense));
    }

    // Data classes
    public static class Transaction {
        private final String type;
        private final String category;
        private final double amount;
        private final LocalDate date;

        public Transaction(String type, String category, double amount, LocalDate date) {
            this.type = type;
            this.category = category;
            this.amount = amount;
            this.date = date;
        }

        // Getters
        public String getType() { return type; }
        public String getCategory() { return category; }
        public double getAmount() { return amount; }
        public LocalDate getDate() { return date; }
    }

    /**
     * View Model class for Budgets in the UI
     */
    public static class BudgetViewModel {
        private final String category;
        private final BigDecimal limit;
        private final String period;

        public BudgetViewModel(String category, BigDecimal limit, String period) {
            this.category = category;
            this.limit = limit;
            this.period = period;
        }

        // Getters
        public String getCategory() { return category; }
        public BigDecimal getLimit() { return limit; }
        public String getPeriod() { return period; }
    }

    /**
     * DisplayReminder class for showing reminders in the UI
     */
    public static class DisplayReminder {
        private final String name;
        private final LocalDate dueDate;
        private final double amount;
        private final int id;

        public DisplayReminder(String name, LocalDate dueDate, double amount, int id) {
            this.name = name;
            this.dueDate = dueDate;
            this.amount = amount;
            this.id = id;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return name + " - $" + String.format("%.2f", amount) + " - Due: " + dueDate.toString();
        }
    }
}