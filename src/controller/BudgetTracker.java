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


public class BudgetTracker implements Tracker<Budget>, Serializable {

    private static BudgetTracker instance;

    private List<Budget> budgets;


    private BudgetTracker() {
        this.budgets = new ArrayList<>();
    }


    public static synchronized BudgetTracker getInstance() {
        if (instance == null) {
            instance = new BudgetTracker();
        }
        return instance;
    }


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


    @Override
    public void delete(String id) {
        budgets.removeIf(budget -> budget.getBudgetId().equals(id));
    }


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


    @Override
    public BigDecimal calculateAmount() {
        return budgets.stream()
                .map(Budget::getLimit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public List<Budget> getBudgets() {
        return new ArrayList<>(budgets);
    }

    public List<Budget> getBudgetsByCategory(String category) {
        return budgets.stream()
                .filter(budget -> budget.getCategory().equals(category))
                .collect(Collectors.toList());
    }


    public Budget getBudgetById(String budgetId) {
        return budgets.stream()
                .filter(budget -> budget.getBudgetId().equals(budgetId))
                .findFirst()
                .orElse(null);
    }


    public boolean saveData() {
        User currentUser = SystemManager.getCurrentUser();
        if (currentUser == null) return false;

        // Convert List to ArrayList for the UserDatabase method
        ArrayList<Budget> budgetsList = new ArrayList<>(budgets);

        // Use UserDatabase to save budgets
        return UserDatabase.saveBudgets(currentUser.getUserID(), budgetsList);
    }


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

    public void clearData() {
        budgets.clear();
        System.out.println("Budget data cleared from memory");
    }
}