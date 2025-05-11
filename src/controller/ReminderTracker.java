package controller;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import model.Reminder;

/**
 * Singleton class for managing reminders in the system.
 * This class handles the creation, storage, retrieval, and management of user reminders.
 * It provides methods for adding new reminders, retrieving user-specific reminders,
 * marking reminders as completed, and persisting reminder data to disk.
 */
public class ReminderTracker {
    /** Directory where reminder data files are stored */
    private static final String DATA_DIR = "data";

    /** Singleton instance of the ReminderTracker */
    private static ReminderTracker instance;

    /** List of all reminders in the system */
    private ArrayList<Reminder> reminders;

    /** Counter for assigning unique IDs to new reminders */
    private int nextReminderId = 1;

    /**
     * Private constructor for singleton pattern.
     * Initializes the reminders list.
     */
    private ReminderTracker() {
        reminders = new ArrayList<>();
    }

    /**
     * Gets the singleton instance of the ReminderTracker.
     * Creates a new instance if one doesn't exist.
     *
     * @return The singleton instance of ReminderTracker
     */
    public static ReminderTracker getInstance() {
        if (instance == null) {
            instance = new ReminderTracker();
        }
        return instance;
    }

    /**
     * Adds a new reminder to the tracker.
     * Creates a new Reminder object with the given parameters and a unique ID.
     *
     * @param name The name or description of the reminder
     * @param amount The monetary amount associated with the reminder
     * @param dueDate The date when the reminder is due
     * @param reminderDate The date when the user should be reminded
     * @param userId The ID of the user who owns this reminder
     * @return The newly created Reminder object
     */
    public Reminder addReminder(String name, double amount, LocalDate dueDate, LocalDate reminderDate, int userId) {
        Reminder reminder = new Reminder(nextReminderId++, name, amount, dueDate, reminderDate, userId);
        reminders.add(reminder);
        return reminder;
    }

    /**
     * Gets all reminders for a specific user.
     * Filters the reminders list to include only those belonging to the specified user.
     *
     * @param userId The ID of the user whose reminders to retrieve
     * @return A list of Reminder objects belonging to the specified user
     */
    public List<Reminder> getUserReminders(int userId) {
        return reminders.stream()
                .filter(r -> r.getUserId() == userId)
                .collect(Collectors.toList());
    }

    /**
     * Marks a reminder as completed.
     * Finds the reminder with the specified ID belonging to the specified user
     * and marks it as completed.
     *
     * @param reminderId The ID of the reminder to mark as completed
     * @param userId The ID of the user who owns the reminder
     * @return true if the reminder was found and marked as completed, false otherwise
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
     * Clears all reminders from memory.
     * Removes all reminders from the list and resets the reminder ID counter.
     */
    public void clearData() {
        reminders.clear();
        nextReminderId = 1;
    }

    /**
     * Loads reminders for a specific user from storage.
     * Reads serialized reminder data from a user-specific file and adds
     * the reminders to the in-memory list.
     *
     * @param userId The ID of the user whose reminders to load
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
     * Saves reminders for a specific user to storage.
     * Writes the user's reminders to a user-specific file as serialized objects.
     *
     * @param userId The ID of the user whose reminders to save
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
