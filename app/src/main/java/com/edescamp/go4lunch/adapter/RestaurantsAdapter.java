package com.edescamp.go4lunch.adapter;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.activity.fragment.DetailsFragment;
import com.edescamp.go4lunch.service.APIClient;
import com.edescamp.go4lunch.service.APIRequest;
import com.edescamp.go4lunch.service.entities.LocationAPIMap;
import com.edescamp.go4lunch.service.entities.ResultAPIDetails;
import com.edescamp.go4lunch.service.entities.ResultAPIMap;
import com.edescamp.go4lunch.service.entities.ResultsAPIDetails;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.edescamp.go4lunch.activity.MainActivity.FIELDS;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsViewHolder> {

    private static final String TAG = "RestaurantAdapter";
    private final List<ResultAPIMap> results;
    private final Location userLocation;
    private Context context;
    private ResultAPIDetails resultAPIDetails;
    private int distance;

    public RestaurantsAdapter(List<ResultAPIMap> results, Location userLocation) {
        this.results = results;
        this.userLocation = userLocation;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.item_restaurant;
    }

    @NonNull
    @Override
    public RestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        context = view.getContext();
        return new RestaurantsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RestaurantsViewHolder holder, int position) {

        distance = getStraightDistance(results.get(position).getGeometry().getLocation());

        getPlaceDetails(results.get(position), holder);



        holder.itemView.setOnClickListener(v ->  openDetailsFragment(resultAPIDetails));

    }

    public void getPlaceDetails(ResultAPIMap resultAPIMap, RestaurantsViewHolder holder) {
        APIRequest apiDetails = APIClient.getClient().create(APIRequest.class);
        Call<ResultsAPIDetails> placeDetails = apiDetails.getPlaceDetails(resultAPIMap.getPlaceId(), FIELDS, context.getResources().getString(R.string.google_maps_key));

        placeDetails.enqueue(new Callback<ResultsAPIDetails>() {
            @Override
            public void onResponse(@NotNull Call<ResultsAPIDetails> call, @NotNull Response<ResultsAPIDetails> response) {
                Log.d(TAG, "getPlaceDetails API ");
                if (response.isSuccessful()) {
                    ResultsAPIDetails body = response.body();
                    if (body != null) {
                        resultAPIDetails = body.getResult();
                        Log.d(TAG, "getPlaceDetails successful response " + resultAPIDetails.getName() + " " + resultAPIDetails.getPlaceId());


                        holder.updateViewWithRestaurants(resultAPIMap, resultAPIDetails.getOpening_hours(), distance);

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

    private void openDetailsFragment(ResultAPIDetails result) {
        AppCompatActivity activity = (AppCompatActivity) context;
        Fragment fragment = new DetailsFragment(result);
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private int getStraightDistance(LocationAPIMap r) {
        Location restaurantLocation = new Location("");
        restaurantLocation.setLongitude(r.getLng());
        restaurantLocation.setLatitude(r.getLat());

        return Math.round(restaurantLocation.distanceTo(userLocation));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }


}
