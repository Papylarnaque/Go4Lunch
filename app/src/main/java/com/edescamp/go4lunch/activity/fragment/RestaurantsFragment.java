package com.edescamp.go4lunch.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.activity.fragment.view.RestaurantsAdapter;
import com.edescamp.go4lunch.model.map.ResultAPIMap;
import com.edescamp.go4lunch.service.LocationService;
import com.edescamp.go4lunch.service.NearByPlacesService;

import java.util.List;

import static com.edescamp.go4lunch.activity.MainActivity.RADIUS_INIT;
import static com.edescamp.go4lunch.service.NearByPlacesService.nearbyPlacesResults;

public class RestaurantsFragment extends BaseFragment {

    private static final String TAG = "RestaurantFragment";
    public static final int RESTAURANT_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;
    private RecyclerView recyclerView;

    // UI parameters
    private ProgressBar progressBar;
    private TextView noRestaurants;


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

        progressBar.setVisibility(View.VISIBLE);


        if (nearbyPlacesResults == null) {
            LocationService locationService = new LocationService(requireActivity());
            if (!locationService.getLocationPermission()) {
                locationDenied();
            }

            NearByPlacesService.listenNearbyPlacesResults.observe(requireActivity(), changedNearbyPlacesResults -> {
                //Do something with the changed value
                if (changedNearbyPlacesResults != null) {
                    sendResultsToAdapter(changedNearbyPlacesResults);
                } else {
                    noRestaurantsToShow();
                }
            });
        } else {
            sendResultsToAdapter(nearbyPlacesResults);
        }

        return view;
    }

    private void locationDenied() {
        progressBar.setVisibility(View.GONE);
        noRestaurants.setText(R.string.restaurant_list_no_restaurants_location_denied);
        noRestaurants.setVisibility(View.VISIBLE);
    }

    private void noRestaurantsToShow() {
        progressBar.setVisibility(View.GONE);
        noRestaurants.setText(getString(R.string.restaurant_list_no_restaurants_to_show, RADIUS_INIT));
        noRestaurants.setVisibility(View.VISIBLE);
    }

    private void sendResultsToAdapter(List<ResultAPIMap> results) {
        recyclerView.setAdapter(new RestaurantsAdapter(results, LocationService.userLocation, this.getActivity()));
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }


}
