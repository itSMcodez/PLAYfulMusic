package com.itsmcodez.playful.models;

import android.net.Uri;

public class AlbumsModel {
    private String album, albumId;
    private Uri albumArtwork;

    public AlbumsModel(String album, String albumId, Uri albumArtwork) {
        this.album = album;
        this.albumId = albumId;
        this.albumArtwork = albumArtwork;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setAlbum(String album) {
        this.album = album;
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
