package com.evs.foodexp.commonPkg.DTO;

public class EditProfileRequestModel {

    private String action;
    private String userId;
    private String address;
    private String fullname;
    private String contactNumber;
    private String image;
    private String device;
    private String deviceToken;
    private String lats;
    private String longs;

    public void setLats(String lats) {
        this.lats = lats;
    }

    public void setLongs(String longs) {
        this.longs = longs;
    }

    public String getLats() {
        return lats;
    }

    public String getLongs() {
        return longs;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
