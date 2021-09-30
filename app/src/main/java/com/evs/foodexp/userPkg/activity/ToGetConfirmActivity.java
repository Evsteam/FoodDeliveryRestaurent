package com.evs.foodexp.userPkg.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.common.sharedPreference.MySharedPreference;
import com.evs.foodexp.utils.Utills;

import java.util.Calendar;
import java.util.Date;

public class ToGetConfirmActivity extends AppCompatActivity implements View.OnClickListener{

    private Button moveToHomeBtn;
    TextView totalChargeTxt, dateTimeTxt;
    String totalChargeStr, dateStr, timeStr;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_get_confirm);
        Utills.StatusBarColour(ToGetConfirmActivity.this);
        preferences = PreferenceManager.getDefaultSharedPreferences(ToGetConfirmActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarOrderConfirmToGet);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("TO GET CONFIRMED");
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

        totalChargeTxt = (TextView)findViewById(R.id.requestConfirmTotalChargeMsgToGet);
        dateTimeTxt = (TextView)findViewById(R.id.bookingDateTimeConfirmedTxtToGet);
        totalChargeStr = MySharedPreference.getTotalAmountForService(preferences);
        dateStr = MySharedPreference.getServiceDate(preferences);
        timeStr = MySharedPreference.getServiceTime(preferences);
        totalChargeTxt.setText("Total Charge: $"+totalChargeStr);
        Date currentTime = Calendar.getInstance().getTime();

        dateTimeTxt.setText(String.valueOf(currentTime));
        moveToHomeBtn = (Button)findViewById(R.id.trackRequestBtnToGet);
        moveToHomeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == moveToHomeBtn){
            Toast.makeText(getApplicationContext(), "Thanks for your request.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),TrackOrderActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
