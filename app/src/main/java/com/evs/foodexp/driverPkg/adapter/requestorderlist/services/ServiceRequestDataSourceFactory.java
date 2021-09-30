package com.evs.foodexp.driverPkg.adapter.requestorderlist.services;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.evs.foodexp.models.BookingModel;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;


public class ServiceRequestDataSourceFactory extends DataSource.Factory {
    private String user_id, status, usertype,action;
    private AuthStatusListener authListener;

    public ServiceRequestDataSourceFactory(String action, String userId, String status, String usertype, AuthStatusListener authListener) {
        this.user_id = userId;
        this.status = status;
        this.usertype = usertype;
        this.action = action;
        this.authListener = authListener;
    }

    //creating the mutable live data
    private MutableLiveData<PageKeyedDataSource<Integer, BookingModel>> itemLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource<Integer, BookingModel> create() {
        //getting our data source object
        ServiceRequestDataSource itemDataSource = new ServiceRequestDataSource(action,user_id,status,usertype,authListener);

        //posting the datasource to get the values
        itemLiveDataSource.postValue(itemDataSource);

        //returning the datasource
        return itemDataSource;
    }


    //getter for itemlivedatasource
    public MutableLiveData<PageKeyedDataSource<Integer, BookingModel>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}