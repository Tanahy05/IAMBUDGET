package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import java.time.LocalDate;

public class Dashboard {

    // Add @FXML annotations to all UI components
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, String> typeColumn;
    @FXML private TableColumn<Transaction, String> categoryColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;
    @FXML private TableColumn<Transaction, LocalDate> dateColumn;

    @FXML private TableView<Budget> budgetTable;
    @FXML private TableColumn<Budget, String> budgetCategoryColumn;
    @FXML private TableColumn<Budget, Double> limitColumn;
    @FXML private TableColumn<Budget, String> periodColumn;

    @FXML private ListView<Reminder> remindersList;
    @FXML private PieChart spendingChart;
    @FXML private Label totalIncomeLabel;
    @FXML private Label totalExpenseLabel;
    @FXML private Label balanceLabel;

    @FXML
    public void initialize() {
        // Initialize only if components are not null
        if (transactionTable != null) {
            initializeTransactionTable();
        }
        if (budgetTable != null) {
            initializeBudgetTable();
        }

        loadSampleData();

        // Only update summary if labels exist
        if (totalIncomeLabel != null && totalExpenseLabel != null && balanceLabel != null) {
            updateSummary();
        }
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

    private void loadSampleData() {
        // Sample transactions
        ObservableList<Transaction> transactions = FXCollections.observableArrayList(
                new Transaction("Expense", "Food", 25.50, LocalDate.now()),
                new Transaction("Income", "Salary", 2000.00, LocalDate.now()),
                new Transaction("Expense", "Transport", 15.75, LocalDate.now())
        );
        transactionTable.setItems(transactions);

        // Sample budgets
        ObservableList<Budget> budgets = FXCollections.observableArrayList(
                new Budget("Food", 300.00, "Monthly"),
                new Budget("Transport", 150.00, "Monthly"),
                new Budget("Entertainment", 100.00, "Monthly")
        );
        budgetTable.setItems(budgets);

        // Sample reminders
        ObservableList<Reminder> reminders = FXCollections.observableArrayList(
                new Reminder("Rent Payment", LocalDate.now().plusDays(3), true),
                new Reminder("Electric Bill", LocalDate.now().plusDays(7), false)
        );
        remindersList.setItems(reminders);

        // Sample pie chart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Food", 25.50),
                new PieChart.Data("Transport", 15.75),
                new PieChart.Data("Entertainment", 0)
        );
        spendingChart.setData(pieChartData);
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

    // Navigation methods
    @FXML
    private void showTransactions() {
        // Implement transaction view
    }

    @FXML
    private void showBudgets() {
        // Implement budget view
    }

    @FXML
    private void showReminders() {
        // Implement reminder view
    }

    @FXML
    private void showReports() {
        // Implement reports view
    }

    @FXML
    private void handleLogout() {
        // Implement logout logic
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

    public static class Budget {
        private final String category;
        private final double limit;
        private final String period;

        public Budget(String category, double limit, String period) {
            this.category = category;
            this.limit = limit;
            this.period = period;
        }

        // Getters
        public String getCategory() { return category; }
        public double getLimit() { return limit; }
        public String getPeriod() { return period; }
    }

    public static class Reminder {
        private final String name;
        private final LocalDate dueDate;
        private final boolean recurring;

        public Reminder(String name, LocalDate dueDate, boolean recurring) {
            this.name = name;
            this.dueDate = dueDate;
            this.recurring = recurring;
        }

        @Override
        public String toString() {
            return name + " - Due: " + dueDate.toString() + (recurring ? " (Recurring)" : "");
        }
    }
}