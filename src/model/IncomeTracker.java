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
 * This class follows the Singleton pattern to ensure only one instance exists.
 * Provides functionality to add, edit, delete, validate, and calculate income entries,
 * as well as persistence capabilities to save and load income data from files.
 *
 * @author Your Name
 * @version 1.0
 * @see Tracker
 * @see Income
 */
public class IncomeTracker implements Tracker<Income> {
    /** The singleton instance of IncomeTracker */
    private static IncomeTracker instance;

    /** List containing all income entries */
    private List<Income> incomeList;

    /**
     * Private constructor for Singleton pattern.
     * Initializes an empty list to store income entries.
     */
    private IncomeTracker() {
        this.incomeList = new ArrayList<>();
    }

    /**
     * Gets the singleton instance of IncomeTracker.
     * Creates a new instance if one doesn't exist yet.
     *
     * @return The singleton instance of IncomeTracker
     */
    public static synchronized IncomeTracker getInstance() {
        if (instance == null) {
            instance = new IncomeTracker();
        }
        return instance;
    }

    /**
     * Retrieves all income entries stored in the tracker.
     * Returns a defensive copy of the internal list to prevent modification.
     *
     * @return A new ArrayList containing all income entries
     */
    public List<Income> getAllIncome() {
        return new ArrayList<>(incomeList);
    }

    /**
     * Retrieves income entries for a specific user.
     *
     * @param userId The ID of the user whose income entries are to be retrieved
     * @return A list of income entries belonging to the specified user
     */
    public List<Income> getUserIncome(int userId) {
        return incomeList.stream()
                .filter(income -> income.getUserId() == userId)
                .collect(Collectors.toList());
    }

    /**
     * Adds a new income entry to the tracker.
     * Validates the income entry before adding it.
     *
     * @param income The income entry to add
     * @throws IllegalArgumentException If the income entry is invalid
     */
    @Override
    public void add(Income income) {
        if (validate(income)) {
            incomeList.add(income);
        } else {
            throw new IllegalArgumentException("Invalid income entry");
        }
    }

    /**
     * Updates an existing income entry in the tracker.
     * Validates the updated income entry before editing.
     *
     * @param updatedIncome The updated income entry
     * @throws IllegalArgumentException If the income entry is invalid or not found
     */
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

    /**
     * Deletes an income entry from the tracker by its ID.
     *
     * @param id The ID of the income entry to delete
     */
    @Override
    public void delete(String id) {
        incomeList.removeIf(income -> income.getId().equals(id));
    }

    /**
     * Validates an income entry by checking that required fields are present and valid.
     * An income entry is valid if:
     * - It is not null
     * - Its source is not null or empty
     * - Its amount is not null and greater than zero
     * - Its date is not null
     *
     * @param income The income entry to validate
     * @return true if the income entry is valid, false otherwise
     */
    @Override
    public boolean validate(Income income) {
        return income != null
                && income.getSource() != null && !income.getSource().trim().isEmpty()
                && income.getAmount() != null && income.getAmount().compareTo(BigDecimal.ZERO) > 0
                && income.getDate() != null;
    }

    /**
     * Calculates the total amount of all income entries.
     *
     * @return The total amount as a BigDecimal
     */
    @Override
    public BigDecimal calculateAmount() {
        return incomeList.stream()
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculates the total amount of income for a specific user.
     *
     * @param userId The ID of the user whose total income is to be calculated
     * @return The total income amount for the specified user as a BigDecimal
     */
    public BigDecimal calculateUserTotalIncome(int userId) {
        return incomeList.stream()
                .filter(income -> income.getUserId() == userId)
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Saves income data for a specific user to a file.
     * The file is stored in the 'data' directory with the name '{userId}_income.dat'.
     *
     * @param userId The ID of the user whose income data is to be saved
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
     * Loads income data for a specific user from a file.
     * If the file doesn't exist, no action is taken.
     * The method removes any existing income entries for the user and replaces
     * them with the loaded ones.
     *
     * @param userId The ID of the user whose income data is to be loaded
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
     * Clears all income data from the tracker.
     */
    public void clearData() {
        incomeList.clear();
    }
}