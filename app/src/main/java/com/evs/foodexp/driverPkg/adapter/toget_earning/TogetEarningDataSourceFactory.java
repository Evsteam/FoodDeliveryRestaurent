package com.evs.foodexp.driverPkg.adapter.toget_earning;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.evs.foodexp.models.GoGetModel;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;


public class TogetEarningDataSourceFactory extends DataSource.Factory {
    private String user_id,userType,status,dateBy,action;
    private AuthStatusListener authListener;

    public TogetEarningDataSourceFactory(String action,String userId, String userType, String status, String dateBy, AuthStatusListener authListener) {
        this.user_id = userId;
        this.status = status;
        this.userType = userType;
        this.dateBy = dateBy;
        this.action = action;
        this.authListener = authListener;
    }

    //creating the mutable live data
    private MutableLiveData<PageKeyedDataSource<Integer, GoGetModel>> itemLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource<Integer, GoGetModel> create() {
        //getting our data source object
        TogetEarningDataSource itemDataSource = new TogetEarningDataSource(action,user_id,userType,status,dateBy,authListener);

        //posting the datasource to get the values
        itemLiveDataSource.postValue(itemDataSource);

        //returning the datasource
        return itemDataSource;
    }


    //getter for itemlivedatasource
    public MutableLiveData<PageKeyedDataSource<Integer, GoGetModel>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}