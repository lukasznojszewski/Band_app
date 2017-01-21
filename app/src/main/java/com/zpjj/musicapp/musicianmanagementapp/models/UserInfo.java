package com.zpjj.musicapp.musicianmanagementapp.models;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 15.01.17.
 */

public class UserInfo {
    private String email;
    private Map<String,Band> bands;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Band> getUserBands() {
        return bands;
    }

    public void setUserBands(Map<String, Band> userBands) {
        this.bands = userBands;
    }
}
