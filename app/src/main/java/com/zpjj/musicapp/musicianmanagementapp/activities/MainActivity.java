package com.zpjj.musicapp.musicianmanagementapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.zpjj.musicapp.musicianmanagementapp.R;
import com.zpjj.musicapp.musicianmanagementapp.models.Band;
import com.zpjj.musicapp.musicianmanagementapp.models.UserInfo;
import com.zpjj.musicapp.musicianmanagementapp.navigation.NavigationListener;
import com.zpjj.musicapp.musicianmanagementapp.services.NotifyService;

public class MainActivity extends BaseAuthActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadState(savedInstanceState);

        loadFirebaseKeyAndInitNotifyService();
        initNavigationMenu();
        redirectToFragment();
    }

    /**
     * initialize firebase notification service
     */
    private void loadFirebaseKeyAndInitNotifyService() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.key), Context.MODE_PRIVATE);
        String key = sharedPref.getString(getString(R.string.key), "");
        if(key != null && !key.equals("")) {
            notifyService = new NotifyService(key);
        }
        if(mAuth.getCurrentUser() != null && notifyService == null) {
            notifyService = new NotifyService(this);
        }
    }

    /**
     * load state if phone was rotated
     * @param savedInstanceState
     */
    private void loadState(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            if(savedInstanceState.getSerializable(getString(R.string.BUNDLE_MAIN_ACTIVITY_SELECTED_BAND)) != null) {
                Band b = (Band) savedInstanceState.getSerializable(getString(R.string.BUNDLE_MAIN_ACTIVITY_SELECTED_BAND));
                setSelectedBand(b);
            }
            if(savedInstanceState.getSerializable(getString(R.string.BUNDLE_MAIN_ACTIVITY_USER_INFO)) != null) {
                UserInfo i = (UserInfo) savedInstanceState.getSerializable(getString(R.string.BUNDLE_MAIN_ACTIVITY_USER_INFO));
                setUserInfo(i);
            }
        }
    }

    /**
     * initialize left menu
     */
    private void initNavigationMenu() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        setmDrawer(drawer);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationListener = new NavigationListener(this);
        navigationView.setNavigationItemSelectedListener(navigationListener);
    }

    /**
     * set init view
     */
    private void redirectToFragment() {
        if(getUserInfo() == null || getSelectedBand() == null) {
            UserInfo info = (UserInfo) getIntent().getSerializableExtra(getString(R.string.INTENT_LOGED_IN_USER_INFO));
            if(info == null) {
                logout();
            } else {
                setUserInfo(info);
                if(info.getBands().size() == 0) {
                    navigateToCreateBand();
                } else {
                    if(info.getBands().size() == 1) {
                        mBandService.getBandById((String) info.getBands().keySet().toArray()[0]).subscribe(
                                band -> {
                                    setSelectedBand(band);
                                    navigateToCurrentSong();
                                }, err -> {
                                    logout();
                                }
                        );
                    } else {
                        navigateToChooseBand();
                    }
                }
            }
        }
    }

    /**
     * on back button click event listener
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * save state before rotate view
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(getString(R.string.BUNDLE_MAIN_ACTIVITY_SELECTED_BAND), getSelectedBand());
        outState.putSerializable(getString(R.string.BUNDLE_MAIN_ACTIVITY_USER_INFO), getUserInfo());

        if(notifyService!= null && notifyService.serverKey != null) {
            SharedPreferences sharedPref = this.getSharedPreferences(
                    getString(R.string.key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.key), notifyService.serverKey);
            editor.commit();
        }
    }

}
