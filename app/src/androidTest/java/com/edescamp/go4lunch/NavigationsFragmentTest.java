package com.edescamp.go4lunch;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.edescamp.go4lunch.activity.SignInActivity;
import com.edescamp.go4lunch.util.SharedPrefs;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class NavigationsFragmentTest {

    @Rule
    public ActivityTestRule activityScenarioRule
            = new ActivityTestRule(SignInActivity.class, false, false);


    @Test
    public void clickOnRestaurantsMenu_shouldShowRestaurantsList() {

        SharedPrefs.saveUserId(InstrumentationRegistry.getInstrumentation().getTargetContext(), "userId");
        activityScenarioRule.launchActivity(null);

        // Open Restaurants
        onView(withId(R.id.navigation_listview))
                .perform(click());

        onView(withId(R.id.restaurants_recyclerview)).check(matches(isDisplayed()));

    }

    @Test
    public void clickOnWorkmatesMenu_shouldShowWorkmatesList() {

        SharedPrefs.saveUserId(InstrumentationRegistry.getInstrumentation().getTargetContext(), "userId");
        activityScenarioRule.launchActivity(null);

        // Open Workmates
        onView(withId(R.id.navigation_workmates))
                .perform(click());

        onView(withId(R.id.workmates_recyclerview)).check(matches(isDisplayed()));

    }

    @Test
    public void launchApp_withUserId_shouldShowMap() {

        SharedPrefs.saveUserId(InstrumentationRegistry.getInstrumentation().getTargetContext(), "userId");
        activityScenarioRule.launchActivity(null);

        onView(withId(R.id.fragment_map)).check(matches(isDisplayed()));

    }


    @Test
    public void launchApp_withoutUserId_shouldShowSignIn() {

        SharedPrefs.saveUserId(InstrumentationRegistry.getInstrumentation().getTargetContext(), null);
        activityScenarioRule.launchActivity(null);

        onView(withId(R.id.login_button_google)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnYourLunch_withoutRestaurantChosen_shouldShowDetailsRestaurant() {

        SharedPrefs.saveUserId(InstrumentationRegistry.getInstrumentation().getTargetContext(), "userId");
        activityScenarioRule.launchActivity(null);

        // Open Drawer
        onView(withId(R.id.activity_main_drawer_layout))
                .check(matches(isClosed())) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Your Lunch
        onView(withId(R.id.activity_main_drawer_lunch))
                .perform(click());

        // Check Toast is Displayed
        onView(withText(R.string.main_activity_lunch_no_restaurant_chosen))
                .inRoot(withDecorView(not(is(activityScenarioRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }


}
