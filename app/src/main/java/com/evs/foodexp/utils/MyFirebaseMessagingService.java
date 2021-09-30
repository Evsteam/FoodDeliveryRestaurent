package com.evs.foodexp.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.common.activity.SplashScreenActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;
import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    String messagetype = "";
    SharedPreferences prefs;
    MyBroadcastreceiver br;


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        try {
            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SessionManager.save_device_token(prefs, s);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Body: " + remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        try {
            Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);
            try {
                messagetype = object.getString("type");
            } catch (Exception e) {

            }

            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Log.d(TAG, "Message type Body: " + remoteMessage.getData().get("type"));
            try {
                sendNotification(remoteMessage.getData().get("message"));
                if(messagetype!=null){
                    if (messagetype.equalsIgnoreCase("ToGetRequest")) {
                        driverTogetNotification(remoteMessage.getData().get("wahtUwant"),
                                remoteMessage.getData().get("address"), remoteMessage.getData().get("togetDriverRequestId"),
                                remoteMessage.getData().get("togetRequestId"),
                                remoteMessage.getData().get("StoreCity"));
                    }else if(messagetype.equalsIgnoreCase("ServiceRequest")){
                        drivercNotification(remoteMessage.getData().get("serviceName"),remoteMessage.getData().get("bookingId"),
                                remoteMessage.getData().get("slote"),remoteMessage.getData().get("address"),remoteMessage.getData().get("bookingDate"));

                    }else if(messagetype.equalsIgnoreCase("specialRequest")){
                        drivercNotificationSpecialRequest(remoteMessage.getData().get("address"),remoteMessage.getData().get("specialrequestId"),
                                remoteMessage.getData().get("whatYouWant"),
                                remoteMessage.getData().get("userId"),
                                remoteMessage.getData().get("userName"),
                                remoteMessage.getData().get("userPhone"),
                                remoteMessage.getData().get("userImage"),
                                remoteMessage.getData().get("driverId"),
                                remoteMessage.getData().get("driverName"),
                                remoteMessage.getData().get("driverPhone"),
                                remoteMessage.getData().get("driverImage"),
                                remoteMessage.getData().get("price"),
                                remoteMessage.getData().get("deliveryFee"),
                                remoteMessage.getData().get("TIP"),
                                remoteMessage.getData().get("salesTax"),
                                remoteMessage.getData().get("latitude"),
                                remoteMessage.getData().get("totalAmount"),
                                remoteMessage.getData().get("transactionFee"),
                                remoteMessage.getData().get("status"),
                                "SpecialRequest");


                    }else if(messagetype.equalsIgnoreCase("FoodRequest")){
                        driverFoodNotification(remoteMessage.getData().get("address"),
                                remoteMessage.getData().get("resturentAddress"), remoteMessage.getData().get("totalTime"),remoteMessage.getData().get("totalDistance"),
                                remoteMessage.getData().get("foodrequestId"),remoteMessage.getData().get("foodOrderId"),remoteMessage.getData().get("resturentName"));
                    }else if(messagetype.equalsIgnoreCase("foodAccept")) {
                        foodAccept(remoteMessage.getData().get("totalAmount"),
                                remoteMessage.getData().get("fooOrderId"),
                                remoteMessage.getData().get("foodOrderGroupId"),
                                remoteMessage.getData().get("driverContect"),
                                remoteMessage.getData().get("driverName"),
                                remoteMessage.getData().get("driverImage"),
                                remoteMessage.getData().get("driverID"),
                                remoteMessage.getData().get("transactionFee"),
                                remoteMessage.getData().get("AdminAmount"),
                                remoteMessage.getData().get("driverStripeAccount"));
                    }else if(messagetype.equalsIgnoreCase("ServiceAccept")){
                        serviceAccept(remoteMessage.getData().get("bookingId"));

                    }else if(messagetype.equalsIgnoreCase("ToGetRequestAccept")){
                        togetAccept(remoteMessage.getData().get("togetRequestId"));
                    }else if(messagetype.equalsIgnoreCase("specialAccept")){
                        drivercNotificationSpecialRequest(remoteMessage.getData().get("address"),remoteMessage.getData().get("specialrequestId"),
                                remoteMessage.getData().get("whatYouWant"),
                                remoteMessage.getData().get("userId"),
                                remoteMessage.getData().get("userName"),
                                remoteMessage.getData().get("userPhone"),
                                remoteMessage.getData().get("userImage"),
                                remoteMessage.getData().get("driverId"),
                                remoteMessage.getData().get("driverName"),
                                remoteMessage.getData().get("driverPhone"),
                                remoteMessage.getData().get("driverImage"),
                                remoteMessage.getData().get("price"),
                                remoteMessage.getData().get("deliveryFee"),
                                remoteMessage.getData().get("TIP"),
                                remoteMessage.getData().get("salesTax"),
                                remoteMessage.getData().get("latitude"),
                                remoteMessage.getData().get("totalAmount"),
                                remoteMessage.getData().get("transactionFee"),
                                remoteMessage.getData().get("status"),
                                "specialAccept");
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void togetAccept(String togetRequestId) {
        Intent intent = new Intent("com.evs.foodexp.utils.MyBroadcastreceiver");
        intent.setAction("com.evs.foodexp.utils.MyBroadcastreceiver");
        intent.putExtra("togetRequestId",togetRequestId);
        intent.putExtra("notificationType","ToGetRequestAccept");
        sendBroadcast(intent);
    }

    private void serviceAccept(String bookingId) {
        Intent intent = new Intent("com.evs.foodexp.utils.MyBroadcastreceiver");
        intent.setAction("com.evs.foodexp.utils.MyBroadcastreceiver");
        intent.putExtra("bookingId",bookingId);
        intent.putExtra("notificationType","ServiceAccept");
        sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        br = new MyBroadcastreceiver();
        IntentFilter filter = new IntentFilter("com.evs.foodexp.utils.MyBroadcastreceiver");
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        this.registerReceiver(br, filter);
    }

    private void driverFoodNotification(String dropLocation,String pickupLoaction,String estimateTime,
                                        String estimateDistace,String foodrequestId, String foodorderId,String resturentName ) {
        Intent intent = new Intent("com.evs.foodexp.utils.MyBroadcastreceiver");
        intent.setAction("com.evs.foodexp.utils.MyBroadcastreceiver");
        intent.putExtra("dropLocation",dropLocation);
        intent.putExtra("pickupLoaction",pickupLoaction);
        intent.putExtra("estimateTime",estimateTime);
        intent.putExtra("estimateDistace",estimateDistace);
        intent.putExtra("foodrequestId",foodrequestId);
        intent.putExtra("foodorderId",foodorderId);
        intent.putExtra("resturentName",resturentName);
        intent.putExtra("notificationType","FoodRequest");
        sendBroadcast(intent);
    }

    private void drivercNotification(String serviceName,String bookingId,String time,
                                     String address,String bookingDate) {
        Intent intent = new Intent("com.evs.foodexp.utils.MyBroadcastreceiver");
        intent.setAction("com.evs.foodexp.utils.MyBroadcastreceiver");
        intent.putExtra("serviceName",serviceName);
        intent.putExtra("bookingId",bookingId);
        intent.putExtra("time",time);
        intent.putExtra("address",address);
        intent.putExtra("bookingDate",bookingDate);
        intent.putExtra("notificationType","ServiceRequest");
        sendBroadcast(intent);
    }


    private void foodAccept(String totalAmount, String fooOrderId, String foodOrderGroupId, String driverContect,
                            String driverName, String driverImage,String driverID,String transactionFee,String AdminAmount,String driverStripeAccount) {
        Intent intent = new Intent("com.evs.foodexp.utils.MyBroadcastreceiver");
        intent.setAction("com.evs.foodexp.utils.MyBroadcastreceiver");
        intent.putExtra("totalAmount",totalAmount);
        intent.putExtra("fooOrderId",fooOrderId);
        intent.putExtra("foodOrderGroupId",foodOrderGroupId);
        intent.putExtra("driverContect",driverContect);
        intent.putExtra("driverName",driverName);
        intent.putExtra("driverImage",driverImage);
        intent.putExtra("driverID",driverID);
        intent.putExtra("transactionFee",transactionFee);
        intent.putExtra("AdminAmount",AdminAmount);
        intent.putExtra("driverStripeAccount",driverStripeAccount);
        intent.putExtra("notificationType","foodAccept");
        sendBroadcast(intent);
    }


    private void drivercNotificationSpecialRequest(String address ,String specialrequestId,String whatYouWant
            ,String userId,String userName,String userPhone,String userImage
            ,String driverId,String driverName,String driverPhone,String driverImage,String price,String deliveryFee
            ,String TIP,String salesTax,String latitude,String totalAmount,String transactionFee,String status, String type) {
        Intent intent = new Intent("com.evs.foodexp.utils.MyBroadcastreceiver");
        intent.setAction("com.evs.foodexp.utils.MyBroadcastreceiver");
        intent.putExtra("specialrequestId",specialrequestId);
        intent.putExtra("whatYouWant",whatYouWant);
        intent.putExtra("address",address);
        intent.putExtra("userId",userId);
        intent.putExtra("userName",userName);
        intent.putExtra("userPhone",userPhone);
        intent.putExtra("userImage",userImage);
        intent.putExtra("driverId",driverId);
        intent.putExtra("driverName",driverName);
        intent.putExtra("driverPhone",driverPhone);
        intent.putExtra("driverImage",driverImage);
        intent.putExtra("price",price);
        intent.putExtra("deliveryFee",deliveryFee);
        intent.putExtra("TIP",TIP);
        intent.putExtra("salesTax",salesTax);
        intent.putExtra("latitude",latitude);
        intent.putExtra("totalAmount",totalAmount);
        intent.putExtra("transactionFee",transactionFee);
        intent.putExtra("status",status);
        intent.putExtra("notificationType",type);

        sendBroadcast(intent);
    }

    private void driverTogetNotification(String wahtUwant,
                                         String address,String togetDriverRequestId,String togetRequestId,String storeCity) {

        Intent intent = new Intent("com.evs.foodexp.utils.MyBroadcastreceiver");
        intent.setAction("com.evs.foodexp.utils.MyBroadcastreceiver");
        intent.putExtra("togetRequestId",togetRequestId);
        intent.putExtra("togetDriverRequestId",togetDriverRequestId);
        intent.putExtra("title",wahtUwant);
        intent.putExtra("address",address);
        intent.putExtra("storeCity",storeCity);
        intent.putExtra("notificationType","ToGetRequest");
        sendBroadcast(intent);
    }


    private void sendNotification(String messageBody) {
        Intent intent;
        intent = new Intent(this, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Uri defaultSoundUri = Uri.parse("android.resource://" + getPackageName() + "/raw/sounds");

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getResources().getString(R.string.default_notification_channel_id));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = this.getString(R.string.default_notification_channel_id);
            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(channelId, getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            notificationBuilder.setChannelId(channelId);
//            "MyBourse01"
        }

        notificationBuilder.setSmallIcon(R.drawable.app_icon_1024);
        notificationBuilder.setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{1000, 1000})
                .setContentIntent(pendingIntent);

        Random rand = new Random();
        int as = rand.nextInt();
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(as /* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(br);
    }

}