package controller;
import model.Expense;
import interfaces.Tracker;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ExpenseTracker implements the Tracker interface to manage user expenses.
 * This class follows the Singleton pattern to ensure only one instance exists.
 * It provides functionality for adding, updating, deleting, and analyzing expense data,
 * as well as persisting expense data to the file system.
 */
public class ExpenseTracker implements Tracker<Expense> {
    private static ExpenseTracker instance;
    private ArrayList<Expense> expenses;
    private static final String DATA_DIR = "data";

    /**
     * Private constructor to enforce Singleton pattern.
     * Initializes an empty list of expenses.
     */
    private ExpenseTracker() {
        expenses = new ArrayList<>();
    }

    /**
     * Gets the singleton instance of ExpenseTracker.
     * Creates a new instance if none exists.
     *
     * @return The singleton instance of ExpenseTracker
     */
    public static ExpenseTracker getInstance() {
        if (instance == null) {
            instance = new ExpenseTracker();
        }
        return instance;
    }

    /**
     * Gets the list of all expenses.
     *
     * @return An ArrayList containing all expense records
     */
    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    /**
     * Adds a new expense to the tracker.
     *
     * @param expense The expense object to add
     */
    @Override
    public void add(Expense expense) {
        expenses.add(expense);
    }

    /**
     * Edits an existing expense in the tracker.
     * Finds the expense with matching ID and replaces it.
     *
     * @param expense The updated expense object
     */
    @Override
    public void edit(Expense expense) {
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).getId().equals(expense.getId())) {
                expenses.set(i, expense);
                return;
            }
        }
    }

    /**
     * Deletes an expense from the tracker by its ID.
     *
     * @param expenseId The ID of the expense to delete
     */
    @Override
    public void delete(String expenseId) {
        expenses.removeIf(expense -> expense.getId().equals(expenseId));
    }

    /**
     * Validates an expense object to ensure it contains all required fields.
     *
     * @param expense The expense object to validate
     * @return true if the expense is valid, false otherwise
     */
    @Override
    public boolean validate(Expense expense) {
        // Basic validation for expense object
        return expense != null &&
                expense.getId() != null &&
                !expense.getId().isEmpty() &&
                expense.getAmount() > 0 &&
                expense.getDate() != null &&
                expense.getCategory() != null &&
                !expense.getCategory().isEmpty();
    }

    /**
     * Calculates the total amount of all expenses.
     *
     * @return The total amount as a BigDecimal
     */
    @Override
    public BigDecimal calculateAmount() {
        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
        return BigDecimal.valueOf(total);
    }

    // Legacy methods that can be kept for backward compatibility

    /**
     * Legacy method to add an expense.
     * Calls the standardized add method.
     *
     * @param expense The expense object to add
     */
    public void addExpense(Expense expense) {
        add(expense);
    }

    /**
     * Legacy method to update an expense.
     * Calls the standardized edit method.
     *
     * @param expense The updated expense object
     */
    public void updateExpense(Expense expense) {
        edit(expense);
    }

    /**
     * Legacy method to remove an expense.
     * Calls the standardized delete method.
     *
     * @param expenseId The ID of the expense to remove
     */
    public void removeExpense(String expenseId) {
        delete(expenseId);
    }

    /**
     * Gets the total of all expenses as a double value.
     *
     * @return The total amount of all expenses
     */
    public double getTotalExpenses() {
        return calculateAmount().doubleValue();
    }

    /**
     * Gets all expenses within a specified date range.
     *
     * @param startDate The start date of the range (inclusive)
     * @param endDate   The end date of the range (inclusive)
     * @return A list of expenses within the specified date range
     */
    public List<Expense> getExpensesByDateRange(LocalDate startDate, LocalDate endDate) {
        return expenses.stream()
                .filter(expense -> !expense.getDate().isBefore(startDate) && !expense.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    /**
     * Gets all expenses for a specific month.
     *
     * @param year  The year
     * @param month The month (1-12)
     * @return A list of expenses for the specified month
     */
    public List<Expense> getExpensesForMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        return getExpensesByDateRange(startDate, endDate);
    }

    /**
     * Calculates the total expense amount for each category.
     *
     * @return A map with category names as keys and total amounts as values
     */
    public Map<String, Double> getCategoryTotals() {
        Map<String, Double> categoryTotals = new HashMap<>();

        for (Expense expense : expenses) {
            String category = expense.getCategory();
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + expense.getAmount());
        }

        return categoryTotals;
    }

    /**
     * Clears all expense data from memory.
     */
    public void clearData() {
        expenses.clear();
    }

    /**
     * Saves expense data to a file for a specific user.
     *
     * @param userId The ID of the user
     * @return true if data was saved successfully, false otherwise
     */
    public boolean saveData(int userId) {
        String filePath = getExpenseFilePath(userId);
        createDataDirIfNeeded();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(expenses);
            System.out.println("Expense data saved successfully for user ID: " + userId);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving expense data: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads expense data from a file for a specific user.
     * If no file exists, initializes an empty expense list.
     *
     * @param userId The ID of the user
     * @return true if data was loaded successfully or if a new list was created, false on error
     */
    @SuppressWarnings("unchecked")
    public boolean loadData(int userId) {
        String filePath = getExpenseFilePath(userId);
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("No expense data file found for user ID: " + userId);
            expenses = new ArrayList<>();
            return true;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            expenses = (ArrayList<Expense>) ois.readObject();
            System.out.println("Expense data loaded successfully for user ID: " + userId);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error loading expense data: " + e.getMessage());
            expenses = new ArrayList<>();
            return false;
        }
    }

    /**
     * Gets the file path for storing expense data for a specific user.
     *
     * @param userId The ID of the user
     * @return The file path string
     */
    private String getExpenseFilePath(int userId) {
        return DATA_DIR + "/" + userId + "_expenses.dat";
    }

    /**
     * Creates the data directory if it doesn't exist.
     */
    private void createDataDirIfNeeded() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
    }
}