package com.edescamp.go4lunch.util;

import com.edescamp.go4lunch.model.map.ResultAPIMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SortRestaurantsUtil {
    
    // Data
    public static final HashMap<String, Integer> workmatesCountHashMap = new HashMap<>();
    public static final HashMap<String, Integer> distanceHashMap = new HashMap<>();

    public static List<ResultAPIMap> sortAZ(List<ResultAPIMap> nearbyPlacesResults) {
        List<ResultAPIMap> sortedList = new ArrayList<>(nearbyPlacesResults);
        Collections.sort(sortedList, (obj1, obj2) -> obj1.getName().compareToIgnoreCase(obj2.getName()));
        return sortedList;
    }

    public static List<ResultAPIMap> sortZA(List<ResultAPIMap> nearbyPlacesResults) {
        List<ResultAPIMap> sortedList = new ArrayList<>(nearbyPlacesResults);
        Collections.sort(sortedList, (obj1, obj2) -> obj2.getName().compareToIgnoreCase(obj1.getName()));
        return sortedList;
    }

    public static List<ResultAPIMap> sortRatingDesc(List<ResultAPIMap> nearbyPlacesResults) {
        List<ResultAPIMap> sortedList = new ArrayList<>(nearbyPlacesResults);
        Collections.sort(sortedList, (obj1, obj2) ->
                obj2.getRating().compareTo(obj1.getRating()));

        return sortedList;
    }

    public static List<ResultAPIMap> sortWorkmatesDesc(List<ResultAPIMap> nearbyPlacesResults) {
        List<ResultAPIMap> sortedList = new ArrayList<>(nearbyPlacesResults);
        Collections.sort(sortedList, (obj1, obj2) -> {
            if (workmatesCountHashMap.containsKey(obj1.getPlaceId())
                    && workmatesCountHashMap.containsKey(obj2.getPlaceId())) {
                return Objects.requireNonNull(workmatesCountHashMap.get(obj2.getPlaceId()))
                        .compareTo(Objects.requireNonNull(workmatesCountHashMap.get(obj1.getPlaceId())));
            }
            return 0;
        });
        return sortedList;
    }

    public static List<ResultAPIMap> sortDistanceAsc(List<ResultAPIMap> nearbyPlacesResults) {
        List<ResultAPIMap> sortedList = new ArrayList<>(nearbyPlacesResults);
        Collections.sort(sortedList, (obj1, obj2) -> {
            if (distanceHashMap.containsKey(obj1.getPlaceId())
                    && distanceHashMap.containsKey(obj2.getPlaceId())) {
                return Objects.requireNonNull(distanceHashMap.get(obj1.getPlaceId()))
                        .compareTo(Objects.requireNonNull(distanceHashMap.get(obj2.getPlaceId())));
            }
            return 0;
        });
        return sortedList;
    }
}
