package com.example.gmauto.models;

import com.google.firebase.Timestamp;

public class contcatus {

    String Phoneno;
    String Email;
    String CustomerName;
    String ExtraDetails;
    String UserId;
    Timestamp Timestamp;

    public contcatus() {
    }

    public contcatus(String phoneno, String email, String customerName, String extraDetails, String userId, com.google.firebase.Timestamp timestamp) {
        Phoneno = phoneno;
        Email = email;
        CustomerName = customerName;
        ExtraDetails = extraDetails;
        UserId = userId;
        Timestamp = timestamp;
    }

    public String getPhoneno() {
        return Phoneno;
    }

    public void setPhoneno(String phoneno) {
        Phoneno = phoneno;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getExtraDetails() {
        return ExtraDetails;
    }

    public void setExtraDetails(String extraDetails) {
        ExtraDetails = extraDetails;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public com.google.firebase.Timestamp getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(com.google.firebase.Timestamp timestamp) {
        Timestamp = timestamp;
    }
}
