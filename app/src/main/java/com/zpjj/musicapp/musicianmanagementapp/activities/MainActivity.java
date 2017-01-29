package com.zpjj.musicapp.musicianmanagementapp.activities;

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
        if(savedInstanceState != null) {
            if(savedInstanceState.getSerializable("MainActivity#selectedBand") != null) {
                Band b = (Band) savedInstanceState.getSerializable("MainActivity#selectedBand");
                setSelectedBand(b);
            }
            if(savedInstanceState.getSerializable("MainActivity#userInfo") != null) {
                UserInfo i = (UserInfo) savedInstanceState.getSerializable("MainActivity#userInfo");
                setUserInfo(i);
            }
        }
        initNavigationMenu();
    }

    private void initNavigationMenu() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        setmDrawer(drawer);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        NavigationListener navigationListener = new NavigationListener(this);
        navigationView.setNavigationItemSelectedListener(navigationListener);
        if(getUserInfo() == null || getSelectedBand() == null) {
            UserInfo info = (UserInfo) getIntent().getSerializableExtra("USER_INFO");
            if(info == null) {
                logout();
            } else {
                notifyService = new NotifyService();
                setUserInfo(info);
                if(info.getBands().size() == 0) {
                    navigationListener.onNavigationItemSelected(navigationView.getMenu().getItem(NavigationListener.CREATE_BAND));
                    navigationView.setCheckedItem(R.id.nav_create_band);
                } else {
                    if(info.getBands().size() == 1) {
                        navigationListener.onNavigationItemSelected(navigationView.getMenu().getItem(NavigationListener.CHOOSE_BAND));
                        navigationView.setCheckedItem(R.id.nav_choose_band);
                    } else {
                        navigationListener.onNavigationItemSelected(navigationView.getMenu().getItem(NavigationListener.CHOOSE_BAND));
                        navigationView.setCheckedItem(R.id.nav_choose_band);
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("MainActivity#selectedBand", getSelectedBand());
        outState.putSerializable("MainActivity#userInfo", getUserInfo());

    }

}
