package com.evs.foodexp.commonPkg.viewModel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.evs.foodexp.models.BookingModel;
import com.evs.foodexp.models.CashoutModel;
import com.evs.foodexp.models.GoGetModel;
import com.evs.foodexp.models.OrderModel;
import com.evs.foodexp.models.RequestModel;
import com.evs.foodexp.models.ReviewModel;

import java.util.List;


public class RestaurentRepository {

    LiveData<PagedList<ReviewModel>> reviews;
     LiveData<PagedList<OrderModel>> earningitemList ;
     LiveData<PagedList<BookingModel>> serviceearnings ;
     LiveData<PagedList<BookingModel>> serviceearningsWeekly ;
     LiveData<PagedList<GoGetModel>> toGeteearnings ;
     LiveData<PagedList<GoGetModel>> toGeteearningsWeekly ;
     LiveData<PagedList<BookingModel>> requestsService ;
     LiveData<PagedList<RequestModel>> foodrewuest ;
     LiveData<PagedList<GoGetModel>> togetRequests ;
     LiveData<PagedList<OrderModel>> spots;
     LiveData<PagedList<CashoutModel>> cashouts;
    MutableLiveData<List<OrderModel>> deliverdorderList = new MutableLiveData<>();
    MutableLiveData<List<OrderModel>> orderList = new MutableLiveData<>();
    MutableLiveData<List<RequestModel>> requestList = new MutableLiveData<>();
    MutableLiveData<OrderModel> orderDetials = new MutableLiveData<>();
    ////////////////////////////////////////////////////////////////////////////////////////////////////


    public LiveData<PagedList<BookingModel>> getServiceearningsWeekly() {
        return serviceearningsWeekly;
    }

    public void setServiceearningsWeekly(LiveData<PagedList<BookingModel>> serviceearningsWeekly) {
        this.serviceearningsWeekly = serviceearningsWeekly;
    }

    public LiveData<PagedList<GoGetModel>> getToGeteearnings() {
        return toGeteearnings;
    }

    public void setToGeteearnings(LiveData<PagedList<GoGetModel>> toGeteearnings) {
        this.toGeteearnings = toGeteearnings;
    }

    public LiveData<PagedList<GoGetModel>> getToGeteearningsWeekly() {
        return toGeteearningsWeekly;
    }

    public void setToGeteearningsWeekly(LiveData<PagedList<GoGetModel>> toGeteearningsWeekly) {
        this.toGeteearningsWeekly = toGeteearningsWeekly;
    }

    public LiveData<PagedList<CashoutModel>> getCashouts() {
        return cashouts;
    }

    public void setCashouts(LiveData<PagedList<CashoutModel>> cashouts) {
        this.cashouts = cashouts;
    }

    public LiveData<PagedList<OrderModel>> getSpots() {
        return spots;
    }

    public void setSpots(LiveData<PagedList<OrderModel>> spots) {
        this.spots = spots;
    }

    public MutableLiveData<List<RequestModel>> getRequestList() {
        return requestList;
    }

    public void setRequestList(MutableLiveData<List<RequestModel>> requestList) {
        this.requestList = requestList;
    }

    public LiveData<PagedList<BookingModel>> getRequestsService() {
        return requestsService;
    }

    public void setRequestsService(LiveData<PagedList<BookingModel>> requestsService) {
        this.requestsService = requestsService;
    }

    public LiveData<PagedList<RequestModel>> getFoodrewuest() {
        return foodrewuest;
    }

    public void setFoodrewuest(LiveData<PagedList<RequestModel>> foodrewuest) {
        this.foodrewuest = foodrewuest;
    }

    public LiveData<PagedList<ReviewModel>> getReviews() {
        return reviews;
    }

    public void setReviews(LiveData<PagedList<ReviewModel>> reviews) {
        this.reviews = reviews;
    }

    public LiveData<PagedList<OrderModel>> getEarningitemList() {
        return earningitemList;
    }

    public void setEarningitemList(LiveData<PagedList<OrderModel>> earningitemList) {
        this.earningitemList = earningitemList;
    }

    public void setEarningitemList(MutableLiveData<PagedList<OrderModel>> earningitemList) {
        this.earningitemList = earningitemList;
    }

    public LiveData<PagedList<BookingModel>> getServiceearnings() {
        return serviceearnings;
    }

    public void setServiceearnings(LiveData<PagedList<BookingModel>> serviceearnings) {
        this.serviceearnings = serviceearnings;
    }

    public MutableLiveData<List<OrderModel>> getDeliverdorderList() {
        return deliverdorderList;
    }

    public void setDeliverdorderList(MutableLiveData<List<OrderModel>> deliverdorderList) {
        this.deliverdorderList = deliverdorderList;
    }

    public MutableLiveData<List<OrderModel>> getOrderList() {
        return orderList;
    }

    public void setOrderList(MutableLiveData<List<OrderModel>> orderList) {
        this.orderList = orderList;
    }

    public MutableLiveData<OrderModel> getOrderDetials() {
        return orderDetials;
    }

    public void setOrderDetials(MutableLiveData<OrderModel> orderDetials) {
        this.orderDetials = orderDetials;
    }

    public LiveData<PagedList<GoGetModel>> getTogetRequests() {
        return togetRequests;
    }

    public void setTogetRequests(LiveData<PagedList<GoGetModel>> togetRequests) {
        this.togetRequests = togetRequests;
    }

    private static RestaurentRepository modelRepository;

    public static RestaurentRepository getInstance() {
        if (modelRepository != null) {
            return modelRepository;
        } else {
            modelRepository = new RestaurentRepository();
            return modelRepository;
        }
    }


////////////////////////////////////////////////////////////////////////////////////////////////////


}
