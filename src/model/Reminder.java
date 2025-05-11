package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Model class representing a financial reminder
 */
public class Reminder implements Serializable {
    private String name;
    private double amount;
    private LocalDate dueDate;
    private LocalDate reminderDate;
    private int userId;
    private boolean completed;
    private int reminderId;

    public Reminder(int reminderId, String name, double amount, LocalDate dueDate, LocalDate reminderDate, int userId) {
        this.reminderId = reminderId;
        this.name = name;
        this.amount = amount;
        this.dueDate = dueDate;
        this.reminderDate = reminderDate;
        this.userId = userId;
        this.completed = false;
    }

    // Getters and setters
    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

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