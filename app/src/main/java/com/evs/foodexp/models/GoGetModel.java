package com.evs.foodexp.models;

import com.google.gson.annotations.SerializedName;

public class GoGetModel {
    @SerializedName("togetDriverRequestId")
    String togetDriverRequestId;

    @SerializedName("togetRequestId")
    String togetRequestId;

    @SerializedName("userId")
    String userId;

    @SerializedName("driverId")
    String driverId;


    @SerializedName("created")
    String created;

    @SerializedName("TIP")
    String TIP;

    @SerializedName("deliveryFee")
    String deliveryFee;

    @SerializedName(value = "wahtUwant", alternate = {"whatYouWant"})
    String wahtUwant;

    @SerializedName("StoreCity")
    String StoreCity;

    @SerializedName("address")
    String address;

    @SerializedName("userName")
    String userName;

    @SerializedName("userImage")
    String userImage;

    @SerializedName("totalAmount")
    String totalAmount;

    @SerializedName("EPrice")
    String EPrice;

    @SerializedName("notes")
    String notes;

    @SerializedName("status")
    String status;

    @SerializedName("phone")
    String phone;


    @SerializedName("vendorPhone")
    String vendorPhone;

    @SerializedName("vendorName")
    String vendorName;

    @SerializedName("vendorImage")
    String vendorImage;

    @SerializedName("vendorAddress")
    String vendorAddress;

    @SerializedName("vendorAVGRating")
    String vendorAVGRating;

    @SerializedName("requestMoney")
    String requestMoney;

    @SerializedName("RequestStatus")
    String RequestStatus;

    @SerializedName("salesTax")
    String salesTax;

    @SerializedName("paymentStatus")
    String paymentStatus;

    @SerializedName("longitude")
    String longitude;

    @SerializedName("latitude")
    String latitude;

    @SerializedName("vendorId")
    String vendorId;

    public String getVendorId() {
        return vendorId;
    }

    @SerializedName("userPhone")
    String userPhone;

    @SerializedName("imageUpload")
    String imageUpload;

    @SerializedName("transactionFee")
    String transactionFee;

    @SerializedName("AdminAmount")
    String AdminAmount;

    @SerializedName("driverStripeAccount")
    String driverStripeAccount;

    public String getAdminAmount() {
        return AdminAmount;
    }

    public String getDriverStripeAccount() {
        return driverStripeAccount;
    }

    public String getTransactionFee() {
        return transactionFee;
    }

    public String getImageUpload() {
        return imageUpload;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatidude() {
        return latitude;
    }


    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getSalesTax() {
        return salesTax;
    }

    public String getRequestMoney() {
        return requestMoney;
    }

    public String getRequestStatus() {
        return RequestStatus;
    }

    public String getVendorAVGRating() {
        return vendorAVGRating;
    }

    public String getVendorPhone() {
        return vendorPhone;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getVendorImage() {
        return vendorImage;
    }

    public String getVendorAddress() {
        return vendorAddress;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }

    public String getEPrice() {
        return EPrice;
    }

    public String getNotes() {
        return notes;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getTogetDriverRequestId() {
        return togetDriverRequestId;
    }

    public void setTogetDriverRequestId(String togetDriverRequestId) {
        this.togetDriverRequestId = togetDriverRequestId;
    }

    public String getTogetRequestId() {
        return togetRequestId;
    }

    public void setTogetRequestId(String togetRequestId) {
        this.togetRequestId = togetRequestId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getTIP() {
        return TIP;
    }

    public void setTIP(String TIP) {
        this.TIP = TIP;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(String deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getWahtUwant() {
        return wahtUwant;
    }

    public void setWahtUwant(String wahtUwant) {
        this.wahtUwant = wahtUwant;
    }

    public String getStoreCity() {
        return StoreCity;
    }

    public void setStoreCity(String storeCity) {
        StoreCity = storeCity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}