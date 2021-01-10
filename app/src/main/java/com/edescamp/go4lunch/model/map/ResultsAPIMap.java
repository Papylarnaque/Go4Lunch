package com.edescamp.go4lunch.model.map;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultsAPIMap {


    @SerializedName("status")
    private String status;

    @SerializedName("next_page_token")
    private String next_page_token;

    @SerializedName("results")
    private List<ResultAPIMap> results;

    public String getStatus() {
        return this.status;
    }

    public String getNext_page_token() {
        return next_page_token;
    }

    public List<ResultAPIMap> getResults() {
        return this.results;
    }
    
}
