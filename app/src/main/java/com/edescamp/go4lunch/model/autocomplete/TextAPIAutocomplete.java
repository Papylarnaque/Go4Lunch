package com.edescamp.go4lunch.model.autocomplete;

import com.google.gson.annotations.SerializedName;

public class TextAPIAutocomplete {

    @SerializedName("main_text")
    private String main_text;

    public String getMain_text() {
        return main_text;
    }

}
