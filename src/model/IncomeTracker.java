package model;

import interfaces.Tracker;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the Tracker interface for managing income entries.
 */
public class IncomeTracker implements Tracker<Income> {
    private static IncomeTracker instance;
    private List<Income> incomeList;

    /**
     * Private constructor for Singleton pattern
     */
    private IncomeTracker() {
        this.incomeList = new ArrayList<>();
    }

    /**
     * Get singleton instance of IncomeTracker
     */
    public static synchronized IncomeTracker getInstance() {
        if (instance == null) {
            instance = new IncomeTracker();
        }
        return instance;
    }

    /**
     * Get all income entries
     */
    public List<Income> getAllIncome() {
        return new ArrayList<>(incomeList);
    }

    /**
     * Get income entries for a specific user
     */
    public List<Income> getUserIncome(int userId) {
        return incomeList.stream()
                .filter(income -> income.getUserId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public void add(Income income) {
        if (validate(income)) {
            incomeList.add(income);
        } else {
            throw new IllegalArgumentException("Invalid income entry");
        }
    }

    @Override
    public void edit(Income updatedIncome) {
        if (!validate(updatedIncome)) {
            throw new IllegalArgumentException("Invalid income entry");
        }

        Optional<Income> existingIncome = incomeList.stream()
                .filter(income -> income.getId().equals(updatedIncome.getId()))
                .findFirst();

        if (existingIncome.isPresent()) {
            int index = incomeList.indexOf(existingIncome.get());
            incomeList.set(index, updatedIncome);
        } else {
            throw new IllegalArgumentException("Income not found with ID: " + updatedIncome.getId());
        }
    }

    @Override
    public void delete(String id) {
        incomeList.removeIf(income -> income.getId().equals(id));
    }

    @Override
    public boolean validate(Income income) {
        return income != null
                && income.getSource() != null && !income.getSource().trim().isEmpty()
                && income.getAmount() != null && income.getAmount().compareTo(BigDecimal.ZERO) > 0
                && income.getDate() != null;
    }

    @Override
    public BigDecimal calculateAmount() {
        return incomeList.stream()
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculate total income for a specific user
     */
    public BigDecimal calculateUserTotalIncome(int userId) {
        return incomeList.stream()
                .filter(income -> income.getUserId() == userId)
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Save income data to file
     */
    public void saveData(int userId) {
        try {
            File file = new File("data/" + userId + "_income.dat");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            // Filter income for current user
            List<Income> userIncome = getUserIncome(userId);

            oos.writeObject(userIncome);
            oos.close();
            fos.close();
        } catch (IOException e) {
            System.err.println("Error saving income data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load income data from file
     */
    @SuppressWarnings("unchecked")
    public void loadData(int userId) {
        try {
            File file = new File("data/" + userId + "_income.dat");
            if (!file.exists()) {
                return;
            }

            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            List<Income> loadedIncome = (List<Income>) ois.readObject();

            // Remove any existing income for this user and add the loaded ones
            incomeList.removeIf(income -> income.getUserId() == userId);
            incomeList.addAll(loadedIncome);

            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading income data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Clear all income data
     */
    public void clearData() {
        incomeList.clear();
    }
}