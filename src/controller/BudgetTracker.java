package controller;

import interfaces.Tracker;
import model.Budget;
import model.SystemManager;
import model.User;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BudgetTracker implements the Tracker interface for Budget objects
 * Handles CRUD operations for budgets and tracking budget spending
 */
public class BudgetTracker implements Tracker<Budget>, Serializable {

    private static final long serialVersionUID = 1L;
    private static BudgetTracker instance;

    private List<Budget> budgets;

    /**
     * Private constructor for Singleton pattern
     */
    private BudgetTracker() {
        this.budgets = new ArrayList<>();
    }

    /**
     * Gets the singleton instance of BudgetTracker
     *
     * @return BudgetTracker instance
     */
    public static synchronized BudgetTracker getInstance() {
        if (instance == null) {
            instance = new BudgetTracker();
        }
        return instance;
    }

    /**
     * Adds a new budget
     *
     * @param budget The budget to add
     */
    @Override
    public void add(Budget budget) {
        // Check if a budget with this category already exists
        for (int i = 0; i < budgets.size(); i++) {
            Budget existingBudget = budgets.get(i);
            if (existingBudget.getCategory().equals(budget.getCategory())) {
                // Replace the existing budget
                budgets.set(i, budget);
                saveData();
                return;
            }
        }

        // Add new budget if no existing one found
        budgets.add(budget);
        saveData();
    }

    /**
     * Edits an existing budget
     *
     * @param budget The updated budget
     */
    @Override
    public void edit(Budget budget) {
        for (int i = 0; i < budgets.size(); i++) {
            if (budgets.get(i).getBudgetId().equals(budget.getBudgetId())) {
                budgets.set(i, budget);
                saveData();
                return;
            }
        }
    }

    /**
     * Deletes a budget by ID
     *
     * @param id The budget ID to delete
     */
    @Override
    public void delete(String id) {
        budgets.removeIf(budget -> budget.getBudgetId().equals(id));
        saveData();
    }

    /**
     * Validates a budget before adding or editing
     *
     * @param budget The budget to validate
     * @return true if the budget is valid
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
     * Calculates the total budget amount
     *
     * @return The sum of all budget limits
     */
    @Override
    public BigDecimal calculateAmount() {
        return budgets.stream()
                .map(Budget::getLimit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Gets all budgets
     *
     * @return List of all budgets
     */
    public List<Budget> getBudgets() {
        return new ArrayList<>(budgets);
    }

    /**
     * Gets budgets for a specific category
     *
     * @param category The category to filter by
     * @return List of budgets for the category
     */
    public List<Budget> getBudgetsByCategory(String category) {
        return budgets.stream()
                .filter(budget -> budget.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    /**
     * Saves the budget data to file
     *
     * @return true if saved successfully
     */
    public boolean saveData() {
        User currentUser = SystemManager.getCurrentUser();
        if (currentUser == null) return false;

        String filePath = "data/" + currentUser.getUserID() + "_budgets.dat";

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(budgets);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Loads the budget data from file
     *
     * @return true if loaded successfully
     */
    @SuppressWarnings("unchecked")
    public boolean loadData() {
        User currentUser = SystemManager.getCurrentUser();
        if (currentUser == null) return false;

        String filePath = "data/" + currentUser.getUserID() + "_budgets.dat";
        File file = new File(filePath);

        if (!file.exists()) return false;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            budgets = (List<Budget>) ois.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Clears all budget data
     */
    public void clearData() {
        budgets.clear();
    }
}