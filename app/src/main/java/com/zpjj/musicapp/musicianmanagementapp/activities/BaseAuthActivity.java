package com.zpjj.musicapp.musicianmanagementapp.activities;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;

import com.zpjj.musicapp.musicianmanagementapp.activities.auth.AuthActivity;
import com.zpjj.musicapp.musicianmanagementapp.activities.auth.BaseActivity;
import com.zpjj.musicapp.musicianmanagementapp.models.Band;
import com.zpjj.musicapp.musicianmanagementapp.models.UserInfo;
import com.zpjj.musicapp.musicianmanagementapp.services.NotifyService;

import icepick.State;

/**
 * Created by daniel on 12.01.17.
 */

public class BaseAuthActivity extends BaseActivity {

    Band selectedBand;

    UserInfo userInfo;

    NotifyService notifyService;

    private DrawerLayout mDrawer;

    public Band getSelectedBand() {
        return selectedBand;
    }

    public void setSelectedBand(Band selectedBand) {
        this.selectedBand = selectedBand;
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
