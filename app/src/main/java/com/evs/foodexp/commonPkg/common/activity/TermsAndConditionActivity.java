package com.evs.foodexp.commonPkg.common.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import  androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.DTO.TermsConditionDto;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.utils.Utills;

public class TermsAndConditionActivity extends AppCompatActivity implements AuthListener<TermsConditionDto> {

    private TextView termsTxt;
    private String termsStr;
    APIViewModel apiViewModel;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        Utills.StatusBarColour(TermsAndConditionActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTermsAndCondition);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        termsTxt = (TextView)findViewById(R.id.termsAndConditionTxt);
        toolbarTitle.setText("TERMS AND CONDITION");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressDialog = new ProgressDialog(TermsAndConditionActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        apiViewModel.termsAndCondition(this);

    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onSuccess(LiveData<TermsConditionDto> data) {
            //termsStr = String.valueOf(data.getValue().getMsg());
        progressDialog.dismiss();
        termsTxt.setText(Html.fromHtml(String.valueOf(data.getValue().getMsg())).toString());

        /*Document document = Jsoup.parse(String.valueOf(data.getValue().getMsg()));
            termsTxt.setText(document.text());*/
    }

    @Override
    public void onFailure(String message) {

    }
}
