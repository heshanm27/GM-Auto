package com.example.gmauto.models;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

public class orders {

    String ItemID;
    String ItemName;
    Double Total;
    Integer Quantity;
    String CustomerName;
    String Email;
    String Phoneno;
    String Address;
    String ExtraDetails;
    String UserId;
    Timestamp TimeStamp;
    String Status;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public orders() {
    }

    public orders(String itemID, String ItemName, Double total, Integer quantity, String customerName, String email, String phoneno, String address, String extraDetails, String userId, Timestamp timeStamp,String Status) {
        ItemID = itemID;
        this.ItemName = ItemName;
        Total = total;
        Quantity = quantity;
        CustomerName = customerName;
        Email = email;
        Phoneno = phoneno;
        Address = address;
        ExtraDetails = extraDetails;
        UserId = userId;
        TimeStamp = timeStamp;
        this.Status=Status;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemname) {
        this.ItemName = itemname;
    }

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double total) {
        Total = total;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public void setQuantity(Integer quantity) {
        Quantity = quantity;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneno() {
        return Phoneno;
    }

    public void setPhoneno(String phoneno) {
        Phoneno = phoneno;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
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

    public Timestamp getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        TimeStamp = timeStamp;
    }
}
