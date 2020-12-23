package com.edescamp.go4lunch.util;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.activity.fragment.DetailsFragment;
import com.edescamp.go4lunch.model.details.ResultAPIDetails;
import com.edescamp.go4lunch.service.PlaceDetailsService;

import static com.edescamp.go4lunch.service.PlaceDetailsService.placeDetailsResultHashmap;

public class DetailsUtil {

    public static void openDetailsFragment(FragmentActivity activity, ResultAPIDetails result) {
        Fragment fragment = new DetailsFragment(result);
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public static void openDetailsFragmentOrCallApiThenOpenDetailsFragment(FragmentActivity activity, String placeId) {
        if (placeDetailsResultHashmap.containsKey(placeId)) {
            openDetailsFragment(
                    activity,
                    placeDetailsResultHashmap.get(placeId));
        } else {
            PlaceDetailsService.getPlaceDetailsAndOpenDetailsFragment(placeId, activity);
        }
    }

}
