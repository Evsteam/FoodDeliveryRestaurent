package com.evs.foodexp.commonPkg.common.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.evs.foodexp.R;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;

import java.util.ArrayList;
import java.util.List;


public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button customerBtn,restaurantBtn, driverBtn;
    SharedPreferences preferences;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
   /* public GPSTracker gpsTracker;
    Geocoder geocoder;*/
   View view;
    List<Address> addresses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        /*gpsTracker = new GPSTracker(WelcomeActivity.this);
        geocoder = new Geocoder(this, Locale.getDefault());*/
        preferences = PreferenceManager.getDefaultSharedPreferences(WelcomeActivity.this);
        if (!checkAndRequestPermissions()) {
            checkAndRequestPermissions();
        }
        Utills.StatusBarColour(WelcomeActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarWelcome);
        customerBtn = (Button)findViewById(R.id.customerWelcome);
        driverBtn = (Button)findViewById(R.id.plusDriverWelcome);
        restaurantBtn = (Button)findViewById(R.id.restaurentWelcome);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("WELCOME");
        setSupportActionBar(toolbar);
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);*/
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(!Global.GpsEnable(WelcomeActivity.this)){
            Global.showGPSDisabledAlertToUser(WelcomeActivity.this);
        }
       /* System.out.println("Latitude === "+gpsTracker.getLatitude());
        System.out.println("Longitude === "+gpsTracker.getLongitude());
        System.out.println("Location === "+gpsTracker.getLocation());

        try {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*String address = addresses.get(0).getLocality(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();*/

       /* System.out.println("Address == "+address+","+state+","+country+","+postalCode+","+knownName);
        String fullAddress = String.valueOf(address+","+state+","+country+","+postalCode+","+knownName);

        Toast.makeText(getApplicationContext(), ""+fullAddress, Toast.LENGTH_SHORT).show();
*/
        customerBtn.setOnClickListener(this);
        driverBtn.setOnClickListener(this);
        restaurantBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == customerBtn){
            Intent intent = new Intent(WelcomeActivity.this, Getstart.class);
            SessionManager.save_userType(preferences,"Member");
            startActivity(intent);
        }

        if(v == driverBtn){
            Intent intent = new Intent(WelcomeActivity.this,Getstart.class);
                SessionManager.save_userType(preferences,"Driver");
            startActivity(intent);
        }

        if(v == restaurantBtn){
            Intent intent = new Intent(WelcomeActivity.this,Getstart.class);
            SessionManager.save_userType(preferences,"Restaurant");
            startActivity(intent);
        }

    }

    private  boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int loc = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

}
