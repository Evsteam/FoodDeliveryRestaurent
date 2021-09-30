package com.evs.foodexp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RequestModel {

    @SerializedName("foodrequestId")
    String foodrequestId;

    @SerializedName("foodorderId")
    String foodorderId;

    @SerializedName("resturentId")
    String resturentId;

    @SerializedName("address")
    String address;

    @SerializedName("state")
    String state;

    @SerializedName("city")
    String city;

    @SerializedName("zipcode")
    String zipcode;

    @SerializedName("name")
    String name;

    @SerializedName("phone")
    String phone;

    @SerializedName("deliveryLat")
    String deliveryLat;

    @SerializedName("deliveryLong")
    String deliveryLong;

    @SerializedName("deliveryFee")
    String deliveryFee;

    @SerializedName("resturentName")
    String resturentName;

    @SerializedName(value = "resturentddress", alternate = {"resturentAddress"})
    String resturentddress;

    @SerializedName("resturentcontactNumber")
    String resturentcontactNumber;

    @SerializedName(value = "resturentlatitude", alternate = {"resturentLatitude"})
    String resturentlatitude;

    @SerializedName(value = "resturentlongitude", alternate = {"resturentLongitude"})
    String resturentlongitude;

    @SerializedName("created")
    String created;

    @SerializedName("resturentImage")
    String resturentImage;

    @SerializedName("totalAmount")
    String totalAmount;

    @SerializedName(value = "TIPAmount", alternate = {"TIP"})
    String TIPAmount;

    @SerializedName("EstTime")
    String EstTime;

    @SerializedName("Mile")
    String Mile;

    @SerializedName("workPlace")
    String workPlace;

    @SerializedName("landmark")
    String landmark;

    @SerializedName("status")
    String status;

    @SerializedName("userName")
    String userName;

    @SerializedName(value = "wahtUwant", alternate = {"whatYouWant"})
    String wahtUwant;

    @SerializedName("storecity")
    String storecity;

    @SerializedName("specialNote")
    String specialNote;

    @SerializedName("driverId")
    String driverId;

    @SerializedName("driverName")
    String driverName;

    @SerializedName("driverAVG")
    String driverAVG;
    @SerializedName("driverContactNumber")
    String driverContactNumber;

    @SerializedName("driverImage")
    String driverImage;

    @SerializedName("salesTax")
    String salesTax;

    @SerializedName("imageUpload")
    String imageUpload;

    public String getImageUpload() {
        return imageUpload;
    }

    public String getSalesTax() {
        return salesTax;
    }

    public String getDriverContactNumber() {
        return driverContactNumber;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public String getDriverAVG() {
        return driverAVG;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getStorecity() {
        return storecity;
    }

    public String getSpecialNote() {
        return specialNote;
    }

    public String getWahtUwant() {
        return wahtUwant;
    }

    public String getUserName() {
        return userName;
    }

    public String getStatus() {
        return status;
    }

    public String getLandmark() {
        return landmark;
    }

    @SerializedName(value = "foodDetails", alternate = {"foodDetailsNew"})
    ArrayList<ItemModel> foodDetails;

    public String getWorkPlace() {
        return workPlace;
    }

    public String getEstTime() {
        return EstTime;
    }

    public String getMile() {
        return Mile;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getTIPAmount() {
        return TIPAmount;
    }

    public String getCreated() {
        return created;
    }

    public String getResturentImage() {
        return resturentImage;
    }

    public ArrayList<ItemModel> getFoodDetails() {
        return foodDetails;
    }

    public String getFoodrequestId() {
        return foodrequestId;
    }

    public String getFoodorderId() {
        return foodorderId;
    }

    public String getResturentId() {
        return resturentId;
    }

    public String getAddress() {
        return address;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDeliveryLat() {
        return deliveryLat;
    }

    public String getDeliveryLong() {
        return deliveryLong;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public String getResturentName() {
        return resturentName;
    }

    public String getResturentddress() {
        return resturentddress;
    }

    public String getResturentcontactNumber() {
        return resturentcontactNumber;
    }

    public String getResturentlatitude() {
        return resturentlatitude;
    }

    public String getResturentlongitude() {
        return resturentlongitude;
    }
}
