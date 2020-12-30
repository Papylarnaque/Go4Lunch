package com.edescamp.go4lunch;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.edescamp.go4lunch.activity.SignInActivity;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class DetailsFragmentTest {

    @Rule
    public ActivityScenarioRule<SignInActivity> activityScenarioRule
            = new ActivityScenarioRule<>(SignInActivity.class);

    @Test
    public void clickOnFavorite_shouldAddRestaurantToFavorites() {
        connect();

        onView(withId(R.id.navbar)).check(matches(isDisplayed()));

        // Open Restaurants
        onView(withId(R.id.navigation_listview))
                .perform(click());
        onView(withId(R.id.fragment_restaurant_list)).check(matches(isDisplayed()));

        // Open Random DetailsFragment
        onView(withId(R.id.restaurants_recyclerview)).perform(click());

        onView(withId(R.id.fragment_restaurant_details)).check(matches(isDisplayed()));


        disconnect();
    }

    private void connect() {
        onView(withId(R.id.login_button_google))
                .perform(click());
    }

    private void disconnect() {
        // Open Drawer
        onView(withId(R.id.activity_main_drawer_layout))
                .check(matches(isClosed())) // Left Drawer should be closed.
                .perform(DrawerActions.open()) // Open Drawer
                .check(matches(isOpen()));

        onView(withId(R.id.activity_main_drawer_logout)).perform(click());

    }


}
