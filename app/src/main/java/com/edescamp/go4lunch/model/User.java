package com.edescamp.go4lunch.model;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class User {


    private String uid;
    private String username;
    private String userMail;
    @Nullable
    private String urlPicture;
    private String chosenRestaurantId;
    private String chosenRestaurantName;
    private String chosenRestaurantAddress;
    private List<String> likedRestaurants;


    public User() {
    }

    public User(String uid, String username, String userMail, @Nullable String urlPicture, String chosenRestaurantId, String chosenRestaurantName) {
        this.uid = uid;
        this.username = username;
        this.userMail = userMail;
        this.chosenRestaurantId = chosenRestaurantId;
        this.urlPicture = urlPicture;
        this.chosenRestaurantName = chosenRestaurantName;
    }



    public void addLikedRestaurant(String restaurantUid){
        if(likedRestaurants == null) {
            this.likedRestaurants = new ArrayList<>();
        }
        this.likedRestaurants.add(restaurantUid);
    }

    public void removeLikedRestaurant(String restaurantUid){
        if(likedRestaurants != null) {
            int position = 0;
            for (String uid : likedRestaurants) {
                if (uid.equals(restaurantUid)) likedRestaurants.remove(position);
                position += 1;
            }
        }
    }




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

    public String getChosenRestaurantId() {
        return chosenRestaurantId;
    }

    public String getChosenRestaurantName() {
        return chosenRestaurantName;
    }

    public String getChosenRestaurantAddress() {
        return chosenRestaurantAddress;
    }

    public List<String> getLikedRestaurants() {
        return likedRestaurants;
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

    public void setChosenRestaurantId(String restaurantId) {
        chosenRestaurantId = restaurantId;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public void setChosenRestaurantName(String restaurantName) {
        chosenRestaurantName = restaurantName;
    }

    public void setChosenRestaurantAddress(String chosenRestaurantAddress) {
        this.chosenRestaurantAddress = chosenRestaurantAddress;
    }

    public void setLikedRestaurants(List<String> likedRestaurants) {
        this.likedRestaurants = likedRestaurants;
    }




}
