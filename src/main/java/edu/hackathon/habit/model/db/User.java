package edu.hackathon.habit.model.db;

import org.springframework.data.annotation.Id;


public class User {

    @Id
    public String userId;

    public String username;
    public String password;
    public boolean isPrivate;

    public User() {
    }

    public User(String username, String password, boolean isPrivate) {
        this.username = username;
        this.password = password;
        this.isPrivate = isPrivate;
    }

}