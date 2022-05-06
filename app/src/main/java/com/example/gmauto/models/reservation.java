package com.example.gmauto.models;

public class reservation {

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
}
