package com.example.gmauto.models;

public class sparepart {
    private String productName;
    private String productDiscription;
    private String img;
    private Double productPrice;
    private  Double rateavg;

    public sparepart(){}
    public sparepart(String productName, String productDiscription, String img, Double productPrice, Double rateavg) {
        this.productName = productName;
        this.productDiscription = productDiscription;
        this.img = img;
        this.productPrice = productPrice;
        this.rateavg = rateavg;
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
}
