package com.evs.foodexp.driverPkg.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.evs.foodexp.R;
import com.evs.foodexp.utils.Utills;

public class DriverToGetDetailActivity extends AppCompatActivity {

    Button requestMoreMoneyBtn;
    TextView sendRequestForMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_to_get_detail);

        Utills.StatusBarColour(DriverToGetDetailActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDriverToGetJobDetail);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("TO GET DETAILS");
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

        requestMoreMoneyBtn = (Button)findViewById(R.id.requestMoreMoney);

        requestMoreMoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DriverToGetDetailActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.request_more_money_alert, viewGroup, false);
                TextView tv_estimatePrice=dialogView.findViewById(R.id.tv_estimatePrice);
                Button btn_send=dialogView.findViewById(R.id.btn_send);
                TextView et_price=dialogView.findViewById(R.id.et_price);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();

                sendRequestForMoney.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(),UploadImageDeliveryActivity.class);
                        startActivity(intent);
                    }
                });

                alertDialog.show();

            }
        });

    }
}
