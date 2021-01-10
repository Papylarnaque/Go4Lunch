package com.edescamp.go4lunch.util;

import android.view.View;
import android.widget.ImageView;

public class RatingUtil {

    public static void showRating(Float rating, ImageView star1, ImageView star2, ImageView star3) {
        double RATING_MAX = 4.5;
        double RATING_MIDDLE = 2.5;
        double RATING_MIN = 1;

        if (rating == null) {
        } else if (rating >= RATING_MAX) {
            star1.setVisibility(View.VISIBLE);
            star2.setVisibility(View.VISIBLE);
            star3.setVisibility(View.VISIBLE);
        } else if (rating >= RATING_MIDDLE && rating < RATING_MAX) {
            star1.setVisibility(View.VISIBLE);
            star2.setVisibility(View.VISIBLE);
            star3.setVisibility(View.INVISIBLE);
        } else if (rating >= RATING_MIN && rating < RATING_MIDDLE) {
            star1.setVisibility(View.VISIBLE);
            star2.setVisibility(View.INVISIBLE);
            star3.setVisibility(View.INVISIBLE);
        } else {
            star1.setVisibility(View.INVISIBLE);
            star2.setVisibility(View.INVISIBLE);
            star3.setVisibility(View.INVISIBLE);
        }
    }
}
