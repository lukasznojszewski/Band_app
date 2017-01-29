package com.zpjj.musicapp.musicianmanagementapp.models;

import com.zpjj.musicapp.musicianmanagementapp.utils.Utils;

import java.io.Serializable;

/**
 * Created by daniel on 24.01.17.
 */

public class Song implements Serializable{
    private String id;
    private String author;
    private String title;

    public Song() {
    }

    public Song(Builder songBuilder) {
        this.id = songBuilder.id;
        this.author = songBuilder.author;
        this.title = songBuilder.title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static class Builder {
        private String id;
        private String author;
        private String title;

        public Builder() {
            id = Utils.getUUID();
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setAuthor(String author) {
            this.author = author;
            return this;
        }

        public Song build() {
            return new Song(this);
        }
    }
}
