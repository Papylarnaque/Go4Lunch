package com.edescamp.go4lunch.adapter;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.model.Restaurant;
import com.edescamp.go4lunch.service.entities.ResultAPIMap;
import com.google.android.gms.maps.model.LatLng;

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
        Restaurant restaurant = new Restaurant();
        ResultAPIMap result = results.get(position);
        restaurant.setRestaurantid(result.getPlaceId());
        restaurant.setLatlng(new LatLng(
                result.getGeometry().getLocation().getLat(),
                result.getGeometry().getLocation().getLng()));

        getStraightDistance(restaurant);

        Context context = holder.itemView.getContext();
        restaurant.setUrlPicture(result.getPhotos().get(0).getPhoto_URL()+ context.getResources().getString(R.string.google_api_key));

        holder.updateViewWithRestaurants(restaurant);
    }

    private void getStraightDistance(Restaurant r){
        Location restaurantLocation = new Location("");
        restaurantLocation.setLongitude(r.getLatlng().longitude);
        restaurantLocation.setLatitude(r.getLatlng().latitude);

        r.setDistance((int) restaurantLocation.distanceTo(userLocation));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }


}
