package com.edescamp.go4lunch.service;

import androidx.lifecycle.MutableLiveData;

import com.edescamp.go4lunch.BuildConfig;
import com.edescamp.go4lunch.model.map.ResultsAPIMap;

import retrofit2.Call;

import static com.edescamp.go4lunch.activity.MainActivity.API_MAP_KEYWORD;
import static com.edescamp.go4lunch.activity.MainActivity.API_MAP_LANGUAGE;
import static com.edescamp.go4lunch.activity.MainActivity.RADIUS_INIT;

public class RetrofitService {



    public static Call<ResultsAPIMap> nearbyPlaces;
    public static MutableLiveData<Call<ResultsAPIMap>> listen = new MutableLiveData<>();

    public static void getNearbyPlaces(String userLocationStr) {
        APIRequest apiMap = APIClient.getClient().create(APIRequest.class);
        nearbyPlaces = apiMap.getNearbyPlaces(
                userLocationStr,
                RADIUS_INIT,
                API_MAP_LANGUAGE,
                API_MAP_KEYWORD,
                BuildConfig.GOOGLE_MAPS_KEY);


        listen.setValue(nearbyPlaces); //Initialize with a value




    }


}
