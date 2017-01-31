package com.zpjj.musicapp.musicianmanagementapp.activities.auth;

import android.app.ProgressDialog;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.zpjj.musicapp.musicianmanagementapp.R;

/**
 * Created by daniel on 12.01.17.
 */

public class BaseActivity extends AppCompatActivity {
    public FirebaseAuth mAuth;

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
}
