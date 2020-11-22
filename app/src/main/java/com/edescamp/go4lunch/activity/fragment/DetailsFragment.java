package com.edescamp.go4lunch.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.service.entities.ResultAPIDetails;

public class DetailsFragment extends BaseFragment {

    private static final String TAG = "DetailsFragment";
    private final ResultAPIDetails result;

    ImageView star1;
    ImageView star2;
    ImageView star3;


    public DetailsFragment(ResultAPIDetails result) {
        this.result = result;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_details, container, false);


        hideActivityViews();
        configureView(view);

        return view;
    }


    private void configureView(View view) {
        Context context = view.getContext();
        String API_KEY = context.getResources().getString(R.string.google_api_key);

        // View
        TextView restaurantName = view.findViewById(R.id.restaurant_details_name);
        TextView restaurantAddress = view.findViewById(R.id.restaurant_details_address);
        ImageView restaurantPicture = view.findViewById(R.id.restaurant_details_picture);
        star1 = view.findViewById(R.id.restaurant_details_star1);
        star2 = view.findViewById(R.id.restaurant_details_star2);
        star3 = view.findViewById(R.id.restaurant_details_star3);
        ImageView phone = view.findViewById(R.id.restaurant_details_phone_image);
        ImageView like = view.findViewById(R.id.restaurant_details_like_image);
        ImageView website = view.findViewById(R.id.restaurant_details_website_image);

        restaurantName.setText(result.getName());
        restaurantAddress.setText(result.getFormatted_address());

        Glide.with(view)
                .load(result.getPhotos().get(0).getPhoto_URL() + API_KEY)
                .apply(new RequestOptions()
                        .fitCenter())
                .into(restaurantPicture);

        phone.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+result.getInternational_phone_number().trim()));
            startActivity(intent);
        });

        website.setOnClickListener(v -> {
            Intent openURL = new Intent(Intent.ACTION_VIEW);
            openURL.setData(Uri.parse(result.getWebsite()));
            startActivity(openURL);
        });

        like.setOnClickListener(v -> {
            // TODO : implement like functionnality
            Toast.makeText(getContext(), "TODO : implement like functionnality", Toast.LENGTH_SHORT ).show();
        });

        showRating();
    }

    private void showRating() {
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


    @Override
    public void onBackPressed() {
        Toast.makeText(getActivity().getApplicationContext(), "OnBackPressed clicked", Toast.LENGTH_SHORT).show();
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        getActivity().findViewById(R.id.navbar).setVisibility(View.INVISIBLE);
    }

    private void showActivityViews() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        getActivity().findViewById(R.id.navbar).setVisibility(View.VISIBLE);
    }
}
