package com.evs.foodexp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("middleName")
    @Expose
    private String middleName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("zipCode")
    @Expose
    private String zipCode;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("contactNumber")
    @Expose
    private String contactNumber;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("device")
    @Expose
    private String device;
    @SerializedName("deviceToken")
    @Expose
    private String deviceToken;
    @SerializedName("firebaseId")
    @Expose
    private String firebaseId;
    @SerializedName("socialId")
    @Expose
    private String socialId;
    @SerializedName("socialType")
    @Expose
    private String socialType;

    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("companyBackground")
    @Expose
    private String companyBackground;
    @SerializedName("accountType")
    @Expose
    private String accountType;

    @SerializedName("RoutingNo")
    @Expose
    private String RoutingNo;

    @SerializedName("branchName")
    @Expose
    private String branchName;

    @SerializedName("AccountNo")
    @Expose
    private String AccountNo;
    @SerializedName("BankName")
    @Expose
    private String BankName;
    @SerializedName("AccountHolderName")
    @Expose
    private String AccountHolderName;

    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("foodTag")
    @Expose
    private String foodTag;

    @SerializedName("wallet")
    @Expose
    private String wallet;

    @SerializedName("drivlingImage")
    @Expose
    private String drivlingImage;

    @SerializedName("cardholderId")
    @Expose
    private String cardholderId;

    @SerializedName("stripeAccountNo")
    @Expose
    private String stripeAccountNo;

    @SerializedName("StripeStatus")
    @Expose
    private String StripeStatus;

    @SerializedName("stripeAccountUrl")
    @Expose
    private String stripeAccountUrl;


    public String getStripeAccountNo() {
        return stripeAccountNo;
    }

    public String getStripeStatus() {
        return StripeStatus;
    }

    public String getStripeAccountUrl() {
        return stripeAccountUrl;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getCardholderId() {
        return cardholderId;
    }

    public String getDrivlingImage() {
        return drivlingImage;
    }

    public void setDrivlingImage(String drivlingImage) {
        this.drivlingImage = drivlingImage;
    }

    @SerializedName("ssnImage")
    @Expose
    private String ssnImage;

    @SerializedName("date_5")
    @Expose
    private String date_5;

    public String getDate_5() {
        return date_5;
    }

    public String getSsnImage() {
        return ssnImage;
    }

    public void setSsnImage(String ssnImage) {
        this.ssnImage = ssnImage;
    }

    public String getWallet() {
        return wallet;
    }

    public String getFoodTag() {
        return foodTag;
    }

    public void setFoodTag(String foodTag) {
        this.foodTag = foodTag;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLogitude() {
        return longitude;
    }

    public void setLogitude(String logitude) {
        this.longitude = logitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCompanyBackground() {
        return companyBackground;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getRoutingNo() {
        return RoutingNo;
    }

    public String getAccountNo() {
        return AccountNo;
    }

    public String getBankName() {
        return BankName;
    }

    public String getAccountHolderName() {
        return AccountHolderName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getSocialType() {
        return socialType;
    }

    public void setSocialType(String socialType) {
        this.socialType = socialType;
    }


}
