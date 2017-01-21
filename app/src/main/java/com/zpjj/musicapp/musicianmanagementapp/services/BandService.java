package com.zpjj.musicapp.musicianmanagementapp.services;

import com.google.firebase.database.FirebaseDatabase;
import com.zpjj.musicapp.musicianmanagementapp.models.Band;

import java.util.UUID;

/**
 * Created by daniel on 15.01.17.
 */

public class BandService {
    private FirebaseDatabase mDatabase;

    public BandService() {
        this.mDatabase = FirebaseDatabase.getInstance();
    }

    public void addBand(Band band) {
        mDatabase.getReference("bands").child(String.valueOf(band.getId())).setValue(band);
    }
}
