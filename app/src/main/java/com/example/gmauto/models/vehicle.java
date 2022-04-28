package com.example.gmauto.models;

import java.sql.Timestamp;
import java.util.List;

public class vehicle {

    String Title ;
    String discription;
    String ManufacturingYear;
    String FuleType;
    String Mileage;
    String Color;
    String TransMissionType;
    String Enginecapaity;
    String ImgUrl;
    List<String> Amenities;
    Timestamp timestamp;

    public vehicle() {
    }

    public vehicle(String title, String discription, String manufacturingYear, String fuleType, String mileage, String color, String transMissionType, String enginecapaity, String imgUrl, List<String> amenities, Timestamp timestamp) {
        this.Title = title;
        this.discription = discription;
        this.ManufacturingYear = manufacturingYear;
        this.FuleType = fuleType;
        this.Mileage = mileage;
        this.Color = color;
        this.TransMissionType = transMissionType;
        this.Enginecapaity = enginecapaity;
        this.ImgUrl = imgUrl;
        this.Amenities = amenities;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getManufacturingYear() {
        return ManufacturingYear;
    }

    public void setManufacturingYear(String manufacturingYear) {
        ManufacturingYear = manufacturingYear;
    }

    public String getFuleType() {
        return FuleType;
    }

    public void setFuleType(String fuleType) {
        FuleType = fuleType;
    }

    public String getMileage() {
        return Mileage;
    }

    public void setMileage(String mileage) {
        Mileage = mileage;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getTransMissionType() {
        return TransMissionType;
    }

    public void setTransMissionType(String transMissionType) {
        TransMissionType = transMissionType;
    }

    public String getEnginecapaity() {
        return Enginecapaity;
    }

    public void setEnginecapaity(String enginecapaity) {
        Enginecapaity = enginecapaity;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public List<String> getAmenities() {
        return Amenities;
    }

    public void setAmenities(List<String> amenities) {
        Amenities = amenities;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
