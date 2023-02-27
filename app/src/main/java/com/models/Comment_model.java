package com.models;

public class Comment_model {
    private String comment;



    private String publisher;



    public Comment_model() {
    }

    public Comment_model(String comment, String publisher) {
        this.comment = comment;
        this.publisher = publisher;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
