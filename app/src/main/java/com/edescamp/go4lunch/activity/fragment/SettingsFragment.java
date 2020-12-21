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
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.util.NotificationHelper;
import com.edescamp.go4lunch.util.SharedPrefs;
import com.edescamp.go4lunch.util.UserHelper;
import com.google.android.material.slider.Slider;

import java.util.Objects;

import static com.edescamp.go4lunch.activity.MainActivity.RADIUS_INIT;
import static com.edescamp.go4lunch.activity.MainActivity.RADIUS_MAX;
import static com.edescamp.go4lunch.activity.MainActivity.RADIUS_MIN;
import static com.edescamp.go4lunch.activity.MainActivity.RADIUS_STEP;
import static com.edescamp.go4lunch.activity.MainActivity.uid;
import static com.edescamp.go4lunch.activity.MainActivity.usernameString;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";

    private TextView textRadius;
    private Slider sliderRadius;
    private ImageButton buttonBackPress;
    private EditText textUsername;
    private Button buttonUsername;
    private SwitchCompat switchNotifications;
    private SwitchCompat switchDarkMode;

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
        buttonUsername.setOnClickListener(v -> {
            Log.i(TAG, "Before username Update request for userId : " + uid + " username was : " + usernameString);
            String usernameString = textUsername.getText().toString();
            Log.i(TAG, "After username Update request for userId : " + uid + " username is : " + usernameString);

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

        //Set a CheckedChange Listener for Switch Button
        switchNotifications.setOnCheckedChangeListener((cb, on) -> {
                NotificationHelper.setAlarmForNotifications(view.getContext(), on);

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
        textUsername = view.findViewById(R.id.fragment_settings_update_username_textbox);
        buttonUsername = view.findViewById(R.id.fragment_settings_validate_username);
        switchNotifications = view.findViewById(R.id.fragment_settings_update_notifications_switch);
        switchDarkMode = view.findViewById(R.id.fragment_settings_update_dark_mode_switch);
        sliderRadius = view.findViewById(R.id.fragment_settings_radius_slider);

        textUsername.setText(usernameString);
        title.setText(R.string.fragment_settings_title);
        textRadius.setText(getString(R.string.fragment_settings_radius_search, RADIUS_INIT));


        sliderRadius.setValue(RADIUS_INIT);
        sliderRadius.setValueTo(RADIUS_MAX);
        sliderRadius.setValueFrom(RADIUS_MIN);
        sliderRadius.setLabelFormatter(value -> String.valueOf(RADIUS_INIT));
        sliderRadius.setStepSize(RADIUS_STEP);

        switchDarkMode.setVisibility(View.GONE);

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
