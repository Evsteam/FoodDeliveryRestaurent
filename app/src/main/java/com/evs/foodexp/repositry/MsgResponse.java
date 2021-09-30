package com.evs.foodexp.repositry;

import com.google.gson.annotations.SerializedName;

public class MsgResponse {
    //{"status":"success","msg":"Data save successfully.","data":
    // {"userId":32,"fullName":"raushan kumar","email":"abc32@gmail.com","contactNumber":"45124512","image":"","device":"IOS",
    // "deviceToken":"4541246454","socialId":"","socialType":"Facebook"}}

    @SerializedName("status")
    private String status;

    @SerializedName(value = "msg",alternate = {"totalCount","StripeStatus"})
    private String msg;

    @SerializedName(value = "totalCartItem",alternate = {"foodOrderGroupId","totalAmount","wallet","percentage","price","url"})
    private String totalCartItem;

    public MsgResponse(String status, String msg, String totalCartItem) {
        this.status = status;
        this.msg = msg;
        this.totalCartItem = totalCartItem;
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

    public String getTotalCartItem() {
        return totalCartItem;
    }

    public void setTotalCartItem(String totalCartItem) {
        this.totalCartItem = totalCartItem;
    }
}
