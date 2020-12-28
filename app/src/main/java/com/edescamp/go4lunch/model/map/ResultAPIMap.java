package com.edescamp.go4lunch.model.map;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResultAPIMap implements Serializable {

    @SerializedName("place_id")
    private String placeId;

    @SerializedName("name")
    private String name;

    @SerializedName("rating")
    private Float rating;

    @SerializedName("business_status")
    private String business_status;

    @SerializedName("vicinity")
    private String vicinity;

    @SerializedName("geometry")
    private GeometryAPIMap geometry;

    @SerializedName("photos")
    private List<PhotoAttributesAPIMap> photos;

    @SerializedName("types")
    private List<String> types;

    // --- GETTERS --- //

    public String getPlaceId() {
        return placeId;
    }

    public String getName() {
        return name;
    }

    public Float getRating() {
        return rating;
    }

    public String getBusiness_status() {
        return business_status;
    }

    public String getVicinity() {
        return vicinity;
    }

    public GeometryAPIMap getGeometry() {
        return geometry;
    }

    public List<PhotoAttributesAPIMap> getPhotos() {
        return photos;
    }

    public List<String> getTypes() {
        return types;
    }

    // --- SETTERS --- For test Purpose //


    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setBusiness_status(String business_status) {
        this.business_status = business_status;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public void setGeometry(GeometryAPIMap geometry) {
        this.geometry = geometry;
    }

    public void setPhotos(List<PhotoAttributesAPIMap> photos) {
        this.photos = photos;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
