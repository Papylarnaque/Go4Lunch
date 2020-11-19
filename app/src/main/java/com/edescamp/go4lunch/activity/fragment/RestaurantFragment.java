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
import android.widget.ProgressBar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.adapter.RestaurantAdapter;
import com.edescamp.go4lunch.service.APIClient;
import com.edescamp.go4lunch.service.APIRequest;
import com.edescamp.go4lunch.service.entities.ResultAPIMap;
import com.edescamp.go4lunch.service.entities.ResultsAPIMap;
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

    private static final String TAG = "RestaurantFragment";
    private static final int RESTAURANT_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;
    private RecyclerView recyclerView;

    // API request parameters
    private static final int radius = 400; // radius in meters around user for search
    private static final String language = "en";
    private static final String keyword = "restaurant";
    private Location userLocation;
    private LatLng oldUserLatLng;
    private ProgressBar progressBar;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {



        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        // Add the following lines to create RecyclerView
        recyclerView = view.findViewById(R.id.restaurant_recyclerview);
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        getLocationPermission();

        return view;
    }


    // USER LOCATION updates listener service //
    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {
        // Create the location request to start receiving updates
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

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
        LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());


        // Userlocation for API request
        String userLocationStr = userLatLng.latitude + "," + userLatLng.longitude;
        // Check location update to avoid unnecessary api calls
        if (userLatLng != oldUserLatLng) {
            getPlace(userLocationStr);
        }
        oldUserLatLng = userLatLng;
    }


    // ---------------------------- Get places for markers----
    private void getPlace(String userLocationStr) {
        APIRequest apiMap = APIClient.getClient().create(APIRequest.class);
        Call<ResultsAPIMap> nearbyPlaces = apiMap.getNearbyPlaces(
                userLocationStr,
                radius,
                language,
                keyword,
                getResources().getString(R.string.google_maps_key)
        );
        progressBar.setVisibility(View.VISIBLE);
        nearbyPlaces.enqueue(new Callback<ResultsAPIMap>() {
            @Override
            public void onResponse(Call<ResultsAPIMap> call, Response<ResultsAPIMap> response) {
                // TODO if no result to show, show a message
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    ResultsAPIMap body = response.body();
                    if (body != null) {
                        List<ResultAPIMap> results = body.getResults();
                        sendResultsToAdapter(results);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultsAPIMap> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "getPlace failure" + t);
            }

        });
    }

    private void sendResultsToAdapter(List<ResultAPIMap> results) {
        recyclerView.setAdapter(new RestaurantAdapter(results, userLocation));
    }


    // Request localisation
    public void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    RESTAURANT_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


}
