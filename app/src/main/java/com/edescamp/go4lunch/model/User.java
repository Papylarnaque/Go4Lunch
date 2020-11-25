package com.edescamp.go4lunch.model;

import androidx.annotation.Nullable;

public class User {


    private String uid;
    private String username;
    private Integer hasChosenRestaurant;
    @Nullable
    private String urlPicture;

    public User() {
    }

    public User(String uid, String username, @org.jetbrains.annotations.Nullable String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
    }

    public User(String uid, String username, @org.jetbrains.annotations.Nullable String urlPicture, int hasChosenRestaurant) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.hasChosenRestaurant = hasChosenRestaurant;
    }

    // --- GETTERS ---
    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    @org.jetbrains.annotations.Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    public Integer getHasChosenRestaurant() {
        return hasChosenRestaurant;
    }

    // --- SETTERS ---
    public void setUsername(String username) {
        this.username = username;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUrlPicture(@org.jetbrains.annotations.Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public void setHasChosenRestaurant(Integer restaurantId) {
        hasChosenRestaurant = restaurantId;
    }

}
