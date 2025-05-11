package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class Expense implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String category;
    private double amount;
    private LocalDate date;
    private String description;
    private boolean isRecurring;
    private String recurringFrequency; // e.g., "weekly", "monthly"

    public Expense(String category, double amount, LocalDate date, String description) {
        this(category, amount, date, description, false, null);
    }

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

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
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
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public String getRecurringFrequency() {
        return recurringFrequency;
    }

    public void setRecurringFrequency(String recurringFrequency) {
        this.recurringFrequency = recurringFrequency;
    }

    @Override
    public String toString() {
        return String.format("%s - $%.2f (%s)", category, amount, date);
    }
}