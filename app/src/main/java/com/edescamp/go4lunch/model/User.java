package com.edescamp.go4lunch.model;

import androidx.annotation.Nullable;

public class User {


    private String uid;
    private String username;
    @Nullable
    private String urlPicture;
    private String chosenRestaurantId;
    private String chosenRestaurantName;

    public User() {
    }

    public User(String uid, String username, String userMail, @Nullable String urlPicture, String chosenRestaurantId, String chosenRestaurantName) {
        this.uid = uid;
        this.username = username;
        this.chosenRestaurantId = chosenRestaurantId;
        this.urlPicture = urlPicture;
        this.chosenRestaurantName = chosenRestaurantName;
    }


    // --- GETTERS ---
    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public String getChosenRestaurantId() {
        return chosenRestaurantId;
    }

    public String getChosenRestaurantName() {
        return chosenRestaurantName;
    }





}
