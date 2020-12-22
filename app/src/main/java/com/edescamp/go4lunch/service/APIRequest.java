package com.edescamp.go4lunch.service;

import com.edescamp.go4lunch.model.autocomplete.PredictionsAPIAutocomplete;
import com.edescamp.go4lunch.model.details.ResultsAPIDetails;
import com.edescamp.go4lunch.model.map.ResultsAPIMap;

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


    @GET("place/autocomplete/json")
    Call<PredictionsAPIAutocomplete> getAutocomplete(
            @Query("location") String location,
            @Query("radius") Integer radius,
            @Query("input") String input,
            @Query("types") String types,
            @Query("strictbounds") String strictbounds,
            @Query("key") String key

    );
}
