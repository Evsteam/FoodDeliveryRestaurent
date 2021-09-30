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
import android.widget.Toast;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.DTO.LoginRequestModel;
import com.evs.foodexp.commonPkg.DTO.UserLoginDto;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.driverPkg.activity.DriverHomeActivity;
import com.evs.foodexp.restaurentPkg.activity.RestaurentHomeActivity;
import com.evs.foodexp.userPkg.activity.UserHomeActivity;
import com.evs.foodexp.models.User;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AuthListener<UserLoginDto> {

    private Button loginBtn;
    private TextView signUp, forgotPassword;
    SharedPreferences preferences;
    private EditText userNameEdit, userPasswordEdit;
    String userNameStr, userPasswordStr;
    LoginRequestModel requestModel;
    APIViewModel apiViewModel;
    LoaderProgress progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        Utills.StatusBarColour(LoginActivity.this);
        userNameEdit = (EditText) findViewById(R.id.userNameLogin);
        userPasswordEdit = (EditText) findViewById(R.id.userPasswordLogin);
        requestModel = new LoginRequestModel();
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        progress = new LoaderProgress(LoginActivity.this);

        loginBtn = (Button) findViewById(R.id.btn_login);
        signUp = (TextView) findViewById(R.id.signUpButton);
        forgotPassword = (TextView) findViewById(R.id.forgot_password);

        loginBtn.setOnClickListener(this);
        signUp.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == loginBtn) {
            userNameStr = userNameEdit.getText().toString().trim();
            userPasswordStr = userPasswordEdit.getText().toString().trim();
            if (userNameStr.isEmpty()) {
                userNameEdit.setError("Enter Your Name");
            } else if (userPasswordStr.isEmpty()) {
                userPasswordEdit.setError("Enter Password");
            } else {
                if (!userNameStr.isEmpty() && !userPasswordStr.isEmpty()) {

                    requestModel.setEmail(userNameStr);
                    requestModel.setPassword(userPasswordStr);
//                    requestModel.setLatitude(latitude);
//                    requestModel.setLogitude(longitude);
                    requestModel.setAction("login");

                    if (Global.isOnline(LoginActivity.this)) {
                        apiViewModel.userLogin(requestModel, this);
                    }else Global.showDialog(LoginActivity.this);

                }
            }


        }
        if (v == signUp) {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        }
        if (v == forgotPassword) {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onSuccess(LiveData<UserLoginDto> data) {
        progress.dismiss();
        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
        try {
            System.out.println("User Id === " + data.getValue().getData().getUserId().toString());
            System.out.println("User Name === " + data.getValue().getData().getFullName());

            SessionManager.save_userType(preferences, String.valueOf(data.getValue().getData().getUserId()));
            saveUserData(data.getValue().getData());
            if (SessionManager.get_userType(preferences).equals("Member")) {
                Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (SessionManager.get_userType(preferences).equals("Restaurant")) {
                Intent intent = new Intent(LoginActivity.this, RestaurentHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(LoginActivity.this, DriverHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error exception === " + e.getMessage());
            //   Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onFailure(String message) {
        System.out.println("Error message === " + message);
        progress.dismiss();
        Global.msgDialog(LoginActivity.this,message);
    }

    public void saveUserData(User user) {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//{"userId":26,"fullName":"Raushan Suri","lastName":"","email":"xyz@gmail.com","role":"Member","address":"E-32, Sector-8, Noida",
// "zipCode":"","contactNumber":"52369852145","wallet":"","image":"","device":"","deviceToken":"","firebaseId":"","socialId":"",
// "socialType":""}
        SessionManager.save_check_login(preferences, true);
        SessionManager.save_user_id(preferences, user.getUserId());
        SessionManager.save_name(preferences, user.getFullName());
        SessionManager.save_emailid(preferences, user.getEmail());
        SessionManager.save_userType(preferences, user.getRole());
        SessionManager.save_address(preferences, user.getAddress());
        SessionManager.save_mobile(preferences, user.getContactNumber());
        SessionManager.save_image(preferences, user.getImage());
        SessionManager.save_device(preferences, user.getDevice());
        SessionManager.save_device_token(preferences, user.getDeviceToken());
        SessionManager.save_firebaseId(preferences, user.getFirebaseId());
        SessionManager.save_socialId(preferences, user.getSocialId());
        SessionManager.save_socialType(preferences, user.getSocialType());
    }
}
