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

import com.evs.foodexp.commonPkg.DTO.AddBookingDto;
import com.evs.foodexp.commonPkg.DTO.AddBookingRequest;
import com.evs.foodexp.commonPkg.DTO.AnotherFoodSpecialRequest;
import com.evs.foodexp.commonPkg.DTO.CategoryDto;
import com.evs.foodexp.commonPkg.DTO.ChangePasswordRequest;
import com.evs.foodexp.commonPkg.DTO.CouponCodeRequest;
import com.evs.foodexp.commonPkg.DTO.EditProfileRequestModel;
import com.evs.foodexp.commonPkg.DTO.ForgotPasswordRequest;
import com.evs.foodexp.commonPkg.DTO.HelpDto;
import com.evs.foodexp.commonPkg.DTO.LoginRequestModel;
import com.evs.foodexp.commonPkg.DTO.MenuListDto;
import com.evs.foodexp.commonPkg.DTO.ProfileDetailRequest;
import com.evs.foodexp.commonPkg.DTO.RestaurantListDto;
import com.evs.foodexp.commonPkg.DTO.SignupRequestModel;
import com.evs.foodexp.commonPkg.DTO.TermsConditionDto;
import com.evs.foodexp.commonPkg.DTO.ToGetRequest;
import com.evs.foodexp.commonPkg.DTO.UpdatePaymentRequest;
import com.evs.foodexp.commonPkg.DTO.UserLoginDto;
import com.evs.foodexp.commonPkg.retrofit.RetrofitApiService;
import com.evs.foodexp.commonPkg.retrofit.StripeApiService;
import com.evs.foodexp.driverPkg.adapter.history.food.FoodHistoryDataSourceFactory;
import com.evs.foodexp.driverPkg.adapter.history.services.ServiceDataSourceFactory;
import com.evs.foodexp.driverPkg.adapter.history.toget.TogetDataSourceFactory;
import com.evs.foodexp.models.CardModel;
import com.evs.foodexp.models.RequestModel;
import com.evs.foodexp.models.SpecialOrderModel;
import com.evs.foodexp.repositry.DataResponse;
import com.evs.foodexp.repositry.ListResponse;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.models.HistoryModel;
import com.evs.foodexp.models.SubService;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIViewModel extends AndroidViewModel {

    //private ProgressDialog progressDialog;
    //  private Context context;
    public APIViewModel(@NonNull Application application) {
        super(application);
    }

    LiveData<PageKeyedDataSource<Integer, HistoryModel>> serviceHistoryDataSource;
    LiveData<PagedList<HistoryModel>> serviceHistoryist;

    LiveData<PageKeyedDataSource<Integer, HistoryModel>> foodHistoryDataSource;
    LiveData<PagedList<HistoryModel>> foodHistoryist;

    LiveData<PageKeyedDataSource<Integer, HistoryModel>> togetHistoryDataSource;
    LiveData<PagedList<HistoryModel>> togetHistoryist;



    MutableLiveData<List<SubService>> states = new MutableLiveData<>();
    MutableLiveData<List<SubService>> citys = new MutableLiveData<>();
    MutableLiveData<List<CardModel>> cards = new MutableLiveData<>();

    public MutableLiveData<List<SubService>> getCitys() {
        return citys;
    }

    public MutableLiveData<List<CardModel>> getCards() {
        return cards;
    }

    public MutableLiveData<List<SubService>> getStates() {
        return states;
    }


    public void signUp(SignupRequestModel signupRequestModel, final AuthListener<UserLoginDto> authListener) {
        authListener.onStarted();

        RetrofitApiService.getApiService().signupUser(signupRequestModel).enqueue(new Callback<UserLoginDto>() {
            @Override
            public void onResponse(Call<UserLoginDto> call, Response<UserLoginDto> response) {
                switch (response.code()) {
                    case 200:
                        authListener.onSuccess(new MutableLiveData(response.body()));
                        break;
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

            @Override
            public void onFailure(Call<UserLoginDto> call, Throwable t) {

            }
        });
    }

    public void userLogin(LoginRequestModel requestModel, final AuthListener<UserLoginDto> authListener) {
        authListener.onStarted();

        RetrofitApiService.getApiService().loginUser(requestModel).enqueue(new Callback<UserLoginDto>() {
            @Override
            public void onResponse(Call<UserLoginDto> call, Response<UserLoginDto> response) {
                System.out.println("Response code Login === " + response.code());
                switch (response.code()) {
                    case 200:
                        System.out.println("response == " + response.message());
                        authListener.onSuccess(new MutableLiveData(response.body()));
                        break;
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

            @Override
            public void onFailure(Call<UserLoginDto> call, Throwable t) {

            }
        });
    }

    public void checkStripAccount(String userId ,String Account, AuthMsgListener authListener) {
        authListener.onStarted();

        RetrofitApiService.getApiService().checkStripeAccount("checkstripe",userId,Account)
                .enqueue(new Callback<MsgResponse>() {
            @Override
            public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                System.out.println("Response code Login === " + response.code());
                switch (response.code()) {
                    case 200:
                        System.out.println("response == " + response.message());
                        authListener.onSuccessMsg(response.body());
                        break;
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

            @Override
            public void onFailure(Call<MsgResponse> call, Throwable t) {

            }
        });
    }

    public void editProfile(EditProfileRequestModel model, final AuthListener<UserLoginDto> authListener) {
        authListener.onStarted();

        RetrofitApiService.getApiService().editUserProfile(bodyPart("editprofile"), bodyPart(model.getUserId()), bodyPart(model.getFullname())
                , bodyPart(model.getContactNumber()), bodyPart(model.getAddress()), bodyPart("Android"), bodyPart(model.getLats()), bodyPart(model.getLongs()), imagePart(model.getImage(), "image")).enqueue(new Callback<UserLoginDto>() {
            @Override
            public void onResponse(Call<UserLoginDto> call, Response<UserLoginDto> response) {
                switch (response.code()) {
                    case 200:
                        authListener.onSuccess(new MutableLiveData(response.body()));
                        break;
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

            @Override
            public void onFailure(Call<UserLoginDto> call, Throwable t) {

            }
        });

    }


    public void restaurentUpdate(String userId,
                                 String address,
                                 String country,
                                 String AccountHolderName,
                                 String BankName,
                                 String AccountNo,
                                 String RoutingNo,
                                 String AcountType,
                                 String Branch,
                                 String latitude,
                                 String logitude,
                                 String type,
                                 String logo,
                                 String coverImage, final AuthListener<UserLoginDto> authListener) {
        authListener.onStarted();

        RetrofitApiService.getApiService().updateRestaurent(bodyPart("editprofile"), bodyPart(userId), bodyPart(address)
                , bodyPart(country), bodyPart(AccountHolderName), bodyPart(BankName), bodyPart(AccountNo), bodyPart(RoutingNo), bodyPart(AcountType),
                bodyPart(Branch), bodyPart(latitude), bodyPart(logitude), bodyPart(type), imagePart(logo, "ssnImage"), imagePart(coverImage, "companyBackground"))
                .enqueue(new Callback<UserLoginDto>() {
                    @Override
                    public void onResponse(Call<UserLoginDto> call, Response<UserLoginDto> response) {
                        switch (response.code()) {
                            case 200:
                                authListener.onSuccess(new MutableLiveData(response.body()));
                                break;
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

                    @Override
                    public void onFailure(Call<UserLoginDto> call, Throwable t) {

                    }
                });

    }


    public void DirverUpdate(String userId,
                             String address,
                             String country,
                             String AccountHolderName,
                             String BankName,
                             String AccountNo,
                             String RoutingNo,
                             String AcountType,
                             String Branch,
                             String latitude,
                             String logitude,
                             String type,
                             String logo,
                             String coverImage, String doc, final AuthListener<UserLoginDto> authListener) {
        authListener.onStarted();

        RetrofitApiService.getApiService().driverRestaurent(bodyPart("editprofile"), bodyPart(userId), bodyPart(address)
                , bodyPart(country), bodyPart(AccountHolderName), bodyPart(BankName), bodyPart(AccountNo), bodyPart(RoutingNo), bodyPart(AcountType),
                bodyPart(Branch), bodyPart(latitude), bodyPart(logitude), bodyPart(type), imagePart(logo, "image"), imagePart(coverImage, "drivlingImage"), imagePart(doc, "ssnImage"))
                .enqueue(new Callback<UserLoginDto>() {
                    @Override
                    public void onResponse(Call<UserLoginDto> call, Response<UserLoginDto> response) {
                        switch (response.code()) {
                            case 200:
                                authListener.onSuccess(new MutableLiveData(response.body()));
                                break;
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

                    @Override
                    public void onFailure(Call<UserLoginDto> call, Throwable t) {

                    }
                });

    }

    public void termsAndCondition(final AuthListener<TermsConditionDto> authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().termsAndCondition("termAndConditions").enqueue(new Callback<TermsConditionDto>() {
            @Override
            public void onResponse(Call<TermsConditionDto> call, Response<TermsConditionDto> response) {
                switch (response.code()) {
                    case 200:
                        System.out.println("Message == " + response.body().getMsg());
                        System.out.println("response == " + response.message());
                        authListener.onSuccess(new MutableLiveData(response.body()));
                        break;
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

            @Override
            public void onFailure(Call<TermsConditionDto> call, Throwable t) {

            }
        });

    }

    public void discolres(String action, final AuthListener<TermsConditionDto> authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().termsAndCondition(action).enqueue(new Callback<TermsConditionDto>() {
            @Override
            public void onResponse(Call<TermsConditionDto> call, Response<TermsConditionDto> response) {
                switch (response.code()) {
                    case 200:
                        System.out.println("Message == " + response.body().getMsg());
                        System.out.println("response == " + response.message());
                        authListener.onSuccess(new MutableLiveData(response.body()));
                        break;
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

            @Override
            public void onFailure(Call<TermsConditionDto> call, Throwable t) {

            }
        });

    }

    public void services(String service, final AuthListener<ListResponse<SubService>> authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().service(service).enqueue(new Callback<ListResponse<SubService>>() {
            @Override
            public void onResponse(Call<ListResponse<SubService>> call, Response<ListResponse<SubService>> response) {
                switch (response.code()) {
                    case 200:
                        authListener.onSuccess(new MutableLiveData(response.body()));
                        break;
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

            @Override
            public void onFailure(Call<ListResponse<SubService>> call, Throwable t) {

            }
        });

    }

    public void callstates(final AuthStatusListener authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().states("statelist").enqueue(new Callback<ListResponse<SubService>>() {
            @Override
            public void onResponse(Call<ListResponse<SubService>> call, Response<ListResponse<SubService>> response) {
                switch (response.code()) {
                    case 200:
                        states.postValue(response.body().getList());
                        break;
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

            @Override
            public void onFailure(Call<ListResponse<SubService>> call, Throwable t) {

            }
        });

    }

    public void callcity(String stateId, final AuthStatusListener authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().citys("citylist", stateId).enqueue(new Callback<ListResponse<SubService>>() {
            @Override
            public void onResponse(Call<ListResponse<SubService>> call, Response<ListResponse<SubService>> response) {
                switch (response.code()) {
                    case 200:
                        citys.postValue(response.body().getList());
                        break;
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

            @Override
            public void onFailure(Call<ListResponse<SubService>> call, Throwable t) {

            }
        });

    }

    public void addBooking(AddBookingRequest request, final AuthListener<AddBookingDto> authListener) {
        authListener.onStarted();

        RetrofitApiService.getApiService().addBooking(request).enqueue(new Callback<AddBookingDto>() {
            @Override
            public void onResponse(Call<AddBookingDto> call, Response<AddBookingDto> response) {
                switch (response.code()) {
                    case 200:
                        System.out.println("Message == " + response.body());
                        System.out.println("response == " + response.message());
                        authListener.onSuccess(new MutableLiveData(response.body()));
                        break;
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

            @Override
            public void onFailure(Call<AddBookingDto> call, Throwable t) {

            }
        });

    }


    public void help(final AuthListener<HelpDto> authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().help("help").enqueue(new Callback<HelpDto>() {
            @Override
            public void onResponse(Call<HelpDto> call, Response<HelpDto> response) {
                switch (response.code()) {
                    case 200:
                        System.out.println("Message == " + response.body());
                        System.out.println("response == " + response.message());
                        authListener.onSuccess(new MutableLiveData(response.body()));
                        break;
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

            @Override
            public void onFailure(Call<HelpDto> call, Throwable t) {

            }
        });

    }

    public void logout(String userId, final AuthMsgListener authListener) {
        authListener.onStarted();

        RetrofitApiService.getApiService().logout("logout", userId).enqueue(new Callback<MsgResponse>() {
            @Override
            public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                switch (response.code()) {
                    case 200:
                        System.out.println("Message == " + response.body());
                        System.out.println("response == " + response.message());
                        authListener.onSuccessMsg(response.body());
                        break;
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

            @Override
            public void onFailure(Call<MsgResponse> call, Throwable t) {

            }
        });

    }

    public void changePassword(ChangePasswordRequest request, final AuthListener<UserLoginDto> authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().changePassword(request).enqueue(new Callback<UserLoginDto>() {
            @Override
            public void onResponse(Call<UserLoginDto> call, Response<UserLoginDto> response) {
                switch (response.code()) {
                    case 200:
                        System.out.println("Message == " + response.body().getMsg());
                        System.out.println("response == " + response.message());
                        authListener.onSuccess(new MutableLiveData(response.body()));
                        break;
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

            @Override
            public void onFailure(Call<UserLoginDto> call, Throwable t) {

            }
        });

    }

    public void forgotPassword(ForgotPasswordRequest request, final AuthListener<TermsConditionDto> authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().forgotPassword(request).enqueue(new Callback<UserLoginDto>() {
            @Override
            public void onResponse(Call<UserLoginDto> call, Response<UserLoginDto> response) {
                switch (response.code()) {
                    case 200:
                        System.out.println("Message == " + response.body().getMsg());
                        System.out.println("response == " + response.message());
                        authListener.onSuccess(new MutableLiveData(response.body()));
                        break;
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

            @Override
            public void onFailure(Call<UserLoginDto> call, Throwable t) {

            }
        });
    }

    public void profileDetail(ProfileDetailRequest request, final AuthListener<UserLoginDto> authListener) {
        authListener.onStarted();

        RetrofitApiService.getApiService().profileDetail(request).enqueue(new Callback<UserLoginDto>() {
            @Override
            public void onResponse(Call<UserLoginDto> call, Response<UserLoginDto> response) {
                switch (response.code()) {
                    case 200:
                        System.out.println("Message == " + response.body().getMsg());
                        System.out.println("response == " + response.message());
                        authListener.onSuccess(new MutableLiveData(response.body()));
                        break;
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

            @Override
            public void onFailure(Call<UserLoginDto> call, Throwable t) {

            }
        });

    }

    public void updatePayment(UpdatePaymentRequest request, final AuthMsgListener authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().paymentUpdate(request).enqueue(new Callback<MsgResponse>() {
            @Override
            public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                switch (response.code()) {
                    case 200:
                        System.out.println("Message == " + response.message());
                        System.out.println("response == " + response.code());
                        authListener.onSuccessMsg(response.body());
                        break;
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

            @Override
            public void onFailure(Call<MsgResponse> call, Throwable t) {
                System.out.println("Error = " + t.getMessage());
            }
        });

    }

    public void moreRequestPayment(String userId, String togetRequestId, String transactionID, final AuthMsgListener authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().morePaymentUpdate("moremoneypaymentupdate", userId, togetRequestId, transactionID).enqueue(new Callback<MsgResponse>() {
            @Override
            public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                switch (response.code()) {
                    case 200:
                        System.out.println("Message == " + response.message());
                        System.out.println("response == " + response.code());
                        authListener.onSuccessMsg(response.body());
                        break;
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

            @Override
            public void onFailure(Call<MsgResponse> call, Throwable t) {
                System.out.println("Error = " + t.getMessage());
            }
        });

    }

    public void category(String category, final AuthCategoryListener<CategoryDto> authListener) {
        authListener.onCategoryStarted();
        RetrofitApiService.getApiService().category(category).enqueue(new Callback<CategoryDto>() {
            @Override
            public void onResponse(Call<CategoryDto> call, Response<CategoryDto> response) {
                switch (response.code()) {
                    case 200:
                        System.out.println("Message == " + response.message());
                        System.out.println("response == " + response.code());
                        authListener.onCategorySuccess(new MutableLiveData(response.body()));
                        break;
                    case 404:
                        authListener.onCategoryFailure("not found");
                        break;
                    case 500:
                        authListener.onCategoryFailure("server Error");
                        break;
                    default:
                        authListener.onCategoryFailure("unknown error ");
                        break;
                }
            }

            @Override
            public void onFailure(Call<CategoryDto> call, Throwable t) {

            }
        });

    }

    public void addMenu(String userId, String category, String title, String foodType, String price,
                        String sPrice, String desc, String image, final AuthMsgListener listener) {
        listener.onStarted();
        RetrofitApiService.getApiService().addMenu(bodyPart("addmenu"), bodyPart(userId),
                bodyPart(category), bodyPart(title), bodyPart(foodType), bodyPart(price), bodyPart(sPrice),
                bodyPart(desc), imagePart(image, "image_1")).enqueue(new Callback<MsgResponse>() {
            @Override
            public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                switch (response.code()) {
                    case 200:
                        System.out.println("Message == " + response.message());
                        System.out.println("response == " + response.code());
                        listener.onSuccessMsg(response.body());
                        break;
                    case 404:
                        listener.onFailure("not found");
                        break;
                    case 500:
                        listener.onFailure("server Error");
                        break;
                    default:
                        listener.onFailure("unknown error ");
                        break;
                }
            }

            @Override
            public void onFailure(Call<MsgResponse> call, Throwable t) {

            }
        });
    }

    public void editMenu(String userId,String menueId, String category, String title, String foodType, String price,
                         String sPrice, String desc, String image, final AuthMsgListener listener) {
        listener.onStarted();
        RetrofitApiService.getApiService().editMenu(bodyPart("editmenu"),bodyPart(menueId), bodyPart(userId),
                bodyPart(category), bodyPart(title), bodyPart(foodType), bodyPart(price), bodyPart(sPrice),
                bodyPart(desc), imagePart(image, "image_1")).enqueue(new Callback<MsgResponse>() {
            @Override
            public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                switch (response.code()) {
                    case 200:
                        System.out.println("Message == " + response.message());
                        System.out.println("response == " + response.code());
                        listener.onSuccessMsg(response.body());
                        break;
                    case 404:
                        listener.onFailure("not found");
                        break;
                    case 500:
                        listener.onFailure("server Error");
                        break;
                    default:
                        listener.onFailure("unknown error ");
                        break;
                }
            }

            @Override
            public void onFailure(Call<MsgResponse> call, Throwable t) {

            }
        });
    }


    public void menuList(String actionStr, String restaurantId, String userId, String categoryId, final AuthListener<MenuListDto> listener) {
        listener.onStarted();
        RetrofitApiService.getApiService().menuList(actionStr, restaurantId, userId, categoryId).enqueue(new Callback<MenuListDto>() {
            @Override
            public void onResponse(Call<MenuListDto> call, Response<MenuListDto> response) {
                switch (response.code()) {
                    case 200:
                        if (response.body().getStatus().equalsIgnoreCase("success")) {
                            listener.onSuccess(new MutableLiveData(response.body()));
                        } else listener.onFailure(response.body().getMsg());

                        break;
                    case 404:
                        listener.onFailure("not found");
                        break;
                    case 500:
                        listener.onFailure("server Error");
                        break;
                    default:
                        listener.onFailure("unknown error ");
                        break;
                }
            }

            @Override
            public void onFailure(Call<MenuListDto> call, Throwable t) {
                System.out.println("getting Error == " + t.getMessage());
            }
        });
    }

    public void deleteMenu(String id, String userId, final AuthMsgListener listener) {
        listener.onStarted();
        RetrofitApiService.getApiService().delMenu("deletemenu", userId, id)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        switch (response.code()) {
                            case 200:
                                listener.onSuccessMsg(response.body());

                                break;
                            case 404:
                                listener.onFailure("not found");
                                break;
                            case 500:
                                listener.onFailure("server Error");
                                break;
                            default:
                                listener.onFailure("unknown error ");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<MsgResponse> call, Throwable t) {
                        System.out.println("getting Error == " + t.getMessage());
                    }
                });
    }

    public void toGet(ToGetRequest request, final AuthListener<ResponseBody> listener) {
        listener.onStarted();
        RetrofitApiService.getApiService().toGet(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                switch (response.code()) {
                    case 200:
                        System.out.println("Message == " + response.message());
                        System.out.println("response == " + response.code());
                        listener.onSuccess(new MutableLiveData(response.body()));
                        break;
                    case 404:
                        listener.onFailure("not found");
                        break;
                    case 500:
                        listener.onFailure("server Error");
                        break;
                    default:
                        listener.onFailure("unknown error ");
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void specialFoodRequest(AnotherFoodSpecialRequest request, final AuthListener<ResponseBody> listener) {
        listener.onStarted();

        RetrofitApiService.getApiService().specialFoodRequest(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                switch (response.code()) {
                    case 200:
                        System.out.println("Message == " + response.message());
                        System.out.println("response == " + response.code());
                        listener.onSuccess(new MutableLiveData(response.body()));
                        break;
                    case 404:
                        listener.onFailure("not found");
                        break;
                    case 500:
                        listener.onFailure("server Error");
                        break;
                    default:
                        listener.onFailure("unknown error ");
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void couponCode(CouponCodeRequest request, final AuthListener<MsgResponse> listener) {
        listener.onStarted();
        RetrofitApiService.getApiService().couponCode(request).enqueue(new Callback<MsgResponse>() {
            @Override
            public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                switch (response.code()) {
                    case 200:
                        if (response.body().getStatus().equalsIgnoreCase("success")) {
                            listener.onSuccess(new MutableLiveData(new MsgResponse("codeApplyed", response.body().getMsg(), response.body().getTotalCartItem())));
                        } else
                            listener.onSuccess(new MutableLiveData(new MsgResponse("codeFaild", response.body().getMsg(), "")));
//                        listener.onSuccess(new MutableLiveData(response.body()));
                        break;
                    case 404:
                        listener.onFailure("not found");
                        break;
                    case 500:
                        listener.onFailure("server Error");
                        break;
                    default:
                        listener.onFailure("unknown error ");
                        break;
                }
            }

            @Override
            public void onFailure(Call<MsgResponse> call, Throwable t) {

            }
        });

    }

    public void restaurantList(String action, String type, String keyword,String latitude,String longitude, final AuthListener<RestaurantListDto> listener) {
        listener.onStarted();
        RetrofitApiService.getApiService().restaurantList(action, type, keyword,latitude,longitude).enqueue(new Callback<RestaurantListDto>() {
            @Override
            public void onResponse(Call<RestaurantListDto> call, Response<RestaurantListDto> response) {
                switch (response.code()) {
                    case 200:
                        System.out.println("Message == " + response.message());
                        System.out.println("response == " + response.code());
                        listener.onSuccess(new MutableLiveData(response.body()));
                        break;
                    case 404:
                        listener.onFailure("not found");
                        break;
                    case 500:
                        listener.onFailure("server Error");
                        break;
                    default:
                        listener.onFailure("unknown error ");
                        break;
                }
            }

            @Override
            public void onFailure(Call<RestaurantListDto> call, Throwable t) {

            }
        });
    }

    public void getCart(String action, final AuthListener<MenuListDto> listener) {
        listener.onStarted();
        RetrofitApiService.getApiService().getCart("getcarts", action).enqueue(new Callback<MenuListDto>() {
            @Override
            public void onResponse(Call<MenuListDto> call, Response<MenuListDto> response) {
                switch (response.code()) {
                    case 200:
                        listener.onSuccess(new MutableLiveData(response.body()));
                        break;
                    case 404:
                        listener.onFailure("not found");
                        break;
                    case 500:
                        listener.onFailure("server Error");
                        break;
                    default:
                        listener.onFailure("unknown error ");
                        break;
                }
            }

            @Override
            public void onFailure(Call<MenuListDto> call, Throwable t) {

            }
        });
    }

    public void getCards(String userId, final AuthListener<DataResponse<CardModel>> listener) {
        listener.onStarted();
        RetrofitApiService.getApiService().getCards("carddetails", userId)
                .enqueue(new Callback<DataResponse<CardModel>>() {
                    @Override
                    public void onResponse(Call<DataResponse<CardModel>> call, Response<DataResponse<CardModel>> response) {
                        switch (response.code()) {
                            case 200:
                                listener.onSuccess(new MutableLiveData<DataResponse<CardModel>>(response.body()));
                                break;
                            case 404:
                                listener.onFailure("not found");
                                break;
                            case 500:
                                listener.onFailure("server Error");
                                break;
                            default:
                                listener.onFailure("unknown error ");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResponse<CardModel>> call, Throwable t) {

                    }
                });
    }

    public void foodOrder(String latitude, String longitude, String userId, String foodDetails, String discount,
                          String couponCode, String totalAmount, String specialNote, String lTIP, String address, String state, String city, String zipcode,
                          String name, String phone, String landmark, String workPlace, String noContact, String stateTax,String transactionfee, final AuthMsgListener listener) {
        listener.onStarted();
        RetrofitApiService.getApiService().foodOrder("addfooorder", latitude, longitude, userId, foodDetails,
                discount, couponCode, totalAmount, specialNote, lTIP, address, state, city, zipcode, name, phone, landmark, workPlace, noContact, stateTax,transactionfee)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        switch (response.code()) {
                            case 200:
                                listener.onSuccessMsg(response.body());
                                break;
                            case 404:
                                listener.onFailure("not found");
                                break;
                            case 500:
                                listener.onFailure("server Error");
                                break;
                            default:
                                listener.onFailure("unknown error ");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<MsgResponse> call, Throwable t) {

                    }
                });
    }

    public void specielOrder(String latitude, String longitude, String userId,
                             String address, String state, String city, String zipcode,
                             String name, String phone, String landmark, String workPlace,
                             String whatYouWant, String price, String tip, String deliveryFee,
                             String totalAmout, String cardNo, String transactionId, String stateTax,String transactionFee, final AuthMsgListener listener) {
        listener.onStarted();
        RetrofitApiService.getApiService().specielOrder("addrequest", latitude,
                longitude, userId, address, state, city, zipcode, name, phone, landmark, workPlace,
                whatYouWant, price, tip, deliveryFee, totalAmout, cardNo, transactionId, stateTax,transactionFee)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        switch (response.code()) {
                            case 200:
                                listener.onSuccessMsg(response.body());
                                break;
                            case 404:
                                listener.onFailure("not found");
                                break;
                            case 500:
                                listener.onFailure("server Error");
                                break;
                            default:
                                listener.onFailure("unknown error ");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<MsgResponse> call, Throwable t) {

                    }
                });
    }

    public void togetOrder(String latitude, String longitude, String userId,
                           String address, String state, String city, String zipcode,
                           String name, String phone, String landmark, String workPlace,
                           String notes, String price, String tip, String deliveryFee, String whatUwant, String store,
                           String nocontact, String trasictionId, String stateTax,String transactionFee, final AuthMsgListener listener) {
        listener.onStarted();
        RetrofitApiService.getApiService().togetlOrder("togetrequest", latitude, longitude, userId, address, state, city, zipcode, name, phone, landmark, workPlace,
                notes, price, tip, deliveryFee, whatUwant, store, nocontact, trasictionId, stateTax,transactionFee)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        switch (response.code()) {
                            case 200:
                                listener.onSuccessMsg(response.body());
                                break;
                            case 404:
                                listener.onFailure("not found");
                                break;
                            case 500:
                                listener.onFailure("server Error");
                                break;
                            default:
                                listener.onFailure("unknown error ");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<MsgResponse> call, Throwable t) {

                    }
                });
    }


    public void togetOrderPayment(String userId, String togetrequestsId, String totalAmount,
                           String transactionId, String TIP, final AuthMsgListener listener) {
        listener.onStarted();
        RetrofitApiService.getApiService().toGetPayment("togetpaymentupdate", userId, togetrequestsId, totalAmount, transactionId, TIP)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        switch (response.code()) {
                            case 200:
                                listener.onSuccessMsg(response.body());
                                break;
                            case 404:
                                listener.onFailure("not found");
                                break;
                            case 500:
                                listener.onFailure("server Error");
                                break;
                            default:
                                listener.onFailure("unknown error ");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<MsgResponse> call, Throwable t) {

                    }
                });
    }

    public void specialOrderPayment(String userId, String togetrequestsId, String totalAmount,
                                  String transactionId, String TIP, final AuthMsgListener listener) {
        listener.onStarted();
        RetrofitApiService.getApiService().specialPayment("specialpaymentupdate", userId, togetrequestsId, totalAmount, transactionId, TIP)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        switch (response.code()) {
                            case 200:
                                listener.onSuccessMsg(response.body());
                                break;
                            case 404:
                                listener.onFailure("not found");
                                break;
                            case 500:
                                listener.onFailure("server Error");
                                break;
                            default:
                                listener.onFailure("unknown error ");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<MsgResponse> call, Throwable t) {

                    }
                });
    }

    public void addToCart(String userId,
                          String foodId,
                          String quantity, final AuthMsgListener listener) {
        listener.onStarted();
        RetrofitApiService.getApiService().addToCard("addcart", userId, foodId, quantity)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        switch (response.code()) {
                            case 200:
                                listener.onSuccessMsg(response.body());
                                break;
                            case 404:
                                listener.onFailure("not found");
                                break;
                            case 500:
                                listener.onFailure("server Error");
                                break;
                            default:
                                listener.onFailure("unknown error ");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<MsgResponse> call, Throwable t) {

                    }
                });
    }


    public void deleteCart(String userId,
                           final AuthMsgListener listener) {
        listener.onStarted();
        RetrofitApiService.getApiService().deleteToAllCart("deleteallcarts", userId)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        switch (response.code()) {
                            case 200:
                                if (response.body().getStatus().equalsIgnoreCase("success")) {
                                    response.body().setStatus("deleteCart");
                                    listener.onSuccessMsg(response.body());
                                } else {
                                    response.body().setStatus("noteDeleted");
                                    listener.onSuccessMsg(response.body());
                                }

                                break;
                            case 404:
                                listener.onFailure("not found");
                                break;
                            case 500:
                                listener.onFailure("server Error");
                                break;
                            default:
                                listener.onFailure("unknown error ");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<MsgResponse> call, Throwable t) {

                    }
                });
    }

    public void addDeleteCartItem(String userId,
                                  String foodId, final AuthMsgListener listener) {
        listener.onStarted();
        RetrofitApiService.getApiService().deleteToCart("deletecarts", userId, foodId)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        switch (response.code()) {
                            case 200:
                                listener.onSuccessMsg(response.body());
                                break;
                            case 404:
                                listener.onFailure("not found");
                                break;
                            case 500:
                                listener.onFailure("server Error");
                                break;
                            default:
                                listener.onFailure("unknown error ");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<MsgResponse> call, Throwable t) {

                    }
                });
    }

    public void updatePayment(String userId,
                              String groupId, String totalAmount, String transactionId,
                              String serviceType, String TIP, String serviceAmount, String discountAmount,
                              String couponCode, final AuthMsgListener listener) {
        listener.onStarted();
        RetrofitApiService.getApiService().updatePayment("updatepayment", userId, groupId, totalAmount, transactionId,
                serviceType, TIP, serviceAmount, discountAmount, couponCode)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        switch (response.code()) {
                            case 200:
                                listener.onSuccessMsg(response.body());
                                break;
                            case 404:
                                listener.onFailure("not found");
                                break;
                            case 500:
                                listener.onFailure("server Error");
                                break;
                            default:
                                listener.onFailure("unknown error ");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<MsgResponse> call, Throwable t) {

                    }
                });
    }

    public void updatePaymentFood(String userId,
                                  String foodOrderId, String transactionId, String cardNo, final AuthMsgListener listener) {
        listener.onStarted();
        RetrofitApiService.getApiService().updatePaymentFood("updatepaymentfood", userId, foodOrderId, transactionId, cardNo)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        switch (response.code()) {
                            case 200:
                                listener.onSuccessMsg(response.body());
                                break;
                            case 404:
                                listener.onFailure("not found");
                                break;
                            case 500:
                                listener.onFailure("server Error");
                                break;
                            default:
                                listener.onFailure("unknown error ");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<MsgResponse> call, Throwable t) {

                    }
                });
    }


    public void updatePaymentBag(String userId,
                                 String transactionId, String shippingAddress,
                                 String shippingCity, String shippingState, String shippingCountry,
                                 String shippingZipcode, final AuthMsgListener listener) {
        listener.onStarted();
        RetrofitApiService.getApiService().updatePaymentBag("purcheseupdate", userId,
                transactionId, shippingAddress, shippingCity, shippingState, shippingCountry, shippingZipcode)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        switch (response.code()) {
                            case 200:
                                if (response.body().getStatus().equalsIgnoreCase("success")) {
                                    response.body().setStatus("BagSuccess");
                                    listener.onSuccessMsg(response.body());
                                } else {
                                    response.body().setStatus("BagFails");
                                    listener.onSuccessMsg(response.body());
                                }

                                break;
                            case 404:
                                listener.onFailure("not found");
                                break;
                            case 500:
                                listener.onFailure("server Error");
                                break;
                            default:
                                listener.onFailure("unknown error ");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<MsgResponse> call, Throwable t) {

                    }
                });
    }

    public void updatePaymentReg(String userId, String amount, String tokenID,
                                 final AuthMsgListener listener) {
        listener.onStarted();
        StripeApiService.getApiService().chargeramount("chargeramount", userId,
                amount, tokenID)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        switch (response.code()) {
                            case 200:
                                if (response.body().getStatus().equalsIgnoreCase("success")) {
                                    response.body().setStatus("RegSuccess");
                                    listener.onSuccessMsg(response.body());
                                } else {
                                    response.body().setStatus("RegFails");
                                    listener.onSuccessMsg(response.body());
                                }

                                break;
                            case 404:
                                listener.onFailure("not found");
                                break;
                            case 500:
                                listener.onFailure("server Error");
                                break;
                            default:
                                listener.onFailure("unknown error ");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<MsgResponse> call, Throwable t) {

                    }
                });
    }

    public void callCardProcess(String userId, String name, String phoneNumber, String email, String address,
                                String city, String state, String country, String zipcode, String transactionId, final AuthMsgListener listener) {
        listener.onStarted();
        StripeApiService.getApiService().cardProcess("cardholder", userId,
                name, phoneNumber, email, address, city, state, country, zipcode, transactionId)
                .enqueue(new Callback<MsgResponse>() {
                    @Override
                    public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                        switch (response.code()) {
                            case 200:
                                if (response.body().getStatus().equalsIgnoreCase("success")) {
                                    response.body().setStatus("CardSuccess");
                                    listener.onSuccessMsg(response.body());
                                } else {
                                    response.body().setStatus("CardFails");
                                    listener.onSuccessMsg(response.body());
                                }

                                break;
                            case 404:
                                listener.onFailure("not found");
                                break;
                            case 500:
                                listener.onFailure("server Error");
                                break;
                            default:
                                listener.onFailure("unknown error ");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<MsgResponse> call, Throwable t) {

                    }
                });
    }


    public LiveData<PagedList<HistoryModel>> getServiceHistoryist() {
        return serviceHistoryist;
    }

    public LiveData<PagedList<HistoryModel>> getTogetHistoryist() {
        return togetHistoryist;
    }

    public void setFoodHistoryist(LiveData<PagedList<HistoryModel>> foodHistoryist) {
        this.foodHistoryist = foodHistoryist;
    }

    public void apiServiceHistory(String action, String userId, String status, String usertype, AuthStatusListener authListener) {
        authListener.onStarted();
        ServiceDataSourceFactory earningDataSourceFactory = new ServiceDataSourceFactory(action, userId, status, usertype, authListener);
        //getting the live data source from data source factory
        serviceHistoryDataSource = earningDataSourceFactory.getItemLiveDataSource();
        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setPageSize(10).build();
        //Building the paged list
        serviceHistoryist = (new LivePagedListBuilder(earningDataSourceFactory, pagedListConfig))
                .build();
//        RestaurentRepository.getInstance().setEarningitemList(serviceHistoryist);
    }

    public LiveData<PagedList<HistoryModel>> getFoodHistoryist() {
        return foodHistoryist;
    }


    public void apiFoodHistory(String action, String userId, String status, String usertype, AuthStatusListener authListener) {
        authListener.onStarted();
        FoodHistoryDataSourceFactory earningDataSourceFactory = new FoodHistoryDataSourceFactory(action, userId, status, usertype, authListener);
        //getting the live data source from data source factory
        foodHistoryDataSource = earningDataSourceFactory.getItemLiveDataSource();
        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setPageSize(10).build();
        //Building the paged list
        foodHistoryist = (new LivePagedListBuilder(earningDataSourceFactory, pagedListConfig))
                .build();
//        RestaurentRepository.getInstance().setEarningitemList(serviceHistoryist);
    }

    public void apiTogetHistory(String action, String userId, String status, String usertype, AuthStatusListener authListener) {
        authListener.onStarted();
        TogetDataSourceFactory earningDataSourceFactory = new TogetDataSourceFactory(action, userId, status, usertype, authListener);
        //getting the live data source from data source factory
        togetHistoryDataSource = earningDataSourceFactory.getItemLiveDataSource();
        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setPageSize(10).build();
        //Building the paged list
        togetHistoryist = (new LivePagedListBuilder(earningDataSourceFactory, pagedListConfig))
                .build();
//        RestaurentRepository.getInstance().setEarningitemList(serviceHistoryist);
    }

//    public void apiSpecialOrder(String action, String userId, String status, String usertype, AuthStatusListener authListener) {
//        authListener.onStarted();
//        SpecialRequestDataSourceFactory earningDataSourceFactory = new SpecialRequestDataSourceFactory(action, userId, authListener);
//        //getting the live data source from data source factory
//        specialOrderDataSource = earningDataSourceFactory.getItemLiveDataSource();
//        //Getting PagedList config
//        PagedList.Config pagedListConfig =
//                (new PagedList.Config.Builder())
//                        .setEnablePlaceholders(true)
//                        .setPageSize(10).build();
//        //Building the paged list
//        specialorderList = (new LivePagedListBuilder(earningDataSourceFactory, pagedListConfig))
//                .build();
////        RestaurentRepository.getInstance().setEarningitemList(serviceHistoryist);
//    }

    public void getTogetList(String action, String user_id, String userType, String status, String pageNo, final AuthListener<ListResponse<HistoryModel>> authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().togetList(action, user_id, userType, status, pageNo)
                .enqueue(new Callback<ListResponse<HistoryModel>>() {
                    @Override
                    public void onResponse(Call<ListResponse<HistoryModel>> call, Response<ListResponse<HistoryModel>> response) {
                        switch (response.code()) {
                            case 200:
                                authListener.onSuccess(new MutableLiveData<ListResponse<HistoryModel>>(response.body()));
                                break;
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

                    @Override
                    public void onFailure(Call<ListResponse<HistoryModel>> call, Throwable t) {

                    }
                });
    }

    public void callStatusOnOff(String userId, String status, final AuthListener<MsgResponse> authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().onoff("onoff", userId, status).enqueue(new Callback<MsgResponse>() {
            @Override
            public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                if (response.isSuccessful()) {
                    Log.e("Profile response : ", response.body().toString());
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        response.body().setStatus("onOff");
                        authListener.onSuccess(new MutableLiveData(response.body()));
                    } else authListener.onSuccess(new MutableLiveData(response.body()));
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
//        userResponse.setValue(responce.getValue());
    }

    public void callState(String state, final AuthMsgListener authListener) {
        authListener.onStarted();
        RetrofitApiService.getApiService().stateTax("statefind", state).enqueue(new Callback<MsgResponse>() {
            @Override
            public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        response.body().setStatus("stateSuccess");
                    } else {
                        response.body().setStatus("stateFails");
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
//        userResponse.setValue(responce.getValue());
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
