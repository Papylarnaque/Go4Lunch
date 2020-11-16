package com.edescamp.go4lunch.adapter;

import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.service.entities.LocationAPIMap;
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
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        ResultAPIMap result = results.get(position);

        float distance = getStraightDistance(result.getGeometry().getLocation());

        holder.updateViewWithRestaurants(result, distance);
    }

    private float getStraightDistance(LocationAPIMap r){
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
