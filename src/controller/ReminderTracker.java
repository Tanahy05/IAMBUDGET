package controller;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import model.Reminder;

/**
 * Singleton class for managing reminders in the system
 */
public class ReminderTracker {
    private static final String DATA_DIR = "data";
    private static ReminderTracker instance;
    private ArrayList<Reminder> reminders;
    private int nextReminderId = 1;

    // Private constructor for singleton pattern
    private ReminderTracker() {
        reminders = new ArrayList<>();
    }

    public static ReminderTracker getInstance() {
        if (instance == null) {
            instance = new ReminderTracker();
        }
        return instance;
    }

    /**
     * Adds a new reminder to the tracker
     */
    public Reminder addReminder(String name, double amount, LocalDate dueDate, LocalDate reminderDate, int userId) {
        Reminder reminder = new Reminder(nextReminderId++, name, amount, dueDate, reminderDate, userId);
        reminders.add(reminder);
        return reminder;
    }

    /**
     * Get all reminders for a specific user
     */
    public List<Reminder> getUserReminders(int userId) {
        return reminders.stream()
                .filter(r -> r.getUserId() == userId)
                .collect(Collectors.toList());
    }

    /**
     * Mark a reminder as completed
     */
    public boolean markReminderCompleted(int reminderId, int userId) {
        for (Reminder reminder : reminders) {
            if (reminder.getReminderId() == reminderId && reminder.getUserId() == userId) {
                reminder.setCompleted(true);
                return true;
            }
        }
        return false;
    }

    /**
     * Clear all reminders from memory
     */
    public void clearData() {
        reminders.clear();
        nextReminderId = 1;
    }

    /**
     * Load reminders for a specific user from storage
     */
    public void loadData(int userId) {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        String filePath = DATA_DIR + "/" + userId + "_reminders.dat";
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("No reminders file found for user " + userId);
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            ArrayList<Reminder> userReminders = (ArrayList<Reminder>) ois.readObject();

            // Add only the user's reminders to our list
            for (Reminder reminder : userReminders) {
                // Check if we already have this reminder in memory
                boolean exists = reminders.stream()
                        .anyMatch(r -> r.getReminderId() == reminder.getReminderId() &&
                                r.getUserId() == reminder.getUserId());

                if (!exists) {
                    reminders.add(reminder);

                    // Update nextReminderId if needed
                    if (reminder.getReminderId() >= nextReminderId) {
                        nextReminderId = reminder.getReminderId() + 1;
                    }
                }
            }

            System.out.println("Loaded " + userReminders.size() + " reminders for user " + userId);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading reminders: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Save reminders for a specific user to storage
     */
    public void saveData(int userId) {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        List<Reminder> userReminders = getUserReminders(userId);

        String filePath = DATA_DIR + "/" + userId + "_reminders.dat";

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(new ArrayList<>(userReminders));
            System.out.println("Saved " + userReminders.size() + " reminders for user " + userId);
        } catch (IOException e) {
            System.err.println("Error saving reminders: " + e.getMessage());
            e.printStackTrace();
        }
    }
}