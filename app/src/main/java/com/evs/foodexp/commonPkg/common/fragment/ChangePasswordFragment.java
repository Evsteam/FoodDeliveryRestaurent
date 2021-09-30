package com.evs.foodexp.commonPkg.common.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import  androidx.lifecycle.ViewModelProvider;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.DTO.ChangePasswordRequest;
import com.evs.foodexp.commonPkg.DTO.UserLoginDto;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;

public class ChangePasswordFragment extends Fragment implements View.OnClickListener, AuthListener<UserLoginDto> {

    private EditText currentPasswordEdit, newPasswordEdit, confirmPasswordEdit;
    String currentPasswordStr, newPasswordStr, confirmPasswordStr,userIdStr;
    APIViewModel apiViewModel;
    ChangePasswordRequest changePasswordRequest;
    LoaderProgress progress;
    SharedPreferences preferences;
    Button changePasswordBtn;
    TextView tv_title;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_change_password,container,false);
        progress=new LoaderProgress(getContext());
        getActivity().setTitle("CHANGE PASSWORD");
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        changePasswordRequest = new ChangePasswordRequest();
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        currentPasswordEdit = (EditText)view.findViewById(R.id.edttxt_current_password);
        newPasswordEdit = (EditText)view.findViewById(R.id.edttxt_new_password);
        confirmPasswordEdit = (EditText)view.findViewById(R.id.edttxt_confirm_password);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        changePasswordBtn = (Button)view.findViewById(R.id.btn_change_password);
        userIdStr = SessionManager.get_user_id(preferences);
        tv_title.setText("Hello "+SessionManager.get_name(preferences)+" welcome!");
        System.out.println(userIdStr);
        changePasswordBtn.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {
        if(view == changePasswordBtn){
            currentPasswordStr = currentPasswordEdit.getText().toString().trim();
            newPasswordStr = newPasswordEdit.getText().toString().trim();
            confirmPasswordStr = confirmPasswordEdit.getText().toString().trim();
            changePasswordRequest.setAction("changePassword");
            changePasswordRequest.setUserId(userIdStr);
            changePasswordRequest.setNewPassword(newPasswordStr);
            changePasswordRequest.setOldPassword(currentPasswordStr);


            if(!Global.validatename(currentPasswordStr)){
                currentPasswordEdit.setError("Enter Current Password!");
            }else if(!Global.validateLength(newPasswordStr,6)){
                newPasswordEdit.setError("Enter Correct Password!");
            }else if(!newPasswordStr.equalsIgnoreCase(confirmPasswordStr)){
                confirmPasswordEdit.setError("Passwords Must Not Be Same As Current Password!");
            }else {
                    apiViewModel.changePassword(changePasswordRequest,this);
            }

        }
    }

    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onSuccess(LiveData<UserLoginDto> data) {
        progress.dismiss();
        currentPasswordEdit.setText("");
        newPasswordEdit.setText("");
        confirmPasswordEdit.setText("");
        if(data.getValue().getStatus().equalsIgnoreCase("success")) {
            Toast.makeText(getActivity(), "" + data.getValue().getMsg(), Toast.LENGTH_SHORT).show();
        }else Global.msgDialog(getActivity(),data.getValue().getMsg());
    }

    @Override
    public void onFailure(String message) {
        try {
            progress.dismiss();
            Global.msgDialog(getActivity(),message);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
