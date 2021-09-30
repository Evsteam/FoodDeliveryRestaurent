package com.evs.foodexp.driverPkg.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import  androidx.lifecycle.ViewModelProvider;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.common.activity.AddYourLoaction;
import com.evs.foodexp.commonPkg.common.activity.StipeAccounteWeb;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.models.SubService;
import com.evs.foodexp.userPkg.activity.PaymentUserActivity;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DriverCompletaddress extends AppCompatActivity implements AuthStatusListener {

    LoaderProgress progress;
    Button btn_save;
    EditText  et_zipcode, et_address, et_city,et_phone;
    Spinner spn_state,spn_city;
    ImageView gettingLocation;
    APIViewModel viewModel;
    SharedPreferences prefe;
    ArrayList<String> statesId,stateCode,statesName,cityId,cityName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drive_address_price);
        Utills.StatusBarColour(DriverCompletaddress.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);

        toolbarTitle.setText("Address");
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
        viewModel = new ViewModelProvider(this).get(APIViewModel.class);
        prefe = PreferenceManager.getDefaultSharedPreferences(DriverCompletaddress.this);
        et_zipcode = findViewById(R.id.et_zipcode);
        et_phone = findViewById(R.id.et_phone);
        et_city = findViewById(R.id.et_city);
        spn_state = findViewById(R.id.spn_state);
        spn_city = findViewById(R.id.spn_city);
        et_address = findViewById(R.id.et_address);
        btn_save = findViewById(R.id.btn_save);
        gettingLocation =findViewById(R.id.gettingLocation);
        progress = new LoaderProgress(DriverCompletaddress.this);

        if(!Global.GpsEnable(DriverCompletaddress.this)){
            Global.showGPSDisabledAlertToUser(DriverCompletaddress.this);
        }else {
            setMyLastLocation();
        }

        gettingLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Global.GpsEnable(DriverCompletaddress.this)) {
                    Global.showGPSDisabledAlertToUser(DriverCompletaddress.this);
                }else {
                    Intent i = new Intent(DriverCompletaddress.this, AddYourLoaction.class);
                    startActivityForResult(i, 1);
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();

            }
        });

        if(Global.isOnline(DriverCompletaddress.this)){
         viewModel.callstates(DriverCompletaddress.this);
        }else Global.showDialog(DriverCompletaddress.this);

        viewModel.getStates().observe(this, new Observer<List<SubService>>() {
            @Override
            public void onChanged(List<SubService> subServices) {
                progress.dismiss();
                statesId=new ArrayList<>();
                stateCode=new ArrayList<>();
                statesName=new ArrayList<>();
                for (SubService model :subServices) {
                    statesId.add(model.getStateCode());
                    stateCode.add(model.getId());
                    statesName.add(model.getName());
                }
                ArrayAdapter<String> departMentAdapter = new ArrayAdapter<String>(DriverCompletaddress.this,
                        R.layout.spinner_item, statesName);
                departMentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_state.setAdapter(departMentAdapter);
            }
        });

        spn_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                viewModel.callcity(stateCode.get(spn_state.getSelectedItemPosition()),DriverCompletaddress.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        viewModel.getCitys().observe(this, new Observer<List<SubService>>() {
            @Override
            public void onChanged(List<SubService> subServices) {
                progress.dismiss();
                cityId=new ArrayList<>();
                cityName=new ArrayList<>();
                for (SubService model :subServices) {
                    cityId.add(model.getStateCode());
                    cityName.add(model.getName());
                }
                ArrayAdapter<String> departMentAdapter = new ArrayAdapter<String>(DriverCompletaddress.this,
                        R.layout.spinner_item, cityName);
                departMentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_city.setAdapter(departMentAdapter);
            }
        });

//        if(SessionManager.get_StripeStatus(prefe).equalsIgnoreCase("0")){
//            startActivity(new Intent(DriverCompletaddress.this, StipeAccounteWeb.class));
//        }
    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.bag_purchase, container, false);
//        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
//        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
//        toolbarTitle.setText("Bag Purchase");
//        et_name = view.findViewById(R.id.et_name);
//        et_Country = view.findViewById(R.id.et_Country);
//        et_state = view.findViewById(R.id.et_state);
//        et_zipcode = view.findViewById(R.id.et_zipcode);
//
//        et_city = view.findViewById(R.id.et_city);
//        et_address = view.findViewById(R.id.et_address);
//        btn_save = view.findViewById(R.id.btn_save);
//
//        if (Global.isOnline(getContext())) {
//
//        }
//
//
//        gettingLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!Global.GpsEnable(getActivity())) {
//                    Global.showGPSDisabledAlertToUser(getActivity());
//                }else {
//                Intent i = new Intent(getContext(), AddYourLoaction.class);
//                startActivityForResult(i, 1);
//                }
//            }
//        });
//
//        return view;
//    }

    private void validation() {
//        if (!Global.validatename(et_name.getText().toString())) {
//            et_name.setError("Enter your Full Name");
//        } else if (!Global.validatename(et_Country.getText().toString())) {
//            et_Country.setError("Enter your Country");
//        } else if (!Global.password(et_state.getText().toString())) {
//            et_state.setError("Enter your State");
         if (!Global.validatename(et_zipcode.getText().toString())) {
            et_zipcode.setError("Enter valid Zipcode");
        } else if (!Global.validateLength(et_phone.getText().toString(),6)) {
            et_phone.setError("Enter valid Phone number");
        } else if (!Global.validatename(et_address.getText().toString())) {
            et_address.setError("Enter valid address!!");
//        } else if (!Global.validatename(et_zipcode.getText().toString())) {
//            et_zipcode.setError("Enter Correct Password");
        } else if (Global.isOnline(DriverCompletaddress.this)) {
            Intent intent = new Intent(DriverCompletaddress.this, PaymentUserActivity.class);
            intent.putExtra("foodOrderGroupId", "reg");
            intent.putExtra("totalAmount", "5.00");
//            intent.putExtra("name", et_name.getText().toString());
//            intent.putExtra("Country", et_Country.getText().toString());
            intent.putExtra("state", statesId.get(spn_state.getSelectedItemPosition()));
            intent.putExtra("zipcode", et_zipcode.getText().toString());
            intent.putExtra("address", et_address.getText().toString());
            intent.putExtra("city",  cityName.get(spn_city.getSelectedItemPosition()));
            intent.putExtra("phone", et_phone.getText().toString());
            startActivity(intent);
        }
    }

    private String getAddress(double lat,Double longs) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(DriverCompletaddress.this, Locale.getDefault());

        String address = "",city="";
        try {
            addresses = geocoder.getFromLocation(lat, longs, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
             city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            et_city.setText(city);
            et_zipcode.setText(postalCode);
//            et_address.setText(knownName+" "+city+" "+state);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return city;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                et_address.setText(data.getStringExtra("address"));
                if (data.getStringExtra("address") != null && data.getStringExtra("address").length() != 0) {
                    getAddress(Double.valueOf(data.getStringExtra("lat")), Double.valueOf(data.getStringExtra("long")));
                    et_address.setError(null);
                }
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
        Global.msgDialogBack(DriverCompletaddress.this,message);
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
                    et_address.setText(getAddress(location.getLatitude(), location.getLongitude()));
                }
            }
        });
    }
}



