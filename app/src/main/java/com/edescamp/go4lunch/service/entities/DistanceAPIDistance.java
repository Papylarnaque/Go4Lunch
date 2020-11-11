package com.edescamp.go4lunch.service.entities;

import com.google.gson.annotations.SerializedName;

public class DistanceAPIDistance {

    @SerializedName("value")
    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
