package com.itsmcodez.playful.models;

import android.net.Uri;

public class ArtistsModel {
    private String artist, albumId;
    private Uri albumArtwork;

    public ArtistsModel(String artist, String albumId, Uri albumArtwork) {
        this.artist = artist;
        this.albumId = albumId;
        this.albumArtwork = albumArtwork;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public Uri getAlbumArtwork() {
        return this.albumArtwork;
    }

    public void setAlbumArtwork(Uri albumArtwork) {
        this.albumArtwork = albumArtwork;
    }
}
