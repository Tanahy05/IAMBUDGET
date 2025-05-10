package model;
import model.User;
import controller.BudgetTracker;
import java.io.File;

public class SystemManager {
    private static User currentUser;
    private static BudgetTracker budgetTracker;

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
        System.out.println("Logged out successfully");
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    private static void loadUserData(int uid) {
        // Initialize the budget tracker
        budgetTracker = BudgetTracker.getInstance();

        // Create data directory if it doesn't exist
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        // Load budget data
        budgetTracker.loadData();

        // TODO: Load Expense, Income, Transaction, etc.
        // from files like "data/uid_expense.dat" etc.
    }

    public static void saveUserData() {
        if (currentUser == null) return;

        // Create data directory if it doesn't exist
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        // Save budget data
        if (budgetTracker != null) {
            budgetTracker.saveData();
        }

        // TODO: Save Expense, Income, Transaction, etc.
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
}