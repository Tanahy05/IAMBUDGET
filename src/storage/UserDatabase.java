package storage;

import model.User;

import java.io.*;
import java.util.ArrayList;

public class UserDatabase {
    private static final String FILE_PATH = "users.dat";

    public static ArrayList<User> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (ArrayList<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public static void saveUsers(ArrayList<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int getNextUserId() {

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            ArrayList<User> users = (ArrayList<User>) ois.readObject();
            if (users.isEmpty()) {
                return 1;
            }
            // Get ID of last user and increment
            return users.get(users.size() - 1).getUserID() + 1;
        } catch (IOException | ClassNotFoundException e) {
            return 0;
        }
    }


}
