package com.edescamp.go4lunch;

import androidx.appcompat.app.ActionBar;

import com.edescamp.go4lunch.activity.MainActivity;
import com.edescamp.go4lunch.activity.fragment.MapFragment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MapUnitTest {

    MainActivity activity;
    MapFragment mapFragment;

    @Before
    public void setUp() {
        activity = new MainActivity();
        mapFragment = new MapFragment();
        activity.showFragment(mapFragment);
    }

    @Test
    public void testActionBarIsShowing() {
        ActionBar supportActionBar = activity.getSupportActionBar();

        assert supportActionBar != null;
        Assert.assertTrue(supportActionBar.isShowing());
    }


    @Test
    public void testActionBarIsShowing() {
        ActionBar supportActionBar = activity.getSupportActionBar();

        assert supportActionBar != null;
        Assert.assertTrue(supportActionBar.isShowing());
    }


}
