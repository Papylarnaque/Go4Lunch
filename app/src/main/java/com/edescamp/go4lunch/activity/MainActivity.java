package com.edescamp.go4lunch.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.activity.fragment.MapFragment;
import com.edescamp.go4lunch.activity.fragment.RestaurantsFragment;
import com.edescamp.go4lunch.activity.fragment.SettingsFragment;
import com.edescamp.go4lunch.activity.fragment.WorkmatesFragment;
import com.edescamp.go4lunch.model.autocomplete.PredictionAPIAutocomplete;
import com.edescamp.go4lunch.service.AutoCompleteService;
import com.edescamp.go4lunch.util.DetailsUtil;
import com.edescamp.go4lunch.util.NotificationHelper;
import com.edescamp.go4lunch.util.SharedPrefs;
import com.edescamp.go4lunch.util.UserHelper;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.Objects;

import static com.edescamp.go4lunch.activity.fragment.MapFragment.MAP_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.edescamp.go4lunch.service.AutoCompleteService.getAutocomplete;
import static com.edescamp.go4lunch.service.AutoCompleteService.listenAutoCompletePredictions;
import static com.edescamp.go4lunch.util.DetailsUtil.openDetailsFragmentOrCallApiThenOpenDetailsFragment;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    // STATIC PARAMETERS
    private static final String TAG = "MAIN_ACTIVITY";
    private static final String API_AUTOCOMPLETE_FILTER_KEYWORD = "food";

    public static int RADIUS_INIT = 2500; // radius in meters around user for search
    public static final int RADIUS_MAX = 5000; // MAX Radius distance in meters
    public static final int RADIUS_MIN = 2500; // MAX Radius distance in meters
    public static final int RADIUS_STEP = 500; // STEP Radius for slider

    public static final String API_MAP_LANGUAGE = "en";
    public static final String API_MAP_KEYWORD = "restaurant";
    public static final String API_AUTOCOMPLETE_KEYWORD = "establishment";
    public static final String API_MAP_FIELDS = "formatted_address,geometry,photos,place_id,name,rating,opening_hours,website,international_phone_number";

    public static final double RATING_MAX = 4.5;
    public static final double RATING_MIDDLE = 2.5;
    public static final double RATING_MIN = 1;

    public static String uid;

    // UI
    public Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private TextView userMail;
    private TextView userName;
    private ImageView userPicture;
    private AutoCompleteTextView autoCompleteTextView;
    private MenuItem clearButton;

    // DATA
    private String restaurantChoice;
    public static List<DocumentSnapshot> workmates;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uid = SharedPrefs.getUserId(getApplicationContext());

        configureToolbar();
        configureDrawerLayout();
        configureNavigationMenu();

        getWorkmates();

        configureInitialState();

        updateUserSnapshot();

        autoCompleteTextListener();

        NotificationHelper.setAlarmForNotifications(
                getApplicationContext(),
                SharedPrefs.getNotifications(getApplicationContext()));

    }

    // ------------------------ LAYOUT CONFIGURATIONS  -----------------------//

    //----- INITIAL STATE -----
    private void configureInitialState() {
        Fragment fragment = new MapFragment();
        mToolbar.setTitle(R.string.title_mapview);
        showFragment(fragment);
    }

    // TOOLBAR configuration
    private void configureToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextViewPlace);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);

        menu.setGroupVisible(R.id.main_activity_restaurants_group_sort, false);

        clearButton = menu.findItem(R.id.main_activity_menu_clear);
        clearButton.setVisible(false);
        return true;
    }

    // BOTTOM NAVIGATION configuration //
    private void configureNavigationMenu() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            final int navigation_mapview_id = R.id.navigation_mapview;
            final int navigation_listview_id = R.id.navigation_listview;
            final int navigation_workmates_id = R.id.navigation_workmates;

            switch (item.getItemId()) {
                case navigation_mapview_id:
                    showSearch();
                    mToolbar.setTitle(R.string.title_mapview);
                    fragment = new MapFragment();
                    showFragment(fragment);
                    break;

                case navigation_listview_id:
                    showSearch();
                    mToolbar.setTitle(R.string.title_listview);
                    fragment = new RestaurantsFragment();
                    showFragment(fragment);
                    break;

                case navigation_workmates_id:
                    hideSearch();
                    mToolbar.setTitle(R.string.title_workmates);
                    fragment = new WorkmatesFragment();
                    showFragment(fragment);
                    break;
            }
            return true;
        });
    }

    private void showSearch() {
        findViewById(R.id.main_activity_menu_search).setVisibility(View.VISIBLE);
    }

    private void hideSearch() {
        if (autoCompleteTextView.getVisibility() == View.VISIBLE) {
            autoCompleteTextView.setVisibility(View.GONE);
        }
        findViewById(R.id.main_activity_menu_search).setVisibility(View.GONE);
    }

    // DRAWER configuration //
    private void configureDrawerLayout() {
        mDrawerLayout = findViewById(R.id.activity_main_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                updateUserSnapshot();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

            }
        };
        mDrawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        //Use Navigation Callback listener as follows
        NavigationView navigationView = findViewById(R.id.activity_main_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        userMail = header.findViewById(R.id.drawer_user_mail);
        userName = header.findViewById(R.id.drawer_user_name);
        userPicture = header.findViewById(R.id.drawer_user_picture);

        ImageButton logo_button = findViewById(R.id.activity_main_drawer_logo);

        logo_button.setOnClickListener(v -> {
            // quit the app
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        });

    }

    // ----------- DATA -------------------- //

    private void updateUserSnapshot() {
        if (uid != null) {
            UserHelper.getUser(uid).addOnSuccessListener(this::updateUI);
        }
    }

    // Handle user data to show in drawer layout
    private void updateUI(DocumentSnapshot result) {

        if (result.get("username") != null) {
            userName.setText(result.getString("username"));
            Log.i(TAG, "firebaseUser/username : " + userName.getText());
        }
        if (result.get("mail") != null) {
            userMail.setText(result.getString("mail"));
            Log.i(TAG, "firebaseUser/mail : " + userMail.getText());
        }
        if (result.get("urlPicture") != null) {
            Glide.with(getApplicationContext())
                    .load(Objects.requireNonNull(result.getString("urlPicture")))
                    .circleCrop()
                    .into(userPicture);
        }

        if (result.get("chosenRestaurantName") != null) {
            String restaurantName = Objects.requireNonNull(result.get("chosenRestaurantName")).toString();
            Log.i(TAG, "firebaseUser/chosenRestaurantName : " + restaurantName);
        }

        if (result.get("chosenRestaurantId") != null) {
            restaurantChoice = Objects.requireNonNull(result.get("chosenRestaurantId")).toString();
            Log.i(TAG, "firebaseUser/chosenRestaurantId : " + restaurantChoice);
        }

    }

    private void getWorkmates() {
        UserHelper.getUsersWithChosenRestaurant()
                .addOnSuccessListener(queryDocumentSnapshots -> workmates = queryDocumentSnapshots.getDocuments());
    }

    // ------------------------ DRAWER options INTERFACE  -----------------------//
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        final int main_drawer_lunch_id = R.id.activity_main_drawer_lunch;
        final int main_drawer_settings_id = R.id.activity_main_drawer_settings;
        final int main_drawer_logout_id = R.id.activity_main_drawer_logout;
        switch (id) {
            // "YOUR LUNCH"
            case main_drawer_lunch_id:
                yourLunchClick();
                return true;
            // "SETTINGS"
            case main_drawer_settings_id:
                this.mDrawerLayout.closeDrawer(GravityCompat.START);
                Fragment fragment = new SettingsFragment();
                showFragment(fragment);
                return true;
            // "LOGOUT"
            case main_drawer_logout_id:
                deleteAuthAndLogOut();
                return true;
            default:
                break;
        }
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void yourLunchClick() {
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        if (restaurantChoice == null || restaurantChoice.equals("")) {
            Toast toast = Toast.makeText(getBaseContext(), R.string.main_activity_lunch_no_restaurant_chosen, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            openDetailsFragmentOrCallApiThenOpenDetailsFragment(this, restaurantChoice);
        }
    }

    // ---------------------- AUTOCOMPLETE SEARCH -----------------------//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final int main_activity_menu_search = R.id.main_activity_menu_search;
        final int main_activity_menu_clear = R.id.main_activity_menu_clear;

        switch (id) {
            // Autocomplete lens
            case main_activity_menu_search:
                showOrHideAutocompleteItem();
                return true;
            // Autocomplete clear
            case main_activity_menu_clear:
                autoCompleteTextView.setText("");
                return true;
            default:
                break;
        }


        return super.onOptionsItemSelected(item);

    }

    private void showOrHideAutocompleteItem() {
        if (autoCompleteTextView.getVisibility() == View.VISIBLE) {
            autoCompleteTextView.setVisibility(View.GONE);
        } else {
            autoCompleteTextView.setVisibility(View.VISIBLE);
        }
    }

    private void autoCompleteTextListener() {

        // call APIAutocomplete when typing in search
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 2) {
                    getAutocomplete(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    clearButton.setVisible(true);
                    findViewById(R.id.main_activity_menu_search).setVisibility(View.GONE);
                } else {
                    clearButton.setVisible(false);
                    findViewById(R.id.main_activity_menu_search).setVisibility(View.VISIBLE);
                }


            }
        });

        listenAutoCompletePredictions.observe(this, this::filterAutocompleteResults);


        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            // Handles no results click
            if (parent.getItemAtPosition(position) == getResources().getString(R.string.autoCompleteTextView_noresult)) {
                autoCompleteTextView.setText("");
            } else {

                // Handles click on an autocomplete search dropdown result
                String placeId = null;
                for (PredictionAPIAutocomplete prediction : Objects.requireNonNull(AutoCompleteService.predictions.getValue())) {
                    if (parent.getItemAtPosition(position) == prediction.getStructured_formatting().getMain_text()) {
                        placeId = prediction.getPlace_id();
                    }
                }

                DetailsUtil.openDetailsFragmentOrCallApiThenOpenDetailsFragment(this, placeId);

            }

        });

    }

    private void filterAutocompleteResults(List<PredictionAPIAutocomplete> predictionAPIAutocompletes) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_dropdown_item_1line);
        adapter.setNotifyOnChange(true);
        //attach the adapter to textview
        autoCompleteTextView.setAdapter(adapter);

        for (PredictionAPIAutocomplete prediction : predictionAPIAutocompletes) {
            if (prediction.getTypes().contains(API_AUTOCOMPLETE_FILTER_KEYWORD)) {
                // only return places as establishment of type "food"
                Log.d(TAG, "getAutocompleteSearch : prediction = " + prediction.getDescription());
                adapter.add(prediction.getStructured_formatting().getMain_text());
                adapter.notifyDataSetChanged();
            }
        }
        if (adapter.getCount() == 0) {
            adapter.add(getResources().getString(R.string.autoCompleteTextView_noresult));
            adapter.notifyDataSetChanged();
        }


    }

    // ----------------------------- LOGOUT ----------------------------//
    private void deleteAuthAndLogOut() {
        SharedPrefs.deleteUserId(getApplicationContext());

        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intent);
    }

    // ------------------- FRAGMENT Navigations ----------------------//
    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    // DRAWER closed with back button
    @Override
    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        }

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (currentFragment instanceof MapFragment
                || currentFragment instanceof RestaurantsFragment
                || currentFragment instanceof WorkmatesFragment) {
            // do nothing
        } else {
            super.onBackPressed();
        }
    }

    // ------------------- HANDLES LOCATION PERMISSIONS RESPONSE ---------------------//
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

        } else if (requestCode == RestaurantsFragment.RESTAURANT_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // Received permission result for location permission.
            Log.i(TAG, "Received response for LOCATION permission request from RestaurantFragment.");

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission has been granted, preview can be displayed
                Log.i(TAG, "LOCATION permission has now been granted. Showing preview.");
                showFragment(new RestaurantsFragment());
            } else {
                // Location permission has been granted, preview can be displayed
                Log.i(TAG, "LOCATION permission was NOT granted.");
                Toast.makeText(getApplicationContext(), R.string.permissions_not_granted, Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWorkmates();

    }


}

