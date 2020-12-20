package com.edescamp.go4lunch.model;

import androidx.annotation.Nullable;

public class User {


    private String uid;
    private String username;
    private String userMail;
    private String hasChosenRestaurant;

    @Nullable
    private String urlPicture;


    public User() {
    }

    public User(String uid, String username, String userMail, String hasChosenRestaurant, @Nullable String urlPicture, String chosenRestaurantName) {
//        this.uid = uid;
//        this.username = username;
//        this.userMail = userMail;
//        this.hasChosenRestaurant = hasChosenRestaurant;
//        this.urlPicture = urlPicture;
//        this.chosenRestaurantName = chosenRestaurantName;
    }

    private String chosenRestaurantName;




    // --- GETTERS ---
    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getUserMail() {
        return userMail;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public String getHasChosenRestaurant() {
        return hasChosenRestaurant;
    }

    public String getChosenRestaurantName() {
        return chosenRestaurantName;
    }

    // --- SETTERS ---
    public void setUsername(String username) {
        this.username = username;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public void setHasChosenRestaurant(String restaurantId) {
        hasChosenRestaurant = restaurantId;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public void setChosenRestaurantName(String restaurantName) {
        chosenRestaurantName = restaurantName;

    }

}
