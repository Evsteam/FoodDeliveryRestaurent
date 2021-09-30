package com.evs.foodexp.commonPkg.common.sharedPreference;

import android.content.SharedPreferences;

public class MySharedPreference {

    private static String USER_TYPE = "USER_TYPE";
    private static String CHECK_AGREEMENT = "CHECK_AGREEMENT";
    private static String USER_ID = "USER_ID";
    private static String GROUP_ID = "GROUP_ID";
    private static String SERVICE_TYPE = "SERVICE_TYPE";
    private static String WHAT_STORE = "WHAT_STORE";
    private static String WHAT_TO_GET = "WHAT_TO_GET";
    private static String LATITUDE = "LATITUDE";
    private static String LONGITUDE = "LONGITUDE";
    private static String RESTAURANT_ID = "RESTAURANT_ID";
    private static String FOOD_ID = "FOOD_ID";
    private static String FOOD_NAME = "FOOD_NAME";
    private static String FOOD_PRICE = "FOOD_PRICE";
    private static String FOOD_SPECIAL_PRICE = "FOOD_SPECIAL_PRICE";
    private static String COUPON_CODE = "COUPON_CODE";
    private static String TIP_AMOUNT = "TIP_AMOUNT";
    private static String FOOD_QUANTITY = "FOOD_QUANTITY";
    private static String FOOD_DETAIL = "FOOD_DETAIL";
    private static String OTHER_SERVICE = "OTHER_SERVICE";
    private static String TOTAL_AMOUNT_FOR_SERVICE = "TOTAL_AMOUNT_FOR_SERVICE";
    private static String SERVICE_DATE = "SERVICE_DATE";
    private static String SERVICE_TIME = "SERVICE_TIME";

    public static void savePreference(SharedPreferences prefs, String key, Boolean value) {
        SharedPreferences.Editor e = prefs.edit();
        e.putBoolean(key, value);
        e.apply();
    }

    public static void savePreference(SharedPreferences prefs, String key, int value) {
        SharedPreferences.Editor e = prefs.edit();
        e.putInt(key, value);
        e.apply();
    }

    public static void savePreference(SharedPreferences prefs, String key, String value) {
        SharedPreferences.Editor e = prefs.edit();
        e.putString(key, value);
        e.apply();
    }

    public static void dataclear(SharedPreferences prefs) {
        SharedPreferences.Editor e = prefs.edit();
        e.clear();
        e.apply();
    }

    public static String getUserType(SharedPreferences preferences){
        return preferences.getString(USER_TYPE,"");
    }

    public static void setUserType(SharedPreferences preferences, String userType){
        MySharedPreference.savePreference(preferences,USER_TYPE,userType);
    }

    public static void save_check_agreement(SharedPreferences prefs, Boolean value) {
        MySharedPreference.savePreference(prefs, CHECK_AGREEMENT, value);
    }

    public static Boolean get_check_agreement(SharedPreferences prefs) {
        return prefs.getBoolean(CHECK_AGREEMENT, false);
    }

    public static String getUserId(SharedPreferences preferences){
        return preferences.getString(USER_ID,"");
    }

    public static void setUserId(SharedPreferences preferences, String userId){
        MySharedPreference.savePreference(preferences,USER_ID,userId);
    }

    public static String getGroupId(SharedPreferences preferences){
        return preferences.getString(GROUP_ID,"");
    }

    public static void setGroupId(SharedPreferences preferences, String groupId){
        MySharedPreference.savePreference(preferences,GROUP_ID,groupId);
    }

    public static String getServicePosition(SharedPreferences preferences){
        return preferences.getString(SERVICE_TYPE,"");
    }

    public static void setServicePosition(SharedPreferences preferences,String serice){

        MySharedPreference.savePreference(preferences,SERVICE_TYPE,serice);

    }

    public static String getWhatStore(SharedPreferences preferences){
        return preferences.getString(WHAT_STORE,"");
    }

    public static void setWhatStore(SharedPreferences preferences, String whatStore){
        MySharedPreference.savePreference(preferences,WHAT_STORE,whatStore);
    }

    public static String getWhatToGet(SharedPreferences preferences){
        return preferences.getString(WHAT_TO_GET,"");
    }

    public static void setWhatToGet(SharedPreferences preferences, String whatToGet){
        MySharedPreference.savePreference(preferences,WHAT_TO_GET,whatToGet);
    }

    public static String getLatitude(SharedPreferences preferences){
        return preferences.getString(LATITUDE,"");
    }

    public static void setLatitude(SharedPreferences preferences, String latitude){
        MySharedPreference.savePreference(preferences,LATITUDE,latitude);
    }

    public static String getLongitude(SharedPreferences preferences){
        return preferences.getString(LONGITUDE,"");
    }

    public static void setLongitude(SharedPreferences preferences, String longitude){
        MySharedPreference.savePreference(preferences,LONGITUDE,longitude);
    }

    public static String getRestaurantId(SharedPreferences preferences){
        return preferences.getString(RESTAURANT_ID,"");
    }

    public static void setRestaurantId(SharedPreferences preferences, String restaurantId){
        MySharedPreference.savePreference(preferences,RESTAURANT_ID,restaurantId);
    }

    public static String getFoodId(SharedPreferences preferences){
        return preferences.getString(FOOD_ID,"");
    }

    public static void setFoodId(SharedPreferences preferences, String foodId){
        MySharedPreference.savePreference(preferences,FOOD_ID,foodId);
    }

    public static String getFoodName(SharedPreferences preferences){
        return preferences.getString(FOOD_NAME,"");
    }

    public static void setFoodName(SharedPreferences preferences, String foodName){
        MySharedPreference.savePreference(preferences,FOOD_NAME,foodName);
    }

    public static String getFoodPrice(SharedPreferences preferences){
        return preferences.getString(FOOD_PRICE,"");
    }

    public static void setFoodPrice(SharedPreferences preferences, String foodPrice){
        MySharedPreference.savePreference(preferences,FOOD_PRICE,foodPrice);
    }

    public static String getFoodSpecialPrice(SharedPreferences preferences){
        return preferences.getString(FOOD_SPECIAL_PRICE,"");
    }

    public static void setFoodSpecialPrice(SharedPreferences preferences, String foodSpecialPrice){
        MySharedPreference.savePreference(preferences,FOOD_SPECIAL_PRICE,foodSpecialPrice);
    }

    public static String getCouponCode(SharedPreferences preferences){
        return preferences.getString(COUPON_CODE,"");
    }

    public static void setCouponCode(SharedPreferences preferences, String couponCode){
        MySharedPreference.savePreference(preferences,COUPON_CODE,couponCode);
    }

    public static String getTipAmount(SharedPreferences preferences){
        return preferences.getString(TIP_AMOUNT,"");
    }

    public static void setTipAmount(SharedPreferences preferences, String tipAmount){
        MySharedPreference.savePreference(preferences,TIP_AMOUNT,tipAmount);
    }

    public static String getFoodQuantity(SharedPreferences preferences){
        return preferences.getString(FOOD_QUANTITY,"");
    }

    public static void setFoodQuantity(SharedPreferences preferences, String foodQty){
        MySharedPreference.savePreference(preferences,FOOD_QUANTITY,foodQty);
    }

    public static String getFoodDetail(SharedPreferences preferences){
        return preferences.getString(FOOD_DETAIL,"");
    }

    public static void setFoodDetail(SharedPreferences preferences, String foodDetail){
        MySharedPreference.savePreference(preferences,FOOD_DETAIL,foodDetail);
    }

    public static String getOtherService(SharedPreferences preferences){
        return preferences.getString(OTHER_SERVICE,"");
    }

    public static void setOtherService(SharedPreferences preferences, String otherService){
        MySharedPreference.savePreference(preferences,OTHER_SERVICE,otherService);
    }

    public static String getTotalAmountForService(SharedPreferences preferences){
        return preferences.getString(TOTAL_AMOUNT_FOR_SERVICE,"");
    }

    public static void setTotalAmountForService(SharedPreferences preferences, String totalAmount){
        MySharedPreference.savePreference(preferences,TOTAL_AMOUNT_FOR_SERVICE,totalAmount);
    }

    public static String getServiceDate(SharedPreferences preferences){
        return preferences.getString(SERVICE_DATE,"");
    }

    public static void setServiceDate(SharedPreferences preferences, String serviceDate){
        MySharedPreference.savePreference(preferences,SERVICE_DATE,serviceDate);
    }

    public static String getServiceTime(SharedPreferences preferences){
        return preferences.getString(SERVICE_TIME,"");
    }

    public static void setServiceTime(SharedPreferences preferences, String serviceTime){
        MySharedPreference.savePreference(preferences,SERVICE_TIME,serviceTime);
    }

}
