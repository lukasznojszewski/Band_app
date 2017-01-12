package com.zpjj.musicapp.musicianmanagementapp.activities.auth;

import android.content.Intent;

import com.zpjj.musicapp.musicianmanagementapp.activities.BaseActivity;

/**
 * Created by daniel on 12.01.17.
 */

public class BaseAuthActivity extends BaseActivity {
    @Override
    protected void onResume() {
        super.onResume();

        if(mAuth.getCurrentUser() == null) {
            Intent i = new Intent(this, AuthActivity.class);
            startActivity(i);
        }
    }
}
