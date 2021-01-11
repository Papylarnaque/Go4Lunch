package com.edescamp.go4lunch.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.util.NotificationHelper;
import com.edescamp.go4lunch.util.SharedPrefs;
import com.google.android.material.slider.Slider;

import java.util.Objects;

import static com.edescamp.go4lunch.activity.MainActivity.RADIUS_INIT;
import static com.edescamp.go4lunch.activity.MainActivity.RADIUS_MAX;

public class SettingsFragment extends Fragment {

    private TextView textRadius;
    private Slider sliderRadius;
    private ImageButton buttonBackPress;
    private SwitchCompat switchNotifications;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        configureView(view);
        setClickHandler(view);

        radiusSliderListener();
        backPressHandler();

        return view;
    }

    private void setClickHandler(View view) {

        //Set a CheckedChange Listener for Switch Button
        switchNotifications.setOnCheckedChangeListener((cb, on) -> {
            NotificationHelper.setAlarmForNotifications(view.getContext(), on);
            SharedPrefs.saveNotifications(view.getContext(), on);

        });


    }


    private void radiusSliderListener() {
        sliderRadius.addOnChangeListener((slider, value, fromUser) -> {
            RADIUS_INIT = (int) value;
            textRadius.setText(getString(R.string.fragment_settings_radius_search, RADIUS_INIT));
        });
    }

    private void backPressHandler() {
        buttonBackPress.setOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void configureView(View view) {
        TextView title = view.findViewById(R.id.fragment_settings_title);
        textRadius = view.findViewById(R.id.fragment_settings_radius_text);
        buttonBackPress = view.findViewById(R.id.fragment_settings_backpress);
        switchNotifications = view.findViewById(R.id.fragment_settings_update_notifications_switch);
        sliderRadius = view.findViewById(R.id.fragment_settings_radius_slider);


        title.setText(R.string.fragment_settings_title);
        textRadius.setText(getString(R.string.fragment_settings_radius_search, RADIUS_INIT));


        sliderRadius.setValue(RADIUS_INIT);
        sliderRadius.setValueTo(RADIUS_MAX);
        // MAX Radius distance in meters
        int RADIUS_MIN = 1000;
        sliderRadius.setValueFrom(RADIUS_MIN);
        sliderRadius.setLabelFormatter(value -> String.valueOf(RADIUS_INIT));
        // STEP Radius for slider
        int RADIUS_STEP = 500;
        sliderRadius.setStepSize(RADIUS_STEP);

        switchNotifications.setChecked(SharedPrefs.getNotifications(requireContext()));

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
