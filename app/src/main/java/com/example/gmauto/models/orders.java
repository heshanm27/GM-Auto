package com.example.gmauto.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

public class orders implements Parcelable {

    String ItemId;
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

    public orders(String ItemId, String ItemName, Double total, Integer quantity, String customerName, String email, String phoneno, String address, String extraDetails, String userId, Timestamp timeStamp,String Status) {
        ItemId = ItemId;
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
        return ItemId;
    }

    public void setItemID(String itemID) {
        ItemId = itemID;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ItemId);
        dest.writeString(this.ItemName);
        dest.writeValue(this.Total);
        dest.writeValue(this.Quantity);
        dest.writeString(this.CustomerName);
        dest.writeString(this.Email);
        dest.writeString(this.Phoneno);
        dest.writeString(this.Address);
        dest.writeString(this.ExtraDetails);
        dest.writeString(this.UserId);
        dest.writeParcelable(this.TimeStamp, flags);
        dest.writeString(this.Status);
    }

    public void readFromParcel(Parcel source) {
        this.ItemId = source.readString();
        this.ItemName = source.readString();
        this.Total = (Double) source.readValue(Double.class.getClassLoader());
        this.Quantity = (Integer) source.readValue(Integer.class.getClassLoader());
        this.CustomerName = source.readString();
        this.Email = source.readString();
        this.Phoneno = source.readString();
        this.Address = source.readString();
        this.ExtraDetails = source.readString();
        this.UserId = source.readString();
        this.TimeStamp = source.readParcelable(Timestamp.class.getClassLoader());
        this.Status = source.readString();
    }

    protected orders(Parcel in) {
        this.ItemId = in.readString();
        this.ItemName = in.readString();
        this.Total = (Double) in.readValue(Double.class.getClassLoader());
        this.Quantity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.CustomerName = in.readString();
        this.Email = in.readString();
        this.Phoneno = in.readString();
        this.Address = in.readString();
        this.ExtraDetails = in.readString();
        this.UserId = in.readString();
        this.TimeStamp = in.readParcelable(Timestamp.class.getClassLoader());
        this.Status = in.readString();
    }

    public static final Parcelable.Creator<orders> CREATOR = new Parcelable.Creator<orders>() {
        @Override
        public orders createFromParcel(Parcel source) {
            return new orders(source);
        }

        @Override
        public orders[] newArray(int size) {
            return new orders[size];
        }
    };
}
