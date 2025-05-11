package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A model class representing a financial reminder.
 * <p>
 * This class stores all relevant information about a financial reminder including the name,
 * amount, due date, reminder date, user association, completion status, and a unique identifier.
 * It implements Serializable to allow for object persistence.
 * </p>
 *
 * @author Your Name
 * @version 1.0
 * @since 2025-05-12
 */
public class Reminder implements Serializable {

    /** The name or description of the reminder */
    private String name;

    /** The financial amount associated with this reminder */
    private double amount;

    /** The date when the financial obligation is due */
    private LocalDate dueDate;

    /** The date when the user should be reminded about this obligation */
    private LocalDate reminderDate;

    /** The user ID to whom this reminder belongs */
    private int userId;

    /** Flag indicating whether the reminder has been completed */
    private boolean completed;

    /** Unique identifier for the reminder */
    private int reminderId;

    /**
     * Constructs a new Reminder with the specified details.
     * <p>
     * By default, new reminders are marked as not completed.
     * </p>
     *
     * @param reminderId    The unique identifier for this reminder
     * @param name          The name or description of the reminder
     * @param amount        The financial amount associated with this reminder
     * @param dueDate       The date when the financial obligation is due
     * @param reminderDate  The date when the user should be reminded about this obligation
     * @param userId        The user ID to whom this reminder belongs
     */
    public Reminder(int reminderId, String name, double amount, LocalDate dueDate, LocalDate reminderDate, int userId) {
        this.reminderId = reminderId;
        this.name = name;
        this.amount = amount;
        this.dueDate = dueDate;
        this.reminderDate = reminderDate;
        this.userId = userId;
        this.completed = false;
    }

    /**
     * Returns the unique identifier of this reminder.
     *
     * @return The reminder ID
     */
    public int getReminderId() {
        return reminderId;
    }

    /**
     * Sets the unique identifier for this reminder.
     *
     * @param reminderId The new reminder ID to set
     */
    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    /**
     * Returns the name or description of this reminder.
     *
     * @return The reminder name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name or description of this reminder.
     *
     * @param name The new name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the financial amount associated with this reminder.
     *
     * @return The financial amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the financial amount associated with this reminder.
     *
     * @param amount The new amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Returns the due date of this reminder.
     *
     * @return The due date
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Sets the due date of this reminder.
     *
     * @param dueDate The new due date to set
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Returns the reminder date of this reminder.
     *
     * @return The reminder date
     */
    public LocalDate getReminderDate() {
        return reminderDate;
    }

    /**
     * Sets the reminder date of this reminder.
     *
     * @param reminderDate The new reminder date to set
     */
    public void setReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
    }

    /**
     * Returns the user ID associated with this reminder.
     *
     * @return The user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user ID associated with this reminder.
     *
     * @param userId The new user ID to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Checks if this reminder has been completed.
     *
     * @return {@code true} if the reminder is completed, {@code false} otherwise
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Sets the completion status of this reminder.
     *
     * @param completed The new completion status to set
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Returns a string representation of this reminder.
     * <p>
     * The returned string includes the name, amount, due date, reminder date, and completion status.
     * </p>
     *
     * @return A string representation of this reminder
     */
    @Override
    public String toString() {
        return "Reminder{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                ", dueDate=" + dueDate +
                ", reminderDate=" + reminderDate +
                ", completed=" + completed +
                '}';
    }
}