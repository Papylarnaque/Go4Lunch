package com.edescamp.go4lunch.service;

import com.edescamp.go4lunch.service.entities.ResultsAPIDetails;
import com.edescamp.go4lunch.service.entities.ResultsAPIMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIRequest {

    @GET("place/nearbysearch/json")
    Call<ResultsAPIMap> getNearbyPlaces(
            @Query("location") String location,
            @Query("radius") Integer radius,
            @Query("language") String language,
            @Query("keyword") String keyword,
            @Query("key") String key
    );


    @GET("place/details/json")
    Call<ResultsAPIDetails> getPlaceDetails(
            @Query("place_id") String place_id,
            @Query("fields") String fields,
            @Query("key") String key
    );




}
