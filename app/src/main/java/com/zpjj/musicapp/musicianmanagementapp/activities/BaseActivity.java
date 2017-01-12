package com.zpjj.musicapp.musicianmanagementapp.activities;

import android.app.ProgressDialog;
import android.support.annotation.VisibleForTesting;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.zpjj.musicapp.musicianmanagementapp.R;

/**
 * Created by daniel on 12.01.17.
 */

public class BaseActivity extends AppCompatActivity {
    public FirebaseAuth mAuth;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public BaseActivity() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void logout() {
        mAuth.signOut();
    }
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    public DrawerLayout getmDrawer() {
        return mDrawer;
    }

    public void setmDrawer(DrawerLayout mDrawer) {
        this.mDrawer = mDrawer;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }
}
