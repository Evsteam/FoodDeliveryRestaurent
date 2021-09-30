package com.evs.foodexp.models;

import com.google.gson.annotations.SerializedName;

public class CashoutModel {

    @SerializedName("requestId")
    String requestId;

    @SerializedName("requestAmount")
    String requestAmount;

    @SerializedName("sendAmount")
    String sendAmount;

    @SerializedName("sendDate")
    String sendDate;

    @SerializedName("created")
    String created;

    public String getCreated() {
        return created;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getRequestAmount() {
        return requestAmount;
    }

    public String getSendAmount() {
        return sendAmount;
    }

    public String getSendDate() {
        return sendDate;
    }
}
