package com.edescamp.go4lunch.model.apimap;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultsAPIMap {


    @SerializedName("status")
    private String status;

    @SerializedName("results")
    private List<ResultAPIMap> results;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ResultAPIMap> getResults() {
        return this.results;
    }

    public void setPredictions(List<ResultAPIMap> results) {
        this.results = results;}
}
