package com.edescamp.go4lunch.service;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.edescamp.go4lunch.BuildConfig;
import com.edescamp.go4lunch.model.details.ResultAPIDetails;
import com.edescamp.go4lunch.model.details.ResultsAPIDetails;
import com.edescamp.go4lunch.util.DetailsUtil;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.edescamp.go4lunch.activity.MainActivity.API_MAP_FIELDS;

public class PlaceDetailsService {


    private static final String TAG = "PlaceDetailsService";
    public static final HashMap<String, ResultAPIDetails> placeDetailsResultHashmap = new HashMap<>();

    public static void getPlaceDetails(String placeId) {
        APIRequest apiDetails = APIClient.getClient().create(APIRequest.class);
        Call<ResultsAPIDetails> placeDetails = apiDetails.getPlaceDetails(placeId, API_MAP_FIELDS, BuildConfig.GOOGLE_MAPS_KEY);

        placeDetails.enqueue(new Callback<ResultsAPIDetails>() {
            @Override
            public void onResponse(@NonNull Call<ResultsAPIDetails> call,
                                   @NonNull Response<ResultsAPIDetails> response) {
                Log.d(TAG, "getPlaceDetails API ");
                if (response.isSuccessful()) {
                    ResultsAPIDetails body = response.body();
                    if (body != null) {
                        if (!placeDetailsResultHashmap.containsKey(placeId)) {
                            placeDetailsResultHashmap.put(placeId, body.getResult());
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultsAPIDetails> call,
                                  @NonNull Throwable t) {
                Log.d(TAG, "getPlaceDetails API failure" + t);
            }

        });
    }


    public static void getPlaceDetailsAndOpenDetailsFragment(String placeId, FragmentActivity activity) {
        APIRequest apiDetails = APIClient.getClient().create(APIRequest.class);
        Call<ResultsAPIDetails> placeDetails = apiDetails.getPlaceDetails(placeId, API_MAP_FIELDS, BuildConfig.GOOGLE_MAPS_KEY);

        placeDetails.enqueue(new Callback<ResultsAPIDetails>() {
            @Override
            public void onResponse(@NonNull Call<ResultsAPIDetails> call,
                                   @NonNull Response<ResultsAPIDetails> response) {
                Log.d(TAG, "getPlaceDetails API ");
                if (response.isSuccessful()) {
                    ResultsAPIDetails body = response.body();
                    if (body != null) {
                        DetailsUtil.openDetailsFragment(activity, body.getResult());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultsAPIDetails> call,
                                  @NonNull Throwable t) {
                Log.d(TAG, "getPlaceDetails API failure" + t);
            }

        });
    }


}
