package com.evs.foodexp.commonPkg.common.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import  androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.DTO.HelpDto;
import com.evs.foodexp.commonPkg.common.activity.TermsAndConditionActivity;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;

public class HelpFragment extends Fragment implements View.OnClickListener, AuthListener<HelpDto> {

    TextView termsAndCondition, helpEmail, helpPhone,tv_privacyPolicy;
    APIViewModel apiViewModel;
    private ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_help,container,false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("Help");
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        termsAndCondition = (TextView)view.findViewById(R.id.termsAndCondition);
        tv_privacyPolicy = (TextView)view.findViewById(R.id.tv_privacyPolicy);
        helpEmail = (TextView)view.findViewById(R.id.help_email);
        helpPhone = (TextView)view.findViewById(R.id.help_phone);
        apiViewModel.help(this);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        termsAndCondition.setOnClickListener(this);
        helpEmail.setOnClickListener(this);
        helpPhone.setOnClickListener(this);
        tv_privacyPolicy.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        getActivity().setTitle("HELP");
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if(v == termsAndCondition){
//            Intent intent = new Intent(getActivity(), TermsAndConditionActivity.class);
//            startActivity(intent);
            String url = "https://www.expressplusnow.com/terms-and-conditions/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }else if(v == tv_privacyPolicy){
//            Intent intent = new Intent(getActivity(), TermsAndConditionActivity.class);
//            startActivity(intent);
            String url = "https://www.expressplusnow.com/privacy-policy/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }else if(v == helpEmail){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto",helpEmail.getText().toString().trim(), null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        }else if(v == helpPhone){
            String phone = helpPhone.getText().toString().trim();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);
        }
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onSuccess(LiveData<HelpDto> data) {
        progressDialog.dismiss();
        helpEmail.setText(String.valueOf(data.getValue().getData().getEamil()));
        helpPhone.setText(String.valueOf(data.getValue().getData().getPhone()));

    }

    @Override
    public void onFailure(String message) {

    }


}
