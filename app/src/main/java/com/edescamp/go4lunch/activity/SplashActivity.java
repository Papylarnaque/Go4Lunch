package com.edescamp.go4lunch.activity;

import android.content.Intent;
import android.os.Bundle;

import com.edescamp.go4lunch.R;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Splash);
        super.onCreate(savedInstanceState);

        Intent intent;
        // TODO Debug login when userlogged
        if (isCurrentUserLogged()) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, SignInActivity.class);
        }


        startActivity(intent);
        finish();

    }

}


