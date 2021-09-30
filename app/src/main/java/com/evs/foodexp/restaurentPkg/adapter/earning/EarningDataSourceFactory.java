package com.evs.foodexp.restaurentPkg.adapter.earning;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.evs.foodexp.models.OrderModel;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;


public class EarningDataSourceFactory extends DataSource.Factory {
    private String user_id,userType,status,dateBy;
    private AuthStatusListener authListener;

    public EarningDataSourceFactory(String userId,String userType,String status,String dateBy, AuthStatusListener authListener) {
        this.user_id = userId;
        this.status = status;
        this.userType = userType;
        this.dateBy = dateBy;
        this.authListener = authListener;
    }

    //creating the mutable live data
    private MutableLiveData<PageKeyedDataSource<Integer, OrderModel>> itemLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource<Integer, OrderModel> create() {
        //getting our data source object
        EarningDataSource itemDataSource = new EarningDataSource(user_id,userType,status,dateBy,authListener);

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