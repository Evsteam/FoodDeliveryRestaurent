package com.evs.foodexp.restaurentPkg.adapter.reviews;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.evs.foodexp.models.ReviewModel;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;


public class ReviewDataSourceFactory extends DataSource.Factory {
    private String user_id,userType,status,dateBy;
    private AuthStatusListener authListener;

    public ReviewDataSourceFactory(String userId,  AuthStatusListener authListener) {
        this.user_id = userId;
        this.authListener = authListener;
    }

    //creating the mutable live data
    private MutableLiveData<PageKeyedDataSource<Integer, ReviewModel>> itemLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource<Integer, ReviewModel> create() {
        //getting our data source object
        ReviewDataSource itemDataSource = new ReviewDataSource(user_id,authListener);

        //posting the datasource to get the values
        itemLiveDataSource.postValue(itemDataSource);

        //returning the datasource
        return itemDataSource;
    }


    //getter for itemlivedatasource
    public MutableLiveData<PageKeyedDataSource<Integer, ReviewModel>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}