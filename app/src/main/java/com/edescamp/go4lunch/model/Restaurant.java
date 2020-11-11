package com.edescamp.go4lunch.model;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Restaurant implements Serializable {


    //    private String restaurantid;
//    private String name;
    private LatLng latlng;
    private String address;
    private Integer rating;
    private Integer distance;
    @Nullable
    private String urlPicture;

    public Restaurant() {
    }

    public Restaurant(String restaurantid, String name, LatLng latlng, String address, Integer rating, @Nullable String urlPicture, Integer distance) {
        this.restaurantid = restaurantid;
        this.name = name;
        this.latlng = latlng;
        this.address = address;
        this.rating = rating;
        this.urlPicture = urlPicture;
        this.distance = distance;
    }


    @SerializedName("offset")
    private String restaurantid;

    @SerializedName("name")
    private String name;


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

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

}
