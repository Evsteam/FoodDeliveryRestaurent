package com.evs.foodexp.models;

import com.google.gson.annotations.SerializedName;

public class ItemModel {
    @SerializedName(value = "name",alternate = {"foodName"})
    String name;

    @SerializedName("id")
    String id;

    @SerializedName(value = "quintity", alternate = {"quantity"})
    String quintity;

    @SerializedName(value = "prize", alternate = {"specialPrice"})
    String prize;

    @SerializedName("resturentId")
    String resturentId;

    @SerializedName("resturentName")
    String resturentName;

    @SerializedName("image_1")
    String image_1;

    @SerializedName("foodTag")
    String foodTag;

    public String getFoodTag() {
        return foodTag;
    }

    public String getResturentName() {
        return resturentName;
    }

    public String getImage_1() {
        return image_1;
    }

    public String getResturentId() {
        return resturentId;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuintity() {
        return quintity;
    }

    public void setQuintity(String quintity) {
        this.quintity = quintity;
    }
}
