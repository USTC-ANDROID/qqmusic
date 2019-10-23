package com.ustc.music.entity;

public class Music {

    private String avatar;
    private String title;
    private String lrc;
    private String musicSource;

    public Music() {
    }

    public Music(String avatar, String title, String lrc, String musicSource) {
        this.avatar = avatar;
        this.title = title;
        this.lrc = lrc;
        this.musicSource = musicSource;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }

    public String getMusicSource() {
        return musicSource;
    }

    public void setMusicSource(String musicSource) {
        this.musicSource = musicSource;
    }
}
