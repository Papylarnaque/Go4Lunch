package com.edescamp.go4lunch.model;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;


public class Restaurant implements Serializable {


    private String placeId;
    private String name;
    private LatLng latlng;
    private String address;
    private Integer rating;
    private Integer distance;
    @Nullable
    private String urlPicture;

    public Restaurant() {
    }

    public Restaurant(String placeId, String name, LatLng latlng, String address, Integer rating, @Nullable String urlPicture, Integer distance) {
        this.placeId = placeId;
        this.name = name;
        this.latlng = latlng;
        this.address = address;
        this.rating = rating;
        this.urlPicture = urlPicture;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
