package model;

import controller.BudgetTracker;
import controller.ExpenseTracker;
import controller.ReminderTracker;
import java.io.File;

/**
 * Manages the current user's session and provides access to data trackers.
 * Handles loading and saving of user-specific data including budget, expenses,
 * income, and reminders.
 */
public class SystemManager {

    /** The currently logged-in user. */
    private static User currentUser;

    /** Singleton instance of the BudgetTracker. */
    private static BudgetTracker budgetTracker;

    /** Singleton instance of the ExpenseTracker. */
    private static ExpenseTracker expenseTracker;

    /** Singleton instance of the IncomeTracker. */
    private static IncomeTracker incomeTracker;

    /** Singleton instance of the ReminderTracker. */
    private static ReminderTracker reminderTracker;

    /**
     * Logs in the given user, initializes trackers, and loads their data.
     *
     * @param user The user to log in.
     */
    public static void loginUser(User user) {
        currentUser = user;
        loadUserData(user.getUserID());
        System.out.println(currentUser.getUserID() + " " + currentUser.getUsername());
    }

    /**
     * Logs out the current user, saves their data, and clears trackers from memory.
     */
    public static void logoutUser() {
        saveUserData(); // Save data before logging out
        currentUser = null;

        if (budgetTracker != null) {
            budgetTracker.clearData();
        }
        if (expenseTracker != null) {
            expenseTracker.clearData();
        }
        if (incomeTracker != null) {
            incomeTracker.clearData();
        }
        if (reminderTracker != null) {
            reminderTracker.clearData();
        }

        System.out.println("Logged out successfully");
    }

    /**
     * Gets the currently logged-in user.
     *
     * @return The current user, or null if no user is logged in.
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Loads user data from disk for the specified user ID.
     *
     * @param uid The user ID to load data for.
     */
    private static void loadUserData(int uid) {
        budgetTracker = BudgetTracker.getInstance();
        expenseTracker = ExpenseTracker.getInstance();
        incomeTracker = IncomeTracker.getInstance();
        reminderTracker = ReminderTracker.getInstance();

        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        budgetTracker.loadData();
        expenseTracker.loadData(uid);
        incomeTracker.loadData(uid);
        reminderTracker.loadData(uid);
    }

    /**
     * Saves the current user's data to disk.
     */
    public static void saveUserData() {
        if (currentUser == null) return;
        int uid = currentUser.getUserID();

        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        if (budgetTracker != null) {
            budgetTracker.saveData();
        }
        if (expenseTracker != null) {
            expenseTracker.saveData(uid);
        }
        if (incomeTracker != null) {
            incomeTracker.saveData(uid);
        }
        if (reminderTracker != null) {
            reminderTracker.saveData(uid);
        }
    }

    /**
     * Checks if a user is currently logged in.
     *
     * @return {@code true} if a user is logged in, {@code false} otherwise.
     */
    public static boolean isUserLoggedIn() {
        return currentUser != null;
    }

    /**
     * Returns the BudgetTracker instance.
     *
     * @return The singleton BudgetTracker instance.
     */
    public static BudgetTracker getBudgetTracker() {
        if (budgetTracker == null) {
            budgetTracker = BudgetTracker.getInstance();
        }
        return budgetTracker;
    }

    /**
     * Returns the ExpenseTracker instance.
     *
     * @return The singleton ExpenseTracker instance.
     */
    public static ExpenseTracker getExpenseTracker() {
        if (expenseTracker == null) {
            expenseTracker = ExpenseTracker.getInstance();
        }
        return expenseTracker;
    }

    /**
     * Returns the IncomeTracker instance.
     *
     * @return The singleton IncomeTracker instance.
     */
    public static IncomeTracker getIncomeTracker() {
        if (incomeTracker == null) {
            incomeTracker = IncomeTracker.getInstance();
        }
        return incomeTracker;
    }

    /**
     * Returns the ReminderTracker instance.
     *
     * @return The singleton ReminderTracker instance.
     */
    public static ReminderTracker getReminderTracker() {
        if (reminderTracker == null) {
            reminderTracker = ReminderTracker.getInstance();
        }
        return reminderTracker;
    }
}
