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
import model.Reminder;
import model.SystemManager;

import java.io.IOException;
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

    @FXML private TableView<Budget> budgetTable;
    @FXML private TableColumn<Budget, String> budgetCategoryColumn;
    @FXML private TableColumn<Budget, Double> limitColumn;
    @FXML private TableColumn<Budget, String> periodColumn;

    @FXML private ListView<DisplayReminder> remindersList;
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
        loadReminders(); // Load actual reminders

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

        // Sample pie chart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Food", 25.50),
                new PieChart.Data("Transport", 15.75),
                new PieChart.Data("Entertainment", 0)
        );
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