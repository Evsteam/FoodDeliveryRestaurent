package com.evs.foodexp.models;

import com.google.gson.annotations.SerializedName;

public class BookingModel {
    @SerializedName("bookingId")
    String bookingId;

    @SerializedName("groupId")
    String groupId;

    @SerializedName("address")
    String address;

    @SerializedName("bookingDate")
    String bookingDate;


    @SerializedName("contactNumber")
    String contactNumber;

    @SerializedName("email")
    String email;

    @SerializedName(value = "fullName", alternate = {"userName"})
    String fullName;

    @SerializedName("image")
    String image;

    @SerializedName("serviceName")
    String serviceName;

    @SerializedName("slote")
    String slote;

    @SerializedName("status")
    String status;

    @SerializedName("totalAmount")
    String totalAmount;

    @SerializedName("userId")
    String userId;

    @SerializedName("zipCode")
    String zipCode;

    @SerializedName("serviceAmount")
    String serviceAmount;

    @SerializedName("TIP")
    String TIP;

    @SerializedName("userPhone")
    String userPhone;

    @SerializedName("longitude")
    String longitude;

    @SerializedName("latidude")
    String latidude;

    @SerializedName("vendorEmail")
    String vendorEmail;

    @SerializedName("vendorPhone")
    String vendorPhone;

    @SerializedName("vendorName")
    String vendorName;

    @SerializedName("vendorImage")
    String vendorImage;

    @SerializedName("vendorAddress")
    String vendorAddress;

    @SerializedName("vendorId")
    String vendorId;

    @SerializedName("vendorAVGRating")
    String vendorAVGRating;

    @SerializedName("specialNote")
    String specialNote;

    @SerializedName("salesTax")
    String salesTax;

    @SerializedName("discountAmount")
    String discountAmount;

    @SerializedName("paymentStatus")
    String paymentStatus;

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

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getSalesTax() {
        return salesTax;
    }

    public String getSpecialNote() {
        return specialNote;
    }

    public String getVendorAVGRating() {
        return vendorAVGRating;
    }

    public String getVendorId() {
        return vendorId;
    }

    public String getVendorEmail() {
        return vendorEmail;
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

    public String getLongitude() {
        return longitude;
    }

    public String getLatidude() {
        return latidude;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getServiceAmount() {
        return serviceAmount;
    }

    public String getTIP() {
        return TIP;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getAddress() {
        return address;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getImage() {
        return image;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getSlote() {
        return slote;
    }

    public String getStatus() {
        return status;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getUserId() {
        return userId;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }
}