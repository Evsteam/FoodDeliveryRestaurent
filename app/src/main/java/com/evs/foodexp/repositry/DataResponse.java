package com.evs.foodexp.repositry;

import com.google.gson.annotations.SerializedName;

public class DataResponse<T>  {

    @SerializedName("status")
    private String status;

    @SerializedName("msg")
    private String msg;

    @SerializedName(value = "data",alternate = {"historyList"})
    private T data;

    public DataResponse(String status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
