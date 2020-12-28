package com.edescamp.go4lunch.util;

import com.edescamp.go4lunch.activity.fragment.RestaurantsFragment;
import com.edescamp.go4lunch.model.map.ResultAPIMap;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SortRestaurantsUtil {


    public static void sortAZ(List<ResultAPIMap> nearbyPlacesResults) {
        Collections.sort(nearbyPlacesResults, (obj1, obj2) -> obj1.getName().compareToIgnoreCase(obj2.getName()));
    }

    public static void sortZA(List<ResultAPIMap> nearbyPlacesResults) {
        Collections.sort(nearbyPlacesResults, (obj1, obj2) -> obj2.getName().compareToIgnoreCase(obj1.getName()));
    }

    public static void sortRatingDesc(List<ResultAPIMap> nearbyPlacesResults) {
        Collections.sort(nearbyPlacesResults, (obj1, obj2) -> obj2.getRating().compareTo(obj1.getRating()));
    }

    public static void sortWorkmatesDesc(List<ResultAPIMap> nearbyPlacesResults) {
        Collections.sort(nearbyPlacesResults, (obj1, obj2) -> {
            if (RestaurantsFragment.workmatesCountHashMap.containsKey(obj1.getPlaceId())
                    && RestaurantsFragment.workmatesCountHashMap.containsKey(obj2.getPlaceId())) {
                return Objects.requireNonNull(RestaurantsFragment.workmatesCountHashMap.get(obj2.getPlaceId()))
                        .compareTo(Objects.requireNonNull(RestaurantsFragment.workmatesCountHashMap.get(obj1.getPlaceId())));
            }
            return 0;
        });
    }

    public static void sortDistanceAsc(List<ResultAPIMap> nearbyPlacesResults) {
        Collections.sort(nearbyPlacesResults, (obj1, obj2) -> {
            if (RestaurantsFragment.distanceHashMap.containsKey(obj1.getPlaceId())
                    && RestaurantsFragment.distanceHashMap.containsKey(obj2.getPlaceId())) {
                return Objects.requireNonNull(RestaurantsFragment.distanceHashMap.get(obj1.getPlaceId()))
                        .compareTo(Objects.requireNonNull(RestaurantsFragment.distanceHashMap.get(obj2.getPlaceId())));
            }
            return 0;
        });
    }
}
