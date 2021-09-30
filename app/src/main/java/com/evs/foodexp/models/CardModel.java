package com.evs.foodexp.models;

import com.google.gson.annotations.SerializedName;

public class CardModel {
    @SerializedName("cardId")
    String cardId;

    @SerializedName("exp_month")
    String exp_month;

    @SerializedName("exp_year")
    String exp_year;

    @SerializedName("last4")
    String last4;

    @SerializedName("cardActive")
    String cardActive;

    @SerializedName("date_5")
    String date_5;

    @SerializedName("wallet")
    String wallet;

    @SerializedName("cardholderId")
    String cardholderId;

    @SerializedName("userId")
    String userId;

    public String getCardId() {
        return cardId;
    }

    public String getExp_month() {
        return exp_month;
    }

    public String getExp_year() {
        return exp_year;
    }

    public String getLast4() {
        return last4;
    }

    public String getCardActive() {
        return cardActive;
    }

    public String getDate_5() {
        return date_5;
    }

    public String getWallet() {
        return wallet;
    }

    public String getCardholderId() {
        return cardholderId;
    }

    public String getUserId() {
        return userId;
    }
}