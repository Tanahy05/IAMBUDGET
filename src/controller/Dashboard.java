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

/**
 * Controller class for the Dashboard view in the personal finance application.
 * This class manages the display of user financial data including transactions,
 * budgets, reminders, and summary statistics.
 */
public class Dashboard {

    // Add @FXML annotations to all UI components
    /** TableView displaying recent transactions */
    @FXML private TableView<Transaction> transactionTable;
    /** Column displaying transaction type (Income/Expense) */
    @FXML private TableColumn<Transaction, String> typeColumn;
    /** Column displaying transaction category */
    @FXML private TableColumn<Transaction, String> categoryColumn;
    /** Column displaying transaction amount */
    @FXML private TableColumn<Transaction, Double> amountColumn;
    /** Column displaying transaction date */
    @FXML private TableColumn<Transaction, LocalDate> dateColumn;

    /** TableView displaying budget information */
    @FXML private TableView<BudgetViewModel> budgetTable;
    /** Column displaying budget category */
    @FXML private TableColumn<BudgetViewModel, String> budgetCategoryColumn;
    /** Column displaying budget limit */
    @FXML private TableColumn<BudgetViewModel, BigDecimal> limitColumn;
    /** Column displaying budget period */
    @FXML private TableColumn<BudgetViewModel, String> periodColumn;

    /** ListView displaying pending reminders */
    @FXML private ListView<DisplayReminder> remindersList;
    /** PieChart displaying spending distribution */
    @FXML private PieChart spendingChart;
    /** Label displaying total income */
    @FXML private Label totalIncomeLabel;
    /** Label displaying total expenses */
    @FXML private Label totalExpenseLabel;
    /** Label displaying current balance */
    @FXML private Label balanceLabel;

    /** Reference to the BudgetTracker singleton */
    private BudgetTracker budgetTracker;

    /**
     * Initializes the dashboard view, loading data from various trackers
     * and setting up UI components.
     * This method is automatically called by JavaFX after the FXML is loaded.
     */
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

    /**
     * Sets up the transaction table by binding column cell factories
     * to the corresponding properties in the Transaction class.
     */
    private void initializeTransactionTable() {
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    /**
     * Sets up the budget table by binding column cell factories
     * to the corresponding properties in the BudgetViewModel class.
     */
    private void initializeBudgetTable() {
        budgetCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        limitColumn.setCellValueFactory(new PropertyValueFactory<>("limit"));
        periodColumn.setCellValueFactory(new PropertyValueFactory<>("period"));
    }

    /**
     * Loads transaction data from ExpenseTracker and IncomeTracker
     * and displays the most recent transactions in the transaction table.
     * Shows the last 5 income and 5 expense transactions.
     */
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

    /**
     * Loads budget data from the BudgetTracker and displays
     * it in the budget table using the BudgetViewModel wrapper class.
     */
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

    /**
     * Updates the spending chart with data from the user's budgets.
     * Uses budget limits to create pie chart segments for each category.
     */
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
     * Loads reminders from the ReminderTracker for the current user
     * and displays them in the reminders list. Sets up a double-click
     * handler to mark reminders as completed.
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

    /**
     * Updates the summary labels (income, expense, balance)
     * based on the data in the transaction table.
     */
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

    /**
     * Internal class representing a transaction for display purposes in the UI.
     * Combines both income and expense transactions into a single view model.
     */
    public static class Transaction {
        private final String type;
        private final String category;
        private final double amount;
        private final LocalDate date;

        /**
         * Creates a new Transaction instance.
         *
         * @param type The transaction type (Income or Expense)
         * @param category The transaction category or source
         * @param amount The transaction amount
         * @param date The transaction date
         */
        public Transaction(String type, String category, double amount, LocalDate date) {
            this.type = type;
            this.category = category;
            this.amount = amount;
            this.date = date;
        }

        /**
         * Gets the transaction type.
         * @return The transaction type (Income or Expense)
         */
        public String getType() { return type; }

        /**
         * Gets the transaction category.
         * @return The transaction category or source
         */
        public String getCategory() { return category; }

        /**
         * Gets the transaction amount.
         * @return The transaction amount
         */
        public double getAmount() { return amount; }

        /**
         * Gets the transaction date.
         * @return The transaction date
         */
        public LocalDate getDate() { return date; }
    }

    /**
     * View Model class for Budgets in the UI.
     * Maps model.Budget objects to a simplified version for display.
     */
    public static class BudgetViewModel {
        private final String category;
        private final BigDecimal limit;
        private final String period;

        /**
         * Creates a new BudgetViewModel instance.
         *
         * @param category The budget category
         * @param limit The budget limit amount
         * @param period The budget period (e.g., "Weekly", "Monthly")
         */
        public BudgetViewModel(String category, BigDecimal limit, String period) {
            this.category = category;
            this.limit = limit;
            this.period = period;
        }

        /**
         * Gets the budget category.
         * @return The budget category
         */
        public String getCategory() { return category; }

        /**
         * Gets the budget limit.
         * @return The budget limit amount
         */
        public BigDecimal getLimit() { return limit; }

        /**
         * Gets the budget period.
         * @return The budget period (e.g., "Weekly", "Monthly")
         */
        public String getPeriod() { return period; }
    }

    /**
     * DisplayReminder class for showing reminders in the UI.
     * Provides a formatted string representation for ListView display.
     */
    public static class DisplayReminder {
        private final String name;
        private final LocalDate dueDate;
        private final double amount;
        private final int id;

        /**
         * Creates a new DisplayReminder instance.
         *
         * @param name The reminder name or description
         * @param dueDate The reminder due date
         * @param amount The associated amount for the reminder
         * @param id The unique identifier for the reminder
         */
        public DisplayReminder(String name, LocalDate dueDate, double amount, int id) {
            this.name = name;
            this.dueDate = dueDate;
            this.amount = amount;
            this.id = id;
        }

        /**
         * Gets the reminder's unique identifier.
         * @return The reminder ID
         */
        public int getId() {
            return id;
        }

        /**
         * Returns a string representation of the reminder for display in the UI.
         * Format: [name] - $[amount] - Due: [date]
         *
         * @return The formatted string representation
         */
        @Override
        public String toString() {
            return name + " - $" + String.format("%.2f", amount) + " - Due: " + dueDate.toString();
        }
    }
}