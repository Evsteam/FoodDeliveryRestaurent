package com.evs.foodexp.commonPkg.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddBookingDto {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("bookingGroupId")
    @Expose
    private String bookingGroupId;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookingGroupId() {
        return bookingGroupId;
    }

    public void setBookingGroupId(String bookingGroupId) {
        this.bookingGroupId = bookingGroupId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
