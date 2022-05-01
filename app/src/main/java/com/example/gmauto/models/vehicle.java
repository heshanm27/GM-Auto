package com.example.gmauto.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class vehicle implements Parcelable {

    String Title ;
    String ImgUrl;
    Double Price;
//    String discription;
//    String ManufacturingYear;
//    String FuleType;
//    String Mileage;
//    String Color;
//    String TransMissionType;
//    String Enginecapaity;
    private Map<String, Object> details;
    List<String> Amenities;
    public vehicle() {
    }

    public vehicle(String title, String imgUrl, Double price, Map<String, Object> details, List<String> amenities) {
        Title = title;
        ImgUrl = imgUrl;
        Price = price;
        this.details = details;
        Amenities = amenities;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    public List<String> getAmenities() {
        return Amenities;
    }

    public void setAmenities(List<String> amenities) {
        Amenities = amenities;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Title);
        dest.writeString(this.ImgUrl);
        dest.writeValue(this.Price);
        dest.writeInt(this.details.size());
        for (Map.Entry<String, Object> entry : this.details.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeParcelable((Parcelable) entry.getValue(), flags);
        }
        dest.writeStringList(this.Amenities);
    }

    public void readFromParcel(Parcel source) {
        this.Title = source.readString();
        this.ImgUrl = source.readString();
        this.Price = (Double) source.readValue(Double.class.getClassLoader());
        int detailsSize = source.readInt();
        this.details = new HashMap<String, Object>(detailsSize);
        for (int i = 0; i < detailsSize; i++) {
            String key = source.readString();
            Object value = source.readParcelable(Object.class.getClassLoader());
            this.details.put(key, value);
        }
        this.Amenities = source.createStringArrayList();
    }

    protected vehicle(Parcel in) {
        this.Title = in.readString();
        this.ImgUrl = in.readString();
        this.Price = (Double) in.readValue(Double.class.getClassLoader());
        int detailsSize = in.readInt();
        this.details = new HashMap<String, Object>(detailsSize);
        for (int i = 0; i < detailsSize; i++) {
            String key = in.readString();
            Object value = in.readParcelable(Object.class.getClassLoader());
            this.details.put(key, value);
        }
        this.Amenities = in.createStringArrayList();
    }

    public static final Parcelable.Creator<vehicle> CREATOR = new Parcelable.Creator<vehicle>() {
        @Override
        public vehicle createFromParcel(Parcel source) {
            return new vehicle(source);
        }

        @Override
        public vehicle[] newArray(int size) {
            return new vehicle[size];
        }
    };
}
