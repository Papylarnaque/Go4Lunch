package com.edescamp.go4lunch.util;


import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UserHelper {

    private static final String TAG = "UserHelper";
    private static final String COLLECTION_NAME = "user";

    // --- COLLECTION REFERENCE ---
    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---
    public static Task<Void> createUser(String userId, String userName, String userUrlPicture, String userMail) {
        Map<String, Object> userToCreate = new HashMap<>();
        userToCreate.put("uid", userId);
        userToCreate.put("username", userName);
        userToCreate.put("urlPicture", userUrlPicture);
        userToCreate.put("mail", userMail);
        userToCreate.put("hasChosenRestaurant", "");
        userToCreate.put("chosenRestaurantName", "");
        Log.i(TAG, "userId: "+userId + " username: "+userName);
        return UserHelper.getUsersCollection().document(userId).set(userToCreate);
    }

    // --- GET ---
    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    public static Task<QuerySnapshot> getAllUsers(){
        return UserHelper.getUsersCollection().get();
    }

    public static Task<QuerySnapshot> getAllUsersExceptCurrent(String userId){
        return UserHelper.getUsersCollection()
                .whereNotEqualTo("uid", userId)
                .get();
    }

    // --- UPDATE ---
    public static Task<Void> updateUsername(String username, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("username", username);
    }

    public static Task<Void> updateRestaurantChoice(String placeId, String restaurantName, String uid) {
        Map<String, Object> userToUpdateWithRestaurantChoice = new HashMap<>();
        userToUpdateWithRestaurantChoice.put("hasChosenRestaurant", placeId);
        userToUpdateWithRestaurantChoice.put("chosenRestaurantName", restaurantName);
        return UserHelper.getUsersCollection().document(uid).update(userToUpdateWithRestaurantChoice);
    }


    // --- DELETE ---
    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }



}
