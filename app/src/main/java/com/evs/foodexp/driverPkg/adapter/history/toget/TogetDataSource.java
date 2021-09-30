package com.evs.foodexp.driverPkg.adapter.history.toget;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.evs.foodexp.commonPkg.retrofit.RetrofitApiService;
import com.evs.foodexp.models.HistoryModel;
import com.evs.foodexp.repositry.ListResponse;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TogetDataSource extends PageKeyedDataSource<Integer, HistoryModel> {

    //the size of a page that we want
    public static final int PAGE_SIZE = 10;

    //we will start from the first page which is 1
    private static final int FIRST_PAGE = 1;

    //we need to fetch from stackoverflow
    private static final String SITE_NAME = "foodorderrequestlist";
    private AuthStatusListener authListener;
    private String userId,action,status,usertype;


    public TogetDataSource(String action, String userId,String status,String usertype, AuthStatusListener authListener) {
        this.userId = userId;
        this.action = action;
        this.status = status;
        this.usertype = usertype;
        this.authListener = authListener;
    }


    //this will be called once to load the initial data
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, HistoryModel> callback) {

        RetrofitApiService.getApiService()
                .togetList(action, userId,usertype,status, String.valueOf(FIRST_PAGE))
                .enqueue(new Callback<ListResponse<HistoryModel>>() {
                    @Override
                    public void onResponse(Call<ListResponse<HistoryModel>> call, Response<ListResponse<HistoryModel>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                callback.onResult(response.body().getList(), null, FIRST_PAGE + 1);
                                if (response.body().getStatus().equalsIgnoreCase("success")) {
                                    if (response.body().getList().size() == 0) {
                                        authListener.onFailure("1");
                                    } else authListener.onFailure("0");
                                }

                            }
                        } else {
                            switch (response.code()) {
                                case 404:
                                    authListener.onFailure("not found");
                                    break;
                                case 500:
                                    authListener.onFailure("server Error");
                                    break;
                                default:
                                    authListener.onFailure("unknown error ");
                                    break;
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ListResponse<HistoryModel>> call, Throwable t) {
                        authListener.onFailure(t.toString());
                    }
                });
    }

    //this will load the previous page
    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, HistoryModel> callback) {

        RetrofitApiService.getApiService()
                .togetList(action, userId,usertype,status, String.valueOf(params.key))
                .enqueue(new Callback<ListResponse<HistoryModel>>() {
                    @Override
                    public void onResponse(Call<ListResponse<HistoryModel>> call, Response<ListResponse<HistoryModel>> response) {
                        if (response.isSuccessful()) {
                        //if the current page is greater than one
                        //we are decrementing the page number
                        //else there is no previous page
                        Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                        if (response.body() != null) {

                            //passing the loaded data
                            //and the previous page key
                            callback.onResult(response.body().getList(), adjacentKey);
                        }
                    } else {
                            switch (response.code()) {
                                case 404:
                                    authListener.onFailure("not found");
                                    break;
                                case 500:
                                    authListener.onFailure("server Error");
                                    break;
                                default:
                                    authListener.onFailure("unknown error ");
                                    break;
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<ListResponse<HistoryModel>> call, Throwable t) {
                        authListener.onFailure(t.toString());
                    }
                });
    }

    //this will load the next page
    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, HistoryModel> callback) {

        RetrofitApiService.getApiService()
                .togetList(action, userId,usertype,status, String.valueOf(params.key))
                .enqueue(new Callback<ListResponse<HistoryModel>>() {
                    @Override
                    public void onResponse(Call<ListResponse<HistoryModel>> call, Response<ListResponse<HistoryModel>> response) {
                        if (response.isSuccessful()) {
                        if (response.body() != null) {
                            //if the response has next page
                            //incrementing the next page number
                            Integer key = (response.body().getList().size() > 0) ? params.key + 1 : null;

                            //passing the loaded data and next page value
                            callback.onResult(response.body().getList(), key);

                        } else {
                            switch (response.code()) {
                                case 404:
                                    authListener.onFailure("not found");
                                    break;
                                case 500:
                                    authListener.onFailure("server Error");
                                    break;
                                default:
                                    authListener.onFailure("unknown error ");
                                    break;
                            }

                        }
                        }
                    }

                    @Override
                    public void onFailure(Call<ListResponse<HistoryModel>> call, Throwable t) {
                        authListener.onFailure(t.toString());
                    }
                });
    }
}