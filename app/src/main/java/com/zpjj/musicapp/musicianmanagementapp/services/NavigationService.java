package com.zpjj.musicapp.musicianmanagementapp.services;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zpjj.musicapp.musicianmanagementapp.R;
import com.zpjj.musicapp.musicianmanagementapp.activities.BaseActivity;
import com.zpjj.musicapp.musicianmanagementapp.activities.MainActivity;
import com.zpjj.musicapp.musicianmanagementapp.fragments.CurrentSongFragment;

/**
 * Created by daniel on 12.01.17.
 */
public class NavigationService {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public boolean navigate(Activity activity, int id) {
        if (id == R.id.nav_current_song) {
            changeFragment((BaseActivity) activity, CurrentSongFragment.class, R.id.flContent);
        } else if (id == R.id.nav_logout) {
            ((BaseActivity) activity).logout();
            activity.finishAffinity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
        return true;
    }

    public void changeFragment(BaseActivity activity, Class fragmentClass, int overrideFragmentId) {
        changeFragment(activity, fragmentClass, overrideFragmentId, true);
    }

    public void changeFragment(BaseActivity activity, Class fragmentClass, int overrideFragmentId, boolean addToBackStack) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(overrideFragmentId, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }


        if (activity.getmDrawer() != null) {
            activity.getmDrawer().closeDrawers();
        }
    }
}
