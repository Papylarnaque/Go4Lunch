package com.oc.go4lunch.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.oc.go4lunch.R;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureToolbar();
        configureDrawerLayout();
        configureNavigationMenu();
    }

    /**
     * This method configures the bottom navigation bar
     */
    void configureNavigationMenu() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_mapview:
                        mToolbar.setTitle(R.string.title_mapview);
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
     * This method configures the drawer layout needed for the drawer menu
     */
    private void configureDrawerLayout() {
        mDrawerLayout = findViewById(R.id.activity_main_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

}
