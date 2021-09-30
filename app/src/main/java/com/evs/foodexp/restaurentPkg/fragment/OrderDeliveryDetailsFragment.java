package com.evs.foodexp.restaurentPkg.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import  androidx.lifecycle.ViewModelProvider;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.commonPkg.viewModel.RestorentViewModel;
import com.evs.foodexp.models.OrderModel;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.userPkg.activity.TrackOrderActivity;
import com.evs.foodexp.utils.Global;
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

public class OrderDeliveryDetailsFragment extends Fragment implements OnMapReadyCallback, AuthMsgListener {
    TextView tv_title, tv_price, tv_address, tv_landMark,tv_specilaNotes;
    Button btn_requestPlusDriver;
    GoogleMap map;
    RestorentViewModel viewModel;
    SharedPreferences prefs;
    LoaderProgress progress;
    String driverId="", bookingId, items, driverName
            ,delLat, delLongs, dropAddress, amount,pickUplat,pickLong,restName,status="";
    RelativeLayout layout_rating;
    RatingBar ratingBar;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_delivery_detail_fragment_layout, container, false);
        tv_title = view.findViewById(R.id.tv_title);
        tv_price = view.findViewById(R.id.tv_price);
        tv_address = view.findViewById(R.id.tv_address);
        tv_landMark = view.findViewById(R.id.tv_landMark);
        tv_specilaNotes = view.findViewById(R.id.tv_specilaNotes);
        layout_rating = view.findViewById(R.id.layout_rating);
        ratingBar = view.findViewById(R.id.ratingBar);
        btn_requestPlusDriver = view.findViewById(R.id.btn_requestPlusDriver);
        viewModel = new ViewModelProvider(getActivity()).get(RestorentViewModel.class);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapLocation);
        mapFragment.getMapAsync(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        progress = new LoaderProgress(getContext());


        if (OrderDetail.status.equalsIgnoreCase("4")) {
            btn_requestPlusDriver.setText("Deliverd");
            btn_requestPlusDriver.setBackgroundColor(getResources().getColor(R.color.green));
        } else if (OrderDetail.status.equalsIgnoreCase("10")) {
            btn_requestPlusDriver.setText("Driver is not Accepted");
        } else if (OrderDetail.status.equalsIgnoreCase("1")) {
            btn_requestPlusDriver.setText("Track Your Order");
        } else {
            btn_requestPlusDriver.setText("On the Delivery");
            btn_requestPlusDriver.setBackgroundColor(getResources().getColor(R.color.green));
        }

        btn_requestPlusDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if( !OrderDetail.status.equalsIgnoreCase("delivered")){
//                    viewModel.excuteRequestForDriver(OrderDetail.orderId,
//                            SessionManager.get_user_id(prefs),OrderDeliveryDetailsFragment.this);
//                }
                if (btn_requestPlusDriver.getText().toString().equalsIgnoreCase("Track Your Order")) {
                    Intent intent = new Intent(getActivity(), TrackOrderActivity.class);
                    intent.putExtra("bookingId", bookingId);
                    intent.putExtra("driverId", driverId);
                    intent.putExtra("dropLat", delLat);
                    intent.putExtra("dropLongs", delLongs);
                    intent.putExtra("dropAddress", dropAddress);
                    intent.putExtra("amount", amount);
                    intent.putExtra("items", items);
                    intent.putExtra("pickLong", pickLong);
                    intent.putExtra("pickUplat", pickUplat);
                    intent.putExtra("restName", restName);
                    intent.putExtra("driverName", driverName);
                    startActivity(intent);
                }else if (btn_requestPlusDriver.getText().toString().equalsIgnoreCase("On the Delivery")) {
                    Intent intent = new Intent(getActivity(), TrackOrderActivity.class);
                    intent.putExtra("bookingId", bookingId);
                    intent.putExtra("driverId", driverId);
                    intent.putExtra("dropLat", delLat);
                    intent.putExtra("dropLongs", delLongs);
                    intent.putExtra("dropAddress", dropAddress);
                    intent.putExtra("amount", amount);
                    intent.putExtra("items", items);
                    intent.putExtra("pickLong", pickLong);
                    intent.putExtra("pickUplat", pickUplat);
                    intent.putExtra("restName", restName);
                    intent.putExtra("driverName", driverName);
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), btn_requestPlusDriver.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });

        layout_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Global.validatename(driverId)) {
                   Submitreview cdd = new Submitreview(getActivity(), driverId);
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cdd.show();
                }
            }
        });


        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        viewModel.getOrderDetails().observe(this, new Observer<OrderModel>() {
            @Override
            public void onChanged(OrderModel orderModel) {

                tv_title.setText(orderModel.getUserName());
                tv_price.setText("$ " + orderModel.getTotalAmount());
                tv_address.setText(orderModel.getUserAddress());
                tv_specilaNotes.setText(orderModel.getSpecialNote());
                tv_landMark.setText(orderModel.getWorkPlace() + " " + orderModel.getLandmark());
                if (Global.validatename(orderModel.getWorkPlace())) {
                    if (orderModel.getDeliveryLat().length() > 4) {
                        myLocation(new LatLng(Double.parseDouble(orderModel.getDeliveryLat()), Double.parseDouble(orderModel.getDeliveryLong())));
                    }

                }
                if (orderModel.getDriverId().equalsIgnoreCase("1")) {
                    if (Global.validatename(orderModel.getDriverId())) {
                        btn_requestPlusDriver.setText("Track your Order");
                    } else btn_requestPlusDriver.setText("Pending");
                }
                driverId = orderModel.getDriverId();
                bookingId = orderModel.getFoodorderId();
                delLat = orderModel.getDeliveryLat();
                delLongs = orderModel.getDeliveryLong();
                dropAddress = orderModel.getUserAddress();
                dropAddress = orderModel.getUserAddress();
                restName = orderModel.getResturentName();
                pickLong = orderModel.getResturentLongitude();
                pickUplat = orderModel.getResturentLatitude();
                driverName = orderModel.getDriverName();
                amount = orderModel.getTotalAmount();
                status = orderModel.getStatus();
                if (orderModel.getFoodDetails().size() > 0) {
                    if (orderModel.getFoodDetails().size() == 1) {
                        items = orderModel.getFoodDetails().get(0).getQuintity() + " " + orderModel.getFoodDetails().get(0).getName();
                    } else {
                        String itemsde = "";
                        for (int i = 0; i < orderModel.getFoodDetails().size(); i++) {
                            if (i == 0) {
                                items = orderModel.getFoodDetails().get(i).getQuintity() + " " + orderModel.getFoodDetails().get(0).getName() + " ";
                            } else
                                items = items + orderModel.getFoodDetails().get(i).getQuintity() + " " + orderModel.getFoodDetails().get(0).getName();
                        }
                        items = itemsde;
                    }

                } else items = orderModel.getWhatYouWant();

                if(Global.validatename(orderModel.getDriverAVG())){
                    ratingBar.setRating(Float.parseFloat(orderModel.getDriverAVG()));
                }else  ratingBar.setRating(0);

                if(SessionManager.get_userType(prefs).equalsIgnoreCase(Global.type_person)){
                    if (orderModel.getStatus().equalsIgnoreCase("4")) {
                        layout_rating.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
    }

    private void myLocation(LatLng latlng1) {
        map.clear();
        map.addMarker(new MarkerOptions()
                .position(latlng1)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin)));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng1).zoom(12.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.moveCamera(cameraUpdate);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(true);

    }

    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialog(getActivity(), message);
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progress.dismiss();
        if (msgResponce.getStatus().equalsIgnoreCase("success")) {
            Global.msgDialogBack(getActivity(), msgResponce.getMsg());
        } else Global.msgDialog(getActivity(), msgResponce.getMsg());
    }

    public class Submitreview extends Dialog implements
            android.view.View.OnClickListener, AuthListener<MsgResponse> {

        public Activity c;
        public Dialog d;
        public TextView no; //yes
        //        Spinner cat_type;
        EditText et_message;
        String id;
        RatingBar ratingBar;
        Button btn_submit;


        public Submitreview(Activity a, final String id) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
            this.id = id;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.submit_review);
            btn_submit = (Button) findViewById(R.id.btn_submit);
//            yes = (TextView) findViewById(R.id.done);
            no = (TextView) findViewById(R.id.cancle);
            ratingBar = (RatingBar) findViewById(R.id.ratingBar);
            et_message = (EditText) findViewById(R.id.et_message);


            btn_submit.setOnClickListener(this);
            no.setOnClickListener(this);
//            get_subcate(parentId);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_submit:
                    viewModel.reviewRating(SessionManager.get_user_id(prefs), id, String.valueOf(ratingBar.getRating()), et_message.getText().toString(), this);
                    dismiss();
                    break;
                case R.id.cancle:
                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }


        @Override
        public void onStarted() {
            progress.show();
        }

        @Override
        public void onSuccess(LiveData<MsgResponse> data) {
            progress.dismiss();
            if (data != null) {
                Global.msgDialog( getActivity(), data.getValue().getMsg());
            }

        }

        @Override
        public void onFailure(String message) {
            progress.dismiss();
            Global.msgDialog(getActivity(), message);
        }
    }
}
