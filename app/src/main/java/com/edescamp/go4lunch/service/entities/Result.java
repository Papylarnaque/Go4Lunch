package com.edescamp.go4lunch.service.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Result implements Serializable {

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
        private Geometry geometry;

        @SerializedName("photos")
        private List<PhotoAttributes> photos;

        @SerializedName("types")
        private List<String> types;

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

        public Geometry getGeometry() {
                return geometry;
        }

        public List<PhotoAttributes> getPhotos() {
                return photos;
        }

        public List<String> getTypes() {
                return types;
        }



}
