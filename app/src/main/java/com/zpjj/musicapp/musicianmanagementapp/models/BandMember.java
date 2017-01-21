package com.zpjj.musicapp.musicianmanagementapp.models;

/**
 * Created by daniel on 15.01.17.
 */

public class BandMember {
    private String uuid;
    private boolean confirmed;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
