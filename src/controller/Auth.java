package controller;

import model.User;
import storage.UserDatabase;

import java.util.ArrayList;

public class Auth {

    public static boolean registerUser(User newUser) {
        ArrayList<User> users = UserDatabase.loadUsers();

        for (User u : users) {
            if (u.getUsername().equals(newUser.getUsername())) {
                return false;
            }
        }

        users.add(newUser);
        UserDatabase.saveUsers(users);
        return true;
    }

    public static int login(String username, String password) {
        ArrayList<User> users = UserDatabase.loadUsers();

        for (User u : users) {
            if (u.getUsername().equals(username)) {
                 if(u.getPassword().equals(password))
                     return 2;
                 else return 1;
            }
        }
        return 0;

    }
}
