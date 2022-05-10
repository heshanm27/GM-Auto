package com.example.gmauto.models;

import android.os.Parcel;
import android.os.Parcelable;

public class reservation implements Parcelable {

    String ContactNumber;
    String  Email;
    String FullName;
    String PreferedDate;
    String PrefferedTime;
    String ServiceType;
    String Title;
    String VehicleRegistrat;
    String userID;
    String Status;

    public reservation() {
    }

    public reservation(String contactNumber, String email, String fullName, String preferedDate, String prefferedTime, String serviceType, String title, String vehicleRegistrat, String userID, String status) {
        ContactNumber = contactNumber;
        Email = email;
        FullName = fullName;
        PreferedDate = preferedDate;
        PrefferedTime = prefferedTime;
        ServiceType = serviceType;
        Title = title;
        VehicleRegistrat = vehicleRegistrat;
        this.userID = userID;
        Status = status;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getPreferedDate() {
        return PreferedDate;
    }

    public void setPreferedDate(String preferedDate) {
        PreferedDate = preferedDate;
    }

    public String getPrefferedTime() {
        return PrefferedTime;
    }

    public void setPrefferedTime(String prefferedTime) {
        PrefferedTime = prefferedTime;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getVehicleRegistrat() {
        return VehicleRegistrat;
    }

    public void setVehicleRegistrat(String vehicleRegistrat) {
        VehicleRegistrat = vehicleRegistrat;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ContactNumber);
        dest.writeString(this.Email);
        dest.writeString(this.FullName);
        dest.writeString(this.PreferedDate);
        dest.writeString(this.PrefferedTime);
        dest.writeString(this.ServiceType);
        dest.writeString(this.Title);
        dest.writeString(this.VehicleRegistrat);
        dest.writeString(this.userID);
        dest.writeString(this.Status);
    }

    public void readFromParcel(Parcel source) {
        this.ContactNumber = source.readString();
        this.Email = source.readString();
        this.FullName = source.readString();
        this.PreferedDate = source.readString();
        this.PrefferedTime = source.readString();
        this.ServiceType = source.readString();
        this.Title = source.readString();
        this.VehicleRegistrat = source.readString();
        this.userID = source.readString();
        this.Status = source.readString();
    }

    protected reservation(Parcel in) {
        this.ContactNumber = in.readString();
        this.Email = in.readString();
        this.FullName = in.readString();
        this.PreferedDate = in.readString();
        this.PrefferedTime = in.readString();
        this.ServiceType = in.readString();
        this.Title = in.readString();
        this.VehicleRegistrat = in.readString();
        this.userID = in.readString();
        this.Status = in.readString();
    }

    public static final Parcelable.Creator<reservation> CREATOR = new Parcelable.Creator<reservation>() {
        @Override
        public reservation createFromParcel(Parcel source) {
            return new reservation(source);
        }

        @Override
        public reservation[] newArray(int size) {
            return new reservation[size];
        }
    };
}
