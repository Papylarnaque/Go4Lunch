package com.edescamp.go4lunch.adapter;

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
import com.edescamp.go4lunch.service.entities.PhotoAttributesAPIMap;
import com.edescamp.go4lunch.service.entities.ResultAPIMap;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "RestaurantViewHolder";
    private final TextView rName = itemView.findViewById(R.id.item_restaurant_name);
    private final TextView rAddress = itemView.findViewById(R.id.item_restaurant_address);
    private final TextView rDistance = itemView.findViewById(R.id.item_restaurant_distance);
    // TODO private final TextView rOpeningHours = itemView.findViewById(R.id.item_restaurant_hours);
    private final ImageView rPicture = itemView.findViewById(R.id.item_restaurant_picture);
    private final ImageView star1 = itemView.findViewById(R.id.item_restaurant_rating_star1);
    private final ImageView star2 = itemView.findViewById(R.id.item_restaurant_rating_star2);
    private final ImageView star3 = itemView.findViewById(R.id.item_restaurant_rating_star3);


    public RestaurantViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void updateViewWithRestaurants(ResultAPIMap result, Float distance) {
        Context context = itemView.getContext();
        String API_KEY = context.getResources().getString(R.string.google_api_key);

        rName.setText(String.valueOf(result.getName()));
        rAddress.setText(String.valueOf(result.getVicinity()));
        rDistance.setText(new StringBuilder().append(distance.intValue()).append("m"));
        // TODO  rOpeningHours.setText(result.getOpening_hours().toString());

        if (result.getPhotos()== null) {
            Log.i(TAG, "result.getPhotos() == null =>  " + result.getPlaceId());
            rPicture.setVisibility(View.INVISIBLE);
            TextView noPhoto = itemView.findViewById(R.id.item_restaurant_no_picture_text);
            noPhoto.setVisibility(View.VISIBLE);
        } else {
                PhotoAttributesAPIMap photoAttributesAPIMap = result.getPhotos().get(0);

                Glide.with(this.itemView.getContext())
                        .load(photoAttributesAPIMap.getPhoto_URL() + API_KEY)
                        .apply(new RequestOptions()
                                .centerCrop())
                        .into(rPicture);
        }
        showRating(result);
    }

    private void showRating(ResultAPIMap result) {
        if (result.getRating() > 4) {
            star1.setVisibility(View.VISIBLE);
            star2.setVisibility(View.VISIBLE);
            star3.setVisibility(View.VISIBLE);
        } else if (result.getRating() > 2.5) {
            star1.setVisibility(View.VISIBLE);
            star2.setVisibility(View.VISIBLE);
        } else if (result.getRating() > 1) {
            star1.setVisibility(View.VISIBLE);
        }
    }

}
