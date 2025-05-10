package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import javafx.util.Callback;

import model.Income;
import model.IncomeTracker;
import model.SystemManager;
import model.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.List;

public class IncomeTrackingController implements Initializable {

    @FXML
    private ComboBox<String> incomeSourceComboBox;
    @FXML
    private TextField amountTextField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private CheckBox recurringCheckBox;
    @FXML
    private ComboBox<String> recurringPeriodComboBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button clearButton;
    @FXML
    private Label statusLabel;
    @FXML
    private TableView<Income> incomeTableView;
    @FXML
    private TableColumn<Income, LocalDate> dateColumn;
    @FXML
    private TableColumn<Income, String> sourceColumn;
    @FXML
    private TableColumn<Income, BigDecimal> amountColumn;
    @FXML
    private TableColumn<Income, String> descriptionColumn;
    @FXML
    private TableColumn<Income, Boolean> recurringColumn;
    @FXML
    private TableColumn<Income, Void> actionsColumn;

    private ObservableList<Income> incomeData = FXCollections.observableArrayList();
    private Income currentIncome = null; // For editing existing income

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize date picker with current date
        datePicker.setValue(LocalDate.now());

        // Setup recurring period options
        recurringPeriodComboBox.setItems(FXCollections.observableArrayList(
                "Weekly", "Monthly", "Yearly"));

        // Disable recurring period dropdown until checkbox is selected
        recurringPeriodComboBox.setDisable(true);
        recurringCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            recurringPeriodComboBox.setDisable(!newVal);
        });

        // Common income sources
        incomeSourceComboBox.setItems(FXCollections.observableArrayList(
                "Salary", "Freelance", "Business", "Investment", "Rental", "Other"));

        // Setup table columns
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        sourceColumn.setCellValueFactory(new PropertyValueFactory<>("source"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        recurringColumn.setCellValueFactory(new PropertyValueFactory<>("recurring"));

        // Format amount column to show currency
        amountColumn.setCellFactory(column -> new TableCell<Income, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("$" + item.toString());
                }
            }
        });

        // Format recurring column to show Yes/No instead of true/false
        recurringColumn.setCellFactory(column -> new TableCell<Income, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Yes" : "No");
                }
            }
        });

        // Add edit and delete buttons to each row
        setupActionsColumn();

        // Load income data for current user
        loadIncomeData();
    }

    private void setupActionsColumn() {
        Callback<TableColumn<Income, Void>, TableCell<Income, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Income, Void> call(final TableColumn<Income, Void> param) {
                return new TableCell<Income, Void>() {
                    private final Button editBtn = new Button("Edit");
                    private final Button deleteBtn = new Button("Delete");
                    private final HBox pane = new HBox(5, editBtn, deleteBtn);

                    {
                        editBtn.setOnAction(event -> {
                            Income income = getTableView().getItems().get(getIndex());
                            populateFormForEdit(income);
                        });

                        deleteBtn.setOnAction(event -> {
                            Income income = getTableView().getItems().get(getIndex());
                            handleDeleteIncome(income);
                        });

                        // Style buttons to be compact
                        editBtn.setStyle("-fx-font-size: 10px; -fx-padding: 2 5;");
                        deleteBtn.setStyle("-fx-font-size: 10px; -fx-padding: 2 5;");
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : pane);
                    }
                };
            }
        };

        actionsColumn.setCellFactory(cellFactory);
    }

    private void loadIncomeData() {
        if (!SystemManager.isUserLoggedIn()) {
            statusLabel.setText("No user logged in");
            return;
        }

        User currentUser = SystemManager.getCurrentUser();
        IncomeTracker incomeTracker = SystemManager.getIncomeTracker();

        // Get income data for the current user
        List<Income> userIncome = incomeTracker.getUserIncome(currentUser.getUserID());

        // Update the observable list
        incomeData.clear();
        incomeData.addAll(userIncome);

        // Set the table data
        incomeTableView.setItems(incomeData);

        // Calculate and display total
        BigDecimal totalIncome = incomeTracker.calculateUserTotalIncome(currentUser.getUserID());
        statusLabel.setText("Total Income: $" + totalIncome);
    }

    private void populateFormForEdit(Income income) {
        // Store the current income for editing
        currentIncome = income;

        // Populate form fields
        incomeSourceComboBox.getSelectionModel().select(income.getSource());
        if (!incomeSourceComboBox.getItems().contains(income.getSource())) {
            incomeSourceComboBox.getItems().add(income.getSource());
            incomeSourceComboBox.getSelectionModel().select(income.getSource());
        }

        amountTextField.setText(income.getAmount().toString());
        datePicker.setValue(income.getDate());
        descriptionTextField.setText(income.getDescription());

        recurringCheckBox.setSelected(income.isRecurring());
        if (income.isRecurring() && income.getRecurringPeriod() != null) {
            recurringPeriodComboBox.getSelectionModel().select(income.getRecurringPeriod());
        }

        // Change button text to indicate we're editing
        saveButton.setText("Update Income");
    }

    @FXML
    private void handleSaveIncome(ActionEvent event) {
        try {
            if (!validateForm()) {
                return;
            }

            // Get form values
            String source = incomeSourceComboBox.getValue();
            BigDecimal amount = new BigDecimal(amountTextField.getText().trim());
            LocalDate date = datePicker.getValue();
            String description = descriptionTextField.getText().trim();
            boolean recurring = recurringCheckBox.isSelected();
            String recurringPeriod = recurring ? recurringPeriodComboBox.getValue() : null;

            // Get current user
            User currentUser = SystemManager.getCurrentUser();
            if (currentUser == null) {
                statusLabel.setText("Error: No user logged in");
                return;
            }

            IncomeTracker incomeTracker = SystemManager.getIncomeTracker();

            // Either update existing income or create a new one
            if (currentIncome != null) {
                // Update existing income
                currentIncome.setSource(source);
                currentIncome.setAmount(amount);
                currentIncome.setDate(date);
                currentIncome.setDescription(description);
                currentIncome.setRecurring(recurring);
                currentIncome.setRecurringPeriod(recurringPeriod);

                incomeTracker.edit(currentIncome);
                statusLabel.setText("Income updated successfully");
            } else {
                // Create new income
                Income newIncome = new Income(
                        source, amount, date, description,
                        recurring, recurringPeriod, currentUser.getUserID()
                );

                incomeTracker.add(newIncome);
                statusLabel.setText("Income added successfully");
            }

            // Save user data
            SystemManager.saveUserData();

            // Refresh the table view
            loadIncomeData();

            // Reset form
            handleClearForm(null);

        } catch (NumberFormatException e) {
            statusLabel.setText("Error: Invalid amount format");
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateForm() {
        StringBuilder errorMsg = new StringBuilder();

        if (incomeSourceComboBox.getValue() == null || incomeSourceComboBox.getValue().trim().isEmpty()) {
            errorMsg.append("Income source is required. ");
        }

        try {
            String amountText = amountTextField.getText().trim();
            if (amountText.isEmpty()) {
                errorMsg.append("Amount is required. ");
            } else {
                BigDecimal amount = new BigDecimal(amountText);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    errorMsg.append("Amount must be positive. ");
                }
            }
        } catch (NumberFormatException e) {
            errorMsg.append("Amount must be a valid number. ");
        }

        if (datePicker.getValue() == null) {
            errorMsg.append("Date is required. ");
        }

        if (recurringCheckBox.isSelected() &&
                (recurringPeriodComboBox.getValue() == null || recurringPeriodComboBox.getValue().isEmpty())) {
            errorMsg.append("Please select a recurring period. ");
        }

        if (errorMsg.length() > 0) {
            statusLabel.setText("Error: " + errorMsg.toString());
            return false;
        }

        return true;
    }

    private void handleDeleteIncome(Income income) {
        IncomeTracker incomeTracker = SystemManager.getIncomeTracker();

        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Income");
        alert.setHeaderText("Delete Income Entry");
        alert.setContentText("Are you sure you want to delete this income entry?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Delete the income entry
                incomeTracker.delete(income.getId());

                // Save user data
                SystemManager.saveUserData();

                // Refresh table
                loadIncomeData();

                statusLabel.setText("Income entry deleted");
            }
        });
    }

    @FXML
    private void handleClearForm(ActionEvent event) {
        // Reset all form fields
        incomeSourceComboBox.setValue(null);
        amountTextField.clear();
        datePicker.setValue(LocalDate.now());
        descriptionTextField.clear();
        recurringCheckBox.setSelected(false);
        recurringPeriodComboBox.setValue(null);

        // Reset edit state
        currentIncome = null;
        saveButton.setText("Save Income");

        statusLabel.setText("Form cleared");
    }

    @FXML
    private void handleGoToDashboard(ActionEvent event) {
        // This would be handled by a navigation service or main controller
        statusLabel.setText("Navigating to dashboard...");

        // For now just print a debug message
        System.out.println("Go to Dashboard clicked");

        // TODO: Implement navigation to dashboard
        // Example: MainApp.loadScene("Dashboard.fxml");
    }
}