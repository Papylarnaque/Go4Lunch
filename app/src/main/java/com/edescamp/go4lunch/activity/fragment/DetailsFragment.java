package com.edescamp.go4lunch.activity.fragment;

import android.app.AlertDialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.edescamp.go4lunch.BuildConfig;
import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.activity.MainActivity;
import com.edescamp.go4lunch.activity.fragment.view.WorkmatesAdapter;
import com.edescamp.go4lunch.model.details.ResultAPIDetails;
import com.edescamp.go4lunch.util.RatingUtil;
import com.edescamp.go4lunch.util.SharedPrefs;
import com.edescamp.go4lunch.util.UserHelper;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.edescamp.go4lunch.activity.MainActivity.RATING_MAX;
import static com.edescamp.go4lunch.activity.MainActivity.RATING_MIDDLE;
import static com.edescamp.go4lunch.activity.MainActivity.RATING_MIN;
import static com.edescamp.go4lunch.activity.MainActivity.uid;

public class DetailsFragment extends Fragment {

    public static final String TAG = "DetailsFragment";
    private final ResultAPIDetails resultAPIDetails;

    private TextView restaurantName;
    private ImageView restaurantPicture;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageButton buttonBack;
    private ImageButton buttonRestaurantChoice;
    private Button buttonPhone;
    private Button buttonLike;
    private Button buttonWebsite;
    private RecyclerView recyclerView;

    private String chosenRestaurantId;
    private List<String> likesChoice;


    public DetailsFragment(ResultAPIDetails resultAPIDetails) {
        this.resultAPIDetails = resultAPIDetails;
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

        setPicture(view);

        restaurantChoiceLayout();
//        showRating();
        RatingUtil.showRating(resultAPIDetails.getRating(), star1, star2, star3);
        populateWorkmates();

        setClickableFunctionality();

        return view;
    }


    // -------------------- CONFIG User Interface LAYOUT ------------------ //

    private void configureView(View view) {
        // View
        restaurantName = view.findViewById(R.id.restaurant_details_name);
        TextView restaurantAddress = view.findViewById(R.id.restaurant_details_address);
        restaurantPicture = view.findViewById(R.id.restaurant_details_picture);
        buttonPhone = view.findViewById(R.id.restaurant_details_phone_call);
        buttonLike = view.findViewById(R.id.restaurant_details_like);
        buttonWebsite = view.findViewById(R.id.restaurant_details_website);
        buttonBack = view.findViewById(R.id.fragment_restaurant_details_button_backpress);
        buttonRestaurantChoice = view.findViewById(R.id.fragment_restaurant_details_button_restaurant_choice);

        recyclerView = view.findViewById(R.id.restaurant_details_workmates_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        buttonPhone.setText(R.string.restaurant_details_phone_call);
        buttonLike.setText(R.string.restaurant_details_like);
        buttonWebsite.setText(R.string.restaurant_details_website);

        restaurantName.setText(resultAPIDetails.getName());
        restaurantAddress.setText(resultAPIDetails.getFormatted_address());

        star1 = view.findViewById(R.id.restaurant_details_star1);
        star2 = view.findViewById(R.id.restaurant_details_star2);
        star3 = view.findViewById(R.id.restaurant_details_star3);

    }


    private void setPicture(View view) {
        if (resultAPIDetails.getPhotos() == null) {
            Log.i(TAG, "result.getPhotos() == null =>  " + resultAPIDetails.getPlaceId());
            restaurantPicture.setVisibility(View.INVISIBLE);
            TextView noPhoto = view.findViewById(R.id.restaurant_details_no_picture_text);
            noPhoto.setText(R.string.no_picture);
            noPhoto.setVisibility(View.VISIBLE);
        } else {
            Glide.with(view)
                    .load(resultAPIDetails.getPhotos().get(0).getPhoto_URL() + BuildConfig.GOOGLE_MAPS_KEY)
                    .apply(new RequestOptions()
                            .fitCenter())
                    .into(restaurantPicture);
            restaurantPicture.setVisibility(View.VISIBLE);
        }
    }


    // Handles DetailsFragment custom user choices layout
    private void restaurantChoiceLayout() {
        UserHelper.getUser(uid).addOnSuccessListener(documentUserSnapshot -> {
            // Handles buttonRestaurantChoice layout

            chosenRestaurantId = (String) documentUserSnapshot.get("chosenRestaurantId");
            if (Objects.equals(chosenRestaurantId, resultAPIDetails.getPlaceId())) {
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

    // Handles Rating calculation
    private void showRating() {
        if (resultAPIDetails.getRating() == null) {
        } else if (resultAPIDetails.getRating() >= RATING_MAX) {
            star1.setVisibility(View.VISIBLE);
            star2.setVisibility(View.VISIBLE);
            star3.setVisibility(View.VISIBLE);
        } else if (resultAPIDetails.getRating() >= RATING_MIDDLE && resultAPIDetails.getRating() < RATING_MAX) {
            star1.setVisibility(View.VISIBLE);
            star2.setVisibility(View.VISIBLE);
            star3.setVisibility(View.INVISIBLE);
        } else if (resultAPIDetails.getRating() >= RATING_MIN && resultAPIDetails.getRating() < RATING_MIDDLE) {
            star1.setVisibility(View.VISIBLE);
            star2.setVisibility(View.INVISIBLE);
            star3.setVisibility(View.INVISIBLE);
        } else {
            star1.setVisibility(View.INVISIBLE);
            star2.setVisibility(View.INVISIBLE);
            star3.setVisibility(View.INVISIBLE);
        }
    }

    // Populates Workmates who choose this restaurant
    private void populateWorkmates() {
        UserHelper.getUsersWhoChoseThisRestaurant(resultAPIDetails.getPlaceId()).addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = new ArrayList<>();
            // Handle removing current user from the list in the fragment
            // as firebase does not support double where queries
            for (DocumentSnapshot user : queryDocumentSnapshots.getDocuments()) {
                if (!Objects.equals(user.get("uid"), uid)) {
                    documents.add(user);
                }
            }
            sendResultsToAdapter(documents);
        });
    }

    private void sendResultsToAdapter(List<DocumentSnapshot> documents) {
        recyclerView.setAdapter(new WorkmatesAdapter(documents, TAG));
    }

    // -------------- Handles USER INTERACTIONS -------------- //

    // CONFIG CLICK LISTENERS
    private void setClickableFunctionality() {
        buttonPhone.setOnClickListener(v -> {
            if (resultAPIDetails.getInternational_phone_number() == null) {
                Toast toast = Toast.makeText(getContext(), R.string.no_phone_number, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(getString(R.string.details_phone) + resultAPIDetails.getInternational_phone_number().trim()));
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

    // RESTAURANT LIKE BUTTON
    private void buttonLikeResponse() {

        if (likesChoice != null && likesChoice.contains(resultAPIDetails.getPlaceId())) {
            // if restaurant is liked
            UserHelper.updateLikesDeleteRestaurant(likesChoice, resultAPIDetails.getPlaceId(), MainActivity.uid)
                    .addOnSuccessListener(aVoid -> restaurantChoiceLayout());
        } else {
            UserHelper.updateLikesAddRestaurant(likesChoice, resultAPIDetails.getPlaceId(), MainActivity.uid)
                    .addOnSuccessListener(aVoid -> restaurantChoiceLayout());
        }

    }

    // RESTAURANT CHOICE BUTTON
    private void buttonRestaurantChoiceResponse() {
        if (chosenRestaurantId != null) {
            if (chosenRestaurantId.equals("")) {
                // if no choice has been made
                saveRestaurantChoice();
                buttonRestaurantChoice.setImageResource(R.drawable.ic_baseline_check_circle_30);
                Toast.makeText(getContext(), getString(R.string.restaurant_Chosen, restaurantName.getText()), Toast.LENGTH_SHORT).show();
            } else if (chosenRestaurantId.equals(resultAPIDetails.getPlaceId())) {
                // if restaurant chosen = restaurant details
                alertRestaurantCancel();
            } else if (!chosenRestaurantId.equals(resultAPIDetails.getPlaceId())) {
                // if restaurant chosen != restaurant details
                alertRestaurantChange();

            }
        }
    }

    // Action on buttonRestaurantChoice click
    private void alertRestaurantCancel() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.details_fragment_alertdialog_restaurant_choice_cancellation_message)
                .setTitle(R.string.details_fragment_alertdialog_restaurant_choice_cancellation_title);

        AlertDialog dialogRestaurantChosen = builder.create();

        dialogRestaurantChosen.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.YES), (dialog1, which) -> {
            chosenRestaurantId = "";
            UserHelper.updateRestaurantChoice(chosenRestaurantId, chosenRestaurantId, chosenRestaurantId, uid).addOnSuccessListener(aVoid -> restaurantChoiceLayout());
        });

        dialogRestaurantChosen.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.NO), (dialog12, which) -> dialog12.dismiss());

        dialogRestaurantChosen.show();

    }

    // Action on buttonRestaurantChoice click -- if restaurant chosen != restaurant details
    private void alertRestaurantChange() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(requireContext().getString(R.string.details_fragment_alertdialog_restaurant_choice_change_message, resultAPIDetails.getName()))
                .setTitle(R.string.details_fragment_alertdialog_restaurant_choice_change_title);

        AlertDialog dialogRestaurantChosen = builder.create();

        dialogRestaurantChosen.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.YES), (dialog1, which) -> saveRestaurantChoice());

        dialogRestaurantChosen.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.NO), (dialog12, which) -> dialog12.dismiss());

        dialogRestaurantChosen.show();

    }

    private void saveRestaurantChoice() {
        chosenRestaurantId = resultAPIDetails.getPlaceId();
        UserHelper.updateRestaurantChoice(chosenRestaurantId, resultAPIDetails.getName(), resultAPIDetails.getFormatted_address(), uid).addOnSuccessListener(aVoid -> restaurantChoiceLayout());
        SharedPrefs.saveRestaurantId(requireContext(), chosenRestaurantId);
        SharedPrefs.saveRestaurantName(requireContext(), resultAPIDetails.getName());
        SharedPrefs.saveRestaurantAddress(requireContext(), resultAPIDetails.getFormatted_address());
    }


    // ---------------- HIDE TOOLBAR AND NAVIGATION BAR ------------------ //

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
