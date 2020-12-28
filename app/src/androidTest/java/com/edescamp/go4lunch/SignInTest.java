package com.edescamp.go4lunch;

import android.net.ConnectivityManager;
import android.net.Network;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.edescamp.go4lunch.activity.SignInActivity;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class SignInTest {


    SignInActivity signInActivity;

    @Rule
    public ActivityScenarioRule<SignInActivity> activityScenarioRule
            = new ActivityScenarioRule<>(SignInActivity.class);


    Network network;

    @Test
    public void unconnectedDevice_shouldAlertUserIsNotConnected() {
        ConnectivityManager.setProcessDefaultNetwork(network);
        // => should not have a connection !

        // Todo Pass device in airplane mode for this Test !

        onView(withId(R.id.signin_no_connection_pull_to_refresh)).check(matches(isDisplayed()));
    }


    @Test
    public void connectedDevice_shouldAlertUserIsNotConnected() {

        onView(withId(R.id.signin_layout_main)).check(matches(isDisplayed()));
    }



}
