package com.edescamp.go4lunch.util;


import android.util.Log;

import com.edescamp.go4lunch.model.apidetails.ResultAPIDetails;
import com.edescamp.go4lunch.model.apimap.ResultAPIMap;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RestaurantHelper {

    private static final String TAG = "RestaurantHelper";
    private static final String COLLECTION_NAME = "restaurant";

    // --- COLLECTION REFERENCE ---
    public static CollectionReference getRestaurantsCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }


    public static Task<Void> createRestaurant(ResultAPIMap result) {
        Map<String, Object> restaurantToCreate = new HashMap<>();
        restaurantToCreate.put("rid", result.getPlaceId());
        restaurantToCreate.put("name", result.getName());
        if (result.getPhotos() != null) {
            restaurantToCreate.put("urlPicture", result.getPhotos().get(0).getPhoto_URL());
        }
        restaurantToCreate.put("address", result.getVicinity());
        restaurantToCreate.put("rating", result.getRating());
        Log.i(TAG, "restaurantId: " + result.getPlaceId() + " restaurantName: " + result.getName());
        return RestaurantHelper.getRestaurantsCollection().document(result.getPlaceId()).set(restaurantToCreate);
    }

    public static Task<Void> setRestaurantDetails(String placeId, ResultAPIDetails result) {
        Map<String, Object> restaurantToUpdate = new HashMap<>();
        restaurantToUpdate.put("website", result.getWebsite());
        restaurantToUpdate.put("phone", result.getInternational_phone_number());
        restaurantToUpdate.put("opening_hours", result.getOpening_hours());
        return RestaurantHelper.getRestaurantsCollection().document(placeId).update(restaurantToUpdate);
    }


    // --- GET ---
    public static Task<DocumentSnapshot> getRestaurant(String placeId) {
        return RestaurantHelper.getRestaurantsCollection().document(placeId).get();
    }

    // --- GET ---
    public static Task<QuerySnapshot> getAllRestaurants() {
        return RestaurantHelper.getRestaurantsCollection().get();
    }

    // --- UPDATE ---
    public static Task<Void> updateRestaurantHours(String placeId, OpeningHours opening_hours) {
        return RestaurantHelper.getRestaurantsCollection().document(placeId).update("opening_hours", opening_hours);
    }


    // --- DELETE ---
    public static Task<Void> deleteRestaurant(String uid) {
        return RestaurantHelper.getRestaurantsCollection().document(uid).delete();
    }


}

