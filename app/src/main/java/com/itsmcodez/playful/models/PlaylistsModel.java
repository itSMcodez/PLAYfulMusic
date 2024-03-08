package com.itsmcodez.playful.models;

import android.net.Uri;
import java.util.ArrayList;

public class PlaylistsModel {
    private String title, songsCount, songsDuration, albumId;
    private Uri albumArtwork;
    private ArrayList<PlaylistSongsModel> songs;

    public PlaylistsModel(
            String title,
            String songsCount,
            String songsDuration,
            String albumId) {
        this.title = title;
        this.songsCount = songsCount;
        this.songsDuration = songsDuration;
        this.albumId = albumId;
        this.songs = new ArrayList<>();
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

    public ArrayList<PlaylistSongsModel> getSongs() {
        return this.songs;
    }

    public void setSongs(ArrayList<PlaylistSongsModel> songs) {
        this.songs = songs;
    }
}
