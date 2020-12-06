package com.edescamp.go4lunch;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.edescamp.go4lunch.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void clickOnBurgerMenu_shouldOpenDrawerMenu() {
        // Open Drawer
        onView(withId(R.id.activity_main_drawer_layout))
                .check(matches(isClosed())) // Left Drawer should be closed.
                .perform(DrawerActions.open()) // Open Drawer
                .check(matches(isOpen()));
    }

    @Test
    public void backPress_shouldCloseDrawerMenu() {
        // Open Drawer
        onView(withId(R.id.activity_main_drawer_layout))
                .check(matches(isClosed())) // Left Drawer should be closed.
                .perform(DrawerActions.open()) // Open Drawer
                .check(matches(isOpen()));

        // then close it
        Espresso.pressBack();

        // Check Drawer is closed
        onView(withId(R.id.activity_main_drawer_layout))
                .check(matches(isClosed()));
    }

    @Test
    public void clickOnNavigationView_shouldOpenFragments() {
        onView(withId(R.id.navigation_mapview))
                .perform(click());
        onView(withId(R.id.fragment_map)).check(matches(isDisplayed()));

        onView(withId(R.id.navigation_listview))
                .perform(click());
        onView(withId(R.id.fragment_restaurant_list)).check(matches(isDisplayed()));

        onView(withId(R.id.navigation_workmates))
                .perform(click());
        onView(withId(R.id.fragment_workmates)).check(matches(isDisplayed()));
    }

}
