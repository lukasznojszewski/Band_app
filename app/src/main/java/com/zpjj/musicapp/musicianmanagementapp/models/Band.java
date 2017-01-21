package com.zpjj.musicapp.musicianmanagementapp.models;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.UUID;

/**
 * Created by daniel on 15.01.17.
 */

public class Band {
    private String id;
    private String name;
    private String masterUID;
    private List<String> membersUID;

    public Band() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMembersUID() {
        return membersUID;
    }

    public void setMembersUID(List<String> membersUID) {
        this.membersUID = membersUID;
    }

    public String getMasterUID() {
        return masterUID;
    }

    public void setMasterUID(String masterUID) {
        this.masterUID = masterUID;
    }
}
