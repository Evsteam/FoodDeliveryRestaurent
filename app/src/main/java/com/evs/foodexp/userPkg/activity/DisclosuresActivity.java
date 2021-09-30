package com.evs.foodexp.userPkg.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import  androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.DTO.TermsConditionDto;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.Utills;

public class DisclosuresActivity extends AppCompatActivity implements AuthListener<TermsConditionDto> {

    private CheckBox  cb_check;
    String desc,services,serviceAmount;
    LoaderProgress progress;
    APIViewModel apiViewModel;
    TextView tv_discoul;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclosures);
        Utills.StatusBarColour(DisclosuresActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDisclosure);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        cb_check = (CheckBox) findViewById(R.id.cb_check);
        toolbarTitle.setText("Disclaimer");
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
        tv_discoul=findViewById(R.id.tv_discoul);
        progress= new LoaderProgress(DisclosuresActivity.this);
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null) {
            services=bundle.getString("service");
            desc=bundle.getString("desc");
            serviceAmount=bundle.getString("serviceAmount");
        }
        if(!Global.GpsEnable(DisclosuresActivity.this)){
            Global.showGPSDisabledAlertToUser(DisclosuresActivity.this);
        }


        apiViewModel.discolres("disclosures",this);

        cb_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Intent intent = new Intent(DisclosuresActivity.this,TimeAndDateScheduleActivity.class);
                    intent.putExtra("service", services);
                    intent.putExtra("desc", desc);
                    intent.putExtra("serviceAmount", serviceAmount);
                    startActivity(intent);
                }else Toast.makeText(DisclosuresActivity.this,"Please Select Terms Condition",Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onSuccess(LiveData<TermsConditionDto> data) {
        progress.dismiss();
        if(data.getValue().getStatus().equalsIgnoreCase("success")){
            tv_discoul.setText(Html.fromHtml(data.getValue().getMsg()));
        }else Global.msgDialog(DisclosuresActivity.this,data.getValue().getMsg());
    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();
    }
}
