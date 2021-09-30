package com.evs.foodexp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderModel {
    @SerializedName("foodorderId")
    String foodorderId;

    @SerializedName("resturentId")
    String resturentId;

    @SerializedName("userId")
    String userId;

    @SerializedName("foodId")
    String foodId;

    @SerializedName("discount")
    String discount;

    @SerializedName("couponCode")
    String couponCode;

    @SerializedName("totalAmount")
    String totalAmount;

    @SerializedName("specialNote")
    String specialNote;

    @SerializedName("TIP")
    String TIP;

    @SerializedName("resturentName")
    String resturentName;

    @SerializedName("resturentAddress")
    String resturentAddress;

    @SerializedName("resturentImage")
    String resturentImage;

    @SerializedName("resturentLatitude")
    String resturentLatitude;

    @SerializedName("resturentLongitude")
    String resturentLongitude;

    @SerializedName("driverName")
    String driverName;

    @SerializedName("userName")
    String userName;

    @SerializedName("noContact")
    String noContact;

    @SerializedName("status")
    String status;

    @SerializedName("created")
    String created;

    @SerializedName("userImage")
    String userImage;

    @SerializedName(value = "userAddress", alternate = {"address"})
    String userAddress;

    @SerializedName("deliveryLong")
    String deliveryLong;

    @SerializedName("deliveryLat")
    String deliveryLat;

    @SerializedName("cardNo")
    String cardNo;

    @SerializedName("workPlace")
    String workPlace;

    @SerializedName("landmark")
    String landmark;

    @SerializedName("driverId")
    String driverId;

    @SerializedName("whatYouWant")
    String whatYouWant;

    @SerializedName("driverAVG")
    String driverAVG;

    @SerializedName("salesTax")
    String salesTax;

    @SerializedName("PaymentStatus")
    String PaymentStatus;

    @SerializedName("foodOrderGroupId")
    String foodOrderGroupId;

    @SerializedName("transactionFee")
    String transactionFee;

    @SerializedName("AdminAmount")
    String AdminAmount;

    @SerializedName("driverStripeAccount")
    String driverStripeAccount;

    @SerializedName("imageUpload")
    String imageUpload;

    @SerializedName("phone")
    String phone;

    @SerializedName("DeliveryImage")
    String DeliveryImage;

    public String getDeliveryImage() {
        return DeliveryImage;
    }

    public String getPhone() {
        return phone;
    }

    public String getImageUpload() {
        return imageUpload;
    }

    public String getDriverStripeAccount() {
        return driverStripeAccount;
    }

    public String getAdminAmount() {
        return AdminAmount;
    }

    public String getTransactionFee() {
        return transactionFee;
    }

    public String getFoodOrderGroupId() {
        return foodOrderGroupId;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public String getSalesTax() {
        return salesTax;
    }

    public String getDriverAVG() {
        return driverAVG;
    }

    public String getWhatYouWant() {
        return whatYouWant;
    }

    @SerializedName(value = "foodDetails", alternate = {"foodDetailsNew"})
    ArrayList<ItemModel> foodDetails;

    public String getResturentLatitude() {
        return resturentLatitude;
    }

    public String getResturentLongitude() {
        return resturentLongitude;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getDeliveryLong() {
        return deliveryLong;
    }

    public String getDeliveryLat() {
        return deliveryLat;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getFoodorderId() {
        return foodorderId;
    }

    public String getResturentId() {
        return resturentId;
    }

    public String getUserId() {
        return userId;
    }

    public String getFoodId() {
        return foodId;
    }

    public String getDiscount() {
        return discount;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getSpecialNote() {
        return specialNote;
    }

    public String getTIP() {
        return TIP;
    }

    public String getResturentName() {
        return resturentName;
    }

    public String getResturentAddress() {
        return resturentAddress;
    }

    public String getResturentImage() {
        return resturentImage;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getUserName() {
        return userName;
    }

    public String getNoContact() {
        return noContact;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated() {
        return created;
    }

    public ArrayList<ItemModel> getFoodDetails() {
        return foodDetails;
    }
}