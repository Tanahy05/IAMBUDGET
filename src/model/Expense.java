package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents an expense entry in the expense tracking system.
 * This class encapsulates all relevant information about an expense,
 * including its category, amount, date, description, and recurring status.
 * Each expense has a unique identifier generated upon creation.
 * The class implements Serializable to allow for persistent storage.
 */
public class Expense implements Serializable {
    /** Serial version UID for serialization compatibility */
    private static final long serialVersionUID = 1L;

    /** Unique identifier for the expense */
    private String id;

    /** Category of the expense (e.g., "Food", "Housing") */
    private String category;

    /** Monetary amount of the expense */
    private double amount;

    /** Date when the expense occurred */
    private LocalDate date;

    /** Optional description of the expense */
    private String description;

    /** Flag indicating whether this is a recurring expense */
    private boolean isRecurring;

    /** Frequency of recurrence if recurring (e.g., "weekly", "monthly") */
    private String recurringFrequency; // e.g., "weekly", "monthly"

    /**
     * Creates a new non-recurring expense with the specified details.
     *
     * @param category    The category of the expense
     * @param amount      The monetary amount of the expense
     * @param date        The date when the expense occurred
     * @param description A description of the expense
     */
    public Expense(String category, double amount, LocalDate date, String description) {
        this(category, amount, date, description, false, null);
    }

    /**
     * Creates a new expense with the specified details, including recurring information.
     *
     * @param category           The category of the expense
     * @param amount             The monetary amount of the expense
     * @param date               The date when the expense occurred
     * @param description        A description of the expense
     * @param isRecurring        Whether the expense is recurring
     * @param recurringFrequency The frequency of recurrence if recurring (e.g., "weekly", "monthly")
     */
    public Expense(String category, double amount, LocalDate date, String description,
                   boolean isRecurring, String recurringFrequency) {
        this.id = UUID.randomUUID().toString();
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.isRecurring = isRecurring;
        this.recurringFrequency = recurringFrequency;
    }

    /**
     * Gets the unique identifier of the expense.
     *
     * @return The expense's unique identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the category of the expense.
     *
     * @return The expense's category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of the expense.
     *
     * @param category The new category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the monetary amount of the expense.
     *
     * @return The expense amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the monetary amount of the expense.
     *
     * @param amount The new amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Gets the date when the expense occurred.
     *
     * @return The expense date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date when the expense occurred.
     *
     * @param date The new date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the description of the expense.
     *
     * @return The expense description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the expense.
     *
     * @param description The new description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Checks if the expense is recurring.
     *
     * @return true if the expense is recurring, false otherwise
     */
    public boolean isRecurring() {
        return isRecurring;
    }

    /**
     * Sets whether the expense is recurring.
     *
     * @param recurring true if the expense is recurring, false otherwise
     */
    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    /**
     * Gets the frequency of recurrence if the expense is recurring.
     *
     * @return The frequency of recurrence (e.g., "weekly", "monthly")
     */
    public String getRecurringFrequency() {
        return recurringFrequency;
    }

    /**
     * Sets the frequency of recurrence for recurring expenses.
     *
     * @param recurringFrequency The new frequency to set (e.g., "weekly", "monthly")
     */
    public void setRecurringFrequency(String recurringFrequency) {
        this.recurringFrequency = recurringFrequency;
    }

    /**
     * Returns a string representation of the expense.
     * Format: "Category - $Amount (Date)"
     *
     * @return A string representation of the expense
     */
    @Override
    public String toString() {
        return String.format("%s - $%.2f (%s)", category, amount, date);
    }
}