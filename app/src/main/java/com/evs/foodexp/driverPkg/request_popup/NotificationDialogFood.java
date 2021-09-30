package com.evs.foodexp.driverPkg.request_popup;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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
import com.evs.foodexp.driverPkg.fragment.OrderRequestFood;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class NotificationDialogFood extends DialogFragment implements AuthMsgListener {


    String foodorderId, foodrequestId, estimateDistace, estimateTime,
            dropLocation, pickupLoaction,resturentName;
    RestorentViewModel dataViewModel;
    SharedPreferences prefs;
    LoaderProgress progress;
    View view;
    ImageView img_mapSearch;
    TextView tv_miles, tv_time, tv_picupLocation,tv_dropLocation,tv_reject,tv_accept;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        progress = new LoaderProgress(getContext());
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        dataViewModel = new ViewModelProvider(getActivity()).get(RestorentViewModel.class);


        Bundle bundle = getArguments();
        foodorderId = bundle.getString("foodorderId", "0");
        foodrequestId = bundle.getString("foodrequestId", "0");
        estimateDistace = bundle.getString("estimateDistace", "");
        estimateTime = bundle.getString("estimateTime", "");
        dropLocation = bundle.getString("dropLocation", "");
        pickupLoaction = bundle.getString("pickupLoaction", "");
        resturentName = bundle.getString("resturentName", "");



        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
            view=null;
        }
        try {
            view = inflater.inflate(R.layout.food_delivery_request_alert, container, false);


        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }


        tv_miles = view.findViewById(R.id.tv_miles);
        img_mapSearch = view.findViewById(R.id.img_mapSearch);
        tv_time = view.findViewById(R.id.tv_time);
        tv_picupLocation = view.findViewById(R.id.tv_picupLocation);
        tv_dropLocation = view.findViewById(R.id.tv_dropLocation);
        tv_reject = view.findViewById(R.id.tv_reject);
        tv_accept = view.findViewById(R.id.tv_accept);
        tv_miles.setText(estimateDistace+" miles" );
        tv_time.setText(estimateTime);
        tv_picupLocation.setText(resturentName);
        tv_dropLocation.setText(dropLocation);

        tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataViewModel.executeUpdateOrder(SessionManager.get_user_id(prefs), foodrequestId,
                        foodorderId, "2", NotificationDialogFood.this);

            }
        });

        tv_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataViewModel.executeUpdateOrder(SessionManager.get_user_id(prefs), foodrequestId,
                        foodorderId, "3", NotificationDialogFood.this);
            }
        });


        img_mapSearch.setVisibility(View.VISIBLE);
        img_mapSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertbox = new AlertDialog.Builder(getActivity());
                alertbox.setTitle(getActivity().getResources().getString(R.string.app_name));
                alertbox.setMessage("Please Google to the closest restaurant to you based on this order");
                alertbox.setPositiveButton(getActivity().getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + resturentName);
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                            }
                        });
                alertbox.show();
            }
        });



        return view;

    }





    @Override
    public void onResume() {
        super.onResume();

//        getDialog().getWindow().setLayout(400, 400);
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
