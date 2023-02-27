package com.models;

public class ModelVideos {
    private String Caption, Username, UID, profileurl, videoUrl, tick, pId;


    public ModelVideos() {


    }

    public ModelVideos(String caption, String username, String UID, String profileurl, String videoUrl, String tick, String pId) {
        Caption = caption;
        Username = username;
        this.UID = UID;
        this.profileurl = profileurl;
        this.videoUrl = videoUrl;
        this.tick = tick;
        this.pId = pId;
    }


    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getProfileurl() {
        return profileurl;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTick() {
        return tick;
    }

    public void setTick(String tick) {
        this.tick = tick;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
}