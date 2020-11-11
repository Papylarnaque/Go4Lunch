package com.edescamp.go4lunch.service.entities;

import com.google.gson.annotations.SerializedName;

public class ElementAPIDistance {

    @SerializedName("distance")
    private DistanceAPIDistance distance;

    @SerializedName("status")
    private String status;

    public DistanceAPIDistance getDistance() {
        return distance;
    }

    public void setDistance(DistanceAPIDistance distance) {
        this.distance = distance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}