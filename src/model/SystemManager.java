package model;

import controller.BudgetTracker;
import controller.ExpenseTracker;
import controller.ReminderTracker;
import java.io.File;

public class SystemManager {
    private static User currentUser;
    private static BudgetTracker budgetTracker;
    private static ExpenseTracker expenseTracker;
    private static IncomeTracker incomeTracker;
    private static ReminderTracker reminderTracker;

    public static void loginUser(User user) {
        currentUser = user;
        loadUserData(user.getUserID());
        System.out.println(currentUser.getUserID() + " " + currentUser.getUsername());
    }

    public static void logoutUser() {
        saveUserData(); // Save data before logging out
        currentUser = null;
        if (budgetTracker != null) {
            budgetTracker.clearData(); // Clear budget data from memory
        }
        if (expenseTracker != null) {
            expenseTracker.clearData(); // Clear expense data from memory
        }
        if (incomeTracker != null) {
            incomeTracker.clearData(); // Clear income data from memory
        }
        if (reminderTracker != null) {
            reminderTracker.clearData(); // Clear reminder data from memory
        }
        System.out.println("Logged out successfully");
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    private static void loadUserData(int uid) {
        // Initialize the budget tracker
        budgetTracker = BudgetTracker.getInstance();

        // Initialize the expense tracker
        expenseTracker = ExpenseTracker.getInstance();

        // Initialize the income tracker
        incomeTracker = IncomeTracker.getInstance();

        // Initialize the reminder tracker
        reminderTracker = ReminderTracker.getInstance();

        // Create data directory if it doesn't exist
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        // Load budget data
        budgetTracker.loadData();

        // Load expense data
        expenseTracker.loadData(uid);

        // Load income data
        incomeTracker.loadData(uid);

        // Load reminder data
        reminderTracker.loadData(uid);
    }

    public static void saveUserData() {
        if (currentUser == null) return;
        int uid = currentUser.getUserID();

        // Create data directory if it doesn't exist
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        // Save budget data
        if (budgetTracker != null) {
            budgetTracker.saveData();
        }

        // Save expense data
        if (expenseTracker != null) {
            expenseTracker.saveData(uid);
        }

        // Save income data
        if (incomeTracker != null) {
            incomeTracker.saveData(uid);
        }

        // Save reminder data
        if (reminderTracker != null) {
            reminderTracker.saveData(uid);
        }
    }

    public static boolean isUserLoggedIn() {
        return currentUser != null;
    }

    public static BudgetTracker getBudgetTracker() {
        if (budgetTracker == null) {
            budgetTracker = BudgetTracker.getInstance();
        }
        return budgetTracker;
    }

    public static ExpenseTracker getExpenseTracker() {
        if (expenseTracker == null) {
            expenseTracker = ExpenseTracker.getInstance();
        }
        return expenseTracker;
    }

    public static IncomeTracker getIncomeTracker() {
        if (incomeTracker == null) {
            incomeTracker = IncomeTracker.getInstance();
        }
        return incomeTracker;
    }

    public static ReminderTracker getReminderTracker() {
        if (reminderTracker == null) {
            reminderTracker = ReminderTracker.getInstance();
        }
        return reminderTracker;
    }
}