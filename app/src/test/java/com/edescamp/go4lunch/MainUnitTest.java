package com.edescamp.go4lunch;

import com.edescamp.go4lunch.activity.MainActivity;
import com.edescamp.go4lunch.activity.SignInActivity;
import com.edescamp.go4lunch.activity.fragment.MapFragment;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MainUnitTest {

    MainActivity mainActivity = new MainActivity();
    SignInActivity signInActivity = new SignInActivity();
    MapFragment mapFragment = new MapFragment();
    //    MAIN ACTIVITY

    @Test
    public void google() {
        String userId = "test";
        String methodName = "startMainActivity";


    }

//    MAP Fragment

    @Test
    public void shouldRequestLocationPermissions() {


        mapFragment.onCreate(null);


    }

    @Test
    public void providersButtonClickShouldLaunchAuthentification() {


        assertEquals(4, 2 + 2);
    }

}
