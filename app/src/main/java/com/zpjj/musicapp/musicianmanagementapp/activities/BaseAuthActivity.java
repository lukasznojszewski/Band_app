package com.zpjj.musicapp.musicianmanagementapp.activities;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.widget.TextView;

import com.zpjj.musicapp.musicianmanagementapp.R;
import com.zpjj.musicapp.musicianmanagementapp.activities.auth.AuthActivity;
import com.zpjj.musicapp.musicianmanagementapp.activities.auth.BaseActivity;
import com.zpjj.musicapp.musicianmanagementapp.models.Band;
import com.zpjj.musicapp.musicianmanagementapp.models.UserInfo;
import com.zpjj.musicapp.musicianmanagementapp.navigation.NavigationListener;
import com.zpjj.musicapp.musicianmanagementapp.services.BandService;
import com.zpjj.musicapp.musicianmanagementapp.services.NotifyService;
import com.zpjj.musicapp.musicianmanagementapp.services.UserService;

import icepick.State;

/**
 * Created by daniel on 12.01.17.
 */

public class BaseAuthActivity extends BaseActivity {

    private Band selectedBand;

    private  UserInfo userInfo;

    protected NotifyService notifyService;
    protected BandService mBandService;
    protected UserService mUserService;

    private DrawerLayout mDrawer;

    protected NavigationView navigationView;

    protected NavigationListener navigationListener;

    public BaseAuthActivity() {
        mBandService = new BandService();
        mUserService = new UserService();
    }

    public Band getSelectedBand() {
        return selectedBand;
    }

    public void setSelectedBand(Band selectedBand) {
        this.selectedBand = selectedBand;
        updateAppTitleName();
        updateMenuItemList();
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public DrawerLayout getmDrawer() {
        return mDrawer;
    }

    public void setmDrawer(DrawerLayout mDrawer) {
        this.mDrawer = mDrawer;
    }

    public NotifyService getNotifyService() {
        return notifyService;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    public BandService getmBandService() {
        return mBandService;
    }

    public UserService getmUserService() {
        return mUserService;
    }

    private void updateMenuItemList() {
        String currentUserId = mAuth.getCurrentUser().getUid();
        String selectedBandMasterId = getSelectedBand().getMasterUID();
        Menu menu = navigationView.getMenu();
        if(currentUserId.equals(selectedBandMasterId)) {
            menu.setGroupVisible(R.id.nav_master_band_items, true);
        } else {
            menu.setGroupVisible(R.id.nav_master_band_items, false);
        }
    }
    private void updateAppTitleName() {
        TextView navAppName = (TextView) findViewById(R.id.nav_app_title);
        navAppName.setText("BandApp - " + selectedBand.getName());
    }

    public void navigateToCurrentSong() {
        navigationListener.onNavigationItemSelected(navigationView.getMenu().getItem(NavigationListener.CURRENT_SONG));
        navigationView.setCheckedItem(R.id.nav_current_song);
    }

    public void navigateToChooseBand() {
        navigationListener.onNavigationItemSelected(navigationView.getMenu().getItem(NavigationListener.CHOOSE_BAND));
        navigationView.setCheckedItem(R.id.nav_choose_band);
    }

    public void navigateToCreateBand() {
        navigationListener.onNavigationItemSelected(navigationView.getMenu().getItem(NavigationListener.CREATE_BAND));
        navigationView.setCheckedItem(R.id.nav_create_band);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(mAuth.getCurrentUser() == null) {
            Intent i = new Intent(this, AuthActivity.class);
            String notifyType = getIntent().getStringExtra("NOTIFY_TYPE");
            if(notifyType != null && !notifyType.equals("")) {
                i.putExtra("NOTIFY_TYPE", notifyType);
            }
            startActivity(i);
        }
    }


}
