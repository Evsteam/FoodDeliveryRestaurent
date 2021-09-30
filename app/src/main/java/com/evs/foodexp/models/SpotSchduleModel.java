package com.evs.foodexp.models;

import com.google.gson.annotations.SerializedName;

public class SpotSchduleModel {

    @SerializedName("reviewId")
    private String reviewId;

    @SerializedName("message")
    private String message;

    @SerializedName("star")
    private String star;

    @SerializedName("userId")
    private String userId;

    @SerializedName("userName")
    private String userName;

    @SerializedName("userAddress")
    private String userAddress;

    @SerializedName("profile_picture")
    private String profile_picture;


    public String getMessage() {
        return message;
    }

    public String getStar() {
        return star;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getProfile_picture() {
        return profile_picture;
    }
}
