package com.edescamp.go4lunch.activity.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static com.edescamp.go4lunch.activity.MainActivity.uid;

public class DetailsFragment extends Fragment {

    private static final String TAG = "DetailsFragment";
    private final ResultAPIDetails resultAPIDetails;

    private TextView restaurantName;
    private TextView restaurantAddress;
    private ImageView restaurantPicture;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageButton buttonBack;
    private ImageButton buttonRestaurantChoice;
    private Button buttonPhone;
    private Button buttonLike;
    private Button buttonWebsite;

    private String restaurantChoice;
    private List<String> likesChoice;


    public DetailsFragment(ResultAPIDetails resultAPIDetails) {
        this.resultAPIDetails = resultAPIDetails;
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


        setClickableFunctionality();

        getLikes();

        return view;
    }

    private void getLikes() {


    }

    // -------------------- CONFIG CLICK RESPONSE ------------------ //

    private void setClickableFunctionality() {
        buttonPhone.setOnClickListener(v -> {
            if (resultAPIDetails.getInternational_phone_number() == null) {
                Toast toast = Toast.makeText(getContext(), R.string.no_phone_number, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + resultAPIDetails.getInternational_phone_number().trim()));
                startActivity(intent);
            }
        });

        buttonWebsite.setOnClickListener(v -> {
            if (resultAPIDetails.getWebsite() == null) {
                Toast toast = Toast.makeText(getContext(), R.string.no_website, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                Intent openURL = new Intent(Intent.ACTION_VIEW);
                openURL.setData(Uri.parse(resultAPIDetails.getWebsite()));
                startActivity(openURL);
            }
        });

        buttonLike.setOnClickListener(v -> buttonLikeResponse());

        buttonBack.setOnClickListener(v -> requireActivity().onBackPressed());

        buttonRestaurantChoice.setOnClickListener(v -> buttonRestaurantChoiceResponse());
    }


    // -------------------- CONFIG User Interface LAYOUT ------------------ //

    private void configureView(View view) {
        Context context = view.getContext();
        String API_KEY = context.getResources().getString(R.string.google_api_key);

        // View
        restaurantName = view.findViewById(R.id.restaurant_details_name);
        restaurantAddress = view.findViewById(R.id.restaurant_details_address);
        restaurantPicture = view.findViewById(R.id.restaurant_details_picture);
        buttonPhone = view.findViewById(R.id.restaurant_details_phone_call);
        buttonLike = view.findViewById(R.id.restaurant_details_like);
        buttonWebsite = view.findViewById(R.id.restaurant_details_website);
        buttonBack = view.findViewById(R.id.fragment_restaurant_details_button_backpress);
        buttonRestaurantChoice = view.findViewById(R.id.fragment_restaurant_details_button_restaurant_choice);

        buttonPhone.setText(R.string.restaurant_details_phone_call);
        buttonLike.setText(R.string.restaurant_details_like);
        buttonWebsite.setText(R.string.restaurant_details_website);

        restaurantName.setText(resultAPIDetails.getName());
        restaurantAddress.setText(resultAPIDetails.getFormatted_address());

        star1 = view.findViewById(R.id.restaurant_details_star1);
        star2 = view.findViewById(R.id.restaurant_details_star2);
        star3 = view.findViewById(R.id.restaurant_details_star3);


        if (resultAPIDetails.getPhotos() == null) {
            Log.i(TAG, "result.getPhotos() == null =>  " + resultAPIDetails.getPlaceId());
            restaurantPicture.setVisibility(View.INVISIBLE);
            TextView noPhoto = view.findViewById(R.id.restaurant_details_no_picture_text);
            noPhoto.setText(R.string.no_picture);
            noPhoto.setVisibility(View.VISIBLE);
        } else {
            Glide.with(view)
                    .load(resultAPIDetails.getPhotos().get(0).getPhoto_URL() + API_KEY)
                    .apply(new RequestOptions()
                            .fitCenter())
                    .into(restaurantPicture);
            restaurantPicture.setVisibility(View.VISIBLE);
        }

        restaurantChoiceLayout();
        showRating();
    }

    // Handles DetailsFragment custom user choices layout
    private void restaurantChoiceLayout() {
        UserHelper.getUser(MainActivity.uid).addOnSuccessListener(documentUserSnapshot -> {
            // Handles buttonRestaurantChoice layout
            restaurantChoice = (String) documentUserSnapshot.get("hasChosenRestaurant");
            if (restaurantChoice.equals(resultAPIDetails.getPlaceId())) {
                buttonRestaurantChoice.setImageResource(R.drawable.ic_baseline_check_circle_30);
            } else {
                buttonRestaurantChoice.setImageResource(R.drawable.ic_baseline_add_30);
            }

            // Handles buttonLike layout

            likesChoice = (List<String>) documentUserSnapshot.get("likes");
            if (likesChoice != null) {
                if (likesChoice.contains(resultAPIDetails.getPlaceId())) {
                    buttonLike.setText(R.string.restaurant_details_unlike);
                } else {
                    buttonLike.setText(R.string.restaurant_details_like);
                }
            }

        });
    }

    private void showRating() {
        if (resultAPIDetails.getRating() == null) {
        } else if (resultAPIDetails.getRating() >= 4.5) {
            star1.setVisibility(View.VISIBLE);
            star2.setVisibility(View.VISIBLE);
            star3.setVisibility(View.VISIBLE);
        } else if (resultAPIDetails.getRating() >= 2.5 && resultAPIDetails.getRating() < 4.5) {
            star1.setVisibility(View.VISIBLE);
            star2.setVisibility(View.VISIBLE);
            star3.setVisibility(View.INVISIBLE);
        } else if (resultAPIDetails.getRating() >= 1 && resultAPIDetails.getRating() < 2.5) {
            star1.setVisibility(View.VISIBLE);
            star2.setVisibility(View.INVISIBLE);
            star3.setVisibility(View.INVISIBLE);
        } else {
            star1.setVisibility(View.INVISIBLE);
            star2.setVisibility(View.INVISIBLE);
            star3.setVisibility(View.INVISIBLE);
        }
    }

    // Handles the response to RESTAURANT CHOICE BUTTON CLICK
    private void buttonLikeResponse() {
        if (likesChoice != null) {
            if (likesChoice.contains(resultAPIDetails.getPlaceId())) {
                // if restaurant is liked
                UserHelper.updateLikesDeleteRestaurant(likesChoice, resultAPIDetails.getPlaceId(), MainActivity.uid).addOnSuccessListener(aVoid -> restaurantChoiceLayout());
                Toast.makeText(getContext(), "restaurant unliked", Toast.LENGTH_SHORT).show();
            } else {
                UserHelper.updateLikesAddRestaurant(likesChoice, resultAPIDetails.getPlaceId(), MainActivity.uid).addOnSuccessListener(aVoid -> restaurantChoiceLayout());
                Toast.makeText(getContext(), "restaurant LIKED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Handles the response to RESTAURANT CHOICE BUTTON CLICK
    private void buttonRestaurantChoiceResponse() {
        if (restaurantChoice != null) {
            if (restaurantChoice.equals("")) {
                // if no choice has been made
                restaurantChoice = resultAPIDetails.getPlaceId();

                UserHelper.updateRestaurantChoice(restaurantChoice, resultAPIDetails.getName(), MainActivity.uid);
                buttonRestaurantChoice.setImageResource(R.drawable.ic_baseline_check_circle_30);
                Toast.makeText(getContext(), getString(R.string.restaurant_Chosen, restaurantName.getText()), Toast.LENGTH_SHORT).show();
            } else if (restaurantChoice.equals(resultAPIDetails.getPlaceId())) {
                // if restaurant chosen = restaurant details
                alertRestaurantCancel();
            } else if (!restaurantChoice.equals(resultAPIDetails.getPlaceId())) {
                // if restaurant chosen != restaurant details
                alertRestaurantChange();

            }
        }
    }

    // Action on buttonRestaurantChoice click
    // if restaurant chosen = restaurant details
    private void alertRestaurantCancel() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.details_fragment_alertdialog_restaurant_choice_cancellation_message)
                .setTitle(R.string.details_fragment_alertdialog_restaurant_choice_cancellation_title);

        AlertDialog dialogRestaurantChosen = builder.create();

        dialogRestaurantChosen.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.YES), (dialog1, which) -> {
            restaurantChoice = "";
            UserHelper.updateRestaurantChoice(restaurantChoice, restaurantChoice, uid).addOnSuccessListener(aVoid -> restaurantChoiceLayout());
        });

        dialogRestaurantChosen.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.NO), (dialog12, which) -> dialog12.dismiss());

        dialogRestaurantChosen.show();

    }

    // Action on buttonRestaurantChoice click
    // if restaurant chosen != restaurant details
    private void alertRestaurantChange() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(getContext().getString(R.string.details_fragment_alertdialog_restaurant_choice_change_message, resultAPIDetails.getName()))
                .setTitle(R.string.details_fragment_alertdialog_restaurant_choice_change_title);

        AlertDialog dialogRestaurantChosen = builder.create();

        dialogRestaurantChosen.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.YES), (dialog1, which) -> {
            restaurantChoice = resultAPIDetails.getPlaceId();
            UserHelper.updateRestaurantChoice(restaurantChoice, resultAPIDetails.getName(), uid).addOnSuccessListener(aVoid -> restaurantChoiceLayout());
        });

        dialogRestaurantChosen.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.NO), (dialog12, which) -> dialog12.dismiss());

        dialogRestaurantChosen.show();

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
