package com.evs.foodexp.models;

import com.google.gson.annotations.SerializedName;

public class SpecialOrderModel {

    @SerializedName("specialrequestId")
    String specialrequestId;

    @SerializedName("userId")
    String userId;

    @SerializedName("userName")
    String userName;

    @SerializedName("userPhone")
    String userPhone;

    @SerializedName("userImage")
    String userImage;

    @SerializedName("driverId")
    String driverId;

    @SerializedName("driverName")
    String driverName;

    @SerializedName("driverPhone")
    String driverPhone;

    @SerializedName("driverImage")
    String driverImage;

    @SerializedName("whatYouWant")
    String whatYouWant;

    @SerializedName("price")
    String price;

    @SerializedName("deliveryFee")
    String deliveryFee;

    @SerializedName("TIP")
    String TIP;

    @SerializedName("salesTax")
    String salesTax;

    @SerializedName("latitude")
    String latitude;

    @SerializedName("longitude")
    String longitude;

    @SerializedName("address")
    String address;

    @SerializedName("totalAmount")
    String totalAmount;

    @SerializedName("status")
    String status;

    @SerializedName("created")
    String created;

    @SerializedName("paymentStatus")
    String paymentStatus;

    @SerializedName("imageUpload")
    String imageUpload;

    @SerializedName("transactionFee")
    String transactionFee;

    public String getTransactionFee() {
        return transactionFee;
    }

    public String getImageUpload() {
        return imageUpload;
    }

    public String getPaymentstatus() {
        return paymentStatus;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getSpecialrequestId() {
        return specialrequestId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public String getWhatYouWant() {
        return whatYouWant;
    }

    public String getPrice() {
        return price;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public String getTIP() {
        return TIP;
    }

    public String getSalesTax() {
        return salesTax;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getAddress() {
        return address;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated() {
        return created;
    }
}
