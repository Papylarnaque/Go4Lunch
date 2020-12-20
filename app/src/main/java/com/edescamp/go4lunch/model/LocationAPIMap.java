package com.edescamp.go4lunch.model;

import com.google.gson.annotations.SerializedName;

public class LocationAPIMap {

    @SerializedName("lat")
    private Double lat;

    @SerializedName("lng")
    private Double lng;

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }
}
