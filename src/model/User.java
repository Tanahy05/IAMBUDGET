package model;

import java.io.Serializable;

/**
 * Represents a user in the system.
 * This class stores user information such as ID, name, username, and password.
 * Implements Serializable to support persistence operations.
 */
public class User implements Serializable {

    /** The unique identifier for the user. Once set, it cannot be changed. */
    final private int UID;

    /** The full name of the user. */
    private String name;

    /** The username used for authentication. */
    private String username;

    /** The password used for authentication. */
    private String password;

    /**
     * Constructs a new User with the specified details.
     *
     * @param userID The unique identifier for the user
     * @param name The full name of the user
     * @param username The username for authentication
     * @param password The password for authentication
     */
    public User(int userID, String name, String username, String password) {
        this.UID = userID;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the user's unique identifier.
     *
     * @return The user ID
     */
    public int getUserID() {
        return UID;
    }

    /**
     * Gets the user's username.
     *
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the user's password.
     *
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's username.
     *
     * @param username The new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the user's password.
     *
     * @param password The new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns a string representation of the User object.
     *
     * @return A string representation of the User
     */
    @Override
    public String toString() {
        return "User{" + "userID='" + UID + "', username='" + username + "', name='" + name + "}";
    }
}