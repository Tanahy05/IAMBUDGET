package controller;

import model.User;
import storage.UserDatabase;

import java.util.ArrayList;

/**
 * Authentication controller class that handles user registration, login, and user retrieval operations.
 * This class interacts with the UserDatabase to perform CRUD operations on User objects.
 */
public class Auth {

    /**
     * Registers a new user in the system if the username is not already taken.
     *
     * @param newUser The User object containing the details of the new user to be registered
     * @return true if registration is successful, false if the username already exists
     */
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

    /**
     * Authenticates a user by checking their username and password against stored credentials.
     *
     * @param username The username of the user attempting to log in
     * @param password The password provided by the user for authentication
     * @return 0 if the username doesn't exist, 1 if the username exists but password is incorrect,
     *         2 if login is successful (username and password match)
     */
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

    /**
     * Finds and retrieves a User object based on the provided username.
     *
     * @param username The username of the user to be found
     * @return The User object if found, null if no user with the given username exists
     */
    public static User findUser(String username) {
        ArrayList<User> users = UserDatabase.loadUsers();
        for (User u : users) {
            if (u.getUsername().equals(username)){
                return u;
            }
        }
        return null;
    }
}