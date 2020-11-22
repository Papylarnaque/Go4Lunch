package com.edescamp.go4lunch.activity.fragment;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.service.APIClient;
import com.edescamp.go4lunch.service.APIRequest;
import com.edescamp.go4lunch.service.entities.ResultAPIDetails;
import com.edescamp.go4lunch.service.entities.ResultAPIMap;
import com.edescamp.go4lunch.service.entities.ResultsAPIDetails;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.edescamp.go4lunch.activity.MainActivity.keyword;
import static com.edescamp.go4lunch.activity.MainActivity.language;
import static com.edescamp.go4lunch.activity.MainActivity.radius;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    private static final String TAG = "MapFragment";
    private static final float INITIAL_ZOOM = 15f;
    private static final int MAP_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String FIELDS = "formatted_address,photos,place_id,name,rating,opening_hours,website,reviews,international_phone_number";

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private LatLng oldUserLatLng;

    public MapFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);
        if (mMapFragment == null) {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.fragment_map, mMapFragment).commit();
        }

        mMapFragment.getMapAsync(this);

        return v;
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    // UI MAP //
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "Enter onMapReady ");
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);

        View mapView = mMapFragment.getView();
        moveCompassButton(mapView);

// TODO Fix second call to getLocationPermission
        getLocationPermission();

        // Handle click on marker info
        mMap.setOnInfoWindowClickListener(marker -> {
            Log.d(TAG, "Click on marker " + marker.getTag());
            getPlaceDetails(marker.getTag().toString());

        });

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
        LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());


        if (mMap != null) {
            //The line below is for camera actualisation if the user moves
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, INITIAL_ZOOM));
        }

        // Userlocation for API request
        String userLocationStr = userLatLng.latitude + "," + userLatLng.longitude;
        // Check location update to avoid unnecessary api calls
        if (userLatLng != oldUserLatLng) {
            getPlace(userLocationStr);
        }

        oldUserLatLng = userLatLng;

        //TODO Manage AddMarker updates while moving the camera
        // (location of the center of the camera) instead of radius around the userLatLng

    }

    private void getPlace(String userLocationStr) {
        APIRequest apiMap = APIClient.getClient().create(APIRequest.class);
        Call<ResultsAPIMap> nearbyPlaces = apiMap.getNearbyPlaces(userLocationStr, radius, language, keyword, getResources().getString(R.string.google_maps_key));

        nearbyPlaces.enqueue(new Callback<ResultsAPIMap>() {
            @Override
            public void onResponse(@NotNull Call<ResultsAPIMap> call, @NotNull Response<ResultsAPIMap> response) {
                if (response.isSuccessful()) {
                    ResultsAPIMap body = response.body();
                    if (body != null) {
                        List<ResultAPIMap> results = body.getResults();
                        if (results.size() == 0) {
                            Toast.makeText(getContext(), "Pas de restaurant a 400m d'ici", Toast.LENGTH_LONG).show();
                        } else {
                            addMarkerResult(results);
                        }
                    }

                }
                // TODO Handle failures, 404 error, etc
            }

            @Override
            public void onFailure(@NotNull Call<ResultsAPIMap> call, @NotNull Throwable t) {

                Log.d(TAG, "getPlace API failure" + t);
            }

        });
    }

    // + show the marker of the restaurant on the map
    private void addMarkerResult(List<ResultAPIMap> results) {
        for (ResultAPIMap result : results) {
            Log.d(TAG, "apiMap result PlaceName  :" + result.getName());

            Marker markerRestaurant = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(
                            result.getGeometry().getLocation().getLat(),
                            result.getGeometry().getLocation().getLng()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .title(result.getName())
                    .snippet(result.getVicinity()));
            markerRestaurant.setTag(result.getPlaceId());
        }


    }

    private void getPlaceDetails(String placeId) {
        APIRequest apiDetails = APIClient.getClient().create(APIRequest.class);
        Call<ResultsAPIDetails> placeDetails = apiDetails.getPlaceDetails(placeId, FIELDS, getResources().getString(R.string.google_maps_key));

        placeDetails.enqueue(new Callback<ResultsAPIDetails>() {
            @Override
            public void onResponse(@NotNull Call<ResultsAPIDetails> call, @NotNull Response<ResultsAPIDetails> response) {
                Log.d(TAG, "getPlaceDetails API ");
                if (response.isSuccessful()) {
                    ResultsAPIDetails body = response.body();
                    if (body != null) {
                        ResultAPIDetails result = body.getResult();
                        Log.d(TAG, "getPlaceDetails successful response "  + result.getName() + " " + result.getPlace_id());

                        Fragment fragment = new DetailsFragment(result);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment)
                                .addToBackStack(null)
                                .commit();

                    }
                    // TODO Handle failures, 404 error, etc
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResultsAPIDetails> call, @NotNull Throwable t) {
                Log.d(TAG, "getPlaceDetails API failure" + t);
            }

        });
    }


    // COMPASS BUTTON //
    @SuppressLint("MissingPermission")
    private void enableCompassButton() {
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(false);
        }
    }

    // Request localisation
    public void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            enableCompassButton();
            startLocationUpdates();

        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MAP_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            Log.d(TAG, "Request location permission ");
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        getLocationPermission();
        return false;
    }

    // UI COMPASS BUTTON location
    private void moveCompassButton(View mapView) {
        try {
            assert mapView != null; // skip this if the mapView has not been set yet
            View view = mapView.findViewWithTag("GoogleMapMyLocationButton");

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);

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

}