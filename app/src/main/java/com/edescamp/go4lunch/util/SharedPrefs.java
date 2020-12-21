package com.edescamp.go4lunch.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefs {

    public final static String PREFS_NAME = "PREFS";
    static SharedPreferences settings;

    // ------- SAVE -------- //

    public static void saveUserId(Context context, String userId){
        settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("USER", userId);
        editor.apply();
    }

    public static void saveRestaurantId(Context context, String restaurantId){
        settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("ChosenRestaurantId", restaurantId);
        editor.apply();
    }

    public static void saveRestaurantName(Context context, String restaurantName){
        settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("ChosenRestaurantName", restaurantName);
        editor.apply();
    }

    public static void saveNotifications(Context context, Boolean notifications){
        settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("NOTIFICATIONS", notifications);
        editor.apply();
    }

    // ------ GET ----- //

    public static String getUserId(Context context){
        settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return settings.getString("USER", null);
    }

    public static String getRestaurantId(Context context){
        settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return settings.getString("ChosenRestaurantId", null);
    }

    public static String getRestaurantName(Context context){
        settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return settings.getString("ChosenRestaurantName", null);
    }

    public static Boolean getNotifications(Context context){
        settings = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return settings.getBoolean("NOTIFICATIONS", true);
    }

}
