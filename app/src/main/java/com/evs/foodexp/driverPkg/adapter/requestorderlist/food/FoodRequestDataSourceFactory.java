package com.evs.foodexp.driverPkg.adapter.requestorderlist.food;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.evs.foodexp.models.RequestModel;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;


public class FoodRequestDataSourceFactory extends DataSource.Factory {
    private String user_id,action;
    private AuthStatusListener authListener;

    public FoodRequestDataSourceFactory(String action, String userId, AuthStatusListener authListener) {
        this.user_id = userId;
        this.action = action;
        this.authListener = authListener;
    }

    //creating the mutable live data
    private MutableLiveData<PageKeyedDataSource<Integer, RequestModel>> itemLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource<Integer, RequestModel> create() {
        //getting our data source object
        FoodRequestDataSource itemDataSource = new FoodRequestDataSource(action,user_id,authListener);

        //posting the datasource to get the values
        itemLiveDataSource.postValue(itemDataSource);

        //returning the datasource
        return itemDataSource;
    }


    //getter for itemlivedatasource
    public MutableLiveData<PageKeyedDataSource<Integer, RequestModel>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}