package com.evs.foodexp.driverPkg.adapter.history.services;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.evs.foodexp.models.HistoryModel;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;


public class ServiceDataSourceFactory extends DataSource.Factory {
    private String user_id,action,status,usertype;
    private AuthStatusListener authListener;

    public ServiceDataSourceFactory(String action, String userId,String status,String usertype, AuthStatusListener authListener) {
        this.user_id = userId;
        this.action = action;
        this.usertype = usertype;
        this.status = status;
        this.authListener = authListener;
    }

    //creating the mutable live data
    private MutableLiveData<PageKeyedDataSource<Integer, HistoryModel>> itemLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource<Integer, HistoryModel> create() {
        //getting our data source object
        ServiceDataSource itemDataSource = new ServiceDataSource(action,user_id,status,usertype,authListener);

        //posting the datasource to get the values
        itemLiveDataSource.postValue(itemDataSource);

        //returning the datasource
        return itemDataSource;
    }


    //getter for itemlivedatasource
    public MutableLiveData<PageKeyedDataSource<Integer, HistoryModel>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}