package com.edescamp.go4lunch.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.edescamp.go4lunch.R;
import com.google.android.material.slider.Slider;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.edescamp.go4lunch.activity.MainActivity.RADIUS_MAX;
import static com.edescamp.go4lunch.activity.MainActivity.radius;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";

    private TextView title;
    private TextView radiusSearch;
    private Slider radiusSlider;
    private ImageButton backPress;

    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        configureView(view);

        radiusSlider.addOnChangeListener((slider, value, fromUser) -> {
            radius = (int) value;
            radiusSearch.setText(getString(R.string.fragment_settings_radius_search, radius));
        });

        backPress.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }

    private void configureView(View view) {
        title = view.findViewById(R.id.fragment_settings_title);
        radiusSearch = view.findViewById(R.id.fragment_settings_radius_text);
        backPress = view.findViewById(R.id.fragment_settings_backpress);

        title.setText(R.string.fragment_settings_title);
        radiusSearch.setText(getString(R.string.fragment_settings_radius_search, radius));

        radiusSlider = view.findViewById(R.id.fragment_settings_radius_slider);
        radiusSlider.setValue(radius);
        radiusSlider.setValueTo(RADIUS_MAX);
        radiusSlider.setLabelFormatter(value -> String.valueOf(radius));
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
