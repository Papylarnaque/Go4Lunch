package com.edescamp.go4lunch.activity.auth;

import android.content.Intent;
import android.os.Bundle;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.activity.BaseActivity;
import com.edescamp.go4lunch.activity.MainActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Splash);
        super.onCreate(savedInstanceState);

        getCurrentUser();

        Intent intent;
        if (isCurrentUserLogged()) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
//            signOutUserFromFirebase(); // TODO = line put temporarily to fix the Facebook auth issue - TOFIX ! Explanation : On startup, the Firebase User is not detected, but Facebook appears as logged
            intent = new Intent(SplashActivity.this, SignInActivity.class);

        }
        startActivity(intent);
        finish();

    }
}


