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
import com.edescamp.go4lunch.model.Restaurant;
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
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    // API request parameters
    private static final int radius = 400; // radius in meters around user for search
    private static final String language = "en";
    private static final String keyword = "restaurant";

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;

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

        //The line below is for camera actualisation if the user moves
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, INITIAL_ZOOM));

        // Userlocation for API request
        String userLocationStr = userLocation.latitude + "," + userLocation.longitude;
        getPlace(userLocationStr);


        //TODO Manage AddMarker updates while moving the camera
        // (location of the center of the camera) instead of radius around the userLocation

    }


    // ---------------------------- Get places ----
    private void getPlace(String userLocationStr) {
        APIRequest apiMap = APIClient.getClient().create(APIRequest.class);
        Call<ResultsAPIMap> nearbyPlaces = apiMap.getNearbyPlaces(userLocationStr, radius, language, keyword, getResources().getString(R.string.google_maps_key));

        nearbyPlaces.enqueue(new Callback<ResultsAPIMap>() {
            @Override
            public void onResponse(Call<ResultsAPIMap> call, Response<ResultsAPIMap> response) {
                if (response.isSuccessful()) {
                    ResultsAPIMap body = response.body();
                    if (body != null) {
                        List<ResultAPIMap> results = body.getResults();
                        addMarkerResult(results);
                    }
                }
                // TODO Handle failures, 404 error, etc
            }
            @Override
            public void onFailure(Call<ResultsAPIMap> call, Throwable t) {

                Log.d(TAG, "getPlace API failure" + t);
            }

        });
    }


    // + show the marker of the restaurant on the map
    private void addMarkerResult(List<ResultAPIMap> results) {
        for (ResultAPIMap result : results) {
            Log.d(TAG, "apiMap result :" + result);
            Restaurant restaurant = new Restaurant();
            restaurant.setRestaurantid(result.getPlaceId());
            restaurant.setName(result.getName());
            restaurant.setAddress(result.getVicinity());
            restaurant.setLatlng(new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng()));
            mMap.addMarker(new MarkerOptions().position(Objects.requireNonNull(restaurant.getLatlng()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .title(restaurant.getName()).snippet(restaurant.getAddress()));
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


}