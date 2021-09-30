package com.evs.foodexp.cart;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_table")
public class Cart {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String menuId;

    @NonNull
    private String resturentId;

    @NonNull
    private String categoryId;

    @NonNull
    private String foodName;

    @NonNull
    private String resturentName;

    @NonNull
    private String quintity;

    @NonNull
    public String getQuintity() {
        return quintity;
    }

    public void setQuintity(@NonNull String quintity) {
        this.quintity = quintity;
    }

    public Cart() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(@NonNull String menuId) {
        this.menuId = menuId;
    }

    @NonNull
    public String getResturentId() {
        return resturentId;
    }

    public void setResturentId(@NonNull String resturentId) {
        this.resturentId = resturentId;
    }

    @NonNull
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NonNull String categoryId) {
        this.categoryId = categoryId;
    }

    @NonNull
    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(@NonNull String foodName) {
        this.foodName = foodName;
    }

    @NonNull
    public String getResturentName() {
        return resturentName;
    }

    public void setResturentName(@NonNull String resturentName) {
        this.resturentName = resturentName;
    }
}
