package com.evs.foodexp.models;

import com.google.gson.annotations.SerializedName;

public class FoodOrderModel {
    @SerializedName(value = "togetrequestId", alternate = {"bookingId"})
    String togetrequestId;

    @SerializedName("userId")
    String userId;

    @SerializedName(value = "userName", alternate = {"fullName"})
    String userName;

    @SerializedName(value = "userImage", alternate = {"image"})
    String userImage;

    @SerializedName("wahtUwant")
    String wahtUwant;

    @SerializedName("StoreCity")
    String StoreCity;

    @SerializedName(value = "EPrice", alternate = {"totalAmount"})
    String EPrice;

    @SerializedName("TIP")
    String TIP;

    @SerializedName("deliveryFee")
    String deliveryFee;

    @SerializedName("notes")
    String notes;

    @SerializedName("address")
    String address;

    @SerializedName("latidude")
    String latidude;

    @SerializedName("longitude")
    String longitude;

    @SerializedName("noContact")
    String noContact;

    @SerializedName("status")
    String status;

    @SerializedName("created")
    String created;

    @SerializedName(value = "groupId",alternate = {"resturentId"})
    String groupId;

    @SerializedName("bookingDate")
    String bookingDate;

    @SerializedName("contactNumber")
    String contactNumber;

    @SerializedName("serviceName")
    String serviceName;

    @SerializedName("zipCode")
    String zipCode;

    @SerializedName("slote")
    String slote;

    public String getSlote() {
        return slote;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getTogetrequestId() {
        return togetrequestId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getWahtUwant() {
        return wahtUwant;
    }

    public String getStoreCity() {
        return StoreCity;
    }

    public String getEPrice() {
        return EPrice;
    }

    public String getTIP() {
        return TIP;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public String getNotes() {
        return notes;
    }

    public String getAddress() {
        return address;
    }

    public String getLatidude() {
        return latidude;
    }

    public String getLongitude() {
        return longitude;
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
}
