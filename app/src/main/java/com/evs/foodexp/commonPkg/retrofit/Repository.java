package com.evs.foodexp.commonPkg.retrofit;


import com.evs.foodexp.models.OrderModel;
import com.evs.foodexp.models.RequestModel;
import com.evs.foodexp.repositry.DataResponse;
import com.evs.foodexp.repositry.ListResponse;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;


public class Repository {
    private static Repository repository;

    public static Repository getInstance() {
        if (repository != null) {
            return repository;
        } else {
            repository = new Repository();
            return repository;
        }
    }

    /*
     * method to call login api
     * */
    ////////////////////////////////////////User API///////////////////////////////////////
    public Call<ListResponse<OrderModel>> orders(String userId, String userType, String status) {
        return RetrofitApiService.getApiService().orderList("foodorderlist", userId, userType,status);
    }

    public Call<ListResponse<RequestModel>> orderRequest(String userId) {
        return RetrofitApiService.getApiService().getFoodRequest("foodorderrequestlist", userId, "1");
    }

    public Call<DataResponse<OrderModel>> foodDetails(String userId, String foodorderId) {
        return RetrofitApiService.getApiService().foodDetails("foodorderdetails", userId, foodorderId);
    }




    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private MultipartBody.Part imagePart(String selectedpath, String name) {
        MultipartBody.Part image = null;
        if (!selectedpath.equals("")) {
            File file = new File(selectedpath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            image = MultipartBody.Part.createFormData(name + "", file.getName(), requestFile);
        }else{
            RequestBody requestFile = RequestBody.create(MediaType.parse("text/plain"), selectedpath);
            image = MultipartBody.Part.createFormData(name, "", requestFile);
        }

        return image;

    }

    private RequestBody bodyPart(String name) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), name);

    }


}