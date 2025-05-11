package storage;

import model.User;
import model.Budget;
import java.io.*;
import java.util.ArrayList;

/**
 * The UserDatabase class provides utility methods for persisting and retrieving
 * user and budget data to/from the file system.
 * <p>
 * This class manages serialization and deserialization of User and Budget objects
 * and handles file storage operations. It maintains user data in a single file
 * and individual budget data files for each user.
 * </p>
 *
 * @author Your Name
 * @version 1.0
 */
public class UserDatabase {
    /** The file path where user data is stored */
    private static final String USERS_FILE_PATH = "users.dat";

    /** The directory where all data files are stored */
    private static final String DATA_DIR = "data";

    /**
     * Loads the list of users from the file system.
     * <p>
     * If the file doesn't exist or there is an error reading the file,
     * a new empty ArrayList is returned.
     * </p>
     *
     * @return An ArrayList containing all User objects, or an empty ArrayList if none exist
     */
    public static ArrayList<User> loadUsers() {
        createDataDirIfNeeded();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE_PATH))) {
            return (ArrayList<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Creating new users list");
            return new ArrayList<>();
        }
    }

    /**
     * Saves the list of users to the file system.
     * <p>
     * The method ensures the data directory exists before attempting to save.
     * </p>
     *
     * @param users The ArrayList of User objects to save
     */
    public static void saveUsers(ArrayList<User> users) {
        createDataDirIfNeeded();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE_PATH))) {
            oos.writeObject(users);
            System.out.println("Users saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    /**
     * Generates the next available user ID.
     * <p>
     * The method loads the current list of users and returns a unique ID
     * for a new user by incrementing the highest existing ID.
     * If no users exist, returns 1 as the first ID.
     * </p>
     *
     * @return The next available user ID
     */
    public static int getNextUserId() {
        ArrayList<User> users = loadUsers();
        if (users.isEmpty()) {
            return 1;
        }
        return users.get(users.size() - 1).getUserID() + 1;
    }

    /**
     * Creates the data directory if it doesn't already exist.
     * <p>
     * This method is called before any file operations to ensure
     * the target directory exists.
     * </p>
     */
    private static void createDataDirIfNeeded() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
    }

    /**
     * Constructs the file path for a specific user's budget file.
     *
     * @param userId The ID of the user whose budget file path is needed
     * @return The file path for the user's budget data
     */
    public static String getBudgetFilePath(int userId) {
        return DATA_DIR + "/" + userId + "_budgets.dat";
    }

    /**
     * Saves a list of budgets for a specific user to the file system.
     * <p>
     * The method ensures the data directory exists before attempting to save.
     * </p>
     *
     * @param userId The ID of the user whose budgets are being saved
     * @param budgets The ArrayList of Budget objects to save
     * @return true if saving was successful, false otherwise
     */
    public static boolean saveBudgets(int userId, ArrayList<Budget> budgets) {
        createDataDirIfNeeded();
        String filePath = getBudgetFilePath(userId);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(budgets);
            System.out.println("Budget data saved successfully for user ID: " + userId);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving budget data: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads the list of budgets for a specific user from the file system.
     * <p>
     * If the file doesn't exist or there is an error reading the file,
     * a new empty ArrayList is returned.
     * </p>
     *
     * @param userId The ID of the user whose budgets are being loaded
     * @return An ArrayList containing the user's Budget objects, or an empty ArrayList if none exist
     */
    public static ArrayList<Budget> loadBudgets(int userId) {
        String filePath = getBudgetFilePath(userId);
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("No budget data file found for user ID: " + userId);
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            ArrayList<Budget> budgets = (ArrayList<Budget>) ois.readObject();
            System.out.println("Budget data loaded successfully for user ID: " + userId);
            return budgets;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error loading budget data: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}