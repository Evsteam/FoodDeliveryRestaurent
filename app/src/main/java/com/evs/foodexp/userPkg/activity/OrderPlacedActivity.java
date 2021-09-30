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

public class OrderPlacedActivity extends AppCompatActivity implements View.OnClickListener{

    private Button trackOrderBtn;
    String foodOrderGroupId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);

        Utills.StatusBarColour(OrderPlacedActivity.this);
        trackOrderBtn = (Button)findViewById(R.id.trackOrderBtn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPlaceOrder);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("SUCCESS");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(OrderPlacedActivity.this,UserHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        trackOrderBtn.setOnClickListener(this);

        Bundle bundle= getIntent().getExtras();
        if (bundle!=null) {
            foodOrderGroupId=bundle.getString("foodOrderGroupId");
           if(foodOrderGroupId!=null){
               trackOrderBtn.setVisibility(View.GONE);
           }
        }

    }
    @Override
    public void onClick(View v) {
        if(v == trackOrderBtn){
//            Intent intent = new Intent(OrderPlacedActivity.this,TrackOrderActivity.class);
//            startActivity(intent);
            Intent intent=new Intent(OrderPlacedActivity.this,UserHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }



}
