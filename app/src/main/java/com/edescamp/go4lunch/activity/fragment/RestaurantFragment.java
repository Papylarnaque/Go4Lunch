package com.edescamp.go4lunch.activity.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.adapter.RestaurantAdapter;
import com.edescamp.go4lunch.service.APIClient;
import com.edescamp.go4lunch.service.GoogleMapAPI;
import com.edescamp.go4lunch.service.entities.Result;
import com.edescamp.go4lunch.service.entities.Results;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class RestaurantFragment extends BaseFragment {

    private static final String TAG = "TAG";
    private static final String COLLECTION_RESTAURANTS = "restaurant";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    // API request parameters
    private static final int radius = 500; // radius in meters around user for search
    private static final String language = "en";
    private static final String keyword = "restaurant";
    private LatLng userLatLng;
    private Location userLocation;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        // Add the following lines to create RecyclerView
        recyclerView = view.findViewById(R.id.restaurant_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
//        recyclerView.setAdapter(new RestaurantAdapter(1234));

        startLocationUpdates();

        return view;
    }


    // USER LOCATION updates listener service //
    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

//        mLocationRequest.setInterval(UPDATE_INTERVAL);
//        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        SettingsClient settingsClient = LocationServices.getSettingsClient(requireActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }
        getFusedLocationProviderClient(requireActivity()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    private void onLocationChanged(Location location) {
        userLocation = location;

        // New location has now been determined
        userLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (userLatLng != null) {

            // Userlocation for API request
            String userLocationStr = userLatLng.latitude + "," + userLatLng.longitude;
            getPlace(userLocationStr);
        }


        //TODO Manage AddMarker updates while moving the camera
        // (location of the center of the camera) instead of radius around the userLocation

    }


    // ---------------------------- Get places ----
    private void getPlace(String userLocationStr) {
        GoogleMapAPI googleMapAPI = APIClient.getClient().create(GoogleMapAPI.class);
        Call<Results> nearbyPlaces = googleMapAPI.getNearbyPlaces(userLocationStr, radius, language, keyword, getResources().getString(R.string.google_maps_key));

        nearbyPlaces.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                if (response.isSuccessful()) {
                    Results body = response.body();
                    List<Result> results = body.getResults();
                    if (results != null && results.size() > 0) {
                        for (Result result : results){
                            sendResultsToAdapter(results);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                // TODO Handle failures, 404 error, etc
                Log.d(TAG, "getPlace API failure" + t);
            }

        });
    }

    private void sendResultsToAdapter(List<Result> results) {
        recyclerView.setAdapter(new RestaurantAdapter(results, userLocation));
    }


}
