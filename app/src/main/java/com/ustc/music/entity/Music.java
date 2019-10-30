package com.ustc.music.entity;

public class Music {

    private String mid;
    private String author;
    private String avatar;
    private String title;
    private String lrc;
    private String musicSource;

    public Music(String mid, String author, String avatar, String lrc, String title) {
        this.mid = mid;
        this.author = author;
        this.avatar = avatar;
        this.lrc = lrc;
        this.title = title;
    }

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

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
