package com.evs.foodexp.commonPkg.viewModel;


import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.evs.foodexp.commonPkg.retrofit.Repository;
import com.evs.foodexp.commonPkg.retrofit.RetrofitApiService;
import com.evs.foodexp.driverPkg.adapter.requestorderlist.food.FoodRequestDataSourceFactory;
import com.evs.foodexp.driverPkg.adapter.requestorderlist.services.ServiceRequestDataSourceFactory;
import com.evs.foodexp.driverPkg.adapter.requestorderlist.specialRequest.SpecialRequestDataSource;
import com.evs.foodexp.driverPkg.adapter.requestorderlist.specialRequest.SpecialRequestDataSourceFactory;
import com.evs.foodexp.driverPkg.adapter.requestorderlist.toget.TogetRequestDataSourceFactory;
import com.evs.foodexp.driverPkg.adapter.service_earning.ServiceEarningSourceFactory;
import com.evs.foodexp.driverPkg.adapter.service_earning.ServiceEarningSourceFactoryWeekly;
import com.evs.foodexp.driverPkg.adapter.spot.spotservice.SpotDataSourceFactory;
import com.evs.foodexp.driverPkg.adapter.toget_earning.TogetEarningDataSourceFactory;
import com.evs.foodexp.models.BookingModel;
import com.evs.foodexp.models.CashoutModel;
import com.evs.foodexp.models.GoGetModel;
import com.evs.foodexp.models.OrderModel;
import com.evs.foodexp.models.RequestModel;
import com.evs.foodexp.models.ReviewModel;
import com.evs.foodexp.models.SpecialOrderModel;
import com.evs.foodexp.repositry.DataResponse;
import com.evs.foodexp.repositry.ListResponse;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.restaurentPkg.adapter.cashout.CashoutDataSourceFactory;
import com.evs.foodexp.restaurentPkg.adapter.earning.EarningDataSourceFactory;
import com.evs.foodexp.restaurentPkg.adapter.reviews.ReviewDataSourceFactory;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestorentViewModel extends AndroidViewModel {
    MutableLiveData<List<OrderModel>> orderList = new MutableLiveData<>();
    MutableLiveData<List<OrderModel>> deliverdorderList = new MutableLiveData<>();
    MutableLiveData<OrderModel> orderDetials = new MutableLiveData<>();
    MutableLiveData<List<RequestModel>> requestList = new MutableLiveData<>();

    LiveData<PageKeyedDataSource<Integer, OrderModel>> earningDataSource;
    LiveData<PagedList<OrderModel>> earningitemList;

    LiveData<PageKeyedDataSource<Integer, GoGetModel>> togetearningDataSource;
    LiveData<PagedList<GoGetModel>> togetearningitemList;

    LiveData<PageKeyedDataSource<Integer, BookingModel>> serviceearningDataSource;
    LiveData<PagedList<BookingModel>> serviceearningitemList;

    LiveData<PageKeyedDataSource<Integer, BookingModel>> serviceearningDataSourceWeekly;
    LiveData<PagedList<BookingModel>> serviceearningitemListWeekly;

    LiveData<PageKeyedDataSource<Integer, BookingModel>> serviceRequestDataSource;
    LiveData<PagedList<BookingModel>> requestsService;

    LiveData<PageKeyedDataSource<Integer, GoGetModel>> togetRequestDataSource;
    LiveData<PagedList<GoGetModel>> togetService;

    LiveData<PageKeyedDataSource<Integer, GoGetModel>> togetRequestDataSourceWeekl;
    LiveData<PagedList<GoGetModel>> togetServiceWeekl;

    LiveData<PageKeyedDataSource<Integer, RequestModel>> foodRequestDataSource;
    LiveData<PagedList<RequestModel>> requestsfood;

    LiveData<PageKeyedDataSource<Integer, SpecialOrderModel>> specialOrderDataSource;
    LiveData<PagedList<SpecialOrderModel>> specialOrderList;

    LiveData<PageKeyedDataSource<Integer, ReviewModel>> reviewsDataSource;
    LiveData<PagedList<ReviewModel>> reviewsitemList;

    LiveData<PageKeyedDataSource<Integer, OrderModel>> spotDataSource;
    LiveData<PagedList<OrderModel>> spotItemList;

    LiveData<PageKeyedDataSource<Integer, CashoutModel>> cashoutDataSource;
    LiveData<PagedList<CashoutModel>> cashoutList;


    public RestorentViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<List<OrderModel>> getOrdersList() {
        if (orderList == null) {
            orderList = RestaurentRepository.getInstance().getOrderList();
        }
        return orderList;
    }

    public LiveData<PagedList<OrderModel>> getSpots() {
        if (spotItemList == null) {
            spotItemList = RestaurentRepository.getInstance().getSpots();
        }
        return spotItemList;
    }


    public LiveData<List<RequestModel>> getRequestList() {
        if (requestList == null) {
            requestList = RestaurentRepository.getInstance().getRequestList();
        }
        return requestList;
    }

    public LiveData<PagedList<GoGetModel>> getTogetRequest() {
        if (togetService == null) {
            togetService = RestaurentRepository.getInstance().getTogetRequests();
        }
        return togetService;
    }

    public LiveData<List<OrderModel>> getDelivredOrdersList() {
        if (deliverdorderList == null) {
            deliverdorderList = RestaurentRepository.getInstance().getDeliverdorderList();
        }
        return deliverdorderList;
    }

    public LiveData<OrderModel> getOrderDetails() {
        if (orderDetials == null) {
            orderDetials = RestaurentRepository.getInstance().getOrderDetials();
        }
        return orderDetials;


    }

    public LiveData<PagedList<OrderModel>> getEarning() {
        if (earningitemList == null) {
            earningitemList = RestaurentRepository.getInstance().getEarningitemList();
        }
        return earningitemList;
    }

    public LiveData<PagedList<BookingModel>> getRequest() {
        if (requestsService == null) {
            requestsService = RestaurentRepository.getInstance().getRequestsService();
        }
        return requestsService;
    }

    public LiveData<PagedList<RequestModel>> getFoodRequest() {
        if (requestsfood == null) {
            requestsfood = RestaurentRepository.getInstance().getFoodrewuest();
        }
        return requestsfood;
    }

    public LiveData<PagedList<ReviewModel>> getReviews() {
        if (reviewsitemList == null) {
            reviewsitemList = RestaurentRepository.getInstance().getReviews();
        }
        return reviewsitemList;
    }

    public LiveData<PagedList<CashoutModel>> getCahouts() {
        if (cashoutList == null) {
            cashoutList = RestaurentRepository.getInstance().getCashouts();
        }
        return cashoutList;
    }

    public LiveData<PagedList<BookingModel>> getServiceEarnigs() {
        if (serviceearningitemList == null) {
            serviceearningitemList = RestaurentRepository.getInstance().getServiceearnings();
        }
        return serviceearningitemList;
    }

    public LiveData<PagedList<GoGetModel>> getTogetEarnigs() {
        if (togetearningitemList == null) {
            togetearningitemList = RestaurentRepository.getInstance().getToGeteearnings();
        }
        return togetearningitemList;
    }

    public LiveData<PagedList<SpecialOrderModel>> getSpecialOrderList() {
        return specialOrderList;
    }

    public LiveData<PagedList<BookingModel>> getServiceWeeklyEarnigs() {
        if (serviceearningitemListWeekly == null) {
            serviceearningitemListWeekly = RestaurentRepository.getInstance().getServiceearningsWeekly();
        }
        return serviceearningitemListWeekly;
    }

    public LiveData<PagedList<GoGetModel>> getTogetWeeklyEarnigs() {
        if (togetServiceWeekl == null) {
            togetServiceWeekl = RestaurentRepository.getInstance().getToGeteearningsWeekly();
        }
        return togetServiceWeekl;
    }


    public void apiOrderList(String userId, String userType, final String status, final AuthStatusListener authListener) {
        authListener.onStarted();
        Repository.getInstance().orders(userId, userType, status).enqueue(new Callback<ListResponse<OrderModel>>() {
            @Override
            public void onResponse(Call<ListResponse<OrderModel>> call, Response<ListResponse<OrderModel>> response) {
                if (response.isSuccessful()) {
                    try {
                        if (status.equalsIgnoreCase("1")) {
                            orderList.postValue(response.body().getList());
                            RestaurentRepository.getInstance().setOrderList(orderList);
                        } else if (status.equalsIgnoreCase("4")) {
                            deliverdorderList.postValue(response.body().getList());
                            RestaurentRepository.getInstance().setDeliverdorderList(deliverdorderList);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        authListener.onFailure(e.getMessage());
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
            public void onFailure(Call<ListResponse<OrderModel>> call, Throwable t) {
                authListener.onFailure(" network failure :( inform the user and possibly retry");
                Log.e("res onFailure(): ", "network failure :( inform the user and possibly retry");
            }
        });
    }

    public void apspotschudle(String userId, String userType, final String status, final AuthStatusListener authListener) {
        authListener.onStarted();
        Repository.getInstance().orders(userId, userType, status).enqueue(new Callback<ListResponse<OrderModel>>() {
            @Override
            public void onResponse(Call<ListResponse<OrderModel>> call, Response<ListResponse<OrderModel>> response) {
                if (response.isSuccessful()) {
                    try {
                        if (status.equalsIgnoreCase("1")) {
                            orderList.postValue(response.body().getList());
                            RestaurentRepository.getInstance().setOrderList(orderList);
                        } else if (status.equalsIgnoreCase("4")) {
                            deliverdorderList.postValue(response.body().getList());
                            RestaurentRepository.getInstance().setDeliverdorderList(deliverdorderList);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        authListener.onFailure(e.getMessage());
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
            public void onFailure(Call<ListResponse<OrderModel>> call, Throwable t) {
                authListener.onFailure(" network failure :( inform the user and possibly retry");
                Log.e("res onFailure(): ", "network failure :( inform the user and possibly retry");
            }
        });
    }

    public void apiRequestOrder(String userId, final AuthStatusListener authListener) {
        authListener.onStarted();
        Repository.getInstance().orderRequest(userId).enqueue(new Callback<ListResponse<RequestModel>>() {
            @Override
            public void onResponse(Call<ListResponse<RequestModel>> call, Response<ListResponse<RequestModel>> response) {
                if (response.isSuccessful()) {
                    try {
                        requestList.postValue(response.body().getList());
                        RestaurentRepository.getInstance().setRequestList(requestList);
                    } catch (Exception e) {
                        e.printStackTrace();
                        authListener.onFailure(e.getMessage());
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
            public void onFailure(Call<ListResponse<RequestModel>> call, Throwable t) {
                authListener.onFailure(" network failure :( inform the user and possibly retry");
                Log.e("res onFailure(): ", "network failure :( inform the user and possibly retry");
            }
        });
    }


    public void apifoodDetails(String userId, String foodorderId, final AuthStatusListener authListener) {
        authListener.onStarted();
        Repository.getInstance().foodDetails(userId, foodorderId).enqueue(new Callback<DataResponse<OrderModel>>() {
            @Override
            public void onResponse(Call<DataResponse<OrderModel>> call, Response<DataResponse<OrderModel>> response) {
                if (response.isSuccessful()) {
                    try {
                        orderDetials.postValue(response.body().getData());
                        RestaurentRepository.getInstance().setOrderDetials(orderDetials);
                    } catch (Exception e) {
                        e.printStackTrace();
                        authListener.onFailure(e.getMessage());
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
            public void onFailure(Call<DataResponse<OrderModel>> call, Throwable t) {
                authListener.onFailure(" network failure :( inform the user and possibly retry");
                Log.e("res onFailure(): ", "network failure :( inform the user and possibly retry");
            }
        });
    }

    public void apiEarning(String user_id, String status, String userType, String dateBy, AuthStatusListener authListener) {
        authListener.onStarted();
        EarningDataSourceFactory earningDataSourceFactory = new EarningDataSourceFactory(user_id, userType, status, dateBy, authListener);
        //getting the live data source from data source factory
        earningDataSource = earningDataSourceFactory.getItemLiveDataSource();
        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setPageSize(10).build();
        //Building the paged list
        earningitemList = (new LivePagedListBuilder(earningDataSourceFactory, pagedListConfig))
                .build();
        RestaurentRepository.getInstance().setEarningitemList(earningitemList);
    }

    public void apiServicEarning(String user_id, String status, String userType, String dateBy, AuthStatusListener authListener) {
        authListener.onStarted();
        ServiceEarningSourceFactory serviceEarningSourceFactory = new ServiceEarningSourceFactory(user_id, userType, status, dateBy, authListener);
        //getting the live data source from data source factory
        serviceearningDataSource = serviceEarningSourceFactory.getItemLiveDataSource();
        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setPageSize(10).build();
        //Building the paged list
        serviceearningitemList = (new LivePagedListBuilder(serviceEarningSourceFactory, pagedListConfig))
                .build();
        RestaurentRepository.getInstance().setServiceearnings(serviceearningitemList);
    }

    public void apiServicEarningWeekly(String user_id, String status, String userType, String dateBy, AuthStatusListener authListener) {
        authListener.onStarted();
        ServiceEarningSourceFactoryWeekly serviceEarningSourceFactory = new ServiceEarningSourceFactoryWeekly(user_id, userType, status, dateBy, authListener);
        //getting the live data source from data source factory
        serviceearningDataSourceWeekly = serviceEarningSourceFactory.getItemLiveDataSource();
        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setPageSize(10).build();
        //Building the paged list
        serviceearningitemListWeekly = (new LivePagedListBuilder(serviceEarningSourceFactory, pagedListConfig))
                .build();
        RestaurentRepository.getInstance().setServiceearningsWeekly(serviceearningitemListWeekly);
    }

    public void apiTogetEarning(String action,String user_id, String status, String userType, String dateBy, AuthStatusListener authListener) {
        authListener.onStarted();
        TogetEarningDataSourceFactory earningDataSourceFactory = new TogetEarningDataSourceFactory(action,user_id, userType, status, dateBy, authListener);
        //getting the live data source from data source factory
        togetearningDataSource = earningDataSourceFactory.getItemLiveDataSource();
        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setPageSize(10).build();
        //Building the paged list
        togetearningitemList = (new LivePagedListBuilder(earningDataSourceFactory, pagedListConfig))
                .build();
        RestaurentRepository.getInstance().setToGeteearnings(togetearningitemList);
    }

    public void apiTogetEarningWeekly(String action,String user_id, String status, String userType, String dateBy, AuthStatusListener authListener) {
        authListener.onStarted();
        TogetEarningDataSourceFactory earningDataSourceFactory = new TogetEarningDataSourceFactory(action,user_id, userType, status, dateBy, authListener);
        //getting the live data source from data source factory
        togetRequestDataSourceWeekl = earningDataSourceFactory.getItemLiveDataSource();
        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setPageSize(10).build();
        //Building the paged list
        togetServiceWeekl = (new LivePagedListBuilder(earningDataSourceFactory, pagedListConfig))
                .build();
        RestaurentRepository.getInstance().setToGeteearningsWeekly(togetServiceWeekl);
    }


    public void apiOrderReqestService(String user_id, String action, String status, String usertype, AuthStatusListener authListener) {
        authListener.onStarted();
        ServiceRequestDataSourceFactory requestDataSourceFactory = new ServiceRequestDataSourceFactory(action, user_id, status, usertype, authListener);
        //getting the live data source from data source factory
        serviceRequestDataSource = requestDataSourceFactory.getItemLiveDataSource();
        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setPageSize(10).build();
        //Building the paged list
        requestsService = (new LivePagedListBuilder(requestDataSourceFactory, pagedListConfig))
                .build();
        RestaurentRepository.getInstance().setRequestsService(requestsService);
    }

    public void apiOrderReqestToget(String user_id, String action, String status, String usertype, AuthStatusListener authListener) {
        authListener.onStarted();
        TogetRequestDataSourceFactory requestDataSourceFactory = new TogetRequestDataSourceFactory(action, user_id, status, usertype, authListener);
        //getting the live data source from data source factory
        togetRequestDataSource = requestDataSourceFactory.getItemLiveDataSource();
        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setPageSize(10).build();
        //Building the paged list
        togetService = (new LivePagedListBuilder(requestDataSourceFactory, pagedListConfig))
                .build();
        RestaurentRepository.getInstance().setTogetRequests(togetService);
    }

    public void apiOrderReqestFood(String user_id, String action, AuthStatusListener authListener) {
        authListener.onStarted();
        FoodRequestDataSourceFactory requestDataSourceFactory = new FoodRequestDataSourceFactory(action, user_id, authListener);
        //getting the live data source from data source factory
        foodRequestDataSource = requestDataSourceFactory.getItemLiveDataSource();
        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setPageSize(10).build();
        //Building the paged list
        requestsfood = (new LivePagedListBuilder(requestDataSourceFactory, pagedListConfig))
                .build();
        RestaurentRepository.getInstance().setFoodrewuest(requestsfood);
    }

    public void apiOrderSpecialOrder(String user_id, String action,String userTpe, AuthStatusListener authListener) {
        authListener.onStarted();
        SpecialRequestDataSourceFactory requestDataSourceFactory = new SpecialRequestDataSourceFactory(action, user_id,userTpe, authListener);
        //getting the live data source from data source factory
        specialOrderDataSource = requestDataSourceFactory.getItemLiveDataSource();
        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setPageSize(10).build();
        //Building the paged list
        specialOrderList = (new LivePagedListBuilder(requestDataSourceFactory, pagedListConfig))
                .build();
        RestaurentRepository.getInstance().setFoodrewuest(requestsfood);
    }


    public void apiReviews(String user_id, AuthStatusListener authListener) {
        authListener.onStarted();
        ReviewDataSourceFactory reviewsDataSourceFactory = new ReviewDataSourceFactory(user_id, authListener);
        //getting the live data source from data source factory
        reviewsDataSource = reviewsDataSourceFactory.getItemLiveDataSource();
        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setPageSize(10).build();
        //Building the paged list
        reviewsitemList = (new LivePagedListBuilder(reviewsDataSourceFactory, pagedListConfig))
                .build();
        RestaurentRepository.getInstance().setReviews(reviewsitemList);
    }

    public void apiSpotScdule(String action, String user_id, String status, String usertype, String dateBy, AuthStatusListener authListener) {
        authListener.onStarted();
        SpotDataSourceFactory spotSourceFactory = new SpotDataSourceFactory(action, user_id, usertype, status, dateBy, authListener);
        //getting the live data source from data source factory
        spotDataSource = spotSourceFactory.getItemLiveDataSource();
        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setPageSize(10).build();
        //Building the paged list
        spotItemList = (new LivePagedListBuilder(spotSourceFactory, pagedListConfig))
                .build();
        RestaurentRepository.getInstance().setSpots(spotItemList);
    }

    public void apiCashout(String action, AuthStatusListener authListener) {
        authListener.onStarted();
        CashoutDataSourceFactory cashoutFactory = new CashoutDataSourceFactory(action, authListener);
        //getting the live data source from data source factory
        cashoutDataSource = cashoutFactory.getItemLiveDataSource();
        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setPageSize(10).build();
        //Building the paged list
        cashoutList = (new LivePagedListBuilder(cashoutFactory, pagedListConfig))
                .build();
        RestaurentRepository.getInstance().setCashouts(cashoutList);
    }

    public void executeEarning(String action, String userId, String utype, String dateBy, final AuthMsgListener authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().earning(action, userId, utype, dateBy)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        if (response.isSuccessful()) {
                            authListener.onSuccessMsg(response.body());

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
                    public void onFailure(Call<MsgResponse> call, Throwable t) {
                        authListener.onFailure(" network failure :( inform the user and possibly retry");
                        Log.e("res onFailure(): ", "network failure :( inform the user and possibly retry");
                    }
                });

//        return response;
    }

    public void executeUpdateOrder(String userId, String foodrequestId, String foodorderId, final String status, final AuthMsgListener authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().requestUpdate("foodrequestaccept", userId, foodrequestId, foodorderId, status)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        if (response.isSuccessful()) {
                                authListener.onSuccessMsg(new MsgResponse(response.body().getStatus(),
                                        response.body().getMsg(),status));

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
                    public void onFailure(Call<MsgResponse> call, Throwable t) {
                        authListener.onFailure(" network failure :( inform the user and possibly retry");
                        Log.e("res onFailure(): ", "network failure :( inform the user and possibly retry");
                    }
                });

//        return response;
    }

    public void executeImgeUpload(String foodorderId,String image, final AuthMsgListener authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().imageUpload(bodyPart("uploadimage"), bodyPart(foodorderId),imagePart(image,"imageUpload"))
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        if (response.isSuccessful()) {
                            if(response.body().getStatus().equalsIgnoreCase("success")){
//                                response.body().setStatus("slipUploaded");
                            }
                                authListener.onSuccessMsg(response.body());

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
                    public void onFailure(Call<MsgResponse> call, Throwable t) {
                        authListener.onFailure(" network failure :( inform the user and possibly retry");
                        Log.e("res onFailure(): ", "network failure :( inform the user and possibly retry");
                    }
                });

//        return response;
    }

    public void executeImgeUpload2(String orderId,String type,String image, final AuthMsgListener authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().imageUpload2(bodyPart("uploadimageall"), bodyPart(orderId),bodyPart(type),imagePart(image,"imageUpload"))
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        if (response.isSuccessful()) {
                            if(response.body().getStatus().equalsIgnoreCase("success")){
                                response.body().setStatus("slipUploaded");
                            }
                                authListener.onSuccessMsg(response.body());

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
                    public void onFailure(Call<MsgResponse> call, Throwable t) {
                        authListener.onFailure(" network failure :( inform the user and possibly retry");
                        Log.e("res onFailure(): ", "network failure :( inform the user and possibly retry");
                    }
                });

//        return response;
    }

    public void executeTogetOrder(String driverId, String togetDriverRequestId, String togetRequestId, String status, final AuthMsgListener authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().requestToGetUpdate("togetrequestaccept", driverId, togetDriverRequestId, togetRequestId, status)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        if (response.isSuccessful()) {
                            authListener.onSuccessMsg(response.body());

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
                    public void onFailure(Call<MsgResponse> call, Throwable t) {
                        authListener.onFailure(" network failure :( inform the user and possibly retry");
                        Log.e("res onFailure(): ", "network failure :( inform the user and possibly retry");
                    }
                });

//        return response;
    }

    public void executeSpecialchangestatus(String driverId, String specialrequestId, String status, final AuthMsgListener authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().requestSpecialUpdate("specialchangestatus", driverId, specialrequestId, status)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        if (response.isSuccessful()) {
                            authListener.onSuccessMsg(response.body());

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
                    public void onFailure(Call<MsgResponse> call, Throwable t) {
                        authListener.onFailure(" network failure :( inform the user and possibly retry");
                        Log.e("res onFailure(): ", "network failure :( inform the user and possibly retry");
                    }
                });

//        return response;
    }

    public void executeServiceOrder(String driverId, String bookingId, String status, final AuthMsgListener authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().requestServiceUpdate("changebookingstatus", driverId, bookingId, status)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        if (response.isSuccessful()) {
                            authListener.onSuccessMsg(response.body());

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
                    public void onFailure(Call<MsgResponse> call, Throwable t) {
                        authListener.onFailure(" network failure :( inform the user and possibly retry");
                        Log.e("res onFailure(): ", "network failure :( inform the user and possibly retry");
                    }
                });

//        return response;
    }


    public void excutecahout(String userId, String requestAmount, final AuthMsgListener authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().cashout("cashoutrequest", userId, requestAmount)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        if (response.isSuccessful()) {
                            authListener.onSuccessMsg(response.body());

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
                    public void onFailure(Call<MsgResponse> call, Throwable t) {
                        authListener.onFailure(" network failure :( inform the user and possibly retry");
                        Log.e("res onFailure(): ", "network failure :( inform the user and possibly retry");
                    }
                });

//        return response;
    }

    public void excuteRequestForDriver(String foodOrderId, String restaurentId, final AuthMsgListener authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().requestForDriver("fooddriverrequest", foodOrderId, restaurentId)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        if (response.isSuccessful()) {
                            authListener.onSuccessMsg(response.body());

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
                    public void onFailure(Call<MsgResponse> call, Throwable t) {
                        authListener.onFailure(" network failure :( inform the user and possibly retry");
                        Log.e("res onFailure(): ", "network failure :( inform the user and possibly retry");
                    }
                });

//        return response;
    }

    public void excuteFoodOrderDetails(String foodOrderId, final AuthListener<DataResponse<RequestModel>> authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().foodOrderDetails("foodorderdetails", foodOrderId)
                .enqueue(new Callback<DataResponse<RequestModel>>() {
                    @Override
                    public void onResponse(Call<DataResponse<RequestModel>> call, Response<DataResponse<RequestModel>> response) {
                        if (response.isSuccessful()) {
                            authListener.onSuccess(new MutableLiveData<DataResponse<RequestModel>>(response.body()));

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
                    public void onFailure(Call<DataResponse<RequestModel>> call, Throwable t) {
                        authListener.onFailure(" network failure :( inform the user and possibly retry");
                        Log.e("res onFailure(): ", "network failure :( inform the user and possibly retry");
                    }
                });

//        return response;
    }

    public void excuteServiceDetails(String bookingId, final AuthListener<DataResponse<BookingModel>> authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().servicerDetails("bookingdetail", bookingId)
                .enqueue(new Callback<DataResponse<BookingModel>>() {
                    @Override
                    public void onResponse(Call<DataResponse<BookingModel>> call,
                                           Response<DataResponse<BookingModel>> response) {
                        if (response.isSuccessful()) {
                            authListener.onSuccess(new MutableLiveData<DataResponse<BookingModel>>(response.body()));

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
                    public void onFailure(Call<DataResponse<BookingModel>> call, Throwable t) {
                        authListener.onFailure(" network failure :( inform the user and possibly retry");
                        Log.e("res onFailure(): ", "network failure :( inform the user and possibly retry");
                    }
                });

//        return response;
    }

        public void excuteTogetDetails(String bookingId, final AuthListener<DataResponse<GoGetModel>> authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().toGetDetails("togetdetils", bookingId)
                .enqueue(new Callback<DataResponse<GoGetModel>>() {
                    @Override
                    public void onResponse(Call<DataResponse<GoGetModel>> call,
                                           Response<DataResponse<GoGetModel>> response) {
                        if (response.isSuccessful()) {
                            authListener.onSuccess(new MutableLiveData<DataResponse<GoGetModel>>(response.body()));

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
                    public void onFailure(Call<DataResponse<GoGetModel>> call, Throwable t) {
                        authListener.onFailure(" network failure :( inform the user and possibly retry");
                        Log.e("res onFailure(): ", "network failure :( inform the user and possibly retry");
                    }
                });

//        return response;
    }

    public void excuteserviceDone(String bookingId, String status,String driverId, final AuthMsgListener authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().serviceDone("changebookingstatus", bookingId,driverId, status)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call,
                                           Response<MsgResponse> response) {
                        if (response.isSuccessful()) {
                            authListener.onSuccessMsg(response.body());

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
                    public void onFailure(Call<MsgResponse> call, Throwable t) {
                        authListener.onFailure(" network failure :( inform the user and possibly retry");
                        Log.e("res onFailure(): ", "network failure :( inform the user and possibly retry");
                    }
                });

//        return response;
    }


    public void reviewRating(String userId, String reviewFrom, String star, String message, final AuthListener<MsgResponse> authListener) {
        authListener.onStarted();

        RetrofitApiService.getApiService().addreviews("submitreview", reviewFrom,userId,star,message)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        if (response.isSuccessful()) {

                            authListener.onSuccess(new MutableLiveData<MsgResponse>(response.body()));


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
                    public void onFailure(Call<MsgResponse> call, Throwable t) {
                        authListener.onFailure(" network failure :( inform the user and possibly retry");
                        Log.e("res onFailure(): ", "network failure :( inform the user and possibly retry");
                    }
                });
    }

    public void excuteUpdateStatus(String foodorderId, String driverId, final String status,String imagePath, final AuthMsgListener authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().updpateStatus(bodyPart("updatefoodorder"), bodyPart(foodorderId), bodyPart(driverId), bodyPart(status),imagePart(imagePath,"DeliveryImage"))
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call,
                                           Response<MsgResponse> response) {
                        if (response.isSuccessful()) {
//                            if(status.equalsIgnoreCase("2")){
////                                authListener.onSuccessMsg(new MsgResponse("Confirm",
////                                        response.body().getMsg(),status));
//                            }else {
                                authListener.onSuccessMsg(new MsgResponse(response.body()
                                        .getStatus(),response.body().getMsg(),status));
//                            }


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
                    public void onFailure(Call<MsgResponse> call, Throwable t) {
                        authListener.onFailure(" network failure :( inform the user and possibly retry");
                        Log.e("res onFailure(): ", "network failure :( inform the user and possibly retry");
                    }
                });

//        return response;
    }

    public void excuteTogetStatus(String togetRequestId, String driverId, String status, final AuthMsgListener authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().togetStatus("togetstatusupdate", togetRequestId, driverId, status)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call,
                                           Response<MsgResponse> response) {
                        if (response.isSuccessful()) {
                            authListener.onSuccessMsg(response.body());

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
                    public void onFailure(Call<MsgResponse> call, Throwable t) {
                        authListener.onFailure(" network failure :( inform the user and possibly retry");
                        Log.e("res onFailure(): ", "network failure :( inform the user and possibly retry");
                    }
                });

//        return response;
    }

    public void excuteTogetupdatePayment(String togetRequestId, String driverId, String payment, final AuthMsgListener authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().updatepaymentToget("sendmoremomeyrequest",
                togetRequestId, driverId, payment)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call,
                                           Response<MsgResponse> response) {
                        if (response.isSuccessful()) {
                            authListener.onSuccessMsg(response.body());

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
                    public void onFailure(Call<MsgResponse> call, Throwable t) {
                        authListener.onFailure(" network failure :( inform the user and possibly retry");
                        Log.e("res onFailure(): ", "network failure :( inform the user and possibly retry");
                    }
                });

//        return response;
    }

    public void excutewallet(String userId, final AuthMsgListener authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().wallet("getwallet ", userId)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("success")) {
                                response.body().setStatus("wallet");
                                authListener.onSuccessMsg(response.body());
                            } else authListener.onSuccessMsg(response.body());


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
                    public void onFailure(Call<MsgResponse> call, Throwable t) {
                        authListener.onFailure(" network failure :( inform the user and possibly retry");
                        Log.e("res onFailure(): ", "network failure :( inform the user and possibly retry");
                    }
                });

//        return response;
    }

    private MultipartBody.Part imagePart(String selectedpath, String name) {
        MultipartBody.Part image = null;
        if (!selectedpath.equals("")) {
            File file = new File(selectedpath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            image = MultipartBody.Part.createFormData(name + "", file.getName(), requestFile);
        } else {
            RequestBody requestFile = RequestBody.create(MediaType.parse("text/plain"), selectedpath);
            image = MultipartBody.Part.createFormData(name, "", requestFile);
        }
        return image;

    }

    private RequestBody bodyPart(String name) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), name);

    }


}
