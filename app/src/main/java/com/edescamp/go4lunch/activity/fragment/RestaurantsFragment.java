package com.edescamp.go4lunch.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.activity.fragment.view.RestaurantsAdapter;
import com.edescamp.go4lunch.model.map.ResultAPIMap;
import com.edescamp.go4lunch.service.LocationService;
import com.edescamp.go4lunch.service.NearByPlacesService;
import com.edescamp.go4lunch.util.SortRestaurantsUtil;

import java.util.ArrayList;
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
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        noRestaurants = view.findViewById(R.id.restaurant_List_no_restaurants_to_show);
        progressBar = view.findViewById(R.id.signin_progress_bar);

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
            sendResultsToAdapter(nearbyPlacesResults.getValue());
        }

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        menu.setGroupVisible(R.id.main_activity_restaurants_group_sort, true);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final int main_activity_restaurants_sort_name_asc = R.id.main_activity_restaurants_sort_name_asc;
        final int main_activity_restaurants_sort_name_desc = R.id.main_activity_restaurants_sort_name_desc;
        final int main_activity_restaurants_sort_rating_desc = R.id.main_activity_restaurants_sort_rating_desc;
        final int main_activity_restaurants_sort_workmates_desc = R.id.main_activity_restaurants_sort_workmates_desc;
        final int main_activity_restaurants_sort_distance_asc = R.id.main_activity_restaurants_sort_distance_asc;

        switch (id) {
            // Sort AZ
            case main_activity_restaurants_sort_name_asc:
                sendResultsToAdapter(SortRestaurantsUtil.sortAZ(nearbyPlacesResults.getValue()));
                return true;
            // Sort ZA
            case main_activity_restaurants_sort_name_desc:
                sendResultsToAdapter(SortRestaurantsUtil.sortZA(nearbyPlacesResults.getValue()));
                return true;
            // Sort Rating
            case main_activity_restaurants_sort_rating_desc:
                sendResultsToAdapter(SortRestaurantsUtil.sortRatingDesc(nearbyPlacesResults.getValue()));
                return true;
            // Sort Workmates
            case main_activity_restaurants_sort_workmates_desc:
                sortByWorkmates();
                return true;
            // Sort Distance
            case main_activity_restaurants_sort_distance_asc:
                sendResultsToAdapter(SortRestaurantsUtil.sortDistanceAsc(nearbyPlacesResults.getValue()));
                return true;
            default:
                break;
        }


        return super.onOptionsItemSelected(item);

    }

    private void sortByWorkmates() {
        boolean sortWorkmates = false;
        for (Integer values : SortRestaurantsUtil.workmatesCountHashMap.values()) {
            if (values > 0) {
                sortWorkmates = true;
                break;
            }
        }
        if (sortWorkmates) {
            sendResultsToAdapter(SortRestaurantsUtil.sortWorkmatesDesc(nearbyPlacesResults.getValue()));
        } else {
            Toast.makeText(getContext(), getString(R.string.main_activity_restaurants_no_workmates_to_sort_toast), Toast.LENGTH_SHORT).show();
        }
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
        if (results == null) {
            recyclerView.setAdapter(new RestaurantsAdapter(new ArrayList<>(), LocationService.userLocation, this.getActivity()));
        } else {
            recyclerView.setAdapter(new RestaurantsAdapter(results, LocationService.userLocation, this.getActivity()));
        }
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }


}
