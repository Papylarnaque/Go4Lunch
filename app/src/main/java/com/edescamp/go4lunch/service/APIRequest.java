package com.edescamp.go4lunch.service;

import com.edescamp.go4lunch.service.entities.ResultsAPIMap;
import com.edescamp.go4lunch.service.entities.RowAPIDistance;

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


    @GET("distancematrix/json")
    Call<RowAPIDistance> getDistanceBetweenLocations(
            @Query("origins") String origins,
            @Query("destinations") String destinations,
            @Query("key") String key
    );


}
