package com.zpjj.musicapp.musicianmanagementapp.models;

import com.zpjj.musicapp.musicianmanagementapp.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daniel on 22.01.17.
 */

public class Band {
    private String id;
    private String name;
    private String masterUID;
    private Map<String,Boolean> userJoinRequest = new HashMap<>();
    private Map<String, Boolean> users = new HashMap<>();
    private Map<String, Song> songs = new HashMap<>();

    public Band() {
    }

    public Band(String name, String masterUID) {
        this.id = Utils.getUUID();
        this.name = name;
        this.masterUID = masterUID;
        this.users.put(masterUID, true);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMasterUID() {
        return masterUID;
    }

    public void setMasterUID(String masterUID) {
        this.masterUID = masterUID;
    }

    public Map<String, Boolean> getUserJoinRequest() {
        return userJoinRequest;
    }

    public void setUserJoinRequest(Map<String, Boolean> userJoinRequest) {
        this.userJoinRequest = userJoinRequest;
    }

    public Map<String, Boolean> getUsers() {
        return users;
    }

    public void setUsers(Map<String, Boolean> users) {
        this.users = users;
    }

    public Map<String, Song> getSongs() {
        return songs;
    }

    public void setSongs(Map<String, Song> songs) {
        this.songs = songs;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(((Band)obj).getId().equals(this.getId())) {
            return true;
        } else {
            return false;
        }
    }
}
