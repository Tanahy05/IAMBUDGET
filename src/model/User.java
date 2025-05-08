package model;

import java.io.Serializable;

public class User implements Serializable {

    final private int UID;
    private String name;
    private String username;
    private String password;

    public User(int userID,String name, String username, String password) {
        this.UID = userID;
        this.name=name;
        this.username = username;
        this.password = password;
    }

    // Getters
    public int getUserID() {
        return UID;
    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" + "userID='" + UID + "', username='" + username+"', name='"+name+"}";
    }
}
