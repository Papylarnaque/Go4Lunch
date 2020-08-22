package com.oc.go4lunch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.oc.go4lunch.R;
import com.oc.go4lunch.activity.auth.SignInActivity;
import com.oc.go4lunch.activity.fragment.MapFragment;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showFragment(new MapFragment());
        configureToolbar();
        configureDrawerLayout();
        configureNavigationMenu();

        getCurrentUser();
    }

    /**
     * This method configures the bottom navigation bar
     */
    void configureNavigationMenu() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.navigation_mapview:
                        mToolbar.setTitle(R.string.title_mapview);
                        fragment = new MapFragment();
                        showFragment(fragment);
                        break;
                    case R.id.navigation_listview:
                        mToolbar.setTitle(R.string.title_listview);
                        break;
                    case R.id.navigation_workmates:
                        mToolbar.setTitle(R.string.title_workmates);
                        break;
                }
                return true;
            }
        });
    }

    /**
     * This method configures the drawer layout needed for the drawer menu
     */
    private void configureDrawerLayout() {
        mDrawerLayout = findViewById(R.id.activity_main_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Use Navigation Callback listener as follows
        NavigationView navigationView = (NavigationView) findViewById(R.id.activity_main_drawer);
        navigationView.setNavigationItemSelectedListener(this);


//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.activity_main_drawer_lunch, R.id.activity_main_drawer_settings, R.id.activity_main_drawer_logout)
//                .setOpenableLayout(mDrawerLayout)
//                .build();

    }

    /**
     * This method manages the action when an item from the navigation drawer menu is clicked
     *
     * @param item is the item clicked
     * @return true
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            // "YOUR LUNCH"
            case R.id.activity_main_drawer_lunch:
                return true;
            // "SETTINGS"
            case R.id.activity_main_drawer_settings:
                return true;
            // "LOGOUT"
            case R.id.activity_main_drawer_logout:

//                UserManagementActivity mUserManagementActivity = new UserManagementActivity();
//                mUserManagementActivity.signOutUserFromFirebase();
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
                
                return true;
            case R.id.activity_main_drawer_logo:
                // test something like secret options ?
                setTheme(R.style.ThemeOverlay_AppCompat_Dark);
                return true;
            default:
                break;
        }
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * This method overrides the onBackPressed method to change the behavior of the Back button
     */
    @Override
    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    /**
     * This method configures the toolbar
     */
    private void configureToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }


    /**
     * This methods loads the specified fragment
     *
     * @param fragment is the fragment to load
     */
    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


//    // Request localisation
//    private void getLocationPermission() {
//        /*
//         * Request location permission, so that we can get the location of the
//         * device. The result of the permission request is handled by a callback,
//         * onRequestPermissionsResult.
//         */
//        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
//                android.Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            mLocationPermissionGranted = true;
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//        }
//    }


//    // Initialize the AutocompleteSupportFragment.
//    AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
//            getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//
//    // Specify the types of place data to return.
//    autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
//
//    // Set up a PlaceSelectionListener to handle the response.
//    autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//        @Override
//        public void onPlaceSelected(@NotNull Place place) {
//            // TODO: Get info about the selected place.
//            Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
//        }
//
//
//        @Override
//        public void onError(@NotNull Status status) {
//            // TODO: Handle the error.
//            Log.i(TAG, "An error occurred: " + status);
//        }
//    });

}
