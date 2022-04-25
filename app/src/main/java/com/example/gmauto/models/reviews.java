package com.example.gmauto.models;

import com.google.firebase.Timestamp;

public class reviews {
    Double rate;
    String review;
    String sparepartid;
    String  userid;
    com.google.firebase.Timestamp Timestamp;
    String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public reviews() {
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getSparepartid() {
        return sparepartid;
    }

    public void setSparepartid(String sparepartid) {
        this.sparepartid = sparepartid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    public com.google.firebase.Timestamp getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(com.google.firebase.Timestamp timestamp) {
        Timestamp = timestamp;
    }

    public reviews(Double rate, String review, String sparepartid, String userid, com.google.firebase.Timestamp timestamp, String userName) {
        this.rate = rate;
        this.review = review;
        this.sparepartid = sparepartid;
        this.userid = userid;
        this.Timestamp = timestamp;
    }
}
