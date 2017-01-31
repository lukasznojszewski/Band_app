package com.zpjj.musicapp.musicianmanagementapp.services;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by daniel on 23.01.17.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    /**
     * update firebase user device id on token change
     */
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        if(FirebaseAuth.getInstance().getCurrentUser()!= null) {
            UserService userService = new UserService();
            userService.updateUserFirebaseId(FirebaseAuth.getInstance().getCurrentUser(), refreshedToken);
        }
    }


}
