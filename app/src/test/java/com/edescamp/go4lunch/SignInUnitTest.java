package com.edescamp.go4lunch;

import com.edescamp.go4lunch.activity.SignInActivity;
import com.edescamp.go4lunch.activity.SplashActivity;

import org.junit.Test;

import static android.app.Activity.RESULT_OK;

public class SignInUnitTest {

    SplashActivity splashActivity = new SplashActivity();
    SignInActivity signInActivity = new SignInActivity();

    @Test
    public void googleActivityResult() {
        int RC_SIGN_IN = 3;


        signInActivity.onActivityResult(RC_SIGN_IN, RESULT_OK, );

    }

}
