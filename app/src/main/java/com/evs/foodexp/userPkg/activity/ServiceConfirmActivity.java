package com.evs.foodexp.userPkg.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.evs.foodexp.R;
import com.evs.foodexp.utils.Utills;

public class ServiceConfirmActivity extends AppCompatActivity {

    private Button backToHomeBtn;
    TextView requestConfirmTotalChargeMsg;
    String totalAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_confirm);

        Utills.StatusBarColour(ServiceConfirmActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarServiceConfirmed);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);

        toolbarTitle.setText("SERVICE CONFIRMED");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ServiceConfirmActivity.this,UserHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        requestConfirmTotalChargeMsg=findViewById(R.id.requestConfirmTotalChargeMsg);
        backToHomeBtn=findViewById(R.id.backToHomeBtn);
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null) {
            totalAmount=bundle.getString("totalAmount");
        }
        requestConfirmTotalChargeMsg.setText("Total Charge $"+totalAmount);
        backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ServiceConfirmActivity.this,UserHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


    }


}
