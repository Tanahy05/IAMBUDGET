package controller;

import interfaces.Tracker;
import model.Budget;
import model.SystemManager;
import model.User;
import storage.UserDatabase;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A singleton controller class that manages budget tracking operations in the system.
 * This class implements the Tracker interface for Budget objects and provides functionality
 * for adding, editing, deleting, and retrieving budgets. It also handles data persistence
 * operations through the UserDatabase.
 *
 * @see interfaces.Tracker
 * @see model.Budget
 * @see storage.UserDatabase
 */
public class BudgetTracker implements Tracker<Budget>, Serializable {

    private static BudgetTracker instance;

    private List<Budget> budgets;

    /**
     * Private constructor to enforce the singleton pattern.
     * Initializes an empty list of budgets.
     */
    private BudgetTracker() {
        this.budgets = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of the BudgetTracker.
     * Creates a new instance if one doesn't exist.
     *
     * @return The singleton instance of BudgetTracker
     */
    public static synchronized BudgetTracker getInstance() {
        if (instance == null) {
            instance = new BudgetTracker();
        }
        return instance;
    }

    /**
     * Adds a new budget to the tracker or updates an existing one if a budget
     * with the same category already exists.
     *
     * @param budget The Budget object to be added or updated
     */
    @Override
    public void add(Budget budget) {
        if (!validate(budget)) {
            return;
        }

        // Check if a budget with this category already exists
        for (int i = 0; i < budgets.size(); i++) {
            Budget existingBudget = budgets.get(i);
            if (existingBudget.getCategory().equals(budget.getCategory())) {
                // Replace the existing budget
                budgets.set(i, budget);
                return;
            }
        }

        // Add new budget if no existing one found
        budgets.add(budget);
    }

    /**
     * Edits an existing budget in the tracker.
     * The budget is identified by its budgetId.
     *
     * @param budget The updated Budget object
     */
    @Override
    public void edit(Budget budget) {
        if (!validate(budget)) {
            return;
        }

        for (int i = 0; i < budgets.size(); i++) {
            if (budgets.get(i).getBudgetId().equals(budget.getBudgetId())) {
                budgets.set(i, budget);
                return;
            }
        }
    }

    /**
     * Deletes a budget from the tracker by its ID.
     *
     * @param id The ID of the budget to be deleted
     */
    @Override
    public void delete(String id) {
        budgets.removeIf(budget -> budget.getBudgetId().equals(id));
    }

    /**
     * Validates a budget object to ensure all required fields are present and valid.
     *
     * @param budget The Budget object to validate
     * @return true if the budget is valid, false otherwise
     */
    @Override
    public boolean validate(Budget budget) {
        // Check that all required fields are present
        if (budget.getCategory() == null || budget.getCategory().trim().isEmpty()) {
            return false;
        }

        if (budget.getLimit() == null || budget.getLimit().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        if (budget.getPeriod() == null || budget.getPeriod().trim().isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * Calculates the total sum of all budget limits.
     *
     * @return The total amount of all budgets as a BigDecimal
     */
    @Override
    public BigDecimal calculateAmount() {
        return budgets.stream()
                .map(Budget::getLimit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Returns a defensive copy of all budgets in the tracker.
     *
     * @return A new list containing all budgets
     */
    public List<Budget> getBudgets() {
        return new ArrayList<>(budgets);
    }

    /**
     * Retrieves all budgets that match the specified category.
     *
     * @param category The category to filter budgets by
     * @return A list of budgets matching the specified category
     */
    public List<Budget> getBudgetsByCategory(String category) {
        return budgets.stream()
                .filter(budget -> budget.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    /**
     * Finds and returns a budget by its ID.
     *
     * @param budgetId The ID of the budget to find
     * @return The Budget object if found, null otherwise
     */
    public Budget getBudgetById(String budgetId) {
        return budgets.stream()
                .filter(budget -> budget.getBudgetId().equals(budgetId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Saves the current budget data to persistent storage using the UserDatabase.
     *
     * @return true if the save operation was successful, false otherwise
     */
    public boolean saveData() {
        User currentUser = SystemManager.getCurrentUser();
        if (currentUser == null) return false;

        // Convert List to ArrayList for the UserDatabase method
        ArrayList<Budget> budgetsList = new ArrayList<>(budgets);

        // Use UserDatabase to save budgets
        return UserDatabase.saveBudgets(currentUser.getUserID(), budgetsList);
    }

    /**
     * Loads budget data from persistent storage for the current user.
     *
     * @return true if data was successfully loaded, false if there was no data or an error occurred
     */
    public boolean loadData() {
        User currentUser = SystemManager.getCurrentUser();
        if (currentUser == null) return false;

        // Use UserDatabase to load budgets
        ArrayList<Budget> loadedBudgets = UserDatabase.loadBudgets(currentUser.getUserID());

        if (loadedBudgets != null && !loadedBudgets.isEmpty()) {
            budgets = loadedBudgets;
            System.out.println("Found " + budgets.size() + " budgets for user ID: " + currentUser.getUserID());
            return true;
        } else {
            budgets = new ArrayList<>();
            return false;
        }
    }

    /**
     * Clears all budget data from memory.
     */
    public void clearData() {
        budgets.clear();
        System.out.println("Budget data cleared from memory");
    }
}