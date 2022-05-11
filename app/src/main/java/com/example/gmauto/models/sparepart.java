package com.example.gmauto.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class sparepart implements Parcelable {
    private String productName;
    private String productDiscription;
    private String img;
    private Double productPrice;
    private  Double rateavg;
    Timestamp Timestamp;
    private  String SearchKey;

    public com.google.firebase.Timestamp getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(com.google.firebase.Timestamp timestamp) {
        Timestamp = timestamp;
    }



    public String getSearchKey() {
        return SearchKey;
    }

    public void setSearchKey(String searchKey) {
        SearchKey = searchKey;
    }


    public sparepart(){}
    public sparepart(String productName, String productDiscription, String img, Double productPrice, Double rateavg, com.google.firebase.Timestamp time, String SearchKey) {
        this.productName = productName;
        this.productDiscription = productDiscription;
        this.img = img;
        this.productPrice = productPrice;
        this.rateavg = rateavg;
        this.Timestamp=time;
        this.SearchKey =SearchKey;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDiscription() {
        return productDiscription;
    }

    public void setProductDiscription(String productDiscription) {
        this.productDiscription = productDiscription;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Double getRateavg() {
        return rateavg;
    }

    public void setRateavg(Double rateavg) {
        this.rateavg = rateavg;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productName);
        dest.writeString(this.productDiscription);
        dest.writeString(this.img);
        dest.writeValue(this.productPrice);
        dest.writeValue(this.rateavg);
        dest.writeParcelable(this.Timestamp, flags);
        dest.writeString(this.SearchKey);
    }

    public void readFromParcel(Parcel source) {
        this.productName = source.readString();
        this.productDiscription = source.readString();
        this.img = source.readString();
        this.productPrice = (Double) source.readValue(Double.class.getClassLoader());
        this.rateavg = (Double) source.readValue(Double.class.getClassLoader());
        this.Timestamp = source.readParcelable(com.google.firebase.Timestamp.class.getClassLoader());
        this.SearchKey = source.readString();
    }

    protected sparepart(Parcel in) {
        this.productName = in.readString();
        this.productDiscription = in.readString();
        this.img = in.readString();
        this.productPrice = (Double) in.readValue(Double.class.getClassLoader());
        this.rateavg = (Double) in.readValue(Double.class.getClassLoader());
        this.Timestamp = in.readParcelable(com.google.firebase.Timestamp.class.getClassLoader());
        this.SearchKey = in.readString();
    }

    public static final Creator<sparepart> CREATOR = new Creator<sparepart>() {
        @Override
        public sparepart createFromParcel(Parcel source) {
            return new sparepart(source);
        }

        @Override
        public sparepart[] newArray(int size) {
            return new sparepart[size];
        }
    };
}
