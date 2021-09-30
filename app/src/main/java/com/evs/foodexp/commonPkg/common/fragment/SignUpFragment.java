package com.evs.foodexp.commonPkg.common.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.evs.foodexp.commonPkg.DTO.SignupRequestModel;
import com.evs.foodexp.commonPkg.DTO.UserLoginDto;
import com.evs.foodexp.commonPkg.common.activity.CompleteProfileActivity;
import com.evs.foodexp.commonPkg.common.activity.Getstart;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.driverPkg.activity.CompleteProfileDriverActivity;
import com.evs.foodexp.userPkg.activity.UserHomeActivity;
import com.evs.foodexp.models.User;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class SignUpFragment extends Fragment implements AuthListener<UserLoginDto> {

    private Button signUpBtn;
    private EditText userNameEdit, userEmailEdit, userPasswordEdit, userPhoneNoEdit;
    private String userNameStr, userEmailStr, userPasswordStr, userPhoneNoStr, latitude = "", longitude = "";
    APIViewModel apiViewModel;
    SharedPreferences prefs;
    SignupRequestModel requestModel;
    LoaderProgress progress;
    TextView btn_signin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sign_up, container, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        userNameEdit = (EditText) view.findViewById(R.id.userNameSignUp);
        userEmailEdit = (EditText) view.findViewById(R.id.userEmailSignUp);
        btn_signin = (TextView) view.findViewById(R.id.btn_signin);
        signUpBtn = (Button) view.findViewById(R.id.btn_sign_up);
        userPasswordEdit = (EditText) view.findViewById(R.id.userPasswordSignUp);
        userPhoneNoEdit = (EditText) view.findViewById(R.id.userPhoneNoSignUp);
        requestModel = new SignupRequestModel();
        apiViewModel = new ViewModelProvider(getActivity()).get(APIViewModel.class);
        progress = new LoaderProgress(getContext());


        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                SessionManager.save_device_token(prefs, task.getResult());
            }
        });


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Getstart.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }


    public void validation() {

        userNameStr = userNameEdit.getText().toString().trim();
        userEmailStr = userEmailEdit.getText().toString().trim();
        userPasswordStr = userPasswordEdit.getText().toString().trim();
        userPhoneNoStr = userPhoneNoEdit.getText().toString().trim();
        if (!Global.validatename(userNameStr)) {
            userNameEdit.setError("Enter Your Name");
        } else if (!Global.validateEmail(userEmailStr)) {
            userEmailEdit.setError("Enter valid E-mail Address");
        } else if (!Global.password(userPasswordStr)) {
            userPasswordEdit.setError("Enter Password");
        } else if (!Global.validateLength(userPhoneNoStr, 10)) {
            userPhoneNoEdit.setError("Enter valid Phone Number");
        } else {
            if (Global.isOnline(getContext())) {
                requestModel.setFullName(userNameStr);
                requestModel.setEmail(userEmailStr);
                requestModel.setPassword(userPasswordStr);
                requestModel.setContactNumber(userPhoneNoStr);
                requestModel.setDevice("Android");
                requestModel.setDeviceToken(SessionManager.get_device_token(prefs));
                requestModel.setAction("registration");
                requestModel.setRole(SessionManager.get_userType(prefs));
                apiViewModel.signUp(requestModel, this);
            } else Global.showDialog(getActivity());

        }

    }


    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onSuccess(LiveData<UserLoginDto> data) {
        progress.dismiss();
        if (data.getValue().getStatus().equalsIgnoreCase("success")) {
            saveUserData(data.getValue().getData());
            Intent intent;
            if (data.getValue().getData().getRole().equalsIgnoreCase("Restaurant")) {
                intent = new Intent(getContext(), CompleteProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            } else if (data.getValue().getData().getRole().equalsIgnoreCase("Driver")) {
                SessionManager.save_check_login(prefs, false);
                intent = new Intent(getContext(), CompleteProfileDriverActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            } else {
                intent = new Intent(getContext(), UserHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }

        } else Global.msgDialog(getActivity(), data.getValue().getMsg());

    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialog(getActivity(), message);

    }

    public void saveUserData(User user) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//{"userId":26,"fullName":"Raushan Suri","lastName":"","email":"xyz@gmail.com","role":"Member","address":"E-32, Sector-8, Noida",
// "zipCode":"","contactNumber":"52369852145","wallet":"","image":"","device":"","deviceToken":"","firebaseId":"","socialId":"",
// "socialType":""}

        SessionManager.save_StripeStatus(prefs, String.valueOf(user.getStripeStatus()));
        SessionManager.save_StripeAccountNo(prefs, String.valueOf(user.getStripeAccountNo()));
        SessionManager.save_StripeAccountUrl(prefs, String.valueOf(user.getStripeAccountUrl()));
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
        SessionManager.save_wallet(prefs, user.getWallet());

        SessionManager.save_country(prefs, user.getCountry());
        SessionManager.save_BankName(prefs, user.getBankName());
        SessionManager.save_AccountHolderName(prefs, user.getAccountHolderName());
        SessionManager.save_AccountNo(prefs, user.getAccountNo());
        SessionManager.save_RoutingNo(prefs, user.getRoutingNo());
        SessionManager.save_accountType(prefs, user.getAccountType());
        SessionManager.save_address(prefs, user.getAddress());
        SessionManager.save_image(prefs, user.getImage());
        SessionManager.save_BusinessType(prefs, user.getFoodTag());
        SessionManager.save_Lats(prefs, user.getLatitude());
        SessionManager.save_Longs(prefs, user.getLogitude());
        SessionManager.save_driverLicence(prefs, user.getCompanyBackground());
        SessionManager.save_Doc(prefs, user.getCompanyBackground());
    }
}
