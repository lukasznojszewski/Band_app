package com.zpjj.musicapp.musicianmanagementapp.services;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.zpjj.musicapp.musicianmanagementapp.exceptions.UserNotFoundException;
import com.zpjj.musicapp.musicianmanagementapp.models.Band;
import com.zpjj.musicapp.musicianmanagementapp.models.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;

import rx.Observable;
import rx.Observer;


/**
 * Created by daniel on 15.01.17.
 */

public class UserService {
    FirebaseDatabase mDatabase;
    public UserService() {
        mDatabase = FirebaseDatabase.getInstance();
    }

    public UserService(FirebaseDatabase mDatabase) {
        this.mDatabase = mDatabase;
    }

    public Observable<Boolean> userHasBand(FirebaseUser user) throws UserNotFoundException {
        if(user == null) {
            throw new NullPointerException();
        }
        DatabaseReference ref = mDatabase.getReference("users").child(user.getUid());
        if(ref == null){
            throw new UserNotFoundException();
        }

        return (Observable<Boolean>) RxFirebaseDatabase.observeSingleValueEvent(mDatabase.getReference("users").child(user.getUid()).child("bands")).map(
                dataSnapshot -> {
                    if(dataSnapshot!= null && dataSnapshot.getChildrenCount()>0){
                        return true;
                    }
                    return false;
                }
        );

    }

    public void addUserInfo(FirebaseUser user, UserInfo info) {
        info.setEmail(user.getEmail());
        DatabaseReference myRef = mDatabase.getReference("users").child(user.getUid());
        myRef.setValue(info);
    }

    public void addBandForUser(FirebaseUser currentUser, Band band) {
        mDatabase.getReference("users").child(currentUser.getUid()).child("bands").child(band.getId()).setValue(band);
    }
}
