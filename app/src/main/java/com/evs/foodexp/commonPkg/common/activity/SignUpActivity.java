package com.evs.foodexp.commonPkg.common.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import  androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.DTO.SignupRequestModel;
import com.evs.foodexp.commonPkg.DTO.UserLoginDto;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.userPkg.activity.UserHomeActivity;
import com.evs.foodexp.models.User;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;
import com.evs.foodexp.utils.LoaderProgress;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, AuthListener<UserLoginDto> {

    private Button signUpBtn;
    private EditText userNameEdit, userEmailEdit, userPasswordEdit, userPhoneNoEdit;
    private String userNameStr, userEmailStr, userPasswordStr, userPhoneNoStr;
    APIViewModel apiViewModel;
    SharedPreferences prefs;
    SignupRequestModel requestModel;
    LoaderProgress progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Utills.StatusBarColour(SignUpActivity.this);
        prefs = PreferenceManager.getDefaultSharedPreferences(SignUpActivity.this);
        userNameEdit = (EditText)findViewById(R.id.userNameSignUp);
        userEmailEdit = (EditText)findViewById(R.id.userEmailSignUp);
        userPasswordEdit = (EditText)findViewById(R.id.userPasswordSignUp);
        userPhoneNoEdit = (EditText)findViewById(R.id.userPhoneNoSignUp);
        requestModel = new SignupRequestModel();
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        progress = new LoaderProgress(SignUpActivity.this);

        signUpBtn = (Button)findViewById(R.id.btn_sign_up);
        signUpBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == signUpBtn){

            userNameStr = userNameEdit.getText().toString().trim();
            userEmailStr = userEmailEdit.getText().toString().trim();
            userPasswordStr = userPasswordEdit.getText().toString().trim();
            userPhoneNoStr = userPhoneNoEdit.getText().toString().trim();
            if(userNameStr.isEmpty()){
                userNameEdit.setError("Enter Your Name");
            }else if(!Global.validateEmail(userEmailStr)){
                userEmailEdit.setError("Enter valid Email Id");
            }else if(!Global.password(userPasswordStr)){
                userPasswordEdit.setError("Enter Password");
            }else if(!Global.validateLength(userPhoneNoStr,10)){
                userPhoneNoEdit.setError("Enter valid Phone Number");
            }else {
                if (Global.isOnline(SignUpActivity.this)) {
                    hitAPI();
                }else Global.showDialog(SignUpActivity.this);

            }

        }
    }

    private void hitAPI() {
        if(SessionManager.get_userType(prefs).equals("Member")){
            requestModel.setFullName(userNameStr);
            requestModel.setEmail(userEmailStr);
            requestModel.setPassword(userPasswordStr);
            requestModel.setContactNumber(userPhoneNoStr);
            requestModel.setAction("registration");
            requestModel.setRole("Member");
            apiViewModel.signUp(requestModel,this);
        }else if(SessionManager.get_userType(prefs).equals("Restaurant")){
            requestModel.setFullName(userNameStr);
            requestModel.setEmail(userEmailStr);
            requestModel.setPassword(userPasswordStr);
            requestModel.setContactNumber(userPhoneNoStr);
            requestModel.setAction("registration");
            requestModel.setRole("Restaurant");
            apiViewModel.signUp(requestModel,this);
        }else {
            requestModel.setFullName(userNameStr);
            requestModel.setEmail(userEmailStr);
            requestModel.setPassword(userPasswordStr);
            requestModel.setContactNumber(userPhoneNoStr);
            requestModel.setAction("registration");
            requestModel.setRole("Driver");
            apiViewModel.signUp(requestModel,this);
        }
    }


    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onSuccess(LiveData<UserLoginDto> data) {
        progress.dismiss();
        saveUserData(data.getValue().getData());
        Intent intent = new Intent(SignUpActivity.this, UserHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialog(SignUpActivity.this,message);

    }

    public void saveUserData(User user) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//{"userId":26,"fullName":"Raushan Suri","lastName":"","email":"xyz@gmail.com","role":"Member","address":"E-32, Sector-8, Noida",
// "zipCode":"","contactNumber":"52369852145","wallet":"","image":"","device":"","deviceToken":"","firebaseId":"","socialId":"",
// "socialType":""}
        SessionManager.save_check_login(prefs, true);
        SessionManager.save_user_id(prefs, user.getUserId());
        SessionManager.save_name(prefs, user.getFullName());
        SessionManager.save_emailid(prefs, user.getEmail());
        SessionManager.save_userType(prefs, user.getRole());
        SessionManager.save_address(prefs, user.getAddress());
        SessionManager.save_mobile(prefs, user.getContactNumber());
        SessionManager.save_image(prefs, user.getImage());
        SessionManager.save_device(prefs, user.getDevice());
        SessionManager.save_device_token(prefs, user.getDeviceToken());
        SessionManager.save_firebaseId(prefs, user.getFirebaseId());
        SessionManager.save_socialId(prefs, user.getSocialId());
        SessionManager.save_socialType(prefs, user.getSocialType());
    }
}
