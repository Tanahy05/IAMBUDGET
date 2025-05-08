package controller;

import model.User;
import storage.UserDatabase;

import java.util.ArrayList;

public class Auth {

    public static boolean registerUser(User newUser) {
        ArrayList<User> users = UserDatabase.loadUsers();

        for (User u : users) {
            if (u.getEmail().equals(newUser.getEmail())) {
                return false;
            }
        }

        users.add(newUser);
        UserDatabase.saveUsers(users);
        return true;
    }

    public static User login(String email, String password) {
        ArrayList<User> users = UserDatabase.loadUsers();

        for (User u : users) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
}
