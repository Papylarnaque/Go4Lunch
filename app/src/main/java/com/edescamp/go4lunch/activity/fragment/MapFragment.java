package com.edescamp.go4lunch.activity.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.model.map.ResultAPIMap;
import com.edescamp.go4lunch.service.LocationService;
import com.edescamp.go4lunch.service.NearByPlacesService;
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

import static com.edescamp.go4lunch.activity.MainActivity.RADIUS_INIT;
import static com.edescamp.go4lunch.activity.MainActivity.uid;
import static com.edescamp.go4lunch.activity.MainActivity.workmates;
import static com.edescamp.go4lunch.service.LocationService.userLatLng;
import static com.edescamp.go4lunch.service.LocationService.userLocationStr;
import static com.edescamp.go4lunch.util.DetailsUtil.openDetailsFragmentOrCallApiThenOpenDetailsFragment;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    private static final String TAG = "MapFragment";
    private static final float INITIAL_ZOOM = 12f;
    public static final int MAP_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;

    private LocationService locationService;

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

        locationService = new LocationService(requireActivity());

        if (locationService.getLocationPermission()) {
            enableCompassButton();
            moveCompassButton(mapView);
        }

        NearByPlacesService.listenNearbyPlacesResults.observe(
                requireActivity(),
                changedNearbyPlacesResults -> {
                    //Do something with the changed value
                    if (changedNearbyPlacesResults != null) {
                        addMarkerResult(changedNearbyPlacesResults);
                        if (mMap != null && userLatLng != null) {
                            MapFragment.this.zoomOnCurrentPosition(mMap);
                        }
                    } else {
                        extendRadiusDialog();
                    }
                });

        // Handle click on marker info
        mMapSetUpClickListener();
    }

    private void zoomOnCurrentPosition(GoogleMap mMap) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, INITIAL_ZOOM));
    }

    // Handle click on marker info
    private void mMapSetUpClickListener() {
        mMap.setOnInfoWindowClickListener(marker -> {
            Log.d(TAG, "Click on marker " + marker.getTag());
            String placeId = Objects.requireNonNull(marker.getTag()).toString();

            openDetailsFragmentOrCallApiThenOpenDetailsFragment(this.getActivity(), placeId);
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
            NearByPlacesService.getNearbyPlaces(userLocationStr);
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
                for (DocumentSnapshot workmate : Objects.requireNonNull(workmates)) {
                    if (!Objects.equals(workmate.get("uid"), uid) &&
                            Objects.equals(workmate.get("chosenRestaurantId"), result.getPlaceId())) {
                        bChosen = true;
                        break;
                    }
                }

            Marker markerRestaurant;
            if (bChosen) {
                markerRestaurant = showChosenRestaurantMarker(result);
            } else {
                markerRestaurant = showRegularRestaurantMarker(result);
            }

            markerRestaurant.setTag(result.getPlaceId());

        }

    }

    private Marker showRegularRestaurantMarker(ResultAPIMap result) {
        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(
                        result.getGeometry().getLocation().getLat(),
                        result.getGeometry().getLocation().getLng()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(result.getName())
                .snippet(result.getVicinity()));
    }

    private Marker showChosenRestaurantMarker(ResultAPIMap result) {
        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(
                        result.getGeometry().getLocation().getLat(),
                        result.getGeometry().getLocation().getLng()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title(result.getName())
                .snippet(result.getVicinity()));
    }


    // ---------------------- COMPASS BUTTON ----------------------//
    @SuppressLint("MissingPermission") // Permission managed before this call
    private void enableCompassButton() {
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(false);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        locationService.getLocationPermission();
        zoomOnCurrentPosition(mMap);
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
            layoutParams.setMargins(0, 0, 150, 40);

            view.setLayoutParams(layoutParams);
            view.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        } catch (Exception ex) {
            Log.e(TAG, "MoveCompassButton() - failed: " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }


}