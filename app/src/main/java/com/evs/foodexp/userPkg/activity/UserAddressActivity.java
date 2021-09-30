package com.evs.foodexp.userPkg.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import  androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class UserAddressActivity extends AppCompatActivity implements View.OnClickListener,
        AuthMsgListener {

    private Button fillAddressBtn;
    EditText addressEdit, stateEdit, cityEdit, zipEdit, nameEdit, phoneEdit, landmarkEdit;
    RadioGroup rg_addressType;
    FoodOrderRequest orderRequest;
    APIViewModel apiViewModel;
    SharedPreferences preferences;
    LoaderProgress progress;
    String addressStr, stateStr, cityStr, zipStr, nameStr, phoneStr, landmarkStr, addressType = "",
            discount, couponCode,transactionfee="0",stateTax="0", noConctact,latitudeStr="",longitudeStr="";
    int PERMISSION_ALL = 1;
    String totalAmount, itemList, tipAmount, specialNotes;
    boolean isGPSEnabled = false;
    private static final int GETLOCATION=3;
    RadioButton rb_home, rb_work;
    ImageView gettingLocation;
    boolean stateTaxAply=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_address);
        Utills.StatusBarColour(UserAddressActivity.this);
        orderRequest = new FoodOrderRequest();
        preferences = PreferenceManager.getDefaultSharedPreferences(UserAddressActivity.this);
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


        progress = new LoaderProgress(UserAddressActivity.this);
        if(!Global.GpsEnable(UserAddressActivity.this)){
            Global.showGPSDisabledAlertToUser(UserAddressActivity.this);
        }

        toolbarTitle.setText("ADDRESS" );
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
            totalAmount = bundle.getString("totalAmount" );
            itemList = bundle.getString("itemList" );
            tipAmount = bundle.getString("tipAmount" );
            specialNotes = bundle.getString("specialNotes" );
            couponCode = bundle.getString("couponCode" );
            discount = bundle.getString("discount" );
            noConctact = bundle.getString("noConctact" );
            transactionfee = bundle.getString("transactionfee" );
        }


        cityEdit.setText(SessionManager.get_city(preferences));
        zipEdit.setText(SessionManager.get_ZipCode(preferences));
        landmarkEdit.setText(SessionManager.get_ladMark(preferences));
        nameEdit.setText(SessionManager.get_AdddressName(preferences));
        addressEdit.setText(SessionManager.get_BAddress(preferences));
        stateEdit.setText(SessionManager.get_State(preferences));
        phoneEdit.setText(SessionManager.get_Bphone(preferences));

        if (Global.validatename(SessionManager.get_AddressType(preferences))) {
            if (SessionManager.get_AddressType(preferences).equalsIgnoreCase("Work" )) {
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

        if(!Global.GpsEnable(UserAddressActivity.this)){
            Global.showGPSDisabledAlertToUser(UserAddressActivity.this);
        }else {
            setMyLastLocation();
        }

        gettingLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Global.GpsEnable(UserAddressActivity.this)){
                    Global.showGPSDisabledAlertToUser(UserAddressActivity.this);
                }else {
                    setMyLastLocation();
                }
            }
        });

        addressEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserAddressActivity.this, AddYourLoaction.class);
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
            Double total=Double.parseDouble(totalAmount)+Double.parseDouble(stateTax);
            totalAmount=String.valueOf(total);
            if (!Global.validatename(addressStr)) {
                addressEdit.setError(Html.fromHtml("Please Enter valid address <font color='red'>*</font>" ));
            } else if (!Global.validatename(stateStr)) {
                stateEdit.setError(Html.fromHtml("Please Enter valid State <font color='red'>*</font>" ));
            } else if (!Global.validatename(cityStr)) {
                cityEdit.setError(Html.fromHtml("Please Enter valid City <font color='red'>*</font>" ));
            } else if (!Global.validateLength(zipStr, 5)) {
                zipEdit.setError(Html.fromHtml("Please Enter valid zipcode <font color='red'>*</font>" ));
            } else if (!Global.validateLength(phoneStr, 10)) {
                phoneEdit.setError(Html.fromHtml("<font color='red'>Please Enter Valid Contact Number</font>" ));
//            } else if (!Global.validatename(landmarkStr)) {
//                landmarkEdit.setError(Html.fromHtml("<font color='red'>Please Enter landmark</font>" ));
            } else if (!Global.validatename(addressType)) {
                Toast.makeText(UserAddressActivity.this, "Please  Select valid Address", Toast.LENGTH_LONG).show();
            } else if (!stateTaxAply) {
                Toast.makeText(UserAddressActivity.this, "State is not Available Please Enter Correct Address", Toast.LENGTH_LONG).show();
            } else if (Global.isOnline(UserAddressActivity.this)) {
//                totalAmount=String.valueOf(Double.parseDouble(totalAmount)-Double.parseDouble(transactionfee));
                apiViewModel.foodOrder(latitudeStr, longitudeStr, SessionManager.get_user_id(preferences), itemList,
                        discount, couponCode, totalAmount, specialNotes, tipAmount, addressStr, stateStr, cityStr, zipStr, nameStr, phoneStr,
                        landmarkStr, addressType, noConctact,stateTax,transactionfee, UserAddressActivity.this);
                saveaddressData(addressStr, stateStr, cityStr, zipStr, landmarkStr, nameStr, phoneStr, addressType);
            } else Global.showDialog(UserAddressActivity.this);


        }
    }

    @Override
    public void onStarted() {
        progress.show();
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

    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialog(UserAddressActivity.this, message);
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progress.dismiss();
        if (msgResponce.getStatus().equalsIgnoreCase("success" )) {
//            Toast.makeText(UserAddressActivity.this, msgResponce.getMsg(), Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(UserAddressActivity.this, PaymentUserActivity.class);
//            intent.putExtra("foodOrderGroupId",msgResponce.getTotalCartItem());
//            intent.putExtra("totalAmount",totalAmount);
//            startActivity(intent);

            Intent intent=new Intent(UserAddressActivity.this,BookingSuceess.class);
            intent.putExtra("foodOrderGroupId",msgResponce.getTotalCartItem());
            intent.putExtra("totalAmount",totalAmount);
            intent.putExtra("transactionFee",transactionfee);
            intent.putExtra("type","order");
            startActivity(intent);

        } else if(msgResponce.getStatus().equalsIgnoreCase("stateSuccess" )){
            if(Double.parseDouble(msgResponce.getTotalCartItem())>0){
                double stateTx=Double.parseDouble(totalAmount) * Double.parseDouble(msgResponce.getTotalCartItem()) / 100;
                stateTax= String.valueOf(stateTx);
            }else stateTax="0.0";
            stateTaxAply=true;
        }else if(msgResponce.getStatus().equalsIgnoreCase("stateSuccess" )){
            stateTaxAply=false;
        }else Global.msgDialog(UserAddressActivity.this, msgResponce.getMsg());
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
            addressEdit.setText(address);
            stateEdit.setText(state);
            cityEdit.setText(city);
            zipEdit.setText(postalCode);
            apiViewModel.callState(state,UserAddressActivity.this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            if (requestCode == GETLOCATION) {
                addressEdit.setText(data.getStringExtra("address"));
                latitudeStr = data.getStringExtra("lat");
                longitudeStr = data.getStringExtra("long");
                if (data.getStringExtra("address") != null && data.getStringExtra("address").length() != 0) {
                    getAddress(Double.valueOf(latitudeStr), Double.valueOf(longitudeStr));
                }
            }
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
                    addressEdit.setText(getAddress(location.getLatitude(), location.getLongitude()));
                    latitudeStr=""+location.getLatitude();
                    longitudeStr=""+location.getLongitude();
                }
            }
        });
    }

}

