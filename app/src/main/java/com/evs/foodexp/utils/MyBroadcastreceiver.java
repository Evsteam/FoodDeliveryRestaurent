package com.evs.foodexp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.evs.foodexp.driverPkg.activity.DriverHomeActivity;
import com.evs.foodexp.userPkg.activity.UserHomeActivity;

/**
 * Created by Manish Yadav on 6/28/2017.
 */

public class MyBroadcastreceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getStringExtra("notificationType").equalsIgnoreCase("ServiceRequest")) {
            Intent sintent = new Intent(context.getApplicationContext(), DriverHomeActivity.class);
            sintent.putExtra("serviceName", intent.getStringExtra("serviceName"));
            sintent.putExtra("bookingId", intent.getStringExtra("bookingId"));
            sintent.putExtra("time", intent.getStringExtra("time"));
            sintent.putExtra("address", intent.getStringExtra("address"));
            sintent.putExtra("bookingDate", intent.getStringExtra("bookingDate"));
            sintent.putExtra("notificationType", intent.getStringExtra("notificationType"));
            sintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(sintent);
        } else if (intent.getStringExtra("notificationType").equalsIgnoreCase("ToGetRequest")) {
            Intent sintent = new Intent(context.getApplicationContext(), DriverHomeActivity.class);
            sintent.putExtra("togetRequestId", intent.getStringExtra("togetRequestId"));
            sintent.putExtra("togetDriverRequestId", intent.getStringExtra("togetDriverRequestId"));
            sintent.putExtra("title", intent.getStringExtra("title"));
            sintent.putExtra("address", intent.getStringExtra("address"));
            sintent.putExtra("storeCity", intent.getStringExtra("storeCity"));
            sintent.putExtra("notificationType", intent.getStringExtra("notificationType"));
            sintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(sintent);
        } else if (intent.getStringExtra("notificationType").equalsIgnoreCase("SpecialRequest")) {
            Intent sintent = new Intent(context.getApplicationContext(), DriverHomeActivity.class);
            sintent.putExtra("specialrequestId", intent.getStringExtra("specialrequestId"));
            sintent.putExtra("whatYouWant",intent.getStringExtra("whatYouWant"));
            sintent.putExtra("address",intent.getStringExtra("address"));
            sintent.putExtra("userId",intent.getStringExtra("userId"));
            sintent.putExtra("userName",intent.getStringExtra("userName"));
            sintent.putExtra("userPhone",intent.getStringExtra("userPhone"));
            sintent.putExtra("userImage",intent.getStringExtra("userImage"));
            sintent.putExtra("driverId",intent.getStringExtra("driverId"));
            sintent.putExtra("driverName",intent.getStringExtra("driverName"));
            sintent.putExtra("driverPhone",intent.getStringExtra("driverPhone"));
            sintent.putExtra("driverImage",intent.getStringExtra("driverImage"));
            sintent.putExtra("price",intent.getStringExtra("price"));
            sintent.putExtra("deliveryFee",intent.getStringExtra("deliveryFee"));
            sintent.putExtra("TIP",intent.getStringExtra("TIP"));
            sintent.putExtra("salesTax",intent.getStringExtra("salesTax"));
            sintent.putExtra("latitude",intent.getStringExtra("latitude"));
            sintent.putExtra("totalAmount",intent.getStringExtra("totalAmount"));
            sintent.putExtra("status",intent.getStringExtra("status"));
            sintent.putExtra("transactionFee",intent.getStringExtra("transactionFee"));
            sintent.putExtra("notificationType", intent.getStringExtra("notificationType"));
            sintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(sintent);

        } else if (intent.getStringExtra("notificationType").equalsIgnoreCase("specialAccept")) {
            Intent sintent = new Intent(context.getApplicationContext(), UserHomeActivity.class);
            sintent.putExtra("specialrequestId", intent.getStringExtra("specialrequestId"));
            sintent.putExtra("whatYouWant",intent.getStringExtra("whatYouWant"));
            sintent.putExtra("address",intent.getStringExtra("address"));
            sintent.putExtra("userId",intent.getStringExtra("userId"));
            sintent.putExtra("userName",intent.getStringExtra("userName"));
            sintent.putExtra("userPhone",intent.getStringExtra("userPhone"));
            sintent.putExtra("userImage",intent.getStringExtra("userImage"));
            sintent.putExtra("driverId",intent.getStringExtra("driverId"));
            sintent.putExtra("driverName",intent.getStringExtra("driverName"));
            sintent.putExtra("driverPhone",intent.getStringExtra("driverPhone"));
            sintent.putExtra("driverImage",intent.getStringExtra("driverImage"));
            sintent.putExtra("price",intent.getStringExtra("price"));
            sintent.putExtra("deliveryFee",intent.getStringExtra("deliveryFee"));
            sintent.putExtra("TIP",intent.getStringExtra("TIP"));
            sintent.putExtra("salesTax",intent.getStringExtra("salesTax"));
            sintent.putExtra("latitude",intent.getStringExtra("latitude"));
            sintent.putExtra("totalAmount",intent.getStringExtra("totalAmount"));
            sintent.putExtra("status",intent.getStringExtra("status"));
            sintent.putExtra("transactionFee",intent.getStringExtra("transactionFee"));
            sintent.putExtra("notificationType", intent.getStringExtra("notificationType"));
            sintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(sintent);

        } else if (intent.getStringExtra("notificationType").equalsIgnoreCase("FoodRequest")) {
            Intent sintent = new Intent(context.getApplicationContext(), DriverHomeActivity.class);
            sintent.putExtra("foodorderId", intent.getStringExtra("foodorderId"));
            sintent.putExtra("foodrequestId", intent.getStringExtra("foodrequestId"));
            sintent.putExtra("estimateDistace", intent.getStringExtra("estimateDistace"));
            sintent.putExtra("estimateTime", intent.getStringExtra("estimateTime"));
            sintent.putExtra("estimateDistace", intent.getStringExtra("estimateDistace"));
            sintent.putExtra("dropLocation", intent.getStringExtra("dropLocation"));
            sintent.putExtra("pickupLoaction", intent.getStringExtra("pickupLoaction"));
            sintent.putExtra("resturentName", intent.getStringExtra("resturentName"));
            sintent.putExtra("notificationType", intent.getStringExtra("notificationType"));
            sintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(sintent);
        } else if (intent.getStringExtra("notificationType").equalsIgnoreCase("foodAccept")) {
            Intent sintent = new Intent(context.getApplicationContext(), UserHomeActivity.class);
            sintent.putExtra("totalAmount", intent.getStringExtra("totalAmount"));
            sintent.putExtra("fooOrderId", intent.getStringExtra("fooOrderId"));
            sintent.putExtra("foodOrderGroupId", intent.getStringExtra("foodOrderGroupId"));
            sintent.putExtra("driverContect", intent.getStringExtra("driverContect"));
            sintent.putExtra("driverName", intent.getStringExtra("driverName"));
            sintent.putExtra("driverImage", intent.getStringExtra("driverImage"));
            sintent.putExtra("driverID", intent.getStringExtra("driverID"));
            sintent.putExtra("transactionFee", intent.getStringExtra("transactionFee"));
            sintent.putExtra("AdminAmount", intent.getStringExtra("AdminAmount"));
            sintent.putExtra("driverStripeAccount", intent.getStringExtra("driverStripeAccount"));
            sintent.putExtra("notificationType","foodAccept");
            sintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(sintent);

        } else if (intent.getStringExtra("notificationType").equalsIgnoreCase("ServiceAccept")) {
            Intent sintent = new Intent(context.getApplicationContext(), UserHomeActivity.class);
            sintent.putExtra("bookingId", intent.getStringExtra("bookingId"));
            sintent.putExtra("notificationType","ServiceAccept");
            sintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(sintent);

        } else if (intent.getStringExtra("notificationType").equalsIgnoreCase("ToGetRequestAccept")) {
            Intent sintent = new Intent(context.getApplicationContext(), UserHomeActivity.class);
            sintent.putExtra("togetRequestId", intent.getStringExtra("togetRequestId"));
            sintent.putExtra("notificationType","ToGetRequestAccept");
            sintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(sintent);


        }



        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(5000);
        }


    }

}