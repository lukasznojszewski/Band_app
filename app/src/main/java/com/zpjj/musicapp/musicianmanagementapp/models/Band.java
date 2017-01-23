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
    private Map<String, Boolean> users = new HashMap<>();

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

    public Map<String, Boolean> getUsers() {
        return users;
    }

    public void setUsers(Map<String, Boolean> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return name;
    }
}
