package com.example.gmauto.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class vehicle implements Parcelable {

    String Title ;
    String img;
    Double Price;
    String Discription;
    private Map<String, Object> details;
    List<String> Amenities;
    Timestamp Timestamp;

    public com.google.firebase.Timestamp getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(com.google.firebase.Timestamp timestamp) {
        Timestamp = timestamp;
    }

    public vehicle() {
    }

    public vehicle(String title, String img, Double price, Map<String, Object> details, List<String> amenities,String Discription,Timestamp Timestamp) {
        this.Title = title;
        this.img = img;
        this. Price = price;
        this.details = details;
        this.Amenities = amenities;
        this.Discription =Discription;
        this.Timestamp=Timestamp;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }
    public String getDiscription() {
        return Discription;
    }

    public void setDiscription(String discription) {
        Discription = discription;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String imgUrl) {
        img = imgUrl;
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
        dest.writeString(this.img);
        dest.writeValue(this.Price);
        dest.writeString(this.Discription);
        dest.writeInt(this.details.size());
        for (Map.Entry<String, Object> entry : this.details.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString((String) entry.getValue());
        }
        dest.writeStringList(this.Amenities);
    }

    public void readFromParcel(Parcel source) {
        this.Title = source.readString();
        this.img = source.readString();
        this.Price = (Double) source.readValue(Double.class.getClassLoader());
        this.Discription = source.readString();
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
        this.img = in.readString();
        this.Price = (Double) in.readValue(Double.class.getClassLoader());
        this.Discription = in.readString();
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
