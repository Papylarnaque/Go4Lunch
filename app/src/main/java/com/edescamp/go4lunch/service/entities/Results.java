package com.edescamp.go4lunch.service.entities;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Results {


    @SerializedName("status")
    private String status;

    @SerializedName("results")
    private List<Result> results;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Result> getResults() {
        return this.results;
    }

    public void setPredictions(List<Result> results) {
        this.results = results;}
}
