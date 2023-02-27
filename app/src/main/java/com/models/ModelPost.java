package com.models;

public class ModelPost {

    String PlaceName, description, town, Username, profileImage, Tick, Pincode, ThumbnailUrl, VideoUrl, Approved, pId, uid, State, Category, plikes, Image1, Image2, image3, image4, Rating , videobyus;


    public ModelPost() {


    }

    public ModelPost(String placeName, String description, String town, String username, String profileImage, String tick, String pincode, String thumbnailUrl, String videoUrl, String approved, String pId, String uid, String state, String category, String plikes, String image1, String image2, String image3, String image4, String rating, String videobyus) {
        PlaceName = placeName;
        this.description = description;
        this.town = town;
        Username = username;
        this.profileImage = profileImage;
        Tick = tick;
        Pincode = pincode;
        ThumbnailUrl = thumbnailUrl;
        VideoUrl = videoUrl;
        Approved = approved;
        this.pId = pId;
        this.uid = uid;
        State = state;
        Category = category;
        this.plikes = plikes;
        Image1 = image1;
        Image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        Rating = rating;
        this.videobyus = videobyus;
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public void setPlaceName(String placeName) {
        PlaceName = placeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getTick() {
        return Tick;
    }

    public void setTick(String tick) {
        Tick = tick;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getThumbnailUrl() {
        return ThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        ThumbnailUrl = thumbnailUrl;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }

    public String getApproved() {
        return Approved;
    }

    public void setApproved(String approved) {
        Approved = approved;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getPlikes() {
        return plikes;
    }

    public void setPlikes(String plikes) {
        this.plikes = plikes;
    }

    public String getImage1() {
        return Image1;
    }

    public void setImage1(String image1) {
        Image1 = image1;
    }

    public String getImage2() {
        return Image2;
    }

    public void setImage2(String image2) {
        Image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getVideobyus() {
        return videobyus;
    }

    public void setVideobyus(String videobyus) {
        this.videobyus = videobyus;
    }
}