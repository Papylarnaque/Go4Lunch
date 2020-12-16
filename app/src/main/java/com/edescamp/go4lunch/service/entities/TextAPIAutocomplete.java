package com.edescamp.go4lunch.service.entities;

import com.google.gson.annotations.SerializedName;

public class TextAPIAutocomplete {

    @SerializedName("main_text")
    private String main_text;

    public String getMain_text() {
        return main_text;
    }

    public void setMain_text(String main_text) {
        this.main_text = main_text;
    }


}
