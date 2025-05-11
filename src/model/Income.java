package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents an income entry in the budget tracking system.
 * This class encapsulates all relevant information about an income source,
 * including its source, amount, date, description, and recurring status.
 * Each income entry has a unique identifier generated upon creation.
 * The class implements Serializable to allow for persistent storage.
 */
public class Income implements Serializable {
    /** Serial version UID for serialization compatibility */
    private static final long serialVersionUID = 1L;

    /** Unique identifier for the income entry */
    private String id;

    /** Source of the income (e.g., "Salary", "Freelance") */
    private String source;

    /** Monetary amount of the income, using BigDecimal for precise currency calculations */
    private BigDecimal amount;

    /** Date when the income was received */
    private LocalDate date;

    /** Optional description of the income */
    private String description;

    /** Flag indicating whether this is a recurring income */
    private boolean recurring;

    /** Period of recurrence if recurring (e.g., "weekly", "monthly", "yearly") */
    private String recurringPeriod; // "weekly", "monthly", "yearly"

    /** User ID to associate this income with a specific user */
    private int userId; // To link with the user data

    /**
     * Default constructor for serialization.
     * Initializes a new income entry with a unique identifier.
     */
    public Income() {
        this.id = UUID.randomUUID().toString();
    }

    /**
     * Constructs a new income entry with the specified details.
     *
     * @param source          The source of the income (e.g., "Salary", "Freelance")
     * @param amount          The monetary amount of the income
     * @param date            The date when the income was received
     * @param description     A description of the income
     * @param recurring       Whether the income is recurring
     * @param recurringPeriod The period of recurrence if recurring (e.g., "weekly", "monthly", "yearly")
     * @param userId          The ID of the user associated with this income
     */
    public Income(String source, BigDecimal amount, LocalDate date, String description,
                  boolean recurring, String recurringPeriod, int userId) {
        this.id = UUID.randomUUID().toString();
        this.source = source;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.recurring = recurring;
        this.recurringPeriod = recurringPeriod;
        this.userId = userId;
    }

    /**
     * Gets the unique identifier of the income entry.
     *
     * @return The income's unique identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the income entry.
     * Generally used only during deserialization.
     *
     * @param id The unique identifier to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the source of the income.
     *
     * @return The income source
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the source of the income.
     *
     * @param source The new income source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Gets the monetary amount of the income.
     *
     * @return The income amount as a BigDecimal for precise currency calculations
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the monetary amount of the income.
     *
     * @param amount The new amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Gets the date when the income was received.
     *
     * @return The income receipt date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date when the income was received.
     *
     * @param date The new date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the description of the income.
     *
     * @return The income description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the income.
     *
     * @param description The new description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Checks if the income is recurring.
     *
     * @return true if the income is recurring, false otherwise
     */
    public boolean isRecurring() {
        return recurring;
    }

    /**
     * Sets whether the income is recurring.
     *
     * @param recurring true if the income is recurring, false otherwise
     */
    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    /**
     * Gets the period of recurrence if the income is recurring.
     *
     * @return The period of recurrence (e.g., "weekly", "monthly", "yearly")
     */
    public String getRecurringPeriod() {
        return recurringPeriod;
    }

    /**
     * Sets the period of recurrence for recurring income.
     *
     * @param recurringPeriod The new recurrence period to set (e.g., "weekly", "monthly", "yearly")
     */
    public void setRecurringPeriod(String recurringPeriod) {
        this.recurringPeriod = recurringPeriod;
    }

    /**
     * Gets the ID of the user associated with this income.
     *
     * @return The user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the user associated with this income.
     *
     * @param userId The new user ID to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Returns a string representation of the income entry.
     * Includes id, source, amount, date, recurring status, and recurring period.
     *
     * @return A string representation of the income entry
     */
    @Override
    public String toString() {
        return "Income{" +
                "id='" + id + '\'' +
                ", source='" + source + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", recurring=" + recurring +
                ", recurringPeriod='" + recurringPeriod + '\'' +
                '}';
    }
}