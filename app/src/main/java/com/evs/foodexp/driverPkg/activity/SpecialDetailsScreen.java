package com.evs.foodexp.driverPkg.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import  androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.common.activity.TrackInProgress;
import com.evs.foodexp.commonPkg.common.activity.TrackOrder1;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.commonPkg.viewModel.RestorentViewModel;
import com.evs.foodexp.models.RequestModel;
import com.evs.foodexp.repositry.DataResponse;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.userPkg.activity.PaymentSpecialOrder;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DecimalFormat;

public class SpecialDetailsScreen extends AppCompatActivity implements
        AuthMsgListener {
    RoundedImageView img_profileImage, img_msg;
    TextView tv_product,transation,tv_transation, tv_whatStore, tv_tip, tv_sateTax, tv_estimatePrice, tv_Notes, tv_sNotes, tv_productName,
            tv_DevilryFee, tv_stoes, tv_title, tv_address;
    Button btn_totalAmount, btn_requestForMoney, btn_markDeliverd, btn_deliverd;
    String oderId, ePrice = "", status = "";
    RestorentViewModel viewModel;
    LoaderProgress progress;
    SharedPreferences prefe;
    String specialrequestId, paymentStatus = "0", userId, userName, userPhone, userImage,
            driverId, driverName, driverPhone, driverImage, whatYouWant, price, deliveryFee, TIP,
            salesTax, latitude, longitude, address, totalAmount, created, number, imageUpload,transactionFee;
    RelativeLayout layout_rating;
    RatingBar ratingBar;
    ImageView img_location;
    Button btn_tracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_job_detail);

        Utills.StatusBarColour(SpecialDetailsScreen.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("Special Order");
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
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            specialrequestId = bundle.getString("specialrequestId");
            userId = bundle.getString("userId");
            userName = bundle.getString("userName");
            userPhone = bundle.getString("userPhone");
            userImage = bundle.getString("userImage");
            driverId = bundle.getString("driverId");
            driverName = bundle.getString("driverName");
            driverPhone = bundle.getString("driverPhone");
            driverImage = bundle.getString("driverImage");
            whatYouWant = bundle.getString("whatYouWant");
            price = bundle.getString("price");
            deliveryFee = bundle.getString("deliveryFee");
            TIP = bundle.getString("TIP");
            salesTax = bundle.getString("salesTax");
            latitude = bundle.getString("latitude");
            longitude = bundle.getString("longitude");
            address = bundle.getString("address");
            totalAmount = bundle.getString("totalAmount");
            created = bundle.getString("created");
            status = bundle.getString("status");
            paymentStatus = bundle.getString("paymentStatus");
            transactionFee = bundle.getString("transactionFee");
            imageUpload = bundle.getString("imageUpload");
        }
        prefe = PreferenceManager.getDefaultSharedPreferences(SpecialDetailsScreen.this);

        tv_product = findViewById(R.id.tv_product);
        transation = findViewById(R.id.transation);
        tv_transation = findViewById(R.id.tv_transation);
        transation.setVisibility(View.VISIBLE);
        tv_transation.setVisibility(View.VISIBLE);

        btn_tracking = findViewById(R.id.btn_tracking);
        img_location = findViewById(R.id.img_location);
        layout_rating = findViewById(R.id.layout_rating);
        ratingBar = findViewById(R.id.ratingBar);
        tv_stoes = findViewById(R.id.tv_stoes);
        tv_sateTax = findViewById(R.id.tv_sateTax);
        tv_sNotes = findViewById(R.id.tv_sNotes);
        tv_stoes.setVisibility(View.GONE);
        tv_sNotes.setVisibility(View.GONE);
        tv_productName = findViewById(R.id.tv_productName);


        img_msg = findViewById(R.id.img_msg);
        btn_deliverd = findViewById(R.id.btn_deliverd);
        tv_address = findViewById(R.id.tv_address);
        btn_markDeliverd = findViewById(R.id.btn_markDeliverd);
        tv_title = findViewById(R.id.tv_title);
        tv_whatStore = findViewById(R.id.tv_whatStore);
        tv_whatStore.setVisibility(View.GONE);
        tv_tip = findViewById(R.id.tv_tip);
        tv_tip = findViewById(R.id.tv_tip);
        tv_estimatePrice = findViewById(R.id.tv_estimatePrice);
        tv_DevilryFee = findViewById(R.id.tv_DevilryFee);
        tv_Notes = findViewById(R.id.tv_Notes);
        tv_Notes.setVisibility(View.GONE);
        btn_totalAmount = findViewById(R.id.btn_totalAmount);
        btn_requestForMoney = findViewById(R.id.btn_requestForMoney);
        img_profileImage = findViewById(R.id.img_profileImage);
        btn_deliverd.setVisibility(View.VISIBLE);
        btn_markDeliverd.setVisibility(View.GONE);
        btn_requestForMoney.setVisibility(View.GONE);

        tv_product.setText(whatYouWant);
        tv_tip.setText("$ " + TIP);
        tv_sateTax.setText("$ " + salesTax);
        tv_estimatePrice.setText("$ " + price);
        tv_DevilryFee.setText("$ " + deliveryFee);
        tv_transation.setText("$ " + transactionFee);
        double totalAmountss=Double.parseDouble(totalAmount)+Double.parseDouble(transactionFee);
        totalAmount= new DecimalFormat("##.##").format(totalAmountss);
        btn_totalAmount.setText("Total Amount $ " +totalAmount);


        if (SessionManager.get_userType(prefe).equalsIgnoreCase(Global.type_person)) {
            if (Global.validatename(driverName)) {
                tv_address.setText(driverPhone);
                tv_title.setText(driverName);
                number = driverPhone;
                Glide.with(SpecialDetailsScreen.this)
                        .load(driverImage)
                        .placeholder(R.drawable.placeholder_user)
                        .into(img_profileImage);
                img_location.setVisibility(View.GONE);
            } else {
                tv_address.setText(userName);
                tv_title.setText(userName);
                number = userPhone;
                Glide.with(SpecialDetailsScreen.this)
                        .load(userImage)
                        .placeholder(R.drawable.placeholder_user)
                        .into(img_profileImage);
            }
        } else {

            tv_address.setText(address);
            tv_title.setText(userName);
            number = userPhone;
            Glide.with(SpecialDetailsScreen.this)
                    .load(userImage)
                    .placeholder(R.drawable.placeholder_user)
                    .into(img_profileImage);
        }

        layout_rating.setVisibility(View.GONE);
        viewModel = new ViewModelProvider(SpecialDetailsScreen.this).get(RestorentViewModel.class);
        progress = new LoaderProgress(SpecialDetailsScreen.this);
        if (!SessionManager.get_userType(prefe).equalsIgnoreCase(Global.type_person)) {
            if (paymentStatus.equalsIgnoreCase("1")) {
                btn_deliverd.setText("Waiting For Payment");
            } else if (status.equalsIgnoreCase("1")) {
                btn_deliverd.setText("Track your Order!!");

            } else btn_deliverd.setText("Completed");
        }else if (paymentStatus.equalsIgnoreCase("0")) {
            btn_deliverd.setText("Waiting For Driver");
        }else if (paymentStatus.equalsIgnoreCase("1")) {
            btn_deliverd.setText(getResources().getString(R.string.confirm_pay));
        }else if(status.equalsIgnoreCase("1")){
            btn_deliverd.setText("Track your Order!!");
        }

        btn_deliverd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SessionManager.get_userType(prefe).equalsIgnoreCase(Global.type_person)) {
                    if (!status.equalsIgnoreCase("2")) {
                        if(paymentStatus.equalsIgnoreCase("2")){
//                            viewModel.executeSpecialchangestatus(SessionManager.get_user_id(prefe), specialrequestId, "2", SpecialDetailsScreen.this);
                            Intent intent = new Intent(SpecialDetailsScreen.this, TrackInProgress.class);
                            intent.putExtra("bookingId", specialrequestId);
                            intent.putExtra("address", address);
                            intent.putExtra("name", userName);
                            intent.putExtra("number", number);
                            intent.putExtra("dropuserlat", latitude);
                            intent.putExtra("dropuserlong", longitude);
                            intent.putExtra("type", "Specialrequests");
                            intent.putExtra("uploadedImage", imageUpload);
                            startActivity(intent);
                        }

                    }
                }else if(paymentStatus.equalsIgnoreCase("1")){
                    Intent intent=new Intent(SpecialDetailsScreen.this,PaymentSpecialOrder.class);
                    intent.putExtra("totalAmount",totalAmount);
                    intent.putExtra("tip",TIP);
                    intent.putExtra("specialrequestId",specialrequestId);
                    startActivity(intent);

                }else if(paymentStatus.equalsIgnoreCase("2")){
                    if (!status.equalsIgnoreCase("2")) {
                        Intent intent=new Intent(SpecialDetailsScreen.this, TrackOrder1.class);
                        intent.putExtra("bookingId",specialrequestId);
                        intent.putExtra("driverId",driverId);
                        intent.putExtra("amount",totalAmount);
                        intent.putExtra("driverName",driverName);
                        intent.putExtra("dropLat",latitude);
                        intent.putExtra("dropLongs",longitude);
                        intent.putExtra("dropAddress",address);
                        intent.putExtra("uploadedImage", imageUpload);
                        startActivity(intent);
                    }
                }
            }
        });

        img_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SessionManager.get_userType(prefe).equalsIgnoreCase(Global.type_person)) {
                    if (Global.validatename(driverPhone)) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + driverPhone));
                        startActivity(callIntent);
                    }
                } else if (Global.validatename(userPhone)) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + userPhone));
                    startActivity(callIntent);
                }
            }
        });

        layout_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (Global.validatename(driverId)) {
//                    Submitreview cdd = new Submitreview(SpecialDetailsScreen.this, driverId);
//                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    cdd.show();
//                }
            }
        });

        btn_tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public void onStarted() {
        progress.show();
    }


    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialogBack(SpecialDetailsScreen.this, message);
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progress.dismiss();
        if (msgResponce.getStatus().equalsIgnoreCase("success")) {
            btn_deliverd.setText("Completed");
            status = "2";
            Toast.makeText(SpecialDetailsScreen.this, msgResponce.getMsg(), Toast.LENGTH_LONG).show();
        } else Global.msgDialog(SpecialDetailsScreen.this, msgResponce.getMsg());


    }


}
