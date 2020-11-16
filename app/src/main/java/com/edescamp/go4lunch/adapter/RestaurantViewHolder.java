package com.edescamp.go4lunch.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.service.entities.PhotoAttributesAPIMap;
import com.edescamp.go4lunch.service.entities.ResultAPIMap;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "RestaurantViewHolder";
    private final TextView rName = itemView.findViewById(R.id.item_restaurant_first_line);
    private final TextView rAddress = itemView.findViewById(R.id.item_restaurant_second_line);
    private final TextView rDistance = itemView.findViewById(R.id.item_restaurant_distance);
    private final ImageView rPicture = itemView.findViewById(R.id.item_restaurant_picture);


    public RestaurantViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void updateViewWithRestaurants(ResultAPIMap result, Float distance) {
        Context context = itemView.getContext();
        String API_KEY = context.getResources().getString(R.string.google_api_key);

        rName.setText(String.valueOf(result.getName()));
        rAddress.setText(String.valueOf(result.getVicinity()));
        rDistance.setText(new StringBuilder().append(distance.intValue()).append("m"));

        try {
            PhotoAttributesAPIMap photoAttributesAPIMap = result.getPhotos().get(0);
                Glide.with(itemView).load(photoAttributesAPIMap.getPhoto_URL() + API_KEY).into(rPicture);
        } catch (Exception e) {
            Log.d(TAG, "photoAttributesAPIMap is null" + e);
        }


    }
}
