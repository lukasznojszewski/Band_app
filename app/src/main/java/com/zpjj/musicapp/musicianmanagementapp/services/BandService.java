package com.zpjj.musicapp.musicianmanagementapp.services;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.kelvinapps.rxfirebase.DataSnapshotMapper;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.zpjj.musicapp.musicianmanagementapp.models.Band;
import com.zpjj.musicapp.musicianmanagementapp.models.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by daniel on 22.01.17.
 */

public class BandService {
    FirebaseDatabase mDatabase;
    public BandService() {
        mDatabase = FirebaseDatabase.getInstance();
    }

    public void addBand(Band band) {
        mDatabase.getReference().child("bands").child(band.getId()).setValue(band);
    }

    public Observable<Band> getBandById(String id) {
        return (Observable<Band>) RxFirebaseDatabase.observeSingleValueEvent(mDatabase.getReference("bands").child(id),
                dataSnapshot -> {
                    Band band = new Band();
                    band = dataSnapshot.getValue(Band.class);
                    return  band;
                });
    }

    public Observable<List<Band>> getBands() {
        return (Observable<List<Band>>) RxFirebaseDatabase.observeSingleValueEvent(mDatabase.getReference().child("bands"), DataSnapshotMapper.listOf(Band.class));
    }

    public Observable<List<UserInfo>> getJoinRequestUsersToBand(String bandId) {
        return Observable.create(subscriber -> {
            List<UserInfo> users = new ArrayList<UserInfo>();
            RxFirebaseDatabase.observeSingleValueEvent(mDatabase.getReference("bands").child(bandId),Band.class).subscribe(
                    band -> {
                        for (String userId : band.getUserJoinRequest().keySet()) {
                            RxFirebaseDatabase.observeSingleValueEvent(mDatabase.getReference("users").child(userId), UserInfo.class).subscribe(
                                    userInfo -> {
                                        users.add(userInfo);
                                        if(band.getUserJoinRequest().size() == users.size()) {
                                            subscriber.onNext(users);
                                            subscriber.onCompleted();
                                        }
                                    }
                            );
                        }
                    }
            );

        });
    }

    public void createUserJoinBandRequest(Band band, FirebaseUser currentUser) {
        mDatabase.getReference("bands").child(band.getId()).child("userJoinRequest").child(currentUser.getUid()).setValue(true);
    }

    public void acceptUserJoinRequest(Band band, String userId) {
        mDatabase.getReference("bands").child(band.getId()).child("userJoinRequest").child(userId).removeValue();
        mDatabase.getReference("bands").child(band.getId()).child("users").child(userId).setValue(true);
        mDatabase.getReference("users").child(userId).child("bands").child(band.getId()).setValue(true);
    }

    public void rejectUserJoindRequest(Band band, String userId) {
        mDatabase.getReference("bands").child(band.getId()).child("userJoinRequest").child(userId).removeValue();
    }
}
