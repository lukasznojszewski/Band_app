package com.zpjj.musicapp.musicianmanagementapp.services;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.kelvinapps.rxfirebase.DataSnapshotMapper;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.zpjj.musicapp.musicianmanagementapp.models.Band;
import com.zpjj.musicapp.musicianmanagementapp.models.Song;
import com.zpjj.musicapp.musicianmanagementapp.models.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by daniel on 22.01.17.
 */

public class BandService {
    public static final String bands = "bands";
    public static final String songs = "songs";
    public static final String currentSong = "currentSong";
    public static final String userJontRequests = "userJoinRequest";


    FirebaseDatabase mDatabase;
    public BandService() {
        mDatabase = FirebaseDatabase.getInstance();
    }

    /**
     * create or update band
     * @param band band object
     */
    public void createOrUpdateBand(Band band) {
        mDatabase.getReference().child(bands).child(band.getId()).setValue(band);
    }

    /**
     * load band iformation with band id
     * @param id band uuid
     * @return Observable band information
     */
    public Observable<Band> getBandById(String id) {
        return (Observable<Band>) RxFirebaseDatabase.observeSingleValueEvent(mDatabase.getReference(bands).child(id),
                dataSnapshot -> {
                    Band band = new Band();
                    band = dataSnapshot.getValue(Band.class);
                    return  band;
                }).onErrorResumeNext(throwable -> {
            System.out.println(throwable.getMessage());
            return Observable.empty();
        });
    }

    /**
     * load band list
     * @return obserevable band list
     */
    public Observable<List<Band>> getBands() {
        return (Observable<List<Band>>) RxFirebaseDatabase.observeSingleValueEvent(mDatabase.getReference().child(bands), DataSnapshotMapper.listOf(Band.class));
    }

    /**
     * load user join request to nabd
     * @param bandId band uuid
     * @return Observable list of user
     */
    public Observable<List<UserInfo>> getJoinRequestUsersToBand(String bandId) {
        return Observable.create(subscriber -> {
            List<UserInfo> users = new ArrayList<>();
            RxFirebaseDatabase.observeSingleValueEvent(mDatabase.getReference(bands).child(bandId),Band.class).subscribe(
                    band -> {
                        for (String userId : band.getUserJoinRequest().keySet()) {
                            RxFirebaseDatabase.observeSingleValueEvent(mDatabase.getReference(UserService.users).child(userId), UserInfo.class).subscribe(
                                    userInfo -> {
                                        users.add(userInfo);
                                        if(band.getUserJoinRequest().size() == users.size()) {
                                            subscriber.onNext(users);
                                            subscriber.onCompleted();
                                        }
                                    }, err -> {
                                        System.out.println(err.getMessage());
                                    }
                            );
                        }
                    }
            );

        });
    }

    /**
     * load current song for band
     * @param bandId band uuid
     * @return observale of current song
     */
    public Observable<Song> getCurrentSongForBand(String bandId) {
        return RxFirebaseDatabase.observeSingleValueEvent(mDatabase.getReference(bands).child(bandId).child(currentSong), Song.class);
    }

    /**
     * create user join request to band
     * @param band
     * @param currentUser
     */
    public void createUserJoinBandRequest(Band band, FirebaseUser currentUser) {
        mDatabase.getReference(bands).child(band.getId()).child(userJontRequests).child(currentUser.getUid()).setValue(true);
    }

    /**
     * accept user join request to band
     * @param band
     * @param userId
     */
    public void acceptUserJoinRequest(Band band, String userId) {
        mDatabase.getReference(bands).child(band.getId()).child(userJontRequests).child(userId).removeValue();
        mDatabase.getReference(bands).child(band.getId()).child(UserService.users).child(userId).setValue(true);
        mDatabase.getReference(UserService.users).child(userId).child(bands).child(band.getId()).setValue(true);
    }

    /**
     * rejest user join request to band
     * @param band
     * @param userId
     */
    public void rejectUserJoindRequest(Band band, String userId) {
        mDatabase.getReference(bands).child(band.getId()).child(userJontRequests).child(userId).removeValue();
    }

    /**
     * add song for band
     * @param bandId
     * @param song
     */
    public void addSongForBand(String bandId, Song song) {
        mDatabase.getReference(bands).child(bandId).child(songs).child(song.getId()).setValue(song);
    }

    /**
     * update current song for band
     * @param bandId
     * @param song
     */
    public void setCurrentSongForBand(String bandId, Song song) {
        mDatabase.getReference(bands).child(bandId).child(currentSong).setValue(song);
    }
}
