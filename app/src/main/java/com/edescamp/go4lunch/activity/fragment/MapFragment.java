package com.edescamp.go4lunch.activity.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.edescamp.go4lunch.BuildConfig;
import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.service.APIClient;
import com.edescamp.go4lunch.service.APIRequest;
import com.edescamp.go4lunch.model.ResultAPIDetails;
import com.edescamp.go4lunch.model.ResultAPIMap;
import com.edescamp.go4lunch.model.ResultsAPIDetails;
import com.edescamp.go4lunch.model.ResultsAPIMap;
import com.edescamp.go4lunch.util.RestaurantHelper;
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
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.edescamp.go4lunch.activity.MainActivity.API_MAP_FIELDS;
import static com.edescamp.go4lunch.activity.MainActivity.API_MAP_KEYWORD;
import static com.edescamp.go4lunch.activity.MainActivity.API_MAP_LANGUAGE;
import static com.edescamp.go4lunch.activity.MainActivity.RADIUS_INIT;
import static com.edescamp.go4lunch.activity.MainActivity.workmates;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    private static final String TAG = "MapFragment";
    private static final float INITIAL_ZOOM = 12f;
    private static final int MAP_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    public static String userLocationStr;
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


    // UI MAP //
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "Enter onMapReady ");
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);

        View mapView = mMapFragment.getView();
        moveCompassButton(mapView);

        getLocationPermission();

        // Handle click on marker info
        mMap.setOnInfoWindowClickListener(marker -> {
            Log.d(TAG, "Click on marker " + marker.getTag());
            getPlaceDetails(Objects.requireNonNull(marker.getTag()).toString());

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
        userLocationStr = userLatLng.latitude + "," + userLatLng.longitude;
        // Check location update to avoid unnecessary api calls
        if (userLatLng != oldUserLatLng) {
            getPlace(userLocationStr);
        }

        oldUserLatLng = userLatLng;

    }

    private void getPlace(String userLocationStr) {
        APIRequest apiMap = APIClient.getClient().create(APIRequest.class);
        Call<ResultsAPIMap> nearbyPlaces = apiMap.getNearbyPlaces(userLocationStr, RADIUS_INIT, API_MAP_LANGUAGE, API_MAP_KEYWORD, BuildConfig.GOOGLE_MAPS_KEY);

        nearbyPlaces.enqueue(new Callback<ResultsAPIMap>() {
            @Override
            public void onResponse(Call<ResultsAPIMap> call, Response<ResultsAPIMap> response) {
                if (response.isSuccessful()) {
                    ResultsAPIMap body = response.body();
                    if (body != null) {
                        List<ResultAPIMap> resultsAPIMap = body.getResults();
                        if (resultsAPIMap.size() == 0) {
                            extendRadiusDialog();
                        } else {
                            addMarkerResult(resultsAPIMap);
                        }
                    }

                }
                // TODO Handle failures, 404 error, etc
            }

            @Override
            public void onFailure(Call<ResultsAPIMap> call,Throwable t) {

                Log.d(TAG, "getPlace API failure" + t);
            }

        });
    }

    private void extendRadiusDialog() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(getString(R.string.alertdialog_extendradius_message, RADIUS_INIT))
                .setTitle(R.string.alertdialog_extendradius_title);
        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.YES), (dialog1, which) -> {
            RADIUS_INIT += 1000;
            getPlace(userLocationStr);
            dialog1.dismiss();
        });

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.NO), (dialog12, which) -> dialog12.dismiss());

        dialog.show();
    }

    // + show the marker of the restaurant on the map
    private void addMarkerResult(List<ResultAPIMap> results) {

        for (ResultAPIMap result : results) {
            Log.d(TAG, "apiMap result PlaceName  :" + result.getName());
            boolean bChosen = false;
            if (workmates != null)
                for (DocumentSnapshot workmate : workmates) {
                    if (Objects.equals(workmate.get("hasChosenRestaurant"), result.getPlaceId())) {
                        bChosen = true;
                        break;
                    }
                }


            Marker markerRestaurant;
            if (bChosen) {

                markerRestaurant = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(
                                result.getGeometry().getLocation().getLat(),
                                result.getGeometry().getLocation().getLng()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .title(result.getName())
                        .snippet(result.getVicinity()));

            } else {
                markerRestaurant = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(
                                result.getGeometry().getLocation().getLat(),
                                result.getGeometry().getLocation().getLng()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .title(result.getName())
                        .snippet(result.getVicinity()));

            }
            markerRestaurant.setTag(result.getPlaceId());

            // Add restaurant to Firestore
            RestaurantHelper.createRestaurant(result);


        }

    }


    private void getPlaceDetails(String placeId) {
        APIRequest apiDetails = APIClient.getClient().create(APIRequest.class);
        Call<ResultsAPIDetails> placeDetails = apiDetails.getPlaceDetails(placeId, API_MAP_FIELDS, BuildConfig.GOOGLE_MAPS_KEY);

        placeDetails.enqueue(new Callback<ResultsAPIDetails>() {
            @Override
            public void onResponse(Call<ResultsAPIDetails> call,  Response<ResultsAPIDetails> response) {
                Log.d(TAG, "getPlaceDetails API ");
                if (response.isSuccessful()) {
                    ResultsAPIDetails body = response.body();
                    if (body != null) {
                        ResultAPIDetails result = body.getResult();
                        Log.d(TAG, "getPlaceDetails successful response " + result.getName() + " " + result.getPlaceId());

                        openDetailsFragment(result);

                        RestaurantHelper.setRestaurantDetails(placeId, result);



                    }
                    // TODO Handle failures, 404 error, etc
                }
            }

            @Override
            public void onFailure( Call<ResultsAPIDetails> call,  Throwable t) {
                Log.d(TAG, "getPlaceDetails API failure" + t);
            }

        });
    }

    private void openDetailsFragment(ResultAPIDetails result) {
        Fragment fragment = new DetailsFragment(result);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
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
            view.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        } catch (Exception ex) {
            Log.e(TAG, "MoveCompassButton() - failed: " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }


}