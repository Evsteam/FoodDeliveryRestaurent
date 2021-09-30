package com.evs.foodexp.driverPkg.request_popup;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import  androidx.lifecycle.ViewModelProvider;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.commonPkg.viewModel.RestorentViewModel;
import com.evs.foodexp.driverPkg.activity.SpecialDetailsScreen;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;

public class NotificationDialogSpecialRequest extends DialogFragment implements AuthMsgListener {


    String specialrequestId, whatYouWant, address,transactionFee,
            userId,userPhone,userImage,driverId,driverName,driverPhone,driverImage,price,deliveryFee,TIP,salesTax,
            latitude,status,totalAmount,userName;
    RestorentViewModel dataViewModel;
    SharedPreferences prefs;
    LoaderProgress progress;
    LinearLayout layour_delavery,action_layout;
    View view;
    TextView tv_title, tv_adress,tv_reject,tv_accept,toGetRequestHeading;
    Button start_job;
    Double totalPrice=0.0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        progress = new LoaderProgress(getContext());
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        dataViewModel = new ViewModelProvider(getActivity()).get(RestorentViewModel.class);


        Bundle bundle = getArguments();
        specialrequestId = bundle.getString("specialrequestId", "0");
        whatYouWant = bundle.getString("whatYouWant", "0");
        address = bundle.getString("address", "");
        userId = bundle.getString("userId", "");
        userId = bundle.getString("userId", "");
        userPhone = bundle.getString("userPhone", "");
        userImage = bundle.getString("userImage", "");
        driverId = bundle.getString("driverId", "");
        driverName = bundle.getString("driverName", "");
        driverPhone = bundle.getString("driverPhone", "");
        driverImage = bundle.getString("driverImage", "");
        price = bundle.getString("price", "");
        deliveryFee = bundle.getString("deliveryFee", "");
        TIP = bundle.getString("TIP", "");
        salesTax = bundle.getString("salesTax", "");
        latitude = bundle.getString("latitude", "");
        status = bundle.getString("status", "");
        totalAmount = bundle.getString("totalAmount", "");
        userName = bundle.getString("userName", "");
        transactionFee = bundle.getString("transactionFee", "");



        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
            view=null;
        }
        try {
            view = inflater.inflate(R.layout.new_job_request_to_get_driver_alert, container, false);


        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

         totalPrice=Double.parseDouble(price)+
                Double.parseDouble(TIP)+Double.parseDouble(salesTax)+Double.parseDouble(deliveryFee);

        start_job = view.findViewById(R.id.start_job);
        toGetRequestHeading = view.findViewById(R.id.toGetRequestHeading);
        layour_delavery = view.findViewById(R.id.layour_delavery);
        action_layout = view.findViewById(R.id.action_layout);
        layour_delavery.setVisibility(View.GONE);
        action_layout.setVisibility(View.GONE);
        toGetRequestHeading.setText("Special Order");
        start_job.setVisibility(View.VISIBLE);
        tv_title = view.findViewById(R.id.tv_title);
        tv_adress = view.findViewById(R.id.tv_adress);
        tv_reject = view.findViewById(R.id.tv_reject);
        tv_accept = view.findViewById(R.id.tv_accept);
        tv_title.setText(whatYouWant);
        tv_adress.setText(address);


        start_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getActivity(), SpecialDetailsScreen.class);
                intent.putExtra("specialrequestId", specialrequestId);
                intent.putExtra("userId", userId);
                intent.putExtra("userName", userName);
                intent.putExtra("userPhone", userPhone);
                intent.putExtra("userImage", userImage);
                intent.putExtra("driverId", driverId);
                intent.putExtra("driverName", driverName);
                intent.putExtra("driverPhone", driverPhone);
                intent.putExtra("driverImage", driverImage);
                intent.putExtra("whatYouWant", whatYouWant);
                intent.putExtra("price", price);
                intent.putExtra("deliveryFee", deliveryFee);
                intent.putExtra("TIP", TIP);
                intent.putExtra("salesTax", salesTax);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude","");
                intent.putExtra("address", address);
                intent.putExtra("totalAmount",totalPrice.toString());
                intent.putExtra("status", status);
                intent.putExtra("created", "");
                intent.putExtra("transactionFee", transactionFee);
                intent.putExtra("paymentStatus","0");
                startActivity(intent);
                getDialog().dismiss();

            }
        });



        return view;

    }




    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStarted() {
        progress.show();
    }


    @Override
    public void onFailure(String message) {
        progress.dismiss();
        getDialog().dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progress.dismiss();
        getDialog().dismiss();
        Toast.makeText(getActivity(), msgResponce.getMsg(), Toast.LENGTH_LONG).show();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(400, 400);
        return dialog;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        onDestroyView();
    }
}
