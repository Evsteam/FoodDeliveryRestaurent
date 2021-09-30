package com.evs.foodexp.commonPkg.retrofit;

import com.evs.foodexp.commonPkg.DTO.AddBookingDto;
import com.evs.foodexp.commonPkg.DTO.AddBookingRequest;
import com.evs.foodexp.commonPkg.DTO.AnotherFoodSpecialRequest;
import com.evs.foodexp.commonPkg.DTO.CategoryDto;
import com.evs.foodexp.commonPkg.DTO.ChangePasswordRequest;
import com.evs.foodexp.commonPkg.DTO.CouponCodeRequest;
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
import com.evs.foodexp.models.BookingModel;
import com.evs.foodexp.models.CardModel;
import com.evs.foodexp.models.CashoutModel;
import com.evs.foodexp.models.GoGetModel;
import com.evs.foodexp.models.OrderModel;
import com.evs.foodexp.models.RequestModel;
import com.evs.foodexp.models.ReviewModel;
import com.evs.foodexp.models.SpecialOrderModel;
import com.evs.foodexp.repositry.DataResponse;
import com.evs.foodexp.repositry.ListResponse;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.models.HistoryModel;
import com.evs.foodexp.models.SubService;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIServiceInterface {


    @POST("services/index")
    Call<UserLoginDto> signupUser(@Body SignupRequestModel requestModel);

    @POST("services/index")
    Call<UserLoginDto> loginUser(@Body LoginRequestModel requestModel);

    @Multipart
    @POST("services/index")
    Call<UserLoginDto> editUserProfile(@Part("action") RequestBody action,
                                       @Part("userId") RequestBody userId,
                                       @Part("fullName") RequestBody fullName,
                                       @Part("contactNumber") RequestBody contactNumber,
                                       @Part("address") RequestBody address,
                                       @Part("device") RequestBody device,
                                       @Part("latitude") RequestBody latitude,
                                       @Part("longitude") RequestBody logitude,
                                       @Part MultipartBody.Part image);

    @Multipart
    @POST("services/index")
    Call<UserLoginDto> updateRestaurent(@Part("action") RequestBody action,
                                        @Part("userId") RequestBody userId,
                                        @Part("address") RequestBody address,
                                        @Part("country") RequestBody country,
                                        @Part("AccountHolderName") RequestBody AccountHolderName,
                                        @Part("BankName") RequestBody BankName,
                                        @Part("AccountNo") RequestBody AccountNo,
                                        @Part("RoutingNo") RequestBody RoutingNo,
                                        @Part("accountType") RequestBody AcountType,
                                        @Part("branchName") RequestBody branchName,
                                        @Part("latitude") RequestBody latitude,
                                        @Part("longitude") RequestBody logitude,
                                        @Part("foodTag") RequestBody type,
                                        @Part MultipartBody.Part logo,
                                        @Part MultipartBody.Part coverImage);

    @Multipart
    @POST("services/index")
    Call<UserLoginDto> driverRestaurent(@Part("action") RequestBody action,
                                        @Part("userId") RequestBody userId,
                                        @Part("address") RequestBody address,
                                        @Part("country") RequestBody country,
                                        @Part("AccountHolderName") RequestBody AccountHolderName,
                                        @Part("BankName") RequestBody BankName,
                                        @Part("AccountNo") RequestBody AccountNo,
                                        @Part("RoutingNo") RequestBody RoutingNo,
                                        @Part("accountType") RequestBody AcountType,
                                        @Part("branchName") RequestBody Branch,
                                        @Part("latitude") RequestBody latitude,
                                        @Part("longitude") RequestBody logitude,
                                        @Part("foodTag") RequestBody type,
                                        @Part MultipartBody.Part logo,
                                        @Part MultipartBody.Part coverImage,
                                        @Part MultipartBody.Part doc);


    @FormUrlEncoded
    @POST("services/index")
    Call<TermsConditionDto> termsAndCondition(@Field("action") String termsCondition);

    @FormUrlEncoded
    @POST("services/index")
    Call<ListResponse<SubService>> service(@Field("action") String service);

    @FormUrlEncoded
    @POST("services/index")
    Call<ListResponse<SubService>> states(@Field("action") String service);

    @FormUrlEncoded
    @POST("services/index")
    Call<ListResponse<SubService>> citys(@Field("action") String action,
                                         @Field("stateId") String stateId);

    @POST("services/index")
    Call<AddBookingDto> addBooking(@Body AddBookingRequest requestModel);

    @POST("services/index")
    Call<UserLoginDto> forgotPassword(@Body ForgotPasswordRequest requestModel);

    @POST("services/index")
    Call<UserLoginDto> changePassword(@Body ChangePasswordRequest requestModel);

    @POST("services/index")
    Call<UserLoginDto> profileDetail(@Body ProfileDetailRequest requestModel);

    @FormUrlEncoded
    @POST("services/index")
    Call<HelpDto> help(@Field("action") String service);


    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> checkStripeAccount(
            @Field("action") String service,
            @Field("userId") String userId,
            @Field("Account") String Account
    );

    @POST("services/index")
    Call<MsgResponse> paymentUpdate(@Body UpdatePaymentRequest requestModel);

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> morePaymentUpdate(
            @Field("action") String category,
            @Field("userId") String userId,
            @Field("togetRequestId") String togetRequestId,
            @Field("transactionID") String transactionID);

    @FormUrlEncoded
    @POST("services/index")
    Call<CategoryDto> category(@Field("action") String category);

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> logout(@Field("action") String category,
                             @Field("userId") String userId);

    @Multipart
    @POST("services/index")
    Call<MsgResponse> addMenu(@Part("action") RequestBody action,
                              @Part("resturentId") RequestBody resturentId,
                              @Part("categoryId") RequestBody categoryId,
                              @Part("foodName") RequestBody foodName,
                              @Part("foodTag") RequestBody foodType,
                              @Part("price") RequestBody price,
                              @Part("specialPrice") RequestBody specialPrice,
                              @Part("description") RequestBody description,
                              @Part MultipartBody.Part image);

    @Multipart
    @POST("services/index")
    Call<MsgResponse> editMenu(@Part("action") RequestBody action,
                               @Part("menueId") RequestBody menueId,
                               @Part("resturentId") RequestBody resturentId,
                               @Part("categoryId") RequestBody categoryId,
                               @Part("foodName") RequestBody foodName,
                               @Part("foodTag") RequestBody foodType,
                               @Part("price") RequestBody price,
                               @Part("specialPrice") RequestBody specialPrice,
                               @Part("description") RequestBody description,
                               @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("services/index")
    Call<MenuListDto> menuList(
            @Field("action") String action,
            @Field("restaurantId") String restaurantId,
            @Field("userId") String userId,
            @Field("categoryId") String categoryId


    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> delMenu(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("menuId") String menuId


    );

    @POST("services/index")
    Call<ResponseBody> toGet(@Body ToGetRequest requestModel);

    @POST("services/index")
    Call<ResponseBody> specialFoodRequest(@Body AnotherFoodSpecialRequest requestModel);

    @POST("services/index")
    Call<MsgResponse> couponCode(@Body CouponCodeRequest requestModel);

    @FormUrlEncoded
    @POST("services/index")
    Call<RestaurantListDto> restaurantList(@Field("action") String action,
                                           @Field("TYPE") String type,
                                           @Field("keyword") String keyword,
                                           @Field("latitude") String latitude,
                                           @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> foodOrder(
            @Field("action") String addfooorder,
            @Field("deliveryLat") String latitude,
            @Field("deliveryLong") String longitude,
            @Field("userId") String userId,
            @Field("foodDetails") String foodDetails,
            @Field("discount") String discount,
            @Field("couponCode") String couponCode,
            @Field("totalAmount") String totalAmount,
            @Field("specialNote") String specialNote,
            @Field("TIP") String lTIP,
            @Field("address") String address,
            @Field("state") String state,
            @Field("city") String city,
            @Field("zipcode") String zipcode,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("landmark") String landmark,
            @Field("workPlace") String workPlace,
            @Field("noContact") String noContact,
            @Field("salesTax") String stateTax,
            @Field("transactionFee") String transactionfee
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> specielOrder(
            @Field("action") String addfooorder,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("userId") String userId,
            @Field("address") String address,
            @Field("state") String state,
            @Field("city") String city,
            @Field("zipcode") String zipcode,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("landmark") String landmark,
            @Field("workPlace") String workPlace,
            @Field("whatYouWant") String whatYouWant,
            @Field("price") String price,
            @Field("TIP") String TIP,
            @Field("deliveryFee") String deliveryFee,
            @Field("totalAmount") String totalAmount,
            @Field("cardNo") String cardNo,
            @Field("transactionId") String transactionId,
            @Field("salesTax") String stateTax,
            @Field("transactionFee") String transactionFee
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> togetlOrder(
            @Field("action") String addfooorder,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("userId") String userId,
            @Field("address") String address,
            @Field("state") String state,
            @Field("city") String city,
            @Field("zipcode") String zipcode,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("landmark") String landmark,
            @Field("workPlace") String workPlace,
            @Field("notes") String whatYouWant,
            @Field("EPrice") String price,
            @Field("TIP") String TIP,
            @Field("deliveryFee") String deliveryFee,
            @Field("wahtUwant") String wahtUwant,
            @Field("StoreCity") String StoreCity,
            @Field("noContac") String nocontact,
            @Field("transactionId") String transactionId,
            @Field("salesTax") String stateTax,
            @Field("transactionFee") String transactionFee
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> addToCard(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("foodId") String foodId,
            @Field("quantity") String quantity
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> toGetPayment(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("togetrequestsId") String foodId,
            @Field("totalAmount") String quantity,
            @Field("transactionId") String transactionId,
            @Field("TIP") String TIP
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> specialPayment(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("specialId") String specialId,
            @Field("totalAmount") String quantity,
            @Field("transactionId") String transactionId,
            @Field("TIP") String TIP
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> updatePayment(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("groupId") String groupId,
            @Field("totalAmount") String totalAmount,
            @Field("transactionId") String transactionId,
            @Field("serviceType") String serviceType,
            @Field("TIP") String TIP,
            @Field("serviceAmount") String serviceAmount,
            @Field("discountAmount") String discountAmount,
            @Field("couponCode") String couponCode
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> updatePaymentFood(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("foodOrderId") String foodOrderId,
            @Field("transactionId") String transactionId,
            @Field("cardNo") String cardNo
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> updatePaymentBag(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("transactionId") String transactionId,
            @Field("shippingAddress") String shippingAddress,
            @Field("shippingCity") String shippingCity,
            @Field("shippingState") String shippingState,
            @Field("shippingCountry") String shippingCountry,
            @Field("shippingZipcode") String shippingZipcode
    );

    @FormUrlEncoded
    @POST(".")
    Call<MsgResponse> chargeramount(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("amount") String amount,
            @Field("transactionId") String transactionId
    );

    @FormUrlEncoded
    @POST(".")
    Call<MsgResponse> cardProcess(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("name") String name,
            @Field("phoneNumber") String phoneNumber,
            @Field("email") String email,
            @Field("address") String address,
            @Field("city") String city,
            @Field("state") String state,
            @Field("country") String country,
            @Field("zipcode") String zipcode,
            @Field("transactionId") String transactionId
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<ListResponse<HistoryModel>> togetList(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("userType") String userType,
            @Field("status") String status,
            @Field("pageNo") String pageNo
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MenuListDto> getCart(
            @Field("action") String action,
            @Field("userId") String userId

    );

    @FormUrlEncoded
    @POST("services/index")
    Call<DataResponse<CardModel>> getCards(
            @Field("action") String action,
            @Field("userId") String userId

    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> deleteToCart(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("foodId") String foodId
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> deleteToAllCart(
            @Field("action") String action,
            @Field("userId") String userId
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> onoff(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("availableStatus") String availableStatus
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> stateTax(
            @Field("action") String action,
            @Field("stateName") String stateName
    );


    ///////////restaurentModel

    @FormUrlEncoded
    @POST("services/index")
    Call<ListResponse<OrderModel>> orderList(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("userType") String userType,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<DataResponse<OrderModel>> foodDetails(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("foodorderId") String foodorderId
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> earning(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("usertype") String usertype,
            @Field("dateBy") String dateBy
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> update(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("usertype") String usertype,
            @Field("dateBy") String dateBy
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> wallet(
            @Field("action") String action,
            @Field("userId") String userId
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> requestForDriver(
            @Field("action") String action,
            @Field("foodOrderId") String foodOrderId,
            @Field("restaurentId") String restaurentId
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<DataResponse<RequestModel>> foodOrderDetails(
            @Field("action") String action,
            @Field("foodorderId") String foodorderId
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<DataResponse<BookingModel>> servicerDetails(
            @Field("action") String action,
            @Field("bookingId") String bookingId
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<DataResponse<GoGetModel>> toGetDetails(
            @Field("action") String action,
            @Field("togetrequestId") String togetrequestId
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> serviceDone(
            @Field("action") String action,
            @Field("bookingId") String bookingId,
            @Field("driverId") String driverId,
            @Field("status") String status
    );

    @Multipart
    @POST("services/index")
    Call<MsgResponse> updpateStatus(
            @Part("action") RequestBody action,
            @Part("foodorderId") RequestBody foodorderId,
            @Part("driverId") RequestBody driverId,
            @Part("status") RequestBody status,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> togetStatus(
            @Field("action") String action,
            @Field("togetRequestId") String togetRequestId,
            @Field("driverId") String driverId,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> updatepaymentToget(
            @Field("action") String action,
            @Field("togetRequestId") String togetRequestId,
            @Field("driverId") String driverId,
            @Field("requestMoney") String requestMoney
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> cashout(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("requestAmount") String requestAmount
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<ListResponse<OrderModel>> getEarning(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("userType") String userType,
            @Field("status") String status,
            @Field("dateBy") String dateBy,
            @Field("pageNo") String pageNO
    );


    @FormUrlEncoded
    @POST("services/index")
    Call<ListResponse<GoGetModel>> getTogetEarning(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("userType") String userType,
            @Field("status") String status,
            @Field("dateBy") String dateBy,
            @Field("pageNo") String pageNO
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<ListResponse<BookingModel>> getServiceEarning(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("userType") String userType,
            @Field("status") String status,
            @Field("dateBy") String dateBy,
            @Field("pageNo") String pageNO
    );


    @FormUrlEncoded
    @POST("services/index")
    Call<ListResponse<CashoutModel>> getCashout(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("pageNo") String pageNO
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<ListResponse<BookingModel>> getrequest(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("status") String status,
            @Field("userType") String usertype,
            @Field("pageNo") String pageNO
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<ListResponse<GoGetModel>> togetRequest(
            @Field("action") String action,
            @Field("driverId") String userId,
//            @Field("status") String status,
//            @Field("usertype") String usertype,
            @Field("pageNo") String pageNO
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<ListResponse<RequestModel>> getFoodRequest(
            @Field("action") String action,
            @Field("driverId") String userId,
            @Field("pageNo") String pageNO
    );


    @FormUrlEncoded
    @POST("services/index")
    Call<ListResponse<SpecialOrderModel>> specialRequest(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("userType") String userType,
            @Field("pageNo") String pageNO
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> requestUpdate(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("foodrequestId") String foodrequestId,
            @Field("foodorderId") String foodorderId,
            @Field("status") String status
    );

    @Multipart
    @POST("services/index")
    Call<MsgResponse> imageUpload(
            @Part("action") RequestBody action,
            @Part("foodorderId") RequestBody foodorderId,
            @Part MultipartBody.Part image
    );

    @Multipart
    @POST("services/index")
    Call<MsgResponse> imageUpload2(
            @Part("action") RequestBody action,
            @Part("orderId") RequestBody orderId,
            @Part("type") RequestBody type,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> requestToGetUpdate(
            @Field("action") String action,
            @Field("driverId") String driverId,
            @Field("togetDriverRequestId") String togetDriverRequestId,
            @Field("togetRequestId") String togetRequestId,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> requestSpecialUpdate(
            @Field("action") String action,
            @Field("driverId") String driverId,
            @Field("specialrequestId") String specialrequestId,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> requestServiceUpdate(
            @Field("action") String action,
            @Field("driverId") String driverId,
            @Field("bookingId") String bookingId,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<ListResponse<ReviewModel>> reviewlist(
            @Field("action") String action,
            @Field("userId") String userId,
            @Field("pageNo") String pageNO
    );

    @FormUrlEncoded
    @POST("services/index")
    Call<MsgResponse> addreviews(
            @Field("action") String action,
            @Field("reviewTo") String userId,
            @Field("reviewFrom") String reviewFrom,
            @Field("star") String star,
            @Field("message") String message

    );


}
