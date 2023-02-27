package com.models;

public class ModelUsers {
    String Name , ProfileImage,Tick, UID , onlineStatus , typingTo;

    public ModelUsers() {


    }

    public ModelUsers(String name, String profileImage, String tick, String UID, String onlineStatus, String typingTo) {
        Name = name;
        ProfileImage = profileImage;
        Tick = tick;
        this.UID = UID;
        this.onlineStatus = onlineStatus;
        this.typingTo = typingTo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getTick() {
        return Tick;
    }

    public void setTick(String tick) {
        Tick = tick;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getTypingTo() {
        return typingTo;
    }

    public void setTypingTo(String typingTo) {
        this.typingTo = typingTo;
    }
}
