package com.edescamp.go4lunch.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.activity.auth.SignInActivity;
import com.edescamp.go4lunch.activity.fragment.MapFragment;
import com.edescamp.go4lunch.activity.fragment.RestaurantFragment;
import com.edescamp.go4lunch.activity.fragment.WorkmatesFragment;
import com.facebook.login.LoginManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.List;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final int AUTOCOMPLETE_REQUEST_CODE = 111;
    public AutoCompleteTextView autoCompleteTextViewPlace;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    // Use fields to define the data types to return.
    private List<Place.Field> placeFields = Collections.singletonList(Place.Field.NAME);
    // Use the builder to create a FindCurrentPlaceRequest.
    private FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Places.initialize(getApplicationContext(), getString(R.string.google_api_key));
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

//        autoCompleteTextViewPlace = findViewById(R.id.autoCompleteTextViewPlace);
//        autoCompleteTextViewPlace.setVisibility(View.INVISIBLE);

        configureInitialState();
        configureToolbar();
        configureDrawerLayout();
        configureNavigationMenu();

        getCurrentUser();
        getLocationPermission();
    }




    //----- INITIAL STATE -----

    private void configureInitialState(){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if(fragment == null) {
            fragment = (MapFragment) new MapFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }

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
                    fragment = new RestaurantFragment();
                    showFragment(fragment);
                    break;

                case navigation_workmates_id:
                    fragment = new WorkmatesFragment();
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
                Toast toast = Toast.makeText(getApplicationContext(),"Manage YOUR LUNCH",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return true;
            // "SETTINGS"
            case main_drawer_settings_id:
                // TODO Manage SETTINGS
                Toast toastSettings = Toast.makeText(getApplicationContext(),"Manage SETTINGS",Toast.LENGTH_LONG);
                toastSettings.setGravity(Gravity.CENTER, 0, 0);
                toastSettings.show();
                return true;
            // "LOGOUT"
            case main_drawer_logout_id:
                logoutToSignInActivity();

                return true;
            case main_drawer_logo_id:
                // test something like secret options ?
                setTheme(R.style.ThemeOverlay_AppCompat_Dark);
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
        int id  = item.getItemId();
        if(id == R.id.main_activity_menu_search){
            // TODO Manage AUTOCOMPLETE SEARCH
            Toast toast = Toast.makeText(getApplicationContext(),"Manage AUTOCOMPLETE SEARCH",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            this.onAutocompleteRequest(resultCode, data);
        }
    }



    // AUTOCOMPLETE CLICK

    private void onAutocompleteRequest(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            boolean isRestaurant = false;
            Place place = Autocomplete.getPlaceFromIntent(data);
            if(place.getTypes() != null) {

                for (Place.Type type : place.getTypes()) {
                    if (type == Place.Type.RESTAURANT) {
                        isRestaurant = true;
                        break;
                    }
                }
            }
            if(isRestaurant || place.getTypes() == null) {
            }
        }
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
    }


    // FRAGMENT MANAGEMENT //
    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    // Request localisation
    public void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }



}

