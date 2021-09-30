package com.evs.foodexp.repositry;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListResponse<T> {

    @SerializedName("status")
    private String status;

    @SerializedName("msg")
    private String msg;

    @SerializedName("bseUrl")
    private String bseUrl;

    public String getBseUrl() {
        return bseUrl;
    }

    public void setBseUrl(String bseUrl) {
        this.bseUrl = bseUrl;
    }

    @SerializedName(value = "data", alternate = {"historyList","response"})
    private ArrayList<T> list;

    public ListResponse(String status, String msg, ArrayList<T> items) {
        this.status = status;
        this.msg = msg;
        this.list = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<T> getList() {
        return list;
    }

    public void setList(ArrayList<T> items) {
        this.list = items;
    }
}
