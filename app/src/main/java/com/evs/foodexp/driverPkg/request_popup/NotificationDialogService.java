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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import  androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.commonPkg.viewModel.RestorentViewModel;
import com.evs.foodexp.driverPkg.fragment.OrderRequestService;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;

public class NotificationDialogService extends DialogFragment implements AuthMsgListener {


    String s_serviceName, s_calenderDate,bookingDate, s_location,bookingId;
    RestorentViewModel dataViewModel;
    SharedPreferences prefs;
    LoaderProgress progress;
    View view;
    TextView tv_serviceName, tv_calenderDate, tv_location,tv_date,tv_reject, tv_accept;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        progress = new LoaderProgress(getContext());
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        dataViewModel = new ViewModelProvider(getActivity()).get(RestorentViewModel.class);


        Bundle bundle = getArguments();
        s_serviceName = bundle.getString("serviceName", "0");
        bookingId = bundle.getString("bookingId", "0");
        s_calenderDate = bundle.getString("time", "");
        s_location = bundle.getString("address", "");
        bookingDate = bundle.getString("bookingDate", "");


        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
            view=null;
        }
        try {
            view = inflater.inflate(R.layout.service_request_alert, container, false);


        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }



        tv_calenderDate = view.findViewById(R.id.tv_calenderDate);
        tv_date = view.findViewById(R.id.tv_date);
        tv_calenderDate = view.findViewById(R.id.tv_calenderDate);
        tv_location = view.findViewById(R.id.tv_location);
        tv_serviceName = view.findViewById(R.id.tv_serviceName);
        tv_reject = view.findViewById(R.id.tv_reject);
        tv_accept = view.findViewById(R.id.tv_accept);

        tv_serviceName.setText(s_serviceName);
        tv_calenderDate.setText(s_calenderDate);
        tv_location.setText(s_location);
        tv_date.setText(bookingDate);

        tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataViewModel.executeServiceOrder(SessionManager.get_user_id(prefs),bookingId,
                        "1", NotificationDialogService.this);

            }
        });

        tv_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataViewModel.executeServiceOrder(SessionManager.get_user_id(prefs), bookingId,
                        "3", NotificationDialogService.this);
            }
        });




        return view;

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

    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progress.dismiss();
        Toast.makeText(getContext(),msgResponce.getMsg(),Toast.LENGTH_LONG).show();
        getDialog().dismiss();
    }
}
