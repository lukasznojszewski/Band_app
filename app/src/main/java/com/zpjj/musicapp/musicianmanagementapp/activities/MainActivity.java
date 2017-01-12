package com.zpjj.musicapp.musicianmanagementapp.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.zpjj.musicapp.musicianmanagementapp.R;
import com.zpjj.musicapp.musicianmanagementapp.activities.auth.BaseAuthActivity;
import com.zpjj.musicapp.musicianmanagementapp.fragments.CurrentSongFragment;
import com.zpjj.musicapp.musicianmanagementapp.services.NavigationService;

public class MainActivity extends BaseAuthActivity  implements NavigationView.OnNavigationItemSelectedListener{
    private NavigationService navigationService = new NavigationService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationService.changeFragment(this, CurrentSongFragment.class, R.id.flContent);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        item.setChecked(true);
        return navigationService.navigate(this, id);
    }

}
