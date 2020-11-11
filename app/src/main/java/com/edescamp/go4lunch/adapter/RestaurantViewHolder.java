package com.edescamp.go4lunch.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.model.Restaurant;
import com.edescamp.go4lunch.service.entities.ResultAPIMap;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    private final TextView rName = itemView.findViewById(R.id.item_restaurant_first_line);
    private final TextView rAddress = itemView.findViewById(R.id.item_restaurant_second_line);
    private final TextView rDistance = itemView.findViewById(R.id.item_restaurant_distance);
    private final ImageView rPicture = itemView.findViewById(R.id.item_restaurant_picture);



    public RestaurantViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void updateViewWithRestaurants(ResultAPIMap result, Restaurant r) {
        rName.setText(String.valueOf(result.getName()));
        rAddress.setText(String.valueOf(result.getVicinity()));
//        rDistance.setText(String.valueOf(distance));
        rDistance.setText(String.format(String.valueOf(r.getDistance()),"%sm"));


    }
}
