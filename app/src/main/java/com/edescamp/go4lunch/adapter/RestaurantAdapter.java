package com.edescamp.go4lunch.adapter;

import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.activity.fragment.DetailsFragment;
import com.edescamp.go4lunch.service.entities.LocationAPIMap;
import com.edescamp.go4lunch.service.entities.ResultAPIDetails;
import com.edescamp.go4lunch.service.entities.ResultAPIMap;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    private final List<ResultAPIMap> results;
    private final Location userLocation;

    public RestaurantAdapter(List<ResultAPIMap> results, Location userLocation) {
        this.results = results;
        this.userLocation = userLocation;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.item_restaurant;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RestaurantViewHolder(view);

        // TODO On click on a restaurant openDetailsFragment

    }


    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {

        ResultAPIMap resultAPIMap = results.get(position);

        ResultAPIDetails result = new ResultAPIDetails();
        result.setPlace_id(resultAPIMap.getPlaceId());
        result.setName(resultAPIMap.getName());
        result.setVicinity(resultAPIMap.getVicinity());
        result.setFormatted_address(resultAPIMap.getVicinity());
        result.setGeometry(resultAPIMap.getGeometry());
        result.setPhotos(resultAPIMap.getPhotos());
        result.setRating(resultAPIMap.getRating());

        // TODO Get Details data for the restaurant


        float distance = getStraightDistance(resultAPIMap.getGeometry().getLocation());
        holder.updateViewWithRestaurants(results.get(position), distance);

        holder.itemView.setOnClickListener(v -> openDetailsFragment(holder, result));

    }


    private void openDetailsFragment(@NonNull RestaurantViewHolder holder, ResultAPIDetails result) {
        AppCompatActivity activity = (AppCompatActivity) holder.itemView.getContext();
        Fragment fragment = new DetailsFragment(result);
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }


    private float getStraightDistance(LocationAPIMap r) {
        Location restaurantLocation = new Location("");
        restaurantLocation.setLongitude(r.getLng());
        restaurantLocation.setLatitude(r.getLat());

        return (int) restaurantLocation.distanceTo(userLocation);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }


}
