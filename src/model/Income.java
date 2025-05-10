package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents an income entry in the budget tracking system.
 */
public class Income implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String source;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private boolean recurring;
    private String recurringPeriod; // "weekly", "monthly", "yearly"
    private int userId; // To link with the user data

    /**
     * Default constructor for serialization
     */
    public Income() {
        this.id = UUID.randomUUID().toString();
    }

    /**
     * Constructor for creating a new income entry
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

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public String getRecurringPeriod() {
        return recurringPeriod;
    }

    public void setRecurringPeriod(String recurringPeriod) {
        this.recurringPeriod = recurringPeriod;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

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