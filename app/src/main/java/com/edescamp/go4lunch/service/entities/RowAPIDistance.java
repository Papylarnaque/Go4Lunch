package com.edescamp.go4lunch.service.entities;

import com.google.gson.annotations.SerializedName;

public class RowAPIDistance {

    @SerializedName("elements")
    private ElementAPIDistance elements = null;

    public ElementAPIDistance getElements() {
        return elements;
    }

}