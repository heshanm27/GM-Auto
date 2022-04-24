package com.example.gmauto.models;

public class reviews {
    Double rate;
    String review;
    String sparepartid;
    String  userid;
    String timestamp;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public reviews(Double rate, String review, String sparepartid, String userid, String timestamp,String userName) {
        this.rate = rate;
        this.review = review;
        this.sparepartid = sparepartid;
        this.userid = userid;
        this.timestamp = timestamp;
    }
}
