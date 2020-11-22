package com.edescamp.go4lunch.service.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultAPIDetails {

    @SerializedName("place_id")
    private String place_id;

    @SerializedName("name")
    private String name;

    @SerializedName("opening_hours")
    private OpeningHoursAPIDetails opening_hours;

    @SerializedName("rating")
    private Float rating;

    @SerializedName("website")
    private String website;

    @SerializedName("reviews")
    private List<ReviewAPIDetails> reviews;

    @SerializedName("international_phone_number")
    private String international_phone_number;

    @SerializedName("formatted_address")
    private String formatted_address;

    @SerializedName("photos")
    private List<PhotoAttributesAPIMap> photos;

    public List<PhotoAttributesAPIMap> getPhotos() {
        return photos;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

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

    public OpeningHoursAPIDetails getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(OpeningHoursAPIDetails opening_hours) {
        this.opening_hours = opening_hours;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
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
