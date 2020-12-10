package com.edescamp.go4lunch.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.model.User;

public class WorkmatesViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "WorkmatesViewHolder";
    private Context context;

    private final TextView userName = itemView.findViewById(R.id.item_workmates_name);
    private final ImageView userPicture = itemView.findViewById(R.id.item_workmates_picture);
    private final TextView userRestaurantChoice = itemView.findViewById(R.id.item_workmates_restaurant);


    public WorkmatesViewHolder(@NonNull View itemView) {
        super(itemView);

        context = itemView.getContext();
    }


    public void updateViewWithWorkmates(User user) {


        userName.setText(user.getUsername());

        Glide.with(context)
                .load(user.getUrlPicture())
                .apply(new RequestOptions()
                        .circleCrop())
                .into(userPicture);

        // TODO Handle chosen restaurant
        if (user.getHasChosenRestaurant()==null || user.getHasChosenRestaurant().equals("")){
            userRestaurantChoice.setText(R.string.item_workmates_restaurant_text_null);
            userRestaurantChoice.setTextColor(context.getResources().getColor(R.color.quantum_grey));
        }
        else {
            String restaurantName = user.getChosenRestaurantName();
            userRestaurantChoice.setText(context.getString(R.string.item_workmates_restaurant_text, restaurantName));
        }
    }

}
