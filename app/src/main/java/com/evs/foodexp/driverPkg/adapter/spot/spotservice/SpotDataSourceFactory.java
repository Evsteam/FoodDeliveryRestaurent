package com.evs.foodexp.driverPkg.adapter.spot.spotservice;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.evs.foodexp.models.OrderModel;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;


public class SpotDataSourceFactory extends DataSource.Factory {
    private String user_id,userType,status,dateBy,action;
    private AuthStatusListener authListener;

    public SpotDataSourceFactory(String action,String userId, String userType, String status, String dateBy, AuthStatusListener authListener) {
        this.user_id = userId;
        this.status = status;
        this.userType = userType;
        this.dateBy = dateBy;
        this.action = action;
        this.authListener = authListener;
    }

    //creating the mutable live data
    private MutableLiveData<PageKeyedDataSource<Integer, OrderModel>> itemLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource<Integer, OrderModel> create() {
        //getting our data source object
        SpotDataSource itemDataSource = new SpotDataSource(action,user_id,userType,status,dateBy,authListener);

        //posting the datasource to get the values
        itemLiveDataSource.postValue(itemDataSource);

        //returning the datasource
        return itemDataSource;
    }


    //getter for itemlivedatasource
    public MutableLiveData<PageKeyedDataSource<Integer, OrderModel>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}