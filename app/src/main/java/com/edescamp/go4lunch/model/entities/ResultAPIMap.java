package com.edescamp.go4lunch.model.entities;

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



}
