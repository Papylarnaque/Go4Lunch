package com.edescamp.go4lunch.activity.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.activity.fragment.view.RestaurantsAdapter;
import com.edescamp.go4lunch.model.map.ResultAPIMap;
import com.edescamp.go4lunch.model.map.ResultsAPIMap;
import com.edescamp.go4lunch.service.LocationService;
import com.edescamp.go4lunch.service.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.edescamp.go4lunch.activity.MainActivity.RADIUS_INIT;
import static com.edescamp.go4lunch.activity.MainActivity.userLocation;

public class RestaurantsFragment extends BaseFragment {

    private static final String TAG = "RestaurantFragment";
    public static final int RESTAURANT_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;
    private RecyclerView recyclerView;

    // UI parameters
    private ProgressBar progressBar;
    private TextView noRestaurants;

    // Data
    private List<ResultAPIMap> results;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (results == null) {
            RetrofitService.listen.observe(requireActivity(), new Observer<Call<ResultsAPIMap>>() {
                @Override
                public void onChanged(Call<ResultsAPIMap> changedValue) {
                    //Do something with the changed value
                if ( changedValue!=null) {
                    getNearbyPlaces(changedValue);
                } else {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                }
            });
        } else {
            // do nothing but update recyclerview
            sendResultsToAdapter(results);
        }


    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        noRestaurants = view.findViewById(R.id.restaurant_List_no_restaurants_to_show);
        progressBar = view.findViewById(R.id.restaurant_list_progress_bar);

        // Add the following lines to create RecyclerView
        recyclerView = view.findViewById(R.id.restaurants_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

//        getLocationPermission();
        LocationService locationService = new LocationService(requireActivity());
        locationService.getLocationPermission();


        progressBar.setVisibility(View.VISIBLE);


        return view;
    }


    // ---------------------------- Get places for markers----
    private void getNearbyPlaces(Call<ResultsAPIMap> nearbyPlaces) {
        nearbyPlaces.clone().enqueue(new Callback<ResultsAPIMap>() {
            @Override
            public void onResponse(Call<ResultsAPIMap> call, Response<ResultsAPIMap> response) {
                if (response.isSuccessful()) {
                    ResultsAPIMap body = response.body();
                    if (body != null) {
                        results = body.getResults();
                        if (results.size() == 0) {
                            noRestaurants.setText(getString(R.string.restaurant_List_no_restaurants_to_show, RADIUS_INIT));
                            noRestaurants.setVisibility(View.VISIBLE);
                        } else {
                            sendResultsToAdapter(results);
                        }
                    }
                }
//                progressBar.setVisibility(View.GONE);
//                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ResultsAPIMap> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "getPlace failure" + t);
            }

        });
    }

    private void sendResultsToAdapter(List<ResultAPIMap> results) {
        recyclerView.setAdapter(new RestaurantsAdapter(results, userLocation));
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }


}
