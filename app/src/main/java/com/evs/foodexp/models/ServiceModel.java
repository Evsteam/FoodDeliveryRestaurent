package com.evs.foodexp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ServiceModel {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("SubCat")
    @Expose
    private ArrayList<SubService> category;


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

    public ArrayList<SubService> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<SubService> category) {
        this.category = category;
    }
}

