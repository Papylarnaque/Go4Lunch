package com.edescamp.go4lunch.service.entities;

import com.google.gson.annotations.SerializedName;

public class ReviewAPIDetails {

    @SerializedName("author_name")
    private String author_name;

    @SerializedName("author_url")
    private String author_url;

    @SerializedName("language")
    private String language;

    @SerializedName("profile_photo_url")
    private String profile_photo_url;

    @SerializedName("rating")
    private Integer rating;

    @SerializedName("relative_time_description")
    private String relative_time_description;

    @SerializedName("text")
    private String text;

    @SerializedName("time")
    private Integer time;

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_url() {
        return author_url;
    }

    public void setAuthor_url(String author_url) {
        this.author_url = author_url;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getProfile_photo_url() {
        return profile_photo_url;
    }

    public void setProfile_photo_url(String profile_photo_url) {
        this.profile_photo_url = profile_photo_url;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getRelative_time_description() {
        return relative_time_description;
    }

    public void setRelative_time_description(String relative_time_description) {
        this.relative_time_description = relative_time_description;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}
