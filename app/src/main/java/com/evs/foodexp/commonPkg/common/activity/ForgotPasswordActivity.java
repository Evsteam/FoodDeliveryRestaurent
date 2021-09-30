package com.evs.foodexp.commonPkg.common.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.DTO.ForgotPasswordRequest;
import com.evs.foodexp.commonPkg.DTO.TermsConditionDto;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.Utills;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener, AuthListener<TermsConditionDto> {

    private EditText emailEdit;
    private Button sendEmail;
     String emailStr;
    private ProgressDialog progressDialog;
    APIViewModel apiViewModel;
    ForgotPasswordRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Utills.StatusBarColour(ForgotPasswordActivity.this);
        apiViewModel =new ViewModelProvider(this).get(APIViewModel.class);
        request = new ForgotPasswordRequest();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarForgotPassword);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        emailEdit = (EditText)findViewById(R.id.emailForgotPassEdit);
        sendEmail = (Button)findViewById(R.id.btn_reset_password);
        toolbarTitle.setText("Forgot Password");
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

        sendEmail.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == sendEmail){
            emailStr = emailEdit.getText().toString().trim();
            progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            if(!Global.validateEmail(emailStr)){
                emailEdit.setError("E-mail Address Is Required!!");
            }else if(Global.isOnline(ForgotPasswordActivity.this)){
                request.setAction("forgotpassword");
                request.setEmail(emailStr);
                apiViewModel.forgotPassword(request,this);
            }else Global.showDialog(ForgotPasswordActivity.this);
        }
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onSuccess(LiveData<TermsConditionDto> data) {
        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(), "Email send to register mail ID", Toast.LENGTH_SHORT).show();
        emailEdit.setText("");
    }

    @Override
    public void onFailure(String message) {

    }
}
