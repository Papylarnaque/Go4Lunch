package com.edescamp.go4lunch.service.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultAPIDetails {

    @SerializedName("place_id")
    private String place_id;

    @SerializedName("name")
    private String name;

    @SerializedName("opening_hours")
    private List<String> opening_hours;

    @SerializedName("rating")
    private Integer rating;

    @SerializedName("website")
    private String website;

    @SerializedName("reviews")
    private List<ReviewAPIDetails> reviews;

    @SerializedName("international_phone_number")
    private String international_phone_number;

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(List<String> opening_hours) {
        this.opening_hours = opening_hours;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<ReviewAPIDetails> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewAPIDetails> reviews) {
        this.reviews = reviews;
    }

    public String getInternational_phone_number() {
        return international_phone_number;
    }

    public void setInternational_phone_number(String international_phone_number) {
        this.international_phone_number = international_phone_number;
    }
}
