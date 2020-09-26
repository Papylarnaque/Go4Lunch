package com.oc.go4lunch.model;

import androidx.annotation.Nullable;

public class Workmate {


    private String uid;
    private String username;
    private Integer hasChosenRestaurant;
    @Nullable
    private String urlPicture;

    public Workmate() {
    }

    public Workmate(String uid, String username, String urlPicture, int hasChosenRestaurant) {
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

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public void setHasChosenRestaurant(Integer restaurantId) {
        hasChosenRestaurant = restaurantId;
    }

}
