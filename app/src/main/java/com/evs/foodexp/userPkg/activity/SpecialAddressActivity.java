package com.evs.foodexp.userPkg.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import  androidx.lifecycle.ViewModelProvider;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.DTO.FoodOrderRequest;
import com.evs.foodexp.commonPkg.common.activity.AddYourLoaction;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SpecialAddressActivity extends AppCompatActivity implements View.OnClickListener , AuthMsgListener {

    private Button fillAddressBtn;
    EditText addressEdit, stateEdit, cityEdit, zipEdit, nameEdit, phoneEdit, landmarkEdit;
    RadioGroup rg_addressType;
    FoodOrderRequest orderRequest;
    APIViewModel apiViewModel;
    SharedPreferences preferences;
    LoaderProgress progress;
    String addressStr, stateStr, cityStr, zipStr, nameStr, phoneStr, landmarkStr, addressType = "",
            latitudeStr, longitudeStr,stateTax="0", whatDoWant, tip, price, delFee,store="",likeGet="",
            nocontact="",transactionFee="0.0";
    public static double longitude = 0.0;
    public static double latitude = 0.0;
    RadioButton rb_home, rb_work;
    ImageView gettingLocation;
    private static final int GETLOCATION=3;
    boolean stateTaxAply=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_address);
        Utills.StatusBarColour(SpecialAddressActivity.this);
        orderRequest = new FoodOrderRequest();
        preferences = PreferenceManager.getDefaultSharedPreferences(SpecialAddressActivity.this);
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        fillAddressBtn = (Button) findViewById(R.id.filledAddressBtn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarUserAddress);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        addressEdit = (EditText) findViewById(R.id.userAddressEdit);
        stateEdit = (EditText) findViewById(R.id.userStateEdit);
        cityEdit = (EditText) findViewById(R.id.userCityEdit);
        zipEdit = (EditText) findViewById(R.id.userZipCodeEdit);
        nameEdit = (EditText) findViewById(R.id.userNameEdit);
        phoneEdit = (EditText) findViewById(R.id.userPhoneEdit);
        landmarkEdit = (EditText) findViewById(R.id.userLandmarkEdit);
        rg_addressType = (RadioGroup) findViewById(R.id.rg_addressType);
        rb_home = (RadioButton) findViewById(R.id.rb_home);
        rb_work = (RadioButton) findViewById(R.id.rb_work);
        gettingLocation = (ImageView) findViewById(R.id.gettingLocation);

        progress = new LoaderProgress(SpecialAddressActivity.this);


        toolbarTitle.setText("ADDRESS");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        fillAddressBtn.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            whatDoWant = bundle.getString("whatDoWant");
            tip = bundle.getString("tip");
            price = bundle.getString("price");
            delFee = bundle.getString("delFee");
            transactionFee=bundle.getString("transactionFee");
            if (bundle.getString("store") != null) {
                store=bundle.getString("store");
            }
            if (bundle.getString("likeGet") != null) {
                likeGet=bundle.getString("likeGet");
                nocontact=bundle.getString("nocontact");
            }
        }


        cityEdit.setText(SessionManager.get_city(preferences));
        zipEdit.setText(SessionManager.get_ZipCode(preferences));
        landmarkEdit.setText(SessionManager.get_ladMark(preferences));
        nameEdit.setText(SessionManager.get_AdddressName(preferences));
        addressEdit.setText(SessionManager.get_BAddress(preferences));
        stateEdit.setText(SessionManager.get_State(preferences));
        phoneEdit.setText(SessionManager.get_Bphone(preferences));
        if (Global.validatename(SessionManager.get_AddressType(preferences))) {
            if (SessionManager.get_AddressType(preferences).equalsIgnoreCase("Work")) {
                rb_work.setChecked(true);
            } else rb_work.setChecked(true);
            addressType = SessionManager.get_AddressType(preferences);
        }


        rg_addressType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        addressType = "Home";

                        break;
                    case R.id.rb_work:
                        addressType = "Work";

                        break;
                    default:
                        addressType = "";
                        break;
                }


            }
        });

        if(!Global.GpsEnable(SpecialAddressActivity.this)){
            Global.showGPSDisabledAlertToUser(SpecialAddressActivity.this);
        }else {
            setMyLastLocation();
        }

        gettingLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Global.GpsEnable(SpecialAddressActivity.this)){
                    Global.showGPSDisabledAlertToUser(SpecialAddressActivity.this);
                }else {
                    setMyLastLocation();

                }
            }
        });

        addressEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SpecialAddressActivity.this, AddYourLoaction.class);
                startActivityForResult(i, GETLOCATION);
            }
        });

    }


    @Override
    public void onClick(View v) {
        if (v == fillAddressBtn) {
            addressStr = addressEdit.getText().toString().trim();
            stateStr = stateEdit.getText().toString().trim();
            cityStr = cityEdit.getText().toString().trim();
            zipStr = zipEdit.getText().toString().trim();
            nameStr = nameEdit.getText().toString().trim();
            phoneStr = phoneEdit.getText().toString().trim();
            landmarkStr = landmarkEdit.getText().toString().trim();


            if (!Global.validatename(addressStr)) {
                addressEdit.setError(Html.fromHtml("Please Enter valid address <font color='red'>*</font>"));
            } else if (!Global.validatename(stateStr)) {
                stateEdit.setError(Html.fromHtml("Please Enter valid State <font color='red'>*</font>"));
            } else if (!Global.validatename(cityStr)) {
                cityEdit.setError(Html.fromHtml("Please Enter valid City <font color='red'>*</font>"));
            } else if (!Global.validateLength(zipStr, 5)) {
                zipEdit.setError(Html.fromHtml("Please Enter valid zipcode <font color='red'>*</font>"));
            } else if (!Global.validateLength(phoneStr, 10)) {
                phoneEdit.setError(Html.fromHtml("Please Enter valid Phone Number <font color='red'>*</font>"));
//            } else if (!Global.validatename(landmarkStr)) {
//                landmarkEdit.setError(Html.fromHtml("Please Enter valid State <font color='red'>*</font>"));
            } else if (!Global.validatename(addressType)) {
                Toast.makeText(SpecialAddressActivity.this, "Plesae Select Address Type", Toast.LENGTH_LONG).show();
            } else if (!stateTaxAply) {
                Toast.makeText(SpecialAddressActivity.this, "State is not Available Please Enter Correct Address", Toast.LENGTH_LONG).show();
            } else if (Global.isOnline(SpecialAddressActivity.this)) {
                price=String.valueOf(Double.parseDouble(price)+Double.parseDouble(stateTax));
//                Intent intent = new Intent(SpecialAddressActivity.this, PaymentSpecialOrder.class);
//                intent.putExtra("latitudeStr", latitudeStr);
//                intent.putExtra("longitudeStr", longitudeStr);
//                intent.putExtra("addressStr", addressStr);
//                intent.putExtra("stateStr", stateStr);
//                intent.putExtra("cityStr", cityStr);
//                intent.putExtra("zipStr", zipStr);
//                intent.putExtra("nameStr", nameStr);
//                intent.putExtra("phoneStr", phoneStr);
//                intent.putExtra("landmarkStr", landmarkStr);
//                intent.putExtra("addressType", addressType);
//                intent.putExtra("whatDoWant", whatDoWant);
//                intent.putExtra("price", price);
//                intent.putExtra("tip", tip);
//                intent.putExtra("delFee", delFee);
//                intent.putExtra("store", store);
//                intent.putExtra("likeGet", likeGet);
//                intent.putExtra("nocontact", nocontact);
//                intent.putExtra("stateTax", stateTax);
//                startActivity(intent);

                if (Global.validatename(likeGet)) {
                    apiViewModel.togetOrder(latitudeStr, longitudeStr, SessionManager.get_user_id(preferences), addressStr, stateStr, cityStr, zipStr, nameStr, phoneStr,
                            landmarkStr, addressType, whatDoWant, price, tip, delFee, likeGet, store, nocontact, "",stateTax, transactionFee,SpecialAddressActivity.this);
                } else {
                    Double pricess = Double.parseDouble(price) + Double.parseDouble(tip) + Double.parseDouble(delFee);
                    apiViewModel.specielOrder(latitudeStr, longitudeStr, SessionManager.get_user_id(preferences), addressStr, stateStr, cityStr, zipStr, nameStr, phoneStr,
                            landmarkStr, addressType, whatDoWant, price, tip, delFee, String.valueOf(pricess), "", "", stateTax,transactionFee,SpecialAddressActivity.this);
                }
                saveaddressData(addressStr, stateStr, cityStr, zipStr, landmarkStr, nameStr, phoneStr, addressType);
            } else Global.showDialog(SpecialAddressActivity.this);


        }
    }


    private void saveaddressData(String addressStr, String stateStr, String cityStr, String zipStr,
                                 String ladmark, String name, String phone, String addressType) {
        SessionManager.save_city(preferences, cityStr);
        SessionManager.save_ZipCode(preferences, zipStr);
        SessionManager.save_ladMark(preferences, ladmark);
        SessionManager.save_AdddressName(preferences, name);
        SessionManager.save_BAddress(preferences, addressStr);
        SessionManager.save_State(preferences, stateStr);
        SessionManager.save_AddressType(preferences, addressType);
        SessionManager.save_Bphone(preferences, phone);

    }
    private String getAddress(double lat,Double longs) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        String address = "";
        try {
            addresses = geocoder.getFromLocation(lat, longs, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
//            addressEdit.setText(address);
            stateEdit.setText(state);
            cityEdit.setText(city);
            zipEdit.setText(postalCode);
            apiViewModel.callState(state,SpecialAddressActivity.this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GETLOCATION) {
            addressEdit.setText(data.getStringExtra("address"));
            latitudeStr = data.getStringExtra("lat");
            longitudeStr = data.getStringExtra("long");
            if (data.getStringExtra("address") != null && data.getStringExtra("address").length() != 0) {
                getAddress(Double.valueOf(latitudeStr), Double.valueOf(longitudeStr));

            }
        }
    }

    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialog(SpecialAddressActivity.this,message);
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progress.dismiss();
        if(msgResponce.getStatus().equalsIgnoreCase("stateSuccess")){
            if(Double.parseDouble(msgResponce.getTotalCartItem())>0){
                double stateTx=Double.parseDouble(price) * Double.parseDouble(msgResponce.getTotalCartItem()) / 100;
                stateTax= String.valueOf(stateTx);
            }else stateTax="0.0";
            stateTaxAply=true;
        }else if(msgResponce.getStatus().equalsIgnoreCase("success")){
            Intent intent = new Intent(SpecialAddressActivity.this, OrderPlacedActivity.class);
            intent.putExtra("foodOrderGroupId", "");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else {
            stateTaxAply=false;
            Global.msgDialog(SpecialAddressActivity.this,msgResponce.getMsg());
        }
    }

    private void setMyLastLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    addressEdit.setText(getAddress(location.getLatitude(),location.getLongitude()));
                    latitudeStr=String.valueOf(location.getLatitude());
                    longitudeStr=String.valueOf(location.getLongitude());
                }
            }
        });
    }

}

