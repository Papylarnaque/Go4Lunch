package com.edescamp.go4lunch.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.activity.MainActivity;
import com.edescamp.go4lunch.service.entities.ResultAPIDetails;
import com.edescamp.go4lunch.util.UserHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DetailsFragment extends Fragment {

    private static final String TAG = "DetailsFragment";
    private final ResultAPIDetails result;

    private TextView restaurantName;
    private TextView restaurantAddress;
    private ImageView restaurantPicture;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageButton buttonBackPress;
    private ImageButton buttonRestaurantChoice;
    private Button phone;
    private Button like;
    private Button website;


    public DetailsFragment(ResultAPIDetails result) {
        this.result = result;
    }


    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_details, container, false);
        hideActivityViews();
        configureView(view);

        UserHelper.getUser(MainActivity.uid).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.get("hasChosenRestaurant").equals(result.getPlaceId())){
                    buttonRestaurantChoice.setImageResource(R.drawable.ic_baseline_check_circle_30);
                }
                else    {
                    buttonRestaurantChoice.setImageResource(R.drawable.ic_baseline_add_30);
                }
            }
        });

        phone.setOnClickListener(v -> {
            if (result.getInternational_phone_number() == null) {
                Toast toast = Toast.makeText(getContext(), R.string.no_phone_number, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + result.getInternational_phone_number().trim()));
                startActivity(intent);
            }
        });

        website.setOnClickListener(v -> {
            if (result.getWebsite() == null) {
                Toast toast = Toast.makeText(getContext(), R.string.no_website, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                Intent openURL = new Intent(Intent.ACTION_VIEW);
                openURL.setData(Uri.parse(result.getWebsite()));
                startActivity(openURL);
            }
        });

        like.setOnClickListener(v -> {
            // TODO : implement like functionnality
            Toast.makeText(getContext(), "TODO : implement like functionality", Toast.LENGTH_SHORT).show();
        });

        buttonBackPress.setOnClickListener(v -> requireActivity().onBackPressed());

        buttonRestaurantChoice.setOnClickListener(v -> selectRestaurant());


        return view;
    }

    private void selectRestaurant() {
        UserHelper.setChosenRestaurant(result.getPlaceId(), MainActivity.uid);
        buttonRestaurantChoice.setImageResource(R.drawable.ic_baseline_check_circle_30);
        Toast.makeText(getContext(), getString(R.string.restaurant_Chosen,restaurantName.getText()), Toast.LENGTH_SHORT).show();
    }


    private void configureView(View view) {
        Context context = view.getContext();
        String API_KEY = context.getResources().getString(R.string.google_api_key);

        // View
        restaurantName = view.findViewById(R.id.restaurant_details_name);
        restaurantAddress = view.findViewById(R.id.restaurant_details_address);
        restaurantPicture = view.findViewById(R.id.restaurant_details_picture);
        phone = view.findViewById(R.id.restaurant_details_phone_call);
        like = view.findViewById(R.id.restaurant_details_like);
        website = view.findViewById(R.id.restaurant_details_website);
        buttonBackPress = view.findViewById(R.id.fragment_restaurant_details_button_backpress);
        buttonRestaurantChoice = view.findViewById(R.id.fragment_restaurant_details_button_restaurant_choice);

        phone.setText(R.string.restaurant_details_phone_call);
        like.setText(R.string.restaurant_details_like);
        website.setText(R.string.restaurant_details_website);

        restaurantName.setText(result.getName());
        restaurantAddress.setText(result.getFormatted_address());

        star1 = view.findViewById(R.id.restaurant_details_star1);
        star2 = view.findViewById(R.id.restaurant_details_star2);
        star3 = view.findViewById(R.id.restaurant_details_star3);


        if (result.getPhotos() == null) {
            Log.i(TAG, "result.getPhotos() == null =>  " + result.getPlaceId());
            restaurantPicture.setVisibility(View.INVISIBLE);
            TextView noPhoto = view.findViewById(R.id.restaurant_details_no_picture_text);
            noPhoto.setText(R.string.no_picture);
            noPhoto.setVisibility(View.VISIBLE);
        } else {
            Glide.with(view)
                    .load(result.getPhotos().get(0).getPhoto_URL() + API_KEY)
                    .apply(new RequestOptions()
                            .fitCenter())
                    .into(restaurantPicture);
            restaurantPicture.setVisibility(View.VISIBLE);
        }


        showRating();
    }

    private void showRating() {
        if (result.getRating() == null) {
        } else if (result.getRating() >= 4) {
            star1.setVisibility(View.VISIBLE);
            star2.setVisibility(View.VISIBLE);
            star3.setVisibility(View.VISIBLE);
        } else if (result.getRating() >= 2.5 && result.getRating() < 4) {
            star1.setVisibility(View.VISIBLE);
            star2.setVisibility(View.VISIBLE);
            star3.setVisibility(View.INVISIBLE);
        } else if (result.getRating() >= 1 && result.getRating() < 2.5) {
            star1.setVisibility(View.VISIBLE);
            star2.setVisibility(View.INVISIBLE);
            star3.setVisibility(View.INVISIBLE);
        } else  {
            star1.setVisibility(View.INVISIBLE);
            star2.setVisibility(View.INVISIBLE);
            star3.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        hideActivityViews();
    }

    @Override
    public void onStop() {
        super.onStop();
        showActivityViews();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        showActivityViews();
    }

    private void hideActivityViews() {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
        requireActivity().findViewById(R.id.navbar).setVisibility(View.INVISIBLE);
    }

    private void showActivityViews() {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
        requireActivity().findViewById(R.id.navbar).setVisibility(View.VISIBLE);
    }
}
