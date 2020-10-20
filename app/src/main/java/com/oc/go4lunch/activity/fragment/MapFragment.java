package com.oc.go4lunch.activity.fragment;

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

import com.google.android.gms.common.api.ApiException;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.oc.go4lunch.R;
import com.oc.go4lunch.activity.MainActivity;
import com.oc.go4lunch.model.Restaurant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

// Add an import statement for the client library.

public class MapFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private PlacesClient mPlacesClient;

    private static final String TAG = "MapFragment";
    private static final float INITIAL_ZOOM = 15f;
    private static final long UPDATE_INTERVAL = 30000;    /* 10 secs */
    private static final long FASTEST_INTERVAL = 10000;    /*  2 secs */
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private MainActivity mainActivity = new MainActivity();

    // TODO : Add Places API for restaurants markers


    // Use fields to define the data types to return.
    List<Place.Field> placeFields = Collections.singletonList(Place.Field.NAME);

    // Use the builder to create a FindCurrentPlaceRequest.
    FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

    public MapFragment() {
    }

//    public static MapFragment newInstance() {
//        return new MapFragment();
//    }

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


    // USER LOCATION updates listener service //

    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

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

    // USER LOCATION UPDATES actions : what to do if the user moves //

    public void onLocationChanged(Location location) {

//        ZOOM_LEVEL = mMap.getCameraPosition().zoom;

        // New location has now been determined
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        //The line below is for camera actualisation if the user moves
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, INITIAL_ZOOM));
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
        initPlaces();
        getNearbyPlaces();
        getPlaces();

    }


    // ---------------------------- Init places ----

    public void initPlaces() {
        // Initialize the Places SDK
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), String.valueOf(R.string.google_maps_key));
        }
        mPlacesClient = Places.createClient(requireContext());
    }


    //---------------------------- Places information type initialization -----

    public void getNearbyPlaces() {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.TYPES, Place.Field.LAT_LNG);
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        mPlacesClient = Places.createClient(requireContext());
        if (ContextCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = mPlacesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful()) {
                        FindCurrentPlaceResponse response = task.getResult();
                        assert response != null;

                        final String placeId = response.getPlaceLikelihoods().get(0).getPlace().getId();
                        for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                            Log.i(TAG, String.format("Place '%s' has likelihood: '%f' ",
                                    placeLikelihood.getPlace().getName(),
                                    placeLikelihood.getLikelihood()));

                            if (Objects.requireNonNull(placeLikelihood.getPlace().getTypes()).contains(Place.Type.RESTAURANT)) {
                                Restaurant r = new Restaurant();
                                r.setRestaurantid(placeLikelihood.getPlace().getId());
                                r.setName(placeLikelihood.getPlace().getName());
                                r.setAddress(placeLikelihood.getPlace().getAddress());
                                r.setLatlng(placeLikelihood.getPlace().getLatLng());
//                                mSharedViewModel.restaurants.add(r);
                                //mMap.setInfoWindowAdapter(new CustomDetailWindowAdapter(getActivity()));
                                mMap.addMarker(new MarkerOptions().position(Objects.requireNonNull(placeLikelihood.getPlace().getLatLng()))
                                        //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
//                                        .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_restaurant_dark_orange_24dp))
                                        .title(r.getName() + "\n" + r.getAddress()));
//                                getRestaurantDetails(r);
                            }

                        }

                    } else {
                        Exception exception = task.getException();
                        if (exception instanceof ApiException) {
                            ApiException apiException = (ApiException) exception;
                            Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                        }
                    }
                }
            });
        } else {
//            getLocationPermission();
        }
    }

    // ---------------------------- Get places ----

    private void getPlaces() {
        mPlacesClient = Places.createClient(requireContext());
        if (ContextCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = mPlacesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FindCurrentPlaceResponse response = task.getResult();
                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        Log.i(TAG, String.format("Place '%s' has likelihood: %f",
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getLikelihood()));
                    }
                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                    }
                }
            });
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
        return false;
    }

}