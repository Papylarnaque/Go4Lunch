package com.edescamp.go4lunch.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.util.UserHelper;
import com.google.android.material.slider.Slider;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.edescamp.go4lunch.activity.MainActivity.RADIUS_MAX;
import static com.edescamp.go4lunch.activity.MainActivity.RADIUS_MIN;
import static com.edescamp.go4lunch.activity.MainActivity.RADIUS_STEP;
import static com.edescamp.go4lunch.activity.MainActivity.RADIUS_INIT;
import static com.edescamp.go4lunch.activity.MainActivity.uid;
import static com.edescamp.go4lunch.activity.MainActivity.usernameString;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";

    private TextView radiusSearch;
    private Slider radiusSlider;
    private ImageButton backPress;
    private EditText username;
    private Button validateUsername;
    private TextView usernameDrawer;

    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        configureView(view);

        usernameDrawer = view.findViewById(R.id.drawer_user_name);
        username.setText(usernameString);

        validateUsername = view.findViewById(R.id.fragment_settings_validate_username);

        validateUsername.setOnClickListener(v -> {
            Log.i(TAG, "Before username Update request for userId : " + uid + " username was : " + usernameString );
            String usernameString = username.getText().toString();
            Log.i(TAG, "After username Update request for userId : " + uid + " username is : " + usernameString );

            UserHelper.updateUsername(usernameString, uid);
            //Post it in a handler to make sure it gets called if coming back from a lifecycle method.
            new Handler().post(() -> {
                Intent intent = requireActivity().getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                requireActivity().overridePendingTransition(0, 0);
                requireActivity().finish();

                requireActivity().overridePendingTransition(0, 0);
                startActivity(intent);
            });
        });


        radiusSliderListener();
        backPressHandler();


        return view;
    }




    private void radiusSliderListener() {
        radiusSlider.addOnChangeListener((slider, value, fromUser) -> {
            RADIUS_INIT = (int) value;
            radiusSearch.setText(getString(R.string.fragment_settings_radius_search, RADIUS_INIT));
        });
    }

    private void backPressHandler() {
        backPress.setOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void configureView(View view) {
        TextView title = view.findViewById(R.id.fragment_settings_title);
        radiusSearch = view.findViewById(R.id.fragment_settings_radius_text);
        backPress = view.findViewById(R.id.fragment_settings_backpress);
        username = view.findViewById(R.id.fragment_settings_update_username_textbox);

        title.setText(R.string.fragment_settings_title);
        radiusSearch.setText(getString(R.string.fragment_settings_radius_search, RADIUS_INIT));

        radiusSlider = view.findViewById(R.id.fragment_settings_radius_slider);
        radiusSlider.setValue(RADIUS_INIT);
        radiusSlider.setValueTo(RADIUS_MAX);
        radiusSlider.setValueFrom(RADIUS_MIN);
        radiusSlider.setLabelFormatter(value -> String.valueOf(RADIUS_INIT));
        radiusSlider.setStepSize(RADIUS_STEP);
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
