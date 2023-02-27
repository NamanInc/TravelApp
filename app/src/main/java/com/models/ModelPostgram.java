package com.models;

public class ModelPostgram {
    private String Username,Caption,PostUrl,Tick,profileImage,time,Uid,pID;


    public ModelPostgram() {
    }

    public ModelPostgram(String username, String caption, String postUrl, String tick, String profileImage, String time, String uid, String pID) {
        Username = username;
        Caption = caption;
        PostUrl = postUrl;
        Tick = tick;
        this.profileImage = profileImage;
        this.time = time;
        Uid = uid;
        this.pID = pID;
    }


    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    public String getPostUrl() {
        return PostUrl;
    }

    public void setPostUrl(String postUrl) {
        PostUrl = postUrl;
    }

    public String getTick() {
        return Tick;
    }

    public void setTick(String tick) {
        Tick = tick;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }
}
