package model;

import java.io.Serializable;

public class User implements Serializable {

    final private int UID;
    private String username;
    private String email;
    private String password;

    public User(int userID, String username, String email, String password) {
        this.UID = userID;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters
    public int getUserID() {
        return UID;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" + "userID='" + UID + "', username='" + username + "', email='" + email + "'}";
    }
}
