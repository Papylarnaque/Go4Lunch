package com.edescamp.go4lunch.adapter;

import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.service.entities.Result;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    private final List<Result> results;
    private final Location userLocation;
    private String distance;

    public RestaurantAdapter(List<Result> results, Location userLocation) {
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
        Double restaurantLat = results.get(position).getGeometry().getLocation().getLat();
        Double restaurantLng = results.get(position).getGeometry().getLocation().getLng();
        Location restaurantLocation = new Location(String.valueOf(new LatLng(restaurantLat, restaurantLng)));
        float[] distancef = new float[3];
        Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), restaurantLocation.getLatitude(), restaurantLocation.getLongitude(), distancef);
//                restaurantLocation.distanceTo(userLocation); // in meters
        float distance = distancef[2];
        holder.updateViewWithRestaurants(results.get(position), distance);
    }


    @Override
    public int getItemCount() {
        return results.size();
    }
}
