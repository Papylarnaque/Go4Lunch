package com.oc.go4lunch.api;

import com.oc.go4lunch.util.Predictions;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapAPI {

    @GET("place/autocomplete/json")
    public Call<Predictions> getPlacesAutoComplete(
            @Query("input") String input,
            @Query("types") String types,
            @Query("language") String language,
            @Query("key") String key
    );

}
