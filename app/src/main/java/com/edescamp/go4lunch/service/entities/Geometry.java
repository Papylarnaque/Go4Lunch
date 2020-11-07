package com.edescamp.go4lunch.service.entities;

import com.google.geo.type.Viewport;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Geometry implements Serializable {

    @SerializedName("location")
    private LocationAPI location;


    @SerializedName("viewport")
    private Viewport viewport;

    public LocationAPI getLocation() {
        return location;
    }

    public Viewport getViewport() {
        return viewport;
    }


}
