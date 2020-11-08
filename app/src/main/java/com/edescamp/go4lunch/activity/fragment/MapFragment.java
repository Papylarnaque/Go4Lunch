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
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.activity.MainActivity;
import com.edescamp.go4lunch.model.Restaurant;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    private static final String TAG = "MapFragment";
    private static final float INITIAL_ZOOM = 15f;
    private static final long UPDATE_INTERVAL = 30000;    /* 10 secs */
    private static final long FASTEST_INTERVAL = 10000;    /*  2 secs */
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    // API request parameters
    private static final int radius = 500; // radius in meters around user for search
    private static final String language = "en";
    private static final String keyword = "restaurant";

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;

    private MainActivity mainActivity = new MainActivity();

    public MapFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mMapFragment == null) {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, mMapFragment).commit();
        }

        mMapFragment.getMapAsync(this);



        return v;
    }


    // UI MAP //
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        enableCompassButton();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);

        View mapView = mMapFragment.getView();
        moveCompassButton(mapView);

        startLocationUpdates();

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

        // New location has now been determined
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

        if (userLocation != null) {
            //The line below is for camera actualisation if the user moves
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, INITIAL_ZOOM));

            // Userlocation for API request
            String userLocationStr = userLocation.latitude + "," + userLocation.longitude;
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
                        addMarkerResult(results);
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


    // + show the marker of the restaurant on the map
    private void addMarkerResult(List<Result> results) {
        for (Result result : results) {
            Log.d(TAG, "GoogleMapAPI result :" + result);
            Restaurant restaurant = new Restaurant();
            restaurant.setRestaurantid(result.getPlaceId());
            restaurant.setName(result.getName());
            restaurant.setAddress(result.getVicinity());
            restaurant.setLatlng(new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng()));
            mMap.addMarker(new MarkerOptions().position(Objects.requireNonNull(restaurant.getLatlng()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .title(restaurant.getName() + "\n" + restaurant.getAddress()));
        }
    }


    // COMPASS BUTTON //
    private void enableCompassButton() {
        if (ContextCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(false);
        }
    }

    // UI COMPASS BUTTON location
    private void moveCompassButton(View mapView) {
        try {
            assert mapView != null; // skip this if the mapView has not been set yet
            View view = mapView.findViewWithTag("GoogleMapMyLocationButton");

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.setMargins(0, 0, 150, 30);

            view.setLayoutParams(layoutParams);
            view.setBackgroundColor(getResources().getColor(R.color.colorText));
        } catch (Exception ex) {
            Log.e(TAG, "MoveCompassButton() - failed: " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
//        if (mMainActivity.mSearchBar.getVisibility() == View.INVISIBLE) getRestaurants();
        startLocationUpdates();
        return false;
    }

    // Request localisation
    public void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

}