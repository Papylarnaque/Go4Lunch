package com.oc.go4lunch.model;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;


public class Restaurant {

    private String restaurantid;
    private String name;
    private LatLng latlng;
    private String address;
    private Integer rating;
    @Nullable
    private String urlPicture;

    public Restaurant() {
    }

    public Restaurant(String restaurantid, String name, LatLng latlng, String address, Integer rating, @Nullable String urlPicture) {
        this.restaurantid = restaurantid;
        this.name = name;
        this.latlng = latlng;
        this.address = address;
        this.rating = rating;
        this.urlPicture = urlPicture;
    }

    public String getRestaurantid() {
        return restaurantid;
    }

    public void setRestaurantid(String restaurantid) {
        this.restaurantid = restaurantid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }
}
