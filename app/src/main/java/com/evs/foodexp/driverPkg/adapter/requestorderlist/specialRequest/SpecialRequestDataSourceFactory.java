package com.evs.foodexp.driverPkg.adapter.requestorderlist.specialRequest;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.evs.foodexp.models.RequestModel;
import com.evs.foodexp.models.SpecialOrderModel;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;


public class SpecialRequestDataSourceFactory extends DataSource.Factory {
    private String user_id,action,userType;
    private AuthStatusListener authListener;

    public SpecialRequestDataSourceFactory(String action, String userId,String userType, AuthStatusListener authListener) {
        this.user_id = userId;
        this.action = action;
        this.userType = userType;
        this.authListener = authListener;
    }

    //creating the mutable live data
    private MutableLiveData<PageKeyedDataSource<Integer, SpecialOrderModel>> itemLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource<Integer, SpecialOrderModel> create() {
        //getting our data source object
        SpecialRequestDataSource itemDataSource = new SpecialRequestDataSource(action,user_id,userType,authListener);

        //posting the datasource to get the values
        itemLiveDataSource.postValue(itemDataSource);

        //returning the datasource
        return itemDataSource;
    }


    //getter for itemlivedatasource
    public MutableLiveData<PageKeyedDataSource<Integer, SpecialOrderModel>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}