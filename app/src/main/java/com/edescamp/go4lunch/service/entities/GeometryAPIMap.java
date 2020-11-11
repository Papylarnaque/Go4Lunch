package com.edescamp.go4lunch.service.entities;

import com.google.geo.type.Viewport;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GeometryAPIMap implements Serializable {

    @SerializedName("location")
    private LocationAPIMap location;


    @SerializedName("viewport")
    private Viewport viewport;

    public LocationAPIMap getLocation() {
        return location;
    }

    public Viewport getViewport() {
        return viewport;
    }


}
