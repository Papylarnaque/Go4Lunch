package com.edescamp.go4lunch.service;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.edescamp.go4lunch.BuildConfig;
import com.edescamp.go4lunch.model.autocomplete.PredictionAPIAutocomplete;
import com.edescamp.go4lunch.model.autocomplete.PredictionsAPIAutocomplete;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.edescamp.go4lunch.activity.MainActivity.API_AUTOCOMPLETE_KEYWORD;
import static com.edescamp.go4lunch.activity.MainActivity.RADIUS_MAX;
import static com.edescamp.go4lunch.service.LocationService.userLocationStr;

public class AutoCompleteService {

    private static final String TAG = "AutoCompleteService";

    public static MutableLiveData<List<PredictionAPIAutocomplete>> listenAutoCompletePredictions = new MutableLiveData<>();
    public static LiveData<List<PredictionAPIAutocomplete>> predictions = listenAutoCompletePredictions;

    public static void getAutocomplete(String input) {
        APIRequest apiAutocomplete = APIClient.getClient().create(APIRequest.class);
        Call<PredictionsAPIAutocomplete> autocompleteSearch = apiAutocomplete.getAutocomplete(
                userLocationStr,
                RADIUS_MAX,
                input,
                API_AUTOCOMPLETE_KEYWORD,
                "",
                BuildConfig.GOOGLE_MAPS_KEY);
        autocompleteSearch.enqueue(new Callback<PredictionsAPIAutocomplete>() {
            @Override
            public void onResponse(@NonNull Call<PredictionsAPIAutocomplete> call,
                                   @NonNull Response<PredictionsAPIAutocomplete> response) {
                if (response.isSuccessful()) {
                    PredictionsAPIAutocomplete predictionsAPIAutocomplete = response.body();
                    if (predictionsAPIAutocomplete != null) {
//                        predictions = predictionsAPIAutocomplete.getPredictions();
                        listenAutoCompletePredictions.setValue(predictionsAPIAutocomplete.getPredictions());
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<PredictionsAPIAutocomplete> call,
                                  @NonNull Throwable t) {
                Log.d(TAG, "getPlace API failure" + t);
            }

        });
    }

}
