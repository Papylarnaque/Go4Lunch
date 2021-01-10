package com.edescamp.go4lunch.activity.fragment.view;

import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.model.map.ResultAPIMap;
import com.edescamp.go4lunch.service.PlaceDetailsService;
import com.edescamp.go4lunch.util.DetailsUtil;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.Objects;

import static com.edescamp.go4lunch.activity.MainActivity.uid;
import static com.edescamp.go4lunch.activity.MainActivity.workmates;
import static com.edescamp.go4lunch.service.PlaceDetailsService.placeDetailsResultHashmap;
import static com.edescamp.go4lunch.util.SortRestaurantsUtil.distanceHashMap;
import static com.edescamp.go4lunch.util.SortRestaurantsUtil.workmatesCountHashMap;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsViewHolder> {

    private static final String TAG = "RestaurantAdapter";
    private final List<ResultAPIMap> results;
    private final Location userLocation;
    private final FragmentActivity activity;
    private int distance;
    private Integer workmatesCount;

    public RestaurantsAdapter(List<ResultAPIMap> results, Location userLocation, FragmentActivity activity) {
        this.results = results;
        this.userLocation = userLocation;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.item_restaurant;
    }

    @NonNull
    @Override
    public RestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        view.getContext();
        return new RestaurantsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RestaurantsViewHolder holder, int position) {
        String placeId = results.get(position).getPlaceId();

        setUpClickFunctionality(holder, position);

        if (!placeDetailsResultHashmap.containsKey(Objects.requireNonNull(results.get(position).getPlaceId()))) {
            // If place details is not already stored in hashmap we query it from api Details
            PlaceDetailsService.getPlaceDetails(results.get(position).getPlaceId());
        } else {
            // If we already got place details, we retrieve them from cache
            getWorkmatesCount(position);

            distance = getStraightDistance(results.get(position));

            holder.updateRestaurantsWithDetails(Objects.requireNonNull(placeDetailsResultHashmap.get(placeId)));
        }

        holder.createViewWithRestaurants(results.get(position), distance, workmatesCount);


    }

    private void setUpClickFunctionality(RestaurantsViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v ->
                DetailsUtil.openDetailsFragmentOrCallApiThenOpenDetailsFragment(
                        activity,
                        results.get(position).getPlaceId()));
    }

    private void getWorkmatesCount(int position) {
        workmatesCount = 0;
        // retrieve workmates with chosenRestaurantId and store the count in hashmap
        for (DocumentSnapshot workmate : Objects.requireNonNull(workmates)) {
            if (Objects.equals(workmate.get("chosenRestaurantId"), results.get(position).getPlaceId())
                    && !Objects.equals(workmate.get("uid"), uid))
                workmatesCount += 1;
        }
        // Save Workmates joining
        workmatesCountHashMap.put(results.get(position).getPlaceId(), workmatesCount);
    }

    private int getStraightDistance(ResultAPIMap resultAPIMap) {
        Location restaurantLocation = new Location("");
        restaurantLocation.setLongitude(resultAPIMap.getGeometry().getLocation().getLng());
        restaurantLocation.setLatitude(resultAPIMap.getGeometry().getLocation().getLat());

        int distance = Math.round(restaurantLocation.distanceTo(userLocation));
        // store distance in Hashmap placeId/distance
        distanceHashMap.put(resultAPIMap.getPlaceId(), distance);

        return distance;
    }


    @Override
    public int getItemCount() {
        return results.size();
    }


}
