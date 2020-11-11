package com.edescamp.go4lunch.service.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhotoAttributesAPIMap {

    @SerializedName("height")
    private Integer height;

    @SerializedName("width")
    private Integer width;

    @SerializedName("html_attributions")
    private List<String> html_attributions;

    @SerializedName("photo_reference")
    private String photo_reference;

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public List<String> getHtml_attributions() {
        return html_attributions;
    }

    public void setHtml_attributions(List<String> html_attributions) {
        this.html_attributions = html_attributions;
    }

    public String getPhoto_reference() {
        return photo_reference;
    }

    public String getPhoto_URL() {
        String API_url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
        String key_request = "&key=";
        return API_url + getPhoto_reference() + key_request;
    }

    public void setPhoto_reference(String photo_reference) {
        this.photo_reference = photo_reference;
    }
}
