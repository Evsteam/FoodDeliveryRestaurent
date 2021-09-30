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
import com.evs.foodexp.driverPkg.fragment.OrderRequestToget;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;

public class NotificationDialogToget extends DialogFragment implements AuthMsgListener {


    String togetRequestId, togetDriverRequestId, title, address,storeCity;
    RestorentViewModel dataViewModel;
    SharedPreferences prefs;
    LoaderProgress progress;
    View view;
    TextView tv_title, tv_adress,tv_reject,tv_accept,tv_stoes,tv_delilry;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        progress = new LoaderProgress(getContext());
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        dataViewModel = new ViewModelProvider(getActivity()).get(RestorentViewModel.class);


        Bundle bundle = getArguments();
        togetRequestId = bundle.getString("togetRequestId", "0");
        togetDriverRequestId = bundle.getString("togetDriverRequestId", "0");
        address = bundle.getString("address", "");
        title = bundle.getString("title", "");
        storeCity = bundle.getString("storeCity", "");



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



        tv_title = view.findViewById(R.id.tv_title);
        tv_delilry = view.findViewById(R.id.tv_delilry);
        tv_stoes = view.findViewById(R.id.tv_stoes);
        tv_adress = view.findViewById(R.id.tv_adress);
        tv_reject = view.findViewById(R.id.tv_reject);
        tv_accept = view.findViewById(R.id.tv_accept);
        tv_title.setText(title);
        tv_adress.setText(address);
        tv_stoes.setText("Store name ");
        tv_delilry.setText(storeCity);

        tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataViewModel.executeTogetOrder(SessionManager.get_user_id(prefs),togetDriverRequestId,
                        togetRequestId,"2", NotificationDialogToget.this);

            }
        });

        tv_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataViewModel.executeUpdateOrder(SessionManager.get_user_id(prefs),togetDriverRequestId,
                        togetRequestId,"3", NotificationDialogToget.this);
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
