package com.edescamp.go4lunch.model.details;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultsAPIDetails {

    @SerializedName("status")
    private String status;

    @SerializedName("result")
    private ResultAPIDetails result;

    @SerializedName("html_attributions")
    private List<String> html_attributions;

    public void setResult(ResultAPIDetails result) {
        this.result = result;
    }

    public List<String> getHtml_attributions() {
        return html_attributions;
    }

    public void setHtml_attributions(List<String> html_attributions) {
        this.html_attributions = html_attributions;
    }


    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ResultAPIDetails getResult() {
        return this.result;
    }

}

