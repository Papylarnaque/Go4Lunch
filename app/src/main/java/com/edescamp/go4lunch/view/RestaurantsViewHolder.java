package com.edescamp.go4lunch.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.model.OpeningHoursAPIDetails;
import com.edescamp.go4lunch.model.PhotoAttributesAPIMap;
import com.edescamp.go4lunch.model.ResultAPIDetails;
import com.edescamp.go4lunch.model.ResultAPIMap;

import java.util.Calendar;
import java.util.Date;

import static com.edescamp.go4lunch.activity.MainActivity.RATING_MAX;
import static com.edescamp.go4lunch.activity.MainActivity.RATING_MIDDLE;
import static com.edescamp.go4lunch.activity.MainActivity.RATING_MIN;

public class RestaurantsViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "RestaurantViewHolder";
    private final TextView rName = itemView.findViewById(R.id.item_restaurant_name);
    private final TextView rAddress = itemView.findViewById(R.id.item_restaurant_address);
    private final TextView rDistance = itemView.findViewById(R.id.item_restaurant_distance);
    private final TextView rOpeningHours = itemView.findViewById(R.id.item_restaurant_hours);
    private final TextView rWorkmates = itemView.findViewById(R.id.item_restaurant_text_number_workmates);
    private final ImageView rPicture = itemView.findViewById(R.id.item_restaurant_picture);
    private final ImageView star1 = itemView.findViewById(R.id.item_restaurant_rating_star1);
    private final ImageView star2 = itemView.findViewById(R.id.item_restaurant_rating_star2);
    private final ImageView star3 = itemView.findViewById(R.id.item_restaurant_rating_star3);


    public RestaurantsViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void createViewWithRestaurants(ResultAPIMap result, int distance, Integer workmates) {
        rName.setText(String.valueOf(result.getName()));
        rAddress.setText(String.valueOf(result.getVicinity()));
        rDistance.setText(new StringBuilder().append(distance).append("m"));
        if (workmates > 0) {
            rWorkmates.setText(String.valueOf(workmates));
        } else {
            rWorkmates.setVisibility(View.INVISIBLE);
        }

        showRating(result);
        showPicture(result);
    }


    public void updateRestaurantsWithDetails(ResultAPIDetails resultAPIDetails) {
        OpeningHoursAPIDetails opening_hours = resultAPIDetails.getOpening_hours();
        rOpeningHours.setSelected(true);
        showOpeningHours(opening_hours);
    }

    public void updateRestaurantsWitWorkmates(int workmates) {
        if (workmates > 0) {
            rWorkmates.setText(String.valueOf(workmates));
        } else {
            rWorkmates.setVisibility(View.INVISIBLE);
        }
    }


    private void showOpeningHours(OpeningHoursAPIDetails opening_hours) {

        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // yourdate is an object of type Date
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK); // this will for example return 3 for tuesday
        Log.i(TAG, "DAY_OF_WEEK = " + dayOfWeek);

        if (opening_hours != null) {
            if (opening_hours.getWeekday_text() != null) {
                if (dayOfWeek == 1) {
                    rOpeningHours.setText(opening_hours.getWeekday_text().get(6));
                } else if (dayOfWeek > 1) {
                    rOpeningHours.setText(opening_hours.getWeekday_text().get(dayOfWeek - 2));
                } else {
                    rOpeningHours.setText(R.string.item_restaurant_no_opening_hours);
                }
            }
        }
    }

    // Handles Rating calculation
    private void showRating(ResultAPIMap result) {
        if (result.getRating() == null) {
        } else if (result.getRating() >= RATING_MAX) {
            star1.setVisibility(View.VISIBLE);
            star2.setVisibility(View.VISIBLE);
            star3.setVisibility(View.VISIBLE);
        } else if (result.getRating() >= RATING_MIDDLE && result.getRating() < RATING_MAX) {
            star1.setVisibility(View.VISIBLE);
            star2.setVisibility(View.VISIBLE);
            star3.setVisibility(View.GONE);
        } else if (result.getRating() >= RATING_MIN && result.getRating() < RATING_MIDDLE) {
            star1.setVisibility(View.VISIBLE);
            star2.setVisibility(View.GONE);
            star3.setVisibility(View.GONE);
        } else {
            star1.setVisibility(View.GONE);
            star2.setVisibility(View.GONE);
            star3.setVisibility(View.GONE);
        }
    }

    private void showPicture(ResultAPIMap result) {

        Context context = itemView.getContext();
        String API_KEY = context.getResources().getString(R.string.google_api_key);


        if (result.getPhotos() == null) {
            Log.i(TAG, "result.getPhotos() == null =>  " + result.getPlaceId());
            rPicture.setVisibility(View.INVISIBLE);
            TextView noPhoto = itemView.findViewById(R.id.item_restaurant_no_picture_text);
            noPhoto.setText(R.string.no_picture);
            noPhoto.setVisibility(View.VISIBLE);
        } else {
            PhotoAttributesAPIMap photoAttributesAPIMap = result.getPhotos().get(0);

            Glide.with(this.itemView.getContext())
                    .load(photoAttributesAPIMap.getPhoto_URL() + API_KEY)
                    .apply(new RequestOptions()
                            .centerCrop())
                    .into(rPicture);
        }

    }


}
