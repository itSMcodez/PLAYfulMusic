package com.itsmcodez.playful.models;

public class PlaylistSongsModel {
    private String path, title, artist, duration, album, albumId, songId;

    public PlaylistSongsModel(
            String path,
            String title,
            String artist,
            String duration,
            String album,
            String albumId,
            String songId) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.album = album;
        this.albumId = albumId;
        this.songId = songId;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
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

    public String getSongId() {
        return this.songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }
}
