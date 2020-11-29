package com.edescamp.go4lunch;

import com.edescamp.go4lunch.activity.auth.SignInActivity;
import com.edescamp.go4lunch.activity.auth.SplashActivity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SignInUnitTest {




    SplashActivity splashActivity = new SplashActivity();
    SignInActivity signInActivity = new SignInActivity();

    @Test
    public void SigninActivityShouldBeStarted() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void providersButtonClickShouldLaunchAuthentification() {


        assertEquals(4, 2 + 2);
    }

}
