package com.evs.foodexp.driverPkg.adapter.service_earning;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.evs.foodexp.models.BookingModel;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;


public class ServiceEarningSourceFactory extends DataSource.Factory {
    private String user_id,userType,status,dateBy;
    private AuthStatusListener authListener;

    public ServiceEarningSourceFactory(String userId, String userType, String status, String dateBy, AuthStatusListener authListener) {
        this.user_id = userId;
        this.status = status;
        this.userType = userType;
        this.dateBy = dateBy;
        this.authListener = authListener;
    }

    //creating the mutable live data
    private MutableLiveData<PageKeyedDataSource<Integer, BookingModel>> itemLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource<Integer, BookingModel> create() {
        //getting our data source object
        ServiceEarningDataSource itemDataSource = new ServiceEarningDataSource(user_id,userType,status,dateBy,authListener);

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