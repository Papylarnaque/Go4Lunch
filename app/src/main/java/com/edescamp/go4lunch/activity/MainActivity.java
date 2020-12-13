package com.edescamp.go4lunch.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.edescamp.go4lunch.activity.auth.SignInActivity;
import com.edescamp.go4lunch.activity.fragment.DetailsFragment;
import com.edescamp.go4lunch.activity.fragment.MapFragment;
import com.edescamp.go4lunch.activity.fragment.RestaurantsFragment;
import com.edescamp.go4lunch.activity.fragment.SettingsFragment;
import com.edescamp.go4lunch.activity.fragment.WorkmatesFragment;
import com.edescamp.go4lunch.service.APIClient;
import com.edescamp.go4lunch.service.APIRequest;
import com.edescamp.go4lunch.service.entities.ResultAPIDetails;
import com.edescamp.go4lunch.service.entities.ResultsAPIDetails;
import com.edescamp.go4lunch.util.UserHelper;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    //
    public static final int RADIUS_MAX = 5000; // MAX Radius distance in meters
    public static final int RADIUS_STEP = 500; // STEP Radius for slider
    public static final String language = "en";
    public static final String keyword = "restaurant";
    public static final String FIELDS = "formatted_address,geometry,photos,place_id,name,rating,opening_hours,website,international_phone_number";
    private static final int MAP_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int RESTAURANT_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;
    private static final String TAG = "MAIN_ACTIVITY";
    public static int radius = 2500; // radius in meters around user for search
    private DocumentReference docRef;
    final static String PREFS_NAME = "AUTH";

    public static String uid;
    public static String usernameString = null;

    // UI
    public Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private TextView userMail;
    private TextView userName;
    private ImageView userPicture;
    private Task<DocumentSnapshot> user;
    private FirebaseUser firebaseUser;
    private ImageButton logo_button;
    private String restaurantChoice;
    private String restaurantName;
    public static List<DocumentSnapshot> workmates;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            uid = bundle.getString("USER");

        configureToolbar();
        configureDrawerLayout();
        configureNavigationMenu();

        getWorkmates();

        configureInitialState();

        updateUserSnaptshot();

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
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
                    mToolbar.setTitle(R.string.title_mapview);
                    fragment = new MapFragment();
                    showFragment(fragment);
                    break;

                case navigation_listview_id:
                    mToolbar.setTitle(R.string.title_listview);
                    fragment = new RestaurantsFragment();
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

        View header = navigationView.getHeaderView(0);

        userMail = header.findViewById(R.id.drawer_user_mail);
        userName = header.findViewById(R.id.drawer_user_name);
        userPicture = header.findViewById(R.id.drawer_user_picture);

        logo_button = findViewById(R.id.activity_main_drawer_logo);

        logo_button.setOnClickListener(v -> {
            // quit the app
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        });

    }

    // ----------- DATA -------------------- //

    private void updateUserSnaptshot() {
        if (uid != null) {
            docRef = UserHelper.getUsersCollection().document(uid);

            docRef.addSnapshotListener((snapshot, e) -> {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    updateUI(snapshot);

                } else {
                    Log.d(TAG, "Current data: null");
                }
            });
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
            restaurantName = result.get("chosenRestaurantName").toString();
            Log.i(TAG, "firebaseUser/chosenRestaurantName : " + restaurantName);
        }

        if (result.get("hasChosenRestaurant") != null) {
            restaurantChoice = result.get("hasChosenRestaurant").toString();
            Log.i(TAG, "firebaseUser/hasChosenRestaurant : " + restaurantChoice);
        }
        usernameString = userName.getText().toString();

    }

    private void getWorkmates() {
        UserHelper.getUsersWithChosenRestaurant().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                workmates = queryDocumentSnapshots.getDocuments();
            }
        });

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
                this.mDrawerLayout.closeDrawer(GravityCompat.START);
                getPlaceDetails(restaurantChoice);
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



    // ---------------------- AUTOCOMPLETE SEARCH -----------------------//
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


    // ----------------------------- LOGOUT ----------------------------//
    private void deleteAuthAndLogOut() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("USER");
        editor.apply();

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

    private void openDetailsFragment(ResultAPIDetails result) {
        Fragment fragment = new DetailsFragment(result);
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void getPlaceDetails(String placeId) {
        APIRequest apiDetails = APIClient.getClient().create(APIRequest.class);
        Call<ResultsAPIDetails> placeDetails = apiDetails.getPlaceDetails(placeId, FIELDS, getResources().getString(R.string.google_maps_key));

        placeDetails.enqueue(new Callback<ResultsAPIDetails>() {
            @Override
            public void onResponse(@NotNull Call<ResultsAPIDetails> call, @NotNull Response<ResultsAPIDetails> response) {
                Log.d(TAG, "getPlaceDetails API ");
                if (response.isSuccessful()) {
                    ResultsAPIDetails body = response.body();
                    if (body != null) {
                        ResultAPIDetails result = body.getResult();
                        Log.d(TAG, "getPlaceDetails successful response " + result.getName() + " " + result.getPlaceId());

                        openDetailsFragment(result);
                    }
                    // TODO Handle failures, 404 error, etc
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResultsAPIDetails> call, @NotNull Throwable t) {
                Log.d(TAG, "getPlaceDetails API failure" + t);
            }

        });
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


    // ------------------------ PERMISSIONS -------------------------//
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
                showFragment(new RestaurantsFragment());
            } else {
                // Location permission has been granted, preview can be displayed
                Log.i(TAG, "LOCATION permission was NOT granted.");
                Toast.makeText(getApplicationContext(), R.string.permissions_not_granted, Toast.LENGTH_SHORT).show();

            }
        }
    }


}

