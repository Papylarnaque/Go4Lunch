package com.edescamp.go4lunch.adapter;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.model.Restaurant;
import com.edescamp.go4lunch.service.APIClient;
import com.edescamp.go4lunch.service.APIRequest;
import com.edescamp.go4lunch.service.entities.ElementAPIDistance;
import com.edescamp.go4lunch.service.entities.ResultAPIMap;
import com.edescamp.go4lunch.service.entities.RowAPIDistance;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    private Context context;
    private List<ResultAPIMap> results;
    private Location userLocation;

    public RestaurantAdapter(List<ResultAPIMap> results, Location userLocation, Context context) {
        this.results = results;
        this.userLocation = userLocation;
        this.context = context;
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
        Restaurant r = new Restaurant();
        r.setRestaurantid(results.get(position).getPlaceId());
        r.setLatlng(new LatLng(
                results.get(position).getGeometry().getLocation().getLat(),
                results.get(position).getGeometry().getLocation().getLng()));

        getStraightDistance(r);

//        getDistanceAPI(r);

        r.setUrlPicture(results.get(position).getPhotos().get(0).getPhoto_URL()+R.string.google_api_key);

        Glide.with(this.context)
                .load(r.getUrlPicture())
                .into((ImageView) holder.itemView.findViewById(R.id.item_restaurant_picture));


        holder.updateViewWithRestaurants(results.get(position), r);
    }

    private void getStraightDistance(Restaurant r){
        Location restaurantLocation = new Location("");
        restaurantLocation.setLongitude(r.getLatlng().longitude);
        restaurantLocation.setLatitude(r.getLatlng().latitude);

        r.setDistance((int) restaurantLocation.distanceTo(userLocation));

    }


    public void getDistanceAPI(Restaurant r) {
        Location restaurantLocation = new Location("");
        restaurantLocation.setLatitude(r.getLatlng().latitude);
        restaurantLocation.setLongitude(r.getLatlng().longitude);

        APIRequest apiRequest = APIClient.getClient().create(APIRequest.class);
        Call<RowAPIDistance> rowAPI = apiRequest.getDistanceBetweenLocations(
                convertLocationForApi(userLocation),
                convertLocationForApi(restaurantLocation),
                context.getResources().getString(R.string.google_maps_key));
        rowAPI.enqueue(new Callback<RowAPIDistance>() {
            @Override
            public void onResponse(Call<RowAPIDistance> call, Response<RowAPIDistance> response) {
                if (response.isSuccessful()) {
                    RowAPIDistance body = response.body();
                    if (body != null) {
                        ElementAPIDistance elements = body.getElements();
                        r.setDistance(elements.getDistance().getValue());
                    }
                }
            }
            @Override
            public void onFailure(Call<RowAPIDistance> call, Throwable t) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return results.size();
    }

    public static String convertLocationForApi(Location location) {
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            return lat + "," + lng;
        }
        return null;
    }

}
