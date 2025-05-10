package model;

import controller.BudgetTracker;
import java.io.File;

public class SystemManager {
    private static User currentUser;
    private static BudgetTracker budgetTracker;
    private static IncomeTracker incomeTracker;

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
        if (incomeTracker != null) {
            incomeTracker.clearData(); // Clear income data from memory
        }
        System.out.println("Logged out successfully");
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    private static void loadUserData(int uid) {
        // Initialize the budget tracker
        budgetTracker = BudgetTracker.getInstance();

        // Initialize the income tracker
        incomeTracker = IncomeTracker.getInstance();

        // Create data directory if it doesn't exist
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        // Load budget data
        budgetTracker.loadData();

        // Load income data
        incomeTracker.loadData(uid);

        // TODO: Load Expense, Transaction, etc.
        // from files like "data/uid_expense.dat" etc.
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

        // Save income data
        if (incomeTracker != null) {
            incomeTracker.saveData(uid);
        }

        // TODO: Save Expense, Transaction, etc.
        // to files like "data/uid_expense.dat" etc.
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

    public static IncomeTracker getIncomeTracker() {
        if (incomeTracker == null) {
            incomeTracker = IncomeTracker.getInstance();
        }
        return incomeTracker;
    }
}