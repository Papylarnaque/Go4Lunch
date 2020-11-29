package com.edescamp.go4lunch.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.activity.auth.SignInActivity;
import com.edescamp.go4lunch.activity.fragment.BaseFragment;
import com.edescamp.go4lunch.activity.fragment.MapFragment;
import com.edescamp.go4lunch.activity.fragment.RestaurantListFragment;
import com.edescamp.go4lunch.activity.fragment.SettingsFragment;
import com.edescamp.go4lunch.activity.fragment.WorkmatesListFragment;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MAP_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int RESTAURANT_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;
    private static final String TAG = "MAIN_ACTIVITY";
    public static final int RADIUS_MAX = 5000; // MAX Radius distance in meters
    public static final int RADIUS_STEP = 500; // STEP Radius for slider


    public Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;

    // API request parameters
    public static int radius = 500; // radius in meters around user for search
    public static final String language = "en";
    public static final String keyword = "restaurant";
    public static final String FIELDS = "formatted_address,geometry,photos,place_id,name,rating,opening_hours,website,international_phone_number";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureToolbar();
        configureDrawerLayout();
        configureNavigationMenu();
        configureInitialState();

//        getCurrentUser();

    }

    //----- INITIAL STATE -----
    private void configureInitialState() {
        Fragment fragment = new MapFragment();
        mToolbar.setTitle(R.string.title_mapview);
        showFragment(fragment);
    }

    // BOTTOM NAVIGATION configuration //
    void configureNavigationMenu() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            final int navigation_mapview_id = R.id.navigation_mapview;
            final int navigation_listview_id = R.id.navigation_listview;
            final int navigation_workmates_id = R.id.navigation_workmates;

            switch (item.getItemId()) {
                case navigation_mapview_id:
                    mToolbar.setTitle(R.string.title_mapview);
                    fragment = new MapFragment();
                    showFragment(fragment);
                    break;

                case navigation_listview_id:
                    mToolbar.setTitle(R.string.title_listview);
                    fragment = new RestaurantListFragment();
                    showFragment(fragment);
                    break;

                case navigation_workmates_id:
                    fragment = new WorkmatesListFragment();
                    showFragment(fragment);
                    mToolbar.setTitle(R.string.title_workmates);
                    break;
            }
            return true;
        });
    }

    // DRAWER configuration //
    private void configureDrawerLayout() {
        mDrawerLayout = findViewById(R.id.activity_main_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Use Navigation Callback listener as follows
        NavigationView navigationView = findViewById(R.id.activity_main_drawer);
        navigationView.setNavigationItemSelectedListener(this);

    }

    // DRAWER navigation  //
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        final int main_drawer_lunch_id = R.id.activity_main_drawer_lunch;
        final int main_drawer_settings_id = R.id.activity_main_drawer_settings;
        final int main_drawer_logout_id = R.id.activity_main_drawer_logout;
        final int main_drawer_logo_id = R.id.activity_main_drawer_logo;
        switch (id) {
            // "YOUR LUNCH"
            case main_drawer_lunch_id:
                // TODO Manage YOUR LUNCH
                Toast toast = Toast.makeText(getApplicationContext(), "Manage YOUR LUNCH", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return true;
            // "SETTINGS"
            case main_drawer_settings_id:
                this.mDrawerLayout.closeDrawer(GravityCompat.START);
                Fragment fragment = new SettingsFragment();
                showFragmentWithBackStack(fragment);
                return true;
            // "LOGOUT"
            case main_drawer_logout_id:
                logoutToSignInActivity();
                return true;
            //  LOGO
            case main_drawer_logo_id:
                return true;
            default:
                break;
        }
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // TOOLBAR configuration
    private void configureToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }


    // Manage the click on search button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.main_activity_menu_search) {
            // TODO Manage AUTOCOMPLETE SEARCH
            Toast toast = Toast.makeText(getApplicationContext(), "Manage AUTOCOMPLETE SEARCH", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    // Log out the User //
    private void logoutToSignInActivity() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intent);
    }


    // DRAWER closed with back button
    @Override
    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        }

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (currentFragment instanceof BaseFragment) {
            // do nothing
        } else {
            super.onBackPressed();
        }
    }

    // FRAGMENT MANAGEMENT //
    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }


    private void showFragmentWithBackStack(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == MAP_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // Received permission result for location permission.
            Log.i(TAG, "Received response for LOCATION permission request from MapFragment.");

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission has been granted, preview can be displayed
                Log.i(TAG, "LOCATION permission has now been granted. Showing preview.");
                showFragment(new MapFragment());
            } else {
                // Location permission has been granted, preview can be displayed
                Log.i(TAG, "LOCATION permission was NOT granted.");
                Toast.makeText(getApplicationContext(), R.string.permissions_not_granted, Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == RESTAURANT_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // Received permission result for location permission.
            Log.i(TAG, "Received response for LOCATION permission request from RestaurantFragment.");

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission has been granted, preview can be displayed
                Log.i(TAG, "LOCATION permission has now been granted. Showing preview.");
                showFragment(new RestaurantListFragment());
            } else {
                // Location permission has been granted, preview can be displayed
                Log.i(TAG, "LOCATION permission was NOT granted.");
                Toast.makeText(getApplicationContext(), R.string.permissions_not_granted, Toast.LENGTH_SHORT).show();

            }

//        } else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

