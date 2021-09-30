package com.evs.foodexp.commonPkg.common.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.DTO.LoginRequestModel;
import com.evs.foodexp.commonPkg.DTO.UserLoginDto;
import com.evs.foodexp.commonPkg.common.activity.ForgotPasswordActivity;
import com.evs.foodexp.commonPkg.common.activity.Getstart;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.driverPkg.activity.DriverCompletaddress;
import com.evs.foodexp.driverPkg.activity.DriverHomeActivity;
import com.evs.foodexp.restaurentPkg.activity.RestaurentHomeActivity;
import com.evs.foodexp.userPkg.activity.UserHomeActivity;
import com.evs.foodexp.models.User;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginFragment extends Fragment implements AuthListener<UserLoginDto> {

    private Button loginBtn;
    private TextView signUp, forgotPassword;
    SharedPreferences preferences;
    String latitude = "", longitude = "";
    private EditText userNameEdit, userPasswordEdit;
    String userNameStr, userPasswordStr;
    LoginRequestModel requestModel;
    APIViewModel apiViewModel;
    LoaderProgress progress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);
        userNameEdit = (EditText) view.findViewById(R.id.userNameLogin);
        userPasswordEdit = (EditText) view.findViewById(R.id.userPasswordLogin);
        requestModel = new LoginRequestModel();

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        apiViewModel = new ViewModelProvider(getActivity()).get(APIViewModel.class);
        progress = new LoaderProgress(getContext());

        loginBtn = (Button) view.findViewById(R.id.btn_login);
        signUp = (TextView) view.findViewById(R.id.signUpButton);
        forgotPassword = (TextView) view.findViewById(R.id.forgot_password);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                SessionManager.save_device_token(preferences, task.getResult());
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNameStr = userNameEdit.getText().toString().trim();
                userPasswordStr = userPasswordEdit.getText().toString().trim();
                if (!Global.validateEmail(userNameStr)) {
                    userNameEdit.setError("Enter Valid E-mail Address");
                } else if (!Global.password(userPasswordStr)) {
                    userPasswordEdit.setError("Enter Correct Password");
                } else if (Global.isOnline(getContext())) {
                    requestModel.setEmail(userNameStr);
                    requestModel.setPassword(userPasswordStr);
//                    requestModel.setLatitude(String.valueOf(gpsTracker.getLatitude()));
//                    requestModel.setLogitude(String.valueOf(gpsTracker.getLongitude()));
                    requestModel.setDevice("Android");
                    requestModel.setDeviceToken(SessionManager.get_device_token(preferences));
                    requestModel.setAction("login");

                    if (Global.isOnline(getContext())) {
                        apiViewModel.userLogin(requestModel, LoginFragment.this);
                    } else Global.showDialog(getActivity());
                } else Global.showDialog(getActivity());
//
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Getstart.class);
                intent.putExtra("pos", "1");
                startActivity(intent);
                getActivity().finish();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onSuccess(LiveData<UserLoginDto> data) {
        progress.dismiss();

        if (data.getValue().getStatus().equalsIgnoreCase("success")) {
            try {
                System.out.println("User Id === " + data.getValue().getData().getUserId().toString());
                System.out.println("User Name === " + data.getValue().getData().getFullName());
                SessionManager.save_userType(preferences, String.valueOf(data.getValue().getData().getUserId()));
                SessionManager.save_StripeStatus(preferences, String.valueOf(data.getValue().getData().getStripeStatus()));
                SessionManager.save_StripeAccountNo(preferences, String.valueOf(data.getValue().getData().getStripeAccountNo()));
                SessionManager.save_StripeAccountUrl(preferences, String.valueOf(data.getValue().getData().getStripeAccountUrl()));
                saveUserData(data.getValue().getData());
                if (SessionManager.get_userType(preferences).equals("Member")) {
                    Intent intent = new Intent(getContext(), UserHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getActivity().finish();
                } else if (SessionManager.get_userType(preferences).equals("Restaurant")) {
                    Intent intent = new Intent(getContext(), RestaurentHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getActivity().finish();
                } else {

                    if (Global.validatename(data.getValue().getData().getDate_5())) {
                        Intent intent = new Intent(getContext(), DriverHomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        SessionManager.save_check_login(preferences, false);
                        Intent intent = new Intent(getContext(), DriverCompletaddress.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error exception === " + e.getMessage());
                //   Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else Global.msgDialog(getActivity(), data.getValue().getMsg());


    }

    @Override
    public void onFailure(String message) {
        System.out.println("Error message === " + message);
        progress.dismiss();
        Global.msgDialog(getActivity(), message);
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
        SessionManager.save_wallet(preferences, user.getWallet());
        SessionManager.save_cardID(preferences, user.getCardholderId());

        SessionManager.save_country(preferences, user.getCountry());
        SessionManager.save_BankName(preferences, user.getBankName());
        SessionManager.save_AccountHolderName(preferences, user.getAccountHolderName());
        SessionManager.save_AccountNo(preferences, user.getAccountNo());
        SessionManager.save_RoutingNo(preferences, user.getRoutingNo());
        SessionManager.save_accountType(preferences, user.getAccountType());
        SessionManager.save_address(preferences, user.getAddress());
        SessionManager.save_image(preferences, user.getImage());
        SessionManager.save_BusinessType(preferences, user.getFoodTag());
        SessionManager.save_Lats(preferences, user.getLatitude());
        SessionManager.save_Longs(preferences, user.getLogitude());
        SessionManager.save_driverLicence(preferences, user.getCompanyBackground());
        SessionManager.save_Doc(preferences, user.getCompanyBackground());
    }
}
