package com.oc.go4lunch.model;

import androidx.annotation.Nullable;

public class Restaurant {

    private String rid;
    private String name;
    private Integer latitude;
    private Integer longitude;
    private Integer rating;
    @Nullable
    private String urlPicture;

    public Restaurant() {
    }

    public Restaurant(String rid, String name, Integer latitude, Integer longitude, Integer rating, @Nullable String urlPicture) {
        this.rid = rid;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.urlPicture = urlPicture;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }
}
