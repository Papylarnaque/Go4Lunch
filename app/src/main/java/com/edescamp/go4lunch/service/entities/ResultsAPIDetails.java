package com.edescamp.go4lunch.service.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultsAPIDetails {

    @SerializedName("status")
    private String status;

    @SerializedName("results")
    private List<ResultAPIDetails> results;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ResultAPIDetails> getResults() {
        return this.results;
    }

    public void setPredictions(List<ResultAPIDetails> results) {
        this.results = results;}
}

