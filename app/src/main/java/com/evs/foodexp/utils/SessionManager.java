package com.evs.foodexp.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Naveen on 1/31/2017.
 */

public class SessionManager {

    private static String USERNAME = "USERNAME";
    private static String NAME = "NAME";
    private static String FNAME = "FNAME";
    private static String LNAME = "LNAME";
    private static String EMAILID = "EMAILID";
    private static String PRICE = "PRICE";
    private static String mobile = "mobile";
    private static String user_id = "user_id";
    private static String user_type = "user_type";
    private static String SESSION_CHECK_LOGIN = "SESSION_CHECK_LOGIN";
    private static String check_agreement = "CHECK_AGREEMENT";
    private static String password = "password";
    private static String address = "address";
    private static String image = "image";
    private static String facilityName = "facilityName";
    private static String appoitment = "Appoitment";
    private static String select_gender = "gender";
    private static String select_PersionIN = "PersionIN";
    private static String select_type = "select_type";
    private static String cardID = "cardID";
    private static String Wallet = "WALLET";
    private static String device = "DEVICE";
    private static String device_token = "DEVICE_TOKEN";
    private static String FirebaseId = "FIREBASEID";
    private static String SocialId = "SocialId";
    private static String SocialType = "SocialType";
    private static String remember = "remember";
    private static String onOff = "onOff";

    private static String AddressType = "AddressType";
    private static String city = "city";
    private static String AdddressName = "AdddressName";
    private static String ZipCode = "ZipCode";
    private static String Bphone = "Bphone";
    private static String BAddress = "BAddress";
    private static String State = "State";
    private static String onlinestatus = "onlinestatus";
    private static String ladMark = "ladMark";
    private static String BusinessType = "BusinessType";
    private static String country = "country";
    private static String accountType = "accountType";
    private static String RoutingNo = "RoutingNo";
    private static String AccountNo = "AccountNo";
    private static String BankName = "BankName";
    private static String AccountHolderName = "AccountHolderName";
    private static String CouverImage = "CouverImage";
    private static String Branch = "Branch";
    private static String Lats = "Lats";
    private static String Longs = "Longs";
    private static String driverLicence = "driverLicence";
    private static String doc = "doc";
    private static String stripeAccountNo = "stripeAccountNo";
    private static String StripeStatus = "StripeStatus";
    private static String stripeAccountUrl = "stripeAccountUrl";

    public static void savePreference(SharedPreferences prefs, String key, Boolean value) {
        Editor e = prefs.edit();
        e.putBoolean(key, value);
        e.apply();
    }

    public static void savePreference(SharedPreferences prefs, String key, int value) {
        Editor e = prefs.edit();
        e.putInt(key, value);
        e.apply();
    }

    public static void savePreference(SharedPreferences prefs, String key, String value) {
        Editor e = prefs.edit();
        e.putString(key, value);
        e.apply();
    }

    public static void dataclear(SharedPreferences prefs) {
        Editor e = prefs.edit();
        e.clear();
        e.apply();
    }

    public static void save_ladMark(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, ladMark, value);
    }

    public static String get_ladMark(SharedPreferences prefs) {
        return prefs.getString(ladMark, "");
    }

    public static void save_check_login(SharedPreferences prefs, Boolean value) {
        SessionManager.savePreference(prefs, SESSION_CHECK_LOGIN, value);
    }

    public static Boolean get_check_login(SharedPreferences prefs) {
        return prefs.getBoolean(SESSION_CHECK_LOGIN, false);
    }

    public static void save_remember(SharedPreferences prefs, Boolean value) {
        SessionManager.savePreference(prefs, remember, value);
    }

    public static Boolean get_remember(SharedPreferences prefs) {
        return prefs.getBoolean(remember, false);
    }

    public static void save_onlinestatus(SharedPreferences prefs, Boolean value) {
        SessionManager.savePreference(prefs, onlinestatus, value);
    }

    public static Boolean get_onlinestatus(SharedPreferences prefs) {
        return prefs.getBoolean(onlinestatus, false);
    }

    public static void save_check_agreement(SharedPreferences prefs, Boolean value) {
        SessionManager.savePreference(prefs, check_agreement, value);
    }

    public static Boolean get_check_agreement(SharedPreferences prefs) {
        return prefs.getBoolean(check_agreement, false);
    }

    public static void save_name(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, NAME, value);
    }

    public static String get_name(SharedPreferences prefs) {
        return prefs.getString(NAME, "");
    }

    public static void save_username(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, USERNAME, value);
    }

    public static String get_username(SharedPreferences prefs) {
        return prefs.getString(USERNAME, "");
    }

    public static void save_fname(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, FNAME, value);
    }

    public static String get_fname(SharedPreferences prefs) {
        return prefs.getString(FNAME, "");
    }

    public static void save_lname(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, LNAME, value);
    }

    public static String get_lname(SharedPreferences prefs) {
        return prefs.getString(LNAME, "");
    }

    public static void save_emailid(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, EMAILID, value);
    }

    public static String get_emailid(SharedPreferences prefs) {
        return prefs.getString(EMAILID, "");
    }

    public static void save_password(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, password, value);
    }

    public static String get_password(SharedPreferences prefs) {
        return prefs.getString(password, "");
    }


    public static void save_mobile(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, mobile, value);
    }

    public static String get_mobile(SharedPreferences prefs) {
        return prefs.getString(mobile, "");
    }

    public static void save_user_id(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, user_id, value);
    }

    public static String get_user_id(SharedPreferences prefs) {
        return prefs.getString(user_id, "");
    }

    public static void save_select_type(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, select_type, value);
    }

    public static String get_select_type(SharedPreferences prefs) {
        return prefs.getString(select_type, "");
    }

    public static void save_userType(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, user_type, value);
    }

    public static String get_userType(SharedPreferences prefs) {
        return prefs.getString(user_type, Global.type_person);
    }

    public static void save_price(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, PRICE, value);
    }

    public static String get_price(SharedPreferences prefs) {

        return prefs.getString(PRICE, "");
    }


    public static void save_address(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, address, value);
    }

    public static String get_address(SharedPreferences prefs) {
        return prefs.getString(address, "");
    }

    public static void save_image(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, image, value);
    }

    public static String get_image(SharedPreferences prefs) {
        return prefs.getString(image, "");
    }

    public static void save_Doc(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, doc, value);
    }

    public static String get_Doc(SharedPreferences prefs) {
        return prefs.getString(doc, "");
    }


    public static String getFacilityName(SharedPreferences prefs) {
        return prefs.getString(facilityName, "");
    }

    public static void save_FacilityName(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, facilityName, value);

    }

    public static String getAppoitment(SharedPreferences prefs) {
        return prefs.getString(appoitment, "");
    }

    public static void save_Appoitment(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, appoitment, value);

    }

    public static String getGender(SharedPreferences prefs) {
        return prefs.getString(select_gender, "");
    }

    public static void save_Gender(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, select_gender, value);

    }


    public static String getPersonIn(SharedPreferences prefs) {
        return prefs.getString(select_PersionIN, "");
    }

    public static void save_PersonIn(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, select_PersionIN, value);

    }


    public static void save_cardID(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, cardID, value);
    }

    public static String get_cardID(SharedPreferences prefs) {
        return prefs.getString(cardID, "");
    }


    public static void save_wallet(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, Wallet, value);
    }

    public static String get_wallet(SharedPreferences prefs) {
        return prefs.getString(Wallet, "");
    }

    public static void save_device_token(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, device_token, value);
    }

    public static String get_device_token(SharedPreferences prefs) {
        return prefs.getString(device_token, "");
    }

    public static void save_device(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, device, value);
    }

    public static String get_device(SharedPreferences prefs) {
        return prefs.getString(device, "");
    }

    public static void save_firebaseId(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, FirebaseId, value);
    }

    public static String get_firebaseId(SharedPreferences prefs) {
        return prefs.getString(FirebaseId, "");
    }

    public static void save_socialType(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, SocialType, value);
    }

    public static String get_socialType(SharedPreferences prefs) {
        return prefs.getString(SocialType, "");
    }

    public static void save_socialId(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, SocialId, value);
    }

    public static String get_socialId(SharedPreferences prefs) {
        return prefs.getString(SocialId, "");
    }

    public static void save_AddressType(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, AddressType, value);
    }

    public static String get_AddressType(SharedPreferences prefs) {
        return prefs.getString(AddressType, "");
    }

    public static void save_city(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, city, value);
    }

    public static String get_city(SharedPreferences prefs) {
        return prefs.getString(city, "");
    }

    public static void save_AdddressName(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, AdddressName, value);
    }

    public static String get_AdddressName(SharedPreferences prefs) {
        return prefs.getString(AdddressName, "");
    }

    public static void save_ZipCode(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, ZipCode, value);
    }

    public static String get_ZipCode(SharedPreferences prefs) {
        return prefs.getString(ZipCode, "");
    }

    public static void save_Bphone(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, Bphone, value);
    }

    public static String get_Bphone(SharedPreferences prefs) {
        return prefs.getString(Bphone, "");
    }

    public static void save_BAddress(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, BAddress, value);
    }

    public static String get_BAddress(SharedPreferences prefs) {
        return prefs.getString(BAddress, "");
    }

    public static void save_State(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, State, value);
    }

    public static String get_State(SharedPreferences prefs) {
        return prefs.getString(State, "");
    }

    public static void save_country(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, country, value);
    }

    public static String get_country(SharedPreferences prefs) {
        return prefs.getString(country, "");
    }

    public static void save_BankName(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, BankName, value);
    }

    public static String get_BankName(SharedPreferences prefs) {
        return prefs.getString(BankName, "");
    }

    public static void save_AccountHolderName(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, AccountHolderName, value);
    }

    public static String get_AccountHolderName(SharedPreferences prefs) {
        return prefs.getString(AccountHolderName, "");
    }

    public static void save_AccountNo(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, AccountNo, value);
    }

    public static String get_AccountNo(SharedPreferences prefs) {
        return prefs.getString(AccountNo, "");
    }

    public static void save_RoutingNo(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, RoutingNo, value);
    }

    public static String get_RoutingNo(SharedPreferences prefs) {
        return prefs.getString(RoutingNo, "");
    }

    public static void save_accountType(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, accountType, value);
    }

    public static String get_accountType(SharedPreferences prefs) {
        return prefs.getString(accountType, "");

    }

    public static void save_CouverImage(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, CouverImage, value);
    }

    public static String get_CouverImage(SharedPreferences prefs) {
        return prefs.getString(CouverImage, "");
    }

    public static void save_driverLicence(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, driverLicence, value);
    }

    public static String get_driverLicence(SharedPreferences prefs) {
        return prefs.getString(driverLicence, "");
    }

    public static void save_Branch(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, Branch, value);
    }

    public static String get_Branch(SharedPreferences prefs) {
        return prefs.getString(Branch, "");
    }

    public static void save_Lats(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, Lats, value);
    }

    public static String get_Lats(SharedPreferences prefs) {
        return prefs.getString(Lats, "");
    }

    public static void save_Longs(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, Longs, value);
    }

    public static String get_Longs(SharedPreferences prefs) {
        return prefs.getString(Longs, "");
    }

    public static void save_BusinessType(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, BusinessType, value);
    }

    public static String get_BusinessType(SharedPreferences prefs) {
        return prefs.getString(BusinessType, "");
    }


    public static void save_StripeAccountNo(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, stripeAccountNo, value);
    }

    public static String get_StripeAccountNo(SharedPreferences prefs) {
        return prefs.getString(stripeAccountNo, "");
    }

    public static void save_StripeStatus(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, StripeStatus, value);
    }

    public static String get_StripeStatus(SharedPreferences prefs) {
        return prefs.getString(StripeStatus, "");
    }

    public static void save_StripeAccountUrl(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, stripeAccountUrl, value);
    }

    public static String get_StripeAccountUrl(SharedPreferences prefs) {
        return prefs.getString(stripeAccountUrl, "");
    }


    public static void save_onOff(SharedPreferences prefs, Boolean value) {
        SessionManager.savePreference(prefs, onOff, value);
    }

    public static Boolean get_onOff(SharedPreferences prefs) {
        return prefs.getBoolean(onOff, true);
    }


}