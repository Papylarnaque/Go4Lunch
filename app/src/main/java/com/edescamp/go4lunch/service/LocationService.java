package com.edescamp.go4lunch.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.edescamp.go4lunch.activity.fragment.RestaurantsFragment;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class LocationService {

    private final Activity activity;

    public static Location userLocation;
    public static LatLng oldUserLatLng;
    public static LatLng userLatLng;
    public static String userLocationStr;

    public LocationService(FragmentActivity activity) {
        this.activity = activity;
    }

    // Request localisation
    public boolean getLocationPermission() {
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
            return true;
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    RestaurantsFragment.RESTAURANT_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        }
        return false;
    }

    // USER LOCATION updates listener service //

    protected void startLocationUpdates() {
        // Create the location request to start receiving updates
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        SettingsClient settingsClient = LocationServices.getSettingsClient(activity);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        getFusedLocation(mLocationRequest);


    }

    @SuppressLint("MissingPermission")
    private void getFusedLocation(LocationRequest mLocationRequest) {
        getFusedLocationProviderClient(activity)
                .requestLocationUpdates(mLocationRequest, new LocationCallback() {
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

        // User location for API request
        userLocationStr = location.getLatitude() + "," + location.getLongitude();
        // Check location update to avoid unnecessary api calls
        if (userLatLng != oldUserLatLng) {
            NearByPlacesService.getNearbyPlaces(userLocationStr);
        }
        oldUserLatLng = userLatLng;

    }


}


