package com.zpjj.musicapp.musicianmanagementapp.services;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.zpjj.musicapp.musicianmanagementapp.exceptions.UserNotFoundException;
import com.zpjj.musicapp.musicianmanagementapp.models.UserInfo;

import rx.Observable;

/**
 * Created by daniel on 22.01.17.
 */

public class UserService {
    FirebaseDatabase mDatabase;
    public UserService() {
        mDatabase = FirebaseDatabase.getInstance();
    }

    public Observable<UserInfo> getUserInfo(FirebaseUser fbUser) {
        if(fbUser == null) {
            return Observable.create(subscriber -> {
                subscriber.onError(new NullPointerException());
            });
        }
        return getUserInfo(fbUser.getUid());
    }

    public Observable<UserInfo> getUserInfo(String userId) {
        return (Observable<UserInfo>) RxFirebaseDatabase.observeSingleValueEvent(mDatabase.getReference("users").child(userId),
                dataSnapshot -> {
                    UserInfo info = new UserInfo();
                    info = dataSnapshot.getValue(UserInfo.class);
                    return  info;
                }).flatMap(userInfo -> {
            if(userInfo != null) {
                return Observable.just(userInfo);
            }
            return Observable.error(new UserNotFoundException());
        });

    }

    public void createOrUpdateUserInfo(FirebaseUser fbUser, UserInfo info) {
        if(fbUser == null) {
            throw new NullPointerException();
        }

        if(info == null) {
            throw new NullPointerException();
        }

        DatabaseReference myRef = mDatabase.getReference("users").child(fbUser.getUid());
        myRef.setValue(info);
    }


    public void updateUserFirebaseId(FirebaseUser currentUser, String refreshedToken) {
        mDatabase.getReference("users").child(currentUser.getUid()).child("firebaseToken").setValue(refreshedToken);
    }
}
