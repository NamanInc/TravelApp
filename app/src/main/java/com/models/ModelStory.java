package com.models;

public class ModelStory {

    private String imageurl;
    private long timestart;
    private long timeend;
    private String storyid;
    private String uid;

    public ModelStory() {



    }


    public ModelStory(String imageurl, long timestart, long timeend, String storyid, String uid) {
        this.imageurl = imageurl;
        this.timestart = timestart;
        this.timeend = timeend;
        this.storyid = storyid;
        this.uid = uid;
    }


    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public long getTimestart() {
        return timestart;
    }

    public void setTimestart(long timestart) {
        this.timestart = timestart;
    }

    public long getTimeend() {
        return timeend;
    }

    public void setTimeend(long timeend) {
        this.timeend = timeend;
    }

    public String getStoryid() {
        return storyid;
    }

    public void setStoryid(String storyid) {
        this.storyid = storyid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}