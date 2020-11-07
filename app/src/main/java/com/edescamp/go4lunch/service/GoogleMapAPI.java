package com.edescamp.go4lunch.service;

import com.edescamp.go4lunch.service.entities.Results;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapAPI {

    @GET("place/nearbysearch/json")
    Call<Results> getNearbyPlaces(
            @Query("location") String location,
            @Query("radius") Integer radius,
            @Query("language") String language,
            @Query("keyword") String keyword,
            @Query("key") String key
    );

}
