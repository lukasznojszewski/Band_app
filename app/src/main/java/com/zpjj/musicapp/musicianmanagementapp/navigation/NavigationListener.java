package com.zpjj.musicapp.musicianmanagementapp.navigation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;

import com.zpjj.musicapp.musicianmanagementapp.R;
import com.zpjj.musicapp.musicianmanagementapp.activities.auth.AuthActivity;
import com.zpjj.musicapp.musicianmanagementapp.activities.BaseAuthActivity;
import com.zpjj.musicapp.musicianmanagementapp.fragments.CurrentSongFragment;

/**
 * Created by daniel on 14.01.17.
 */

public class NavigationListener implements NavigationView.OnNavigationItemSelectedListener {
    BaseAuthActivity authActivity;

    public NavigationListener(BaseAuthActivity authActivity) {
        this.authActivity = authActivity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d("NAVIGATION", "item selected : " + item.getItemId());
        DrawerLayout drawer = (DrawerLayout) authActivity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.nav_current_song:
                changeFragment(authActivity, CurrentSongFragment.class, R.id.content_main);
                authActivity.setTitle(item.getTitle());
                break;
            case R.id.nav_logout:
                authActivity.logout();
                Intent i = new Intent(authActivity, AuthActivity.class);
                authActivity.startActivity(i);
                break;
            default:
                changeFragment(authActivity, CurrentSongFragment.class, R.id.content_main);
                authActivity.setTitle(item.getTitle());
                break;
        }
        return true;
    }

    public void changeFragment(BaseAuthActivity activity, Class fragmentClass, int overrideFragmentId) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(overrideFragmentId, fragment).commit();
        activity.getmDrawer().closeDrawers();
    }
}
