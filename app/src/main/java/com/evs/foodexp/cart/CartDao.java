package com.evs.foodexp.cart;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CartDao {

    @Insert
    Long insert(Cart note);

    @Update
    void update(Cart note);

    @Delete
    void delete(Cart note);

    @Query("SELECT * FROM cart_table")
    LiveData<List<Cart>> getAllData();

    @Query("UPDATE cart_table SET quintity =:quintity WHERE menuId=:ids")
    void updateCart(String quintity, String ids);

    @Query("SELECT * FROM cart_table WHERE menuId=:itemId LIMIT 1")
    Cart getupdatedData(String itemId);

    @Query("SELECT * FROM cart_table WHERE menuId=:itemId LIMIT 1")
    LiveData<Cart> getliveNote(String itemId);

//    @Query("SELECT * FROM cart_table WHERE type=:type")
//    LiveData<List<Cart>> getMemberList(String type);


}
