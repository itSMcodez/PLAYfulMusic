package com.itsmcodez.playful.models;

import android.net.Uri;

public class PlaylistsModel {
    private String title, songsCount, songsDuration, albumId;
    private Uri albumArtwork;

    public PlaylistsModel(
            String title,
            String songsCount,
            String songsDuration,
            String albumId,
            Uri albumArtwork) {
        this.title = title;
        this.songsCount = songsCount;
        this.songsDuration = songsDuration;
        this.albumId = albumId;
        this.albumArtwork = albumArtwork;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSongsCount() {
        return this.songsCount;
    }

    public void setSongsCount(String songsCount) {
        this.songsCount = songsCount;
    }

    public String getSongsDuration() {
        return this.songsDuration;
    }

    public void setSongsDuration(String songsDuration) {
        this.songsDuration = songsDuration;
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
