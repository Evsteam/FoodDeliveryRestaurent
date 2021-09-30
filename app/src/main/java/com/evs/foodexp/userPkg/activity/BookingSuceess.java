package com.evs.foodexp.userPkg.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import  androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.commonPkg.viewModel.RestorentViewModel;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class BookingSuceess extends AppCompatActivity
        implements AuthMsgListener {
    Button confirmPay;
    RelativeLayout layout_call;
    TextView tv_serviceName, tv_contactNumber, below_title, tv_titleMsg, tv_requestMsg, tv_dateTime, tv_name;
    CircleImageView providerImg;
    RestorentViewModel viewModel;
    LoaderProgress progress;
    SharedPreferences prefs;
    LinearLayout userDetails;
    RelativeLayout layout_dateTime;
    GifImageView gifImageView;
    APIViewModel apiViewModel;
    String totalAmount, fooOrderId,AdminAmount,driverStripeAccount, transactionFee, foodOrderGroupId, driverContect, driverName, driverImage, driverID = "", type = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_request_accept);
        Utills.StatusBarColour(BookingSuceess.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("Request Sent");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        confirmPay = findViewById(R.id.confirmPay);
        layout_call = findViewById(R.id.layout_call);
        layout_dateTime = findViewById(R.id.layout_dateTime);
        gifImageView = findViewById(R.id.confirmTick);
        tv_titleMsg = findViewById(R.id.tv_titleMsg);
        below_title = findViewById(R.id.below_title);
        tv_requestMsg = findViewById(R.id.tv_requestMsg);
        userDetails = findViewById(R.id.userDetails);
        tv_contactNumber = findViewById(R.id.tv_contactNumber);
        tv_serviceName = findViewById(R.id.tv_serviceName);
        tv_dateTime = findViewById(R.id.tv_dateTime);
        tv_name = findViewById(R.id.tv_name);
        providerImg = findViewById(R.id.providerImg);

        prefs = PreferenceManager.getDefaultSharedPreferences(BookingSuceess.this);
        progress = new LoaderProgress(BookingSuceess.this);
        viewModel = new ViewModelProvider(BookingSuceess.this).get(RestorentViewModel.class);
        apiViewModel = new ViewModelProvider(BookingSuceess.this).get(APIViewModel.class);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            totalAmount = bundle.getString("totalAmount");
            fooOrderId = bundle.getString("fooOrderId");
            foodOrderGroupId = bundle.getString("foodOrderGroupId");
            driverContect = bundle.getString("driverContect");
            driverName = bundle.getString("driverName");
            driverImage = bundle.getString("driverImage");
            driverID = bundle.getString("driverID");
            transactionFee = bundle.getString("transactionFee");
            driverStripeAccount = bundle.getString("driverStripeAccount");
            AdminAmount = bundle.getString("AdminAmount");
            type = bundle.getString("type");

            totalAmount = String.valueOf(Double.parseDouble(totalAmount) + Double.parseDouble(transactionFee));

            if (driverID != null) {
                tv_name.setText(driverName);
                toolbarTitle.setText("Booking Success!!");
                tv_contactNumber.setText(driverContect);
                userDetails.setVisibility(View.VISIBLE);
                confirmPay.setVisibility(View.VISIBLE);
                tv_titleMsg.setText("Booking Success!!");
                below_title.setText("Please find the Service Provider info below!");
                Glide.with(BookingSuceess.this).load("").placeholder(R.drawable.sucess).into(gifImageView);
                Glide.with(BookingSuceess.this).load(driverImage).placeholder(R.drawable.sucess).into(providerImg);
            }


        }

        layout_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + driverContect));
                startActivity(callIntent);
            }
        });


        confirmPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("order")) {
                    Intent intent = new Intent(BookingSuceess.this, PaymentUserActivity.class);
                    intent.putExtra("foodOrderGroupId", foodOrderGroupId);
                    intent.putExtra("totalAmount", totalAmount);
                    intent.putExtra("driverStripeAccount", driverStripeAccount);
                    intent.putExtra("AdminAmount", AdminAmount);
                    startActivity(intent);
                } else if (type.equalsIgnoreCase("service")) {

                }
            }
        });
        if (type.equalsIgnoreCase("order")) {
            apiViewModel.deleteCart(SessionManager.get_user_id(prefs), this);
        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(BookingSuceess.this, UserHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialog(BookingSuceess.this, message);
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progress.dismiss();
    }


}
