package com.edescamp.go4lunch.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PredictionAPIAutocomplete {

    @SerializedName("description")
    private String description;

    @SerializedName("place_id")
    private String place_id;

    @SerializedName("structured_formatting")
    private TextAPIAutocomplete structured_formatting;

    @SerializedName("types")
    private List<String> types;


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

    public TextAPIAutocomplete getStructured_formatting() {
        return structured_formatting;
    }

    public void setStructured_formatting(TextAPIAutocomplete structured_formatting) {
        this.structured_formatting = structured_formatting;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }


}
