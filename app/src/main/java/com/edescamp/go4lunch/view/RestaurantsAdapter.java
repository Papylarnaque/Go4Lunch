package com.edescamp.go4lunch.view;

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

import com.edescamp.go4lunch.BuildConfig;
import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.activity.fragment.DetailsFragment;
import com.edescamp.go4lunch.model.api.APIClient;
import com.edescamp.go4lunch.model.api.APIRequest;
import com.edescamp.go4lunch.model.entities.LocationAPIMap;
import com.edescamp.go4lunch.model.entities.ResultAPIDetails;
import com.edescamp.go4lunch.model.entities.ResultAPIMap;
import com.edescamp.go4lunch.model.entities.ResultsAPIDetails;
import com.edescamp.go4lunch.util.RestaurantHelper;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.edescamp.go4lunch.activity.MainActivity.API_MAP_FIELDS;
import static com.edescamp.go4lunch.activity.MainActivity.workmates;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsViewHolder> {

    private static final String TAG = "RestaurantAdapter";
    private final List<ResultAPIMap> results;
    private final Location userLocation;
    private Context context;
    private int distance;
    private Integer workmatesCount;
    private ResultAPIDetails resultAPIDetails;

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

        getWorkmates(position, holder);

        getPlaceDetails(results.get(position), holder);

        holder.itemView.setOnClickListener(v -> getPlaceDetails(results.get(position).getPlaceId()));

    }

    private void getWorkmates(int position, RestaurantsViewHolder holder) {
        workmatesCount = 0;
        for (DocumentSnapshot workmate : workmates){
            if (Objects.equals(workmate.get("hasChosenRestaurant"), results.get(position).getPlaceId()))
                workmatesCount +=1;
        }

        holder.updateRestaurantsWitWorkmates(workmatesCount);

    }


    // --------------------  DETAILS DATA request -------------------- //

    private int getStraightDistance(LocationAPIMap restaurantLatLng) {
        Location restaurantLocation = new Location("");
        restaurantLocation.setLongitude(restaurantLatLng.getLng());
        restaurantLocation.setLatitude(restaurantLatLng.getLat());

        return Math.round(restaurantLocation.distanceTo(userLocation));
    }

    public void getPlaceDetails(ResultAPIMap resultAPIMap, RestaurantsViewHolder holder) {
        APIRequest apiDetails = APIClient.getClient().create(APIRequest.class);
        Call<ResultsAPIDetails> placeDetails = apiDetails.getPlaceDetails(resultAPIMap.getPlaceId(), API_MAP_FIELDS, BuildConfig.GOOGLE_MAPS_KEY);

        placeDetails.enqueue(new Callback<ResultsAPIDetails>() {
            @Override
            public void onResponse(@NotNull Call<ResultsAPIDetails> call, @NotNull Response<ResultsAPIDetails> response) {
                Log.d(TAG, "getPlaceDetails API ");
                if (response.isSuccessful()) {
                    ResultsAPIDetails body = response.body();
                    if (body != null) {
                        resultAPIDetails = body.getResult();
                        Log.d(TAG, "getPlaceDetails successful response " + resultAPIDetails.getName() + " " + resultAPIDetails.getPlaceId());

                        RestaurantHelper.setRestaurantDetails(resultAPIMap.getPlaceId(),resultAPIDetails);
                        holder.updateRestaurantsWithDetails(resultAPIDetails);

                    }
                    // TODO Handle failures, 404 error, etc
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResultsAPIDetails> call, @NotNull Throwable t) {
                Log.d(TAG, "getPlaceDetails API failure" + t);
            }
        });

        holder.createViewWithRestaurants(resultAPIMap, distance, workmatesCount);
    }

    // ------------------ DETAILS FRAGMENT accessibility -------------- //

    public void getPlaceDetails(String placeId) {
        APIRequest apiDetails = APIClient.getClient().create(APIRequest.class);
        Call<ResultsAPIDetails> placeDetails = apiDetails.getPlaceDetails(placeId, API_MAP_FIELDS, BuildConfig.GOOGLE_MAPS_KEY);

        placeDetails.enqueue(new Callback<ResultsAPIDetails>() {
            @Override
            public void onResponse(@NotNull Call<ResultsAPIDetails> call, @NotNull Response<ResultsAPIDetails> response) {
                Log.d(TAG, "getPlaceDetails API ");
                if (response.isSuccessful()) {
                    ResultsAPIDetails body = response.body();
                    if (body != null) {
                        ResultAPIDetails result = body.getResult();
                        Log.d(TAG, "getPlaceDetails successful response " + result.getName() + " " + result.getPlaceId());


                        openDetailsFragment(result);
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

    @Override
    public int getItemCount() {
        return results.size();
    }


}
