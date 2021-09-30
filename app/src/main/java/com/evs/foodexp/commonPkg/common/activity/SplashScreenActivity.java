package com.evs.foodexp.commonPkg.common.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

import com.evs.foodexp.R;
import com.evs.foodexp.driverPkg.activity.DriverHomeActivity;
import com.evs.foodexp.restaurentPkg.activity.RestaurentHomeActivity;
import com.evs.foodexp.userPkg.activity.UserHomeActivity;
import com.evs.foodexp.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    boolean permission = false;
    int PERMISSION_ALL = 1;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_scren);
        prefs = PreferenceManager.getDefaultSharedPreferences(SplashScreenActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // only for gingerbread and newer versions
            if (!permission) {
                if (checkAndRequestPermissions()) {
                    // carry on the normal flow, as the case of  permissions  granted.
                    sendIntent();
                    permission = true;
                }
            }
        } else {
            sendIntent();
        }

    }


    private boolean checkAndRequestPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int writeSDPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readSDPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int readCalender = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR);
        int writeCalender = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
//        if (writeCalender != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.WRITE_CALENDAR);
//        }
//        if (readCalender != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.READ_CALENDAR);
//        }
        if (writeSDPermission != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (readSDPermission != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        if (cameraPermission != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_ALL);
            return false;
        }
        return true;
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                sendIntent();
                return;
            }
            default:
                finish();
        }
    }

    void sendIntent() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (SessionManager.get_check_login(prefs)) {
                    if (SessionManager.get_userType(prefs).equalsIgnoreCase("Driver")) {
                        Intent intent = new Intent(SplashScreenActivity.this, DriverHomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (SessionManager.get_userType(prefs).equals("Restaurant")) {
                        Intent intent = new Intent(SplashScreenActivity.this, RestaurentHomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashScreenActivity.this, UserHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 1000);
    }
}
