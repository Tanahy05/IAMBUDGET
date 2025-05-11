package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.chart.*;
import javafx.util.StringConverter;
import model.Expense;
import controller.ExpenseTracker;
import model.SystemManager;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Controller class for the Expense Tracker application that manages the UI
 * interactions and connects the model with the view. This class implements
 * Initializable to set up the UI components when the view is loaded.
 */
public class ExpenseTrackerController implements Initializable {

    /** Reference to the expense tracker model */
    private ExpenseTracker expenseTracker;

    /** Observable list to store and display expenses in the table view */
    private ObservableList<Expense> expensesList;

    /** Array of predefined expense categories available for selection */
    private final String[] expenseCategories = {
            "Food", "Housing", "Transportation", "Utilities", "Entertainment",
            "Healthcare", "Education", "Shopping", "Personal Care", "Travel", "Other"
    };

    /** Array of time range options for filtering expenses */
    private final String[] timeRanges = {
            "Current Month", "Last Month", "Last 3 Months", "Last 6 Months", "Year to Date", "Custom Date Range"
    };

    /** Array of recurring expense frequency options */
    private final String[] recurringOptions = {
            "Not Recurring", "Daily", "Weekly", "Bi-weekly", "Monthly", "Quarterly", "Yearly"
    };

    /** Text field for entering expense amount */
    @FXML private TextField expenseAmountField;

    /** Date picker for selecting expense date */
    @FXML private DatePicker expenseDatePicker;

    /** Text field for entering expense description */
    @FXML private TextField expenseDescriptionField;

    /** Combo box for selecting expense category */
    @FXML private ComboBox<String> categoryComboBox;

    /** Check box for indicating if an expense is recurring */
    @FXML private CheckBox recurringCheckBox;

    /** Combo box for selecting recurring frequency */
    @FXML private ComboBox<String> recurringFrequencyComboBox;

    /** Table view for displaying expenses */
    @FXML private TableView<Expense> expensesTableView;

    /** Table column for expense category */
    @FXML private TableColumn<Expense, String> categoryColumn;

    /** Table column for expense amount */
    @FXML private TableColumn<Expense, Double> amountColumn;

    /** Table column for expense date */
    @FXML private TableColumn<Expense, LocalDate> dateColumn;

    /** Table column for expense description */
    @FXML private TableColumn<Expense, String> descriptionColumn;

    /** Table column for recurring status */
    @FXML private TableColumn<Expense, Boolean> recurringColumn;

    /** Table column for action buttons */
    @FXML private TableColumn<Expense, String> actionsColumn;

    /** Combo box for selecting time range for filtering */
    @FXML private ComboBox<String> timeRangeComboBox;

    /** Date picker for selecting filter start date */
    @FXML private DatePicker startDatePicker;

    /** Date picker for selecting filter end date */
    @FXML private DatePicker endDatePicker;

    /** Button to apply date filter */
    @FXML private Button applyFilterButton;

    /** Button to reset date filter */
    @FXML private Button resetFilterButton;

    /** Label to display total expenses */
    @FXML private Label totalExpensesLabel;

    /** Chart to display expense distribution by category */
    @FXML private PieChart categoryPieChart;

    /** Chart to display monthly expense trends */
    @FXML private BarChart<String, Number> monthlyExpensesChart;

    /** Currently selected expense for editing */
    private Expense selectedExpense;

    /** Date formatter for displaying dates */
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the expense tracker
        expenseTracker = SystemManager.getExpenseTracker();
        expensesList = FXCollections.observableArrayList();

        // Set up category combobox
        categoryComboBox.setItems(FXCollections.observableArrayList(expenseCategories));

        // Set up recurring frequency combobox
        recurringFrequencyComboBox.setItems(FXCollections.observableArrayList(recurringOptions));
        recurringFrequencyComboBox.setDisable(true);

        // Set up time range combobox
        timeRangeComboBox.setItems(FXCollections.observableArrayList(timeRanges));
        timeRangeComboBox.getSelectionModel().selectFirst();

        // Set up date pickers
        expenseDatePicker.setValue(LocalDate.now());
        startDatePicker.setValue(YearMonth.now().atDay(1));
        endDatePicker.setValue(LocalDate.now());

        // Configure table columns
        setupTableColumns();

        // Load expenses
        loadExpenses();

        // Listener for recurring checkbox
        recurringCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            recurringFrequencyComboBox.setDisable(!newVal);
            if (newVal) {
                recurringFrequencyComboBox.getSelectionModel().select(2); // Default to "Weekly"
            } else {
                recurringFrequencyComboBox.getSelectionModel().select(0); // "Not Recurring"
            }
        });

        // Listener for time range combobox
        timeRangeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            handleTimeRangeChange(newVal);
        });

        // Listener for table selection
        expensesTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedExpense = newVal;
                populateExpenseFields(newVal);
            }
        });

        // Update charts
        updateDashboard();
    }

    /**
     * Sets up the table columns with appropriate cell factories and value factories.
     * Configures special formatting for date, amount, and recurring columns.
     * Also sets up action buttons for each row.
     */
    private void setupTableColumns() {
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        recurringColumn.setCellValueFactory(new PropertyValueFactory<>("recurring"));

        // Format date column
        dateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(date));
                }
            }
        });

        // Format amount column
        amountColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", amount));
                }
            }
        });

        // Format recurring column
        recurringColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean recurring, boolean empty) {
                super.updateItem(recurring, empty);
                if (empty || recurring == null) {
                    setText(null);
                } else {
                    setText(recurring ? "Yes" : "No");
                }
            }
        });

        // Add action buttons to the last column
        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(event -> {
                    Expense expense = getTableView().getItems().get(getIndex());
                    selectedExpense = expense;
                    populateExpenseFields(expense);
                });

                deleteButton.setOnAction(event -> {
                    Expense expense = getTableView().getItems().get(getIndex());
                    handleDeleteExpense(expense);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Create button container
                    ButtonBar buttonBar = new ButtonBar();
                    buttonBar.getButtons().addAll(editButton, deleteButton);
                    setGraphic(buttonBar);
                }
            }
        });
    }

    /**
     * Loads all expenses from the expense tracker model into the table view
     * and updates the dashboard charts.
     */
    private void loadExpenses() {
        expensesList.clear();
        expensesList.addAll(expenseTracker.getExpenses());
        expensesTableView.setItems(expensesList);
        updateDashboard();
    }

    /**
     * Updates the dashboard elements including the total expenses label
     * and the category pie chart.
     */
    private void updateDashboard() {
        // Update total expenses
        double total = expenseTracker.getTotalExpenses();
        totalExpensesLabel.setText(String.format("$%.2f", total));

        // Update category pie chart
        updateCategoryPieChart();
    }

    /**
     * Updates the category pie chart based on the currently filtered expenses.
     * The chart shows the distribution of expenses across different categories.
     */
    private void updateCategoryPieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Get the current filtered expenses from the table
        List<Expense> filteredExpenses = expensesTableView.getItems();

        // Calculate category totals for the filtered expenses
        Map<String, Double> categoryTotals = new HashMap<>();

        for (Expense expense : filteredExpenses) {
            String category = expense.getCategory();
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + expense.getAmount());
        }

        // Add the data to the pie chart
        categoryTotals.forEach((category, total) -> {
            pieChartData.add(new PieChart.Data(category + " ($" + String.format("%.2f", total) + ")", total));
        });

        categoryPieChart.setData(pieChartData);
    }

    /**
     * Populates the expense form fields with the data from the given expense.
     * Used when selecting an expense from the table for editing.
     *
     * @param expense The expense to display in the form
     */
    private void populateExpenseFields(Expense expense) {
        expenseAmountField.setText(String.valueOf(expense.getAmount()));
        expenseDatePicker.setValue(expense.getDate());
        expenseDescriptionField.setText(expense.getDescription());
        categoryComboBox.getSelectionModel().select(expense.getCategory());
        recurringCheckBox.setSelected(expense.isRecurring());

        if (expense.isRecurring() && expense.getRecurringFrequency() != null) {
            recurringFrequencyComboBox.setDisable(false);
            recurringFrequencyComboBox.getSelectionModel().select(expense.getRecurringFrequency());
        } else {
            recurringFrequencyComboBox.setDisable(true);
            recurringFrequencyComboBox.getSelectionModel().select(0);
        }
    }

    /**
     * Clears all expense form fields and resets them to default values.
     * Also clears the currently selected expense.
     */
    private void clearExpenseFields() {
        expenseAmountField.clear();
        expenseDatePicker.setValue(LocalDate.now());
        expenseDescriptionField.clear();
        categoryComboBox.getSelectionModel().select(null);
        recurringCheckBox.setSelected(false);
        recurringFrequencyComboBox.setDisable(true);
        recurringFrequencyComboBox.getSelectionModel().select(0);
        selectedExpense = null;
    }

    /**
     * Handles changes to the time range selection for filtering expenses.
     * Sets appropriate start and end dates based on the selected time range.
     *
     * @param timeRange The selected time range option
     */
    private void handleTimeRangeChange(String timeRange) {
        LocalDate start = null;
        LocalDate end = LocalDate.now();
        boolean enableCustomDateRange = false;

        switch (timeRange) {
            case "Current Month":
                start = YearMonth.now().atDay(1);
                break;
            case "Last Month":
                YearMonth lastMonth = YearMonth.now().minusMonths(1);
                start = lastMonth.atDay(1);
                end = lastMonth.atEndOfMonth();
                break;
            case "Last 3 Months":
                start = LocalDate.now().minusMonths(3).withDayOfMonth(1);
                break;
            case "Last 6 Months":
                start = LocalDate.now().minusMonths(6).withDayOfMonth(1);
                break;
            case "Year to Date":
                start = LocalDate.now().withDayOfYear(1);
                break;
            case "Custom Date Range":
                enableCustomDateRange = true;
                break;
        }

        startDatePicker.setDisable(!enableCustomDateRange);
        endDatePicker.setDisable(!enableCustomDateRange);

        if (start != null) {
            startDatePicker.setValue(start);
            endDatePicker.setValue(end);
            filterExpenses();
        }
    }

    /**
     * Handles the action of adding or updating an expense.
     * Validates the input fields before processing the expense.
     * Shows appropriate alerts to indicate success or failure.
     */
    @FXML
    private void handleAddExpense() {
        try {
            String category = categoryComboBox.getValue();
            double amount = Double.parseDouble(expenseAmountField.getText());
            LocalDate date = expenseDatePicker.getValue();
            String description = expenseDescriptionField.getText();
            boolean isRecurring = recurringCheckBox.isSelected();
            String recurringFrequency = isRecurring ? recurringFrequencyComboBox.getValue() : null;

            if (category == null || category.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a category");
                return;
            }

            if (amount <= 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Amount must be greater than zero");
                return;
            }

            if (date == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a date");
                return;
            }

            if (selectedExpense == null) {
                // Add new expense
                Expense expense = new Expense(category, amount, date, description, isRecurring, recurringFrequency);
                expenseTracker.addExpense(expense);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Expense added successfully");
            } else {
                // Update existing expense
                selectedExpense.setCategory(category);
                selectedExpense.setAmount(amount);
                selectedExpense.setDate(date);
                selectedExpense.setDescription(description);
                selectedExpense.setRecurring(isRecurring);
                selectedExpense.setRecurringFrequency(recurringFrequency);
                expenseTracker.updateExpense(selectedExpense);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Expense updated successfully");
            }

            clearExpenseFields();
            loadExpenses();
            SystemManager.saveUserData(); // Save to file

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid amount");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
        }
    }

    /**
     * Handles the action of clearing the expense form.
     * Resets all form fields to their default values.
     */
    @FXML
    private void handleClearForm() {
        clearExpenseFields();
    }

    /**
     * Handles the action of deleting an expense.
     * Shows a confirmation dialog before proceeding with deletion.
     *
     * @param expense The expense to be deleted
     */
    @FXML
    private void handleDeleteExpense(Expense expense) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Expense");
        confirmAlert.setContentText("Are you sure you want to delete this expense?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            expenseTracker.removeExpense(expense.getId());
            loadExpenses();
            SystemManager.saveUserData(); // Save to file
            showAlert(Alert.AlertType.INFORMATION, "Success", "Expense deleted successfully");
        }
    }

    /**
     * Handles the action of applying the date filter.
     * Calls the filterExpenses method to filter expenses based on selected date range.
     */
    @FXML
    private void handleApplyFilter() {
        filterExpenses();
    }

    /**
     * Filters the expenses based on the selected date range.
     * Updates the table view and charts to display only the filtered expenses.
     */
    private void filterExpenses() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select valid start and end dates");
            return;
        }

        if (startDate.isAfter(endDate)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Start date must be before end date");
            return;
        }

        List<Expense> filteredExpenses = expenseTracker.getExpensesByDateRange(startDate, endDate);
        expensesList.clear();
        expensesList.addAll(filteredExpenses);

        // Update dashboard with filtered data
        double total = filteredExpenses.stream().mapToDouble(Expense::getAmount).sum();
        totalExpensesLabel.setText(String.format("$%.2f", total));

        // Update charts based on filtered data
        updateCategoryPieChart();
    }

    /**
     * Shows an alert dialog with the specified type, title, and message.
     *
     * @param alertType The type of alert to show
     * @param title The title of the alert dialog
     * @param message The message to display in the alert dialog
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}