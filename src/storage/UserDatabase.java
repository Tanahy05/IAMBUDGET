package storage;

import model.User;
import model.Budget;
import java.io.*;
import java.util.ArrayList;

public class UserDatabase {
    private static final String USERS_FILE_PATH = "users.dat";
    private static final String DATA_DIR = "data";


    public static ArrayList<User> loadUsers() {
        createDataDirIfNeeded();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE_PATH))) {
            return (ArrayList<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Creating new users list");
            return new ArrayList<>();
        }
    }


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


    public static int getNextUserId() {
        ArrayList<User> users = loadUsers();
        if (users.isEmpty()) {
            return 1;
        }
        return users.get(users.size() - 1).getUserID() + 1;
    }


    private static void createDataDirIfNeeded() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
    }


    public static String getBudgetFilePath(int userId) {
        return DATA_DIR + "/" + userId + "_budgets.dat";
    }


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