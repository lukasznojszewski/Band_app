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
    private String id;
    private String email;
    private String firebaseToken;
    private Map<String,Boolean> bands = new HashMap<>();
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }


    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public Map<String, Boolean> getBands() {
        return bands;
    }

    public void setBands(Map<String, Boolean> bands) {
        this.bands = bands;
    }

    @Override
    public String toString() {
        return email;
    }
}
