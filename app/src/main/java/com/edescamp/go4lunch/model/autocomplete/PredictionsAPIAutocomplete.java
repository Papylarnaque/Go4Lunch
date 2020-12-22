package com.edescamp.go4lunch.model.autocomplete;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PredictionsAPIAutocomplete {

    @SerializedName("status")
    private String status;

    @SerializedName("predictions")
    private List<PredictionAPIAutocomplete> predictions;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PredictionAPIAutocomplete> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<PredictionAPIAutocomplete> predictions) {
        this.predictions = predictions;
    }
}
