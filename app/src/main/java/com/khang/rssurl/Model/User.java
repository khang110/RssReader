package com.khang.rssurl.Model;

import java.util.List;

public class User {


    public User(String id, String userName, String email, String gender, String homeTown,
                String avatarUrl, String token) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.gender = gender;
        this.homeTown = homeTown;
        this.avatarUrl = avatarUrl;
        this.token = token;
    }
    public User() {}
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String id;
    private String userName;
    private String email;
    private String gender;
    private String homeTown;
    private String avatarUrl;
    private String token;
}
