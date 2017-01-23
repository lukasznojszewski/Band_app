package com.zpjj.musicapp.musicianmanagementapp.services;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.zpjj.musicapp.musicianmanagementapp.models.Band;

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
}
