package com.edescamp.go4lunch.activity.auth;

import android.content.Intent;
import android.os.Bundle;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.activity.BaseActivity;
import com.edescamp.go4lunch.activity.MainActivity;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Splash);
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = getCurrentUser();

        Intent intent;
        // TODO Debug login when userlogged
//        if (isCurrentUserLogged()) {
        if (currentUser!=null) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, SignInActivity.class);

        }
        startActivity(intent);
        finish();

    }
}


