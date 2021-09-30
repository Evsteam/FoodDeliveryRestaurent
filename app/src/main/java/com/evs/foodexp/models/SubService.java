package com.evs.foodexp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubService {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("stateCode")
    @Expose
    private String stateCode;

    public String getStateCode() {
        return stateCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private boolean slected = false;

    public boolean getSlected() {
        return slected;
    }

    public void setSlected(boolean slected) {
        this.slected = slected;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
