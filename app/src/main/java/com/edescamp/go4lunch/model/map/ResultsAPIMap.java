package com.edescamp.go4lunch.model.map;


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

    public List<ResultAPIMap> getResults() {
        return this.results;
    }

    public void setResults(List<ResultAPIMap> results) {
        this.results = results;}
}
