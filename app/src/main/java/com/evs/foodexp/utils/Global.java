package com.evs.foodexp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;


import com.evs.foodexp.R;

import static android.content.Context.LOCATION_SERVICE;



public class Global {

    public static String type_business = "Restaurant";
    public static String type_person = "Member";
    public static String type_driver = "Driver";
    public static String api_key = "&key=AIzaSyDEYP9nenPV-zJjlCSbPuyf9eYSOfBdKlk";
    public static final String JOB_STATE_CHANGED = "jobStateChanged";
    public static final String LOCATION_ACQUIRED = "locAcquired";


//    public static String PUBLISHABLE_KEY = "pk_live_IOyNh3qhvNhZ9ySchHMgrXto00RRDR6N7f";
//    public static String SECRET_KEY = "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";


    public static String PUBLISHABLE_KEY = "pk_test_g81fdr3TVQTVTVTvyleDtLkM00pnWCXte4";
    public static String SECRET_KEY = "sk_test_jtj3hTUlMCczOUZoietBEofn003muU0wDe";

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
            return true;
        else return false;
    }

    public static boolean validatename(String firstrname) {
        if (firstrname.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateLength(String firstrname, int s) {
        if (firstrname.length() >= s) {
            return true;
        } else {
            return false;
        }

    }

    public final static boolean validateEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean password(String firstrname) {
        if (firstrname.length() >= 6) {
            return true;
        } else {
            return false;
        }
    }

    public final static boolean ValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }



    public static void msgDialog(Activity ac, String msg) {
        try {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(ac);
            alertbox.setTitle(ac.getResources().getString(R.string.app_name));
            alertbox.setMessage(msg);
            alertbox.setPositiveButton(ac.getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });
            alertbox.show();
        } catch (Exception e) {
        }
    }

     public static void msgDialogBack(final  Activity ac, String msg) {
         try {
             AlertDialog.Builder alertbox = new AlertDialog.Builder(ac);
             alertbox.setTitle(ac.getResources().getString(R.string.app_name));
             alertbox.setMessage(msg);
             alertbox.setPositiveButton(ac.getResources().getString(R.string.ok),
                     new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface arg0, int arg1) {
                             ac.onBackPressed();
                         }
                     });
             alertbox.show();
         } catch (Exception e) {
         }
     }

    public static void showDialog(final Activity ac) {
        android.app.AlertDialog.Builder alertbox = new android.app.AlertDialog.Builder(ac);
        alertbox.setTitle(ac.getResources().getString(R.string.app_name));
        alertbox.setMessage("No Internet Connection!");
        alertbox.setPositiveButton(ac.getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        ac.finish();
                    }
                });
        alertbox.show();
    }

    public static void showSettingsDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("Go - To SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings(activity);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }


    // navigating user to app settings
    private static void openSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, 101);
    }

    public static void showGPSDisabledAlertToUser(final Activity activity) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Go - To Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                activity.startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public static boolean GpsEnable(Activity activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean CheckGpsLocation(final Context context){
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE );
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return statusOfGPS;
    }
}
