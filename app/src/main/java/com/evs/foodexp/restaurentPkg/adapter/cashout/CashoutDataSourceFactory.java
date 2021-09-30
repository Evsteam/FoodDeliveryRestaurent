package com.evs.foodexp.restaurentPkg.adapter.cashout;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.evs.foodexp.models.CashoutModel;

import com.evs.foodexp.userPkg.listeners.AuthStatusListener;


public class CashoutDataSourceFactory extends DataSource.Factory {
    private String user_id;
    private AuthStatusListener authListener;

    public CashoutDataSourceFactory(String userId, AuthStatusListener authListener) {
        this.user_id = userId;
        this.authListener = authListener;
    }

    //creating the mutable live data
    private MutableLiveData<PageKeyedDataSource<Integer, CashoutModel>> itemLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource<Integer, CashoutModel> create() {
        //getting our data source object
        CashoutDataSource itemDataSource = new CashoutDataSource(user_id,authListener);

        //posting the datasource to get the values
        itemLiveDataSource.postValue(itemDataSource);

        //returning the datasource
        return itemDataSource;
    }


    //getter for itemlivedatasource
    public MutableLiveData<PageKeyedDataSource<Integer, CashoutModel>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}