package com.edescamp.go4lunch.service.entities;

import com.google.gson.annotations.SerializedName;

public class PredictionAPIAutocomplete {

    @SerializedName("description")
    private String description;

    @SerializedName("place_id")
    private String place_id;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }
}
