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
    public static final String users = "users";
    public  static final String firebaseToken = "firebaseToken";
    FirebaseDatabase mDatabase;
    public UserService() {
        mDatabase = FirebaseDatabase.getInstance();
    }

    /**
     * load user information with firebase user object
     * @param fbUser
     * @return Observable user information object
     */
    public Observable<UserInfo> getUserInfo(FirebaseUser fbUser) {
        if(fbUser == null) {
            return Observable.create(subscriber -> {
                subscriber.onError(new NullPointerException());
            });
        }
        return getUserInfo(fbUser.getUid());
    }

    /**
     * load user information with user id
     * @param userId user unique id
     * @return Observable user info object
     */
    public Observable<UserInfo> getUserInfo(String userId) {
        return (Observable<UserInfo>) RxFirebaseDatabase.observeSingleValueEvent(mDatabase.getReference(users).child(userId),
                dataSnapshot -> {
                    UserInfo info = new UserInfo();
                    info = dataSnapshot.getValue(UserInfo.class);
                    return  info;
                }).flatMap(userInfo -> {
            if(userInfo != null) {
                return Observable.just(userInfo);
            }
            return Observable.error(new UserNotFoundException());
        }).onErrorResumeNext(throwable -> {
            System.out.println(throwable.getMessage());
            return Observable.error(throwable);
        });

    }

    /**
     * create or update user information
     * @param firebaseUser firebase authentication object
     * @param info user information object
     */
    public void createOrUpdateUserInfo(FirebaseUser firebaseUser, UserInfo info) {
        if(firebaseUser == null) {
            throw new NullPointerException();
        }

        if(info == null) {
            throw new NullPointerException();
        }

        DatabaseReference myRef = mDatabase.getReference(users).child(firebaseUser.getUid());
        myRef.setValue(info);
    }

    /**
     * update firebase notification device id
     * @param currentUser signed in user object
     * @param refreshedToken new firebase device token
     */
    public void updateUserFirebaseId(FirebaseUser currentUser, String refreshedToken) {
        mDatabase.getReference(users).child(currentUser.getUid()).child(firebaseToken).setValue(refreshedToken);
    }
}
