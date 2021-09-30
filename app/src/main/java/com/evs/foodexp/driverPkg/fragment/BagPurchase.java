package com.evs.foodexp.driverPkg.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.common.activity.AddYourLoaction;
import com.evs.foodexp.userPkg.activity.PaymentUserActivity;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class BagPurchase extends Fragment {

    LoaderProgress progress;
    Button btn_save;
    EditText et_name, et_Country, et_state, et_zipcode, et_address, et_city,et_phone;
    Spinner spn_state;
    ImageView gettingLocation;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bag_purchase, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("Bag Purchase");
        et_name = view.findViewById(R.id.et_name);
        et_Country = view.findViewById(R.id.et_Country);
        et_state = view.findViewById(R.id.et_state);
        et_zipcode = view.findViewById(R.id.et_zipcode);
        et_phone = view.findViewById(R.id.et_phone);
        et_city = view.findViewById(R.id.et_city);
        et_address = view.findViewById(R.id.et_address);
        btn_save = view.findViewById(R.id.btn_save);
        gettingLocation = view.findViewById(R.id.gettingLocation);
        progress = new LoaderProgress(getContext());
        if (Global.isOnline(getContext())) {

        }
        if(!Global.GpsEnable(getActivity())){
            Global.showGPSDisabledAlertToUser(getActivity());
        }else {
            setMyLastLocation();
        }
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();

            }
        });
        gettingLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Global.GpsEnable(getActivity())) {
                    Global.showGPSDisabledAlertToUser(getActivity());
                }else {
                Intent i = new Intent(getContext(), AddYourLoaction.class);
                startActivityForResult(i, 1);
                }
            }
        });

        return view;
    }

    private void validation() {
        if (!Global.validatename(et_name.getText().toString())) {
            et_name.setError("Enter your Full Name");
        } else if (!Global.validatename(et_Country.getText().toString())) {
            et_Country.setError("Enter your Country");
        } else if (!Global.password(et_state.getText().toString())) {
            et_state.setError("Enter your State");
        } else if (!Global.validatename(et_zipcode.getText().toString())) {
            et_zipcode.setError("Enter valid Zipcode");
        } else if (!Global.validateLength(et_phone.getText().toString(),6)) {
            et_phone.setError("Enter valid Phone number");
        } else if (!Global.validatename(et_address.getText().toString())) {
            et_address.setError("Enter valid address!!");
        } else if (!Global.password(et_zipcode.getText().toString())) {
            et_zipcode.setError("Enter Correct Password");
        } else if (Global.isOnline(getContext())) {
            Intent intent = new Intent(getContext(), PaymentUserActivity.class);
            intent.putExtra("foodOrderGroupId", "bag");
            intent.putExtra("totalAmount", "20.00");
            intent.putExtra("name", et_name.getText().toString());
            intent.putExtra("Country", et_Country.getText().toString());
            intent.putExtra("state", et_state.getText().toString());
            intent.putExtra("zipcode", et_zipcode.getText().toString());
            intent.putExtra("address", et_address.getText().toString());
            intent.putExtra("city", et_city.getText().toString());
            intent.putExtra("phone", et_phone.getText().toString());
            startActivity(intent);
        }
    }

    private String getAddress(double lat,Double longs) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        String address = "";
        try {
            addresses = geocoder.getFromLocation(lat, longs, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            et_state.setText(state);
            et_Country.setText(country);
            et_city.setText(city);
            et_zipcode.setText(postalCode);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                et_address.setText(data.getStringExtra("address"));
                if (data.getStringExtra("address") != null && data.getStringExtra("address").length() != 0) {
                    getAddress(Double.valueOf(data.getStringExtra("lat")), Double.valueOf(data.getStringExtra("long")));
                }
            }
        }
    }


    private void setMyLastLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    et_address.setText(getAddress(location.getLatitude(), location.getLongitude()));
                }
            }
        });
    }
}



