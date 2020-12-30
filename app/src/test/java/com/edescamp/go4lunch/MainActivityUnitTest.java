//package com.edescamp.go4lunch;
//
//import android.content.Context;
//
//import androidx.appcompat.app.ActionBar;
//
//import com.edescamp.go4lunch.activity.MainActivity;
//import com.edescamp.go4lunch.activity.fragment.MapFragment;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.JUnit4;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//@RunWith(JUnit4.class)
//public class  MainActivityUnitTest {
//
//    MainActivity activity;
//
//    @Mock
//    private Context context;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        activity = new MainActivity();
//    }
//
//    @Test
//    public void testActionBarIsShowing() {
//        ActionBar supportActionBar = activity.getSupportActionBar();
//
//        assert supportActionBar != null;
//        Assert.assertTrue(supportActionBar.isShowing());
//    }
//
//    @Test
//    public void testNavBarIsShowing() {
//        activity.showFragment(new MapFragment());
//
////        Assert.assertTrue(activity.getSupportFragmentManager().);
//    }
//
//
//}
