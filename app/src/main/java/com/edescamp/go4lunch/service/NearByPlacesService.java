package com.edescamp.go4lunch.service;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.edescamp.go4lunch.BuildConfig;
import com.edescamp.go4lunch.model.map.ResultAPIMap;
import com.edescamp.go4lunch.model.map.ResultsAPIMap;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.edescamp.go4lunch.activity.MainActivity.RADIUS_INIT;
import static com.edescamp.go4lunch.service.PlaceDetailsService.getPlaceDetails;
import static com.edescamp.go4lunch.service.PlaceDetailsService.placeDetailsResultHashmap;

public class NearByPlacesService {


    private static final String TAG = "NearByPlacesService";

    public static final String API_MAP_KEYWORD = "restaurant";

    // Nearby Places API variables

    public static final MutableLiveData<List<ResultAPIMap>> listenNearbyPlacesResults = new MutableLiveData<>();
    public static final LiveData<List<ResultAPIMap>> nearbyPlacesResults = listenNearbyPlacesResults;

    public static void getNearbyPlaces(String userLocationStr) {
        APIRequest apiMap = APIClient.getClient().create(APIRequest.class);
        Call<ResultsAPIMap> nearbyPlaces = apiMap.getNearbyPlaces(
                userLocationStr,
                RADIUS_INIT,
                Locale.getDefault().getLanguage(),
                API_MAP_KEYWORD,
                BuildConfig.GOOGLE_MAPS_KEY);

        nearbyPlaces.clone().enqueue(new Callback<ResultsAPIMap>() {
            @Override
            public void onResponse(@NonNull Call<ResultsAPIMap> call, @NonNull Response<ResultsAPIMap> response) {
                if (response.isSuccessful()) {
                    ResultsAPIMap body = response.body();
                    if (body != null) {
                        listenNearbyPlacesResults.setValue(body.getResults());
                        for (ResultAPIMap nearbyPlacesResult : body.getResults()) {
                            if (!placeDetailsResultHashmap.containsKey(nearbyPlacesResult.getPlaceId())) {
                                getPlaceDetails(nearbyPlacesResult.getPlaceId());
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultsAPIMap> call, @NonNull Throwable t) {
                Log.d(TAG, "getPlace failure" + t);
            }
        });
    }


}
