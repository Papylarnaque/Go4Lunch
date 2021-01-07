package com.edescamp.go4lunch;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.edescamp.go4lunch.activity.SignInActivity;
import com.edescamp.go4lunch.util.SharedPrefs;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class NavigationsFragmentTest {

    @Rule
    public ActivityTestRule activityScenarioRule
            = new ActivityTestRule(SignInActivity.class, false, false);


    @Test
    public void clickOnFavorite_shouldShowRestaurantsList() {

        SharedPrefs.saveUserId(InstrumentationRegistry.getInstrumentation().getTargetContext(), "testId");
        activityScenarioRule.launchActivity(null);

        // Open Restaurants
        onView(withId(R.id.navigation_listview))
                .perform(click());

        onView(withId(R.id.restaurants_recyclerview)).check(matches(isDisplayed()));

    }

    @Test
    public void clickOnFavorite_shouldShowWorkmatesList() {

        SharedPrefs.saveUserId(InstrumentationRegistry.getInstrumentation().getTargetContext(), "testId");
        activityScenarioRule.launchActivity(null);

        // Open Workmates
        onView(withId(R.id.navigation_workmates))
                .perform(click());

        onView(withId(R.id.workmates_recyclerview)).check(matches(isDisplayed()));

    }

    @Test
    public void clickOnFavorite_shouldShowMap() {

        SharedPrefs.saveUserId(InstrumentationRegistry.getInstrumentation().getTargetContext(), "testId");
        activityScenarioRule.launchActivity(null);

        onView(withId(R.id.fragment_map)).check(matches(isDisplayed()));

    }


    @Test
    public void clickOnFavorite_shouldShowSignIn() {

        SharedPrefs.saveUserId(InstrumentationRegistry.getInstrumentation().getTargetContext(), null);
        activityScenarioRule.launchActivity(null);

        onView(withId(R.id.login_button_google)).check(matches(isDisplayed()));

    }




}
