package com.zpjj.musicapp.musicianmanagementapp.models;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 15.01.17.
 */

public class UserInfo implements Serializable{
    private String email;
    private Map<String,Boolean> bands = new HashMap<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Boolean> getBands() {
        return bands;
    }

    public void setBands(Map<String, Boolean> bands) {
        this.bands = bands;
    }
}
