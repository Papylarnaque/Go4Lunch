package com.edescamp.go4lunch.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.service.entities.Result;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    private final TextView rName = itemView.findViewById(R.id.item_restaurant_first_line);
    private final TextView rAddress = itemView.findViewById(R.id.item_restaurant_second_line);
    private final TextView rDistance = itemView.findViewById(R.id.item_restaurant_distance);



    public RestaurantViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void updateViewWithRestaurants(Result result, float distance) {
        rName.setText(String.valueOf(result.getName()));
        rAddress.setText(String.valueOf(result.getVicinity()));
        rDistance.setText(String.valueOf(distance));

    }
}
