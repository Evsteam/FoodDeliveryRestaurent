package com.evs.foodexp.driverPkg.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import  androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.commonPkg.viewModel.RestorentViewModel;
import com.evs.foodexp.models.RequestModel;
import com.evs.foodexp.repositry.DataResponse;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.Utills;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DriverJobDetailFoodOrder extends AppCompatActivity
        implements OnMapReadyCallback, AuthListener<DataResponse<RequestModel>> {

    private GoogleMap mMap,mMap1;
    Button btn_foodPickUp;
    boolean isGPSEnabled = false;
    public static double longitude = 0.0;
    public static double latitude = 0.0;
    String foodOrderId;
    TextView tv_title, tv_price, tv_address, tv_restaurentName, tv_restaurentPrice, tv_restaurentAdreess,
            tv_restaurentLandmark, tv_landMark;
    LoaderProgress progress;
    RestorentViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_food_order_job_detail);
        Utills.StatusBarColour(DriverJobDetailFoodOrder.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        progress = new LoaderProgress(DriverJobDetailFoodOrder.this);
        viewModel = new ViewModelProvider(this).get(RestorentViewModel.class);

        btn_foodPickUp = (Button) findViewById(R.id.btn_foodPickUp);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_restaurentName = (TextView) findViewById(R.id.tv_restaurentName);
        tv_restaurentPrice = (TextView) findViewById(R.id.tv_restaurentPrice);
        tv_restaurentAdreess = (TextView) findViewById(R.id.tv_restaurentAdreess);
        tv_restaurentLandmark = (TextView) findViewById(R.id.tv_restaurentLandmark);
        tv_landMark = (TextView) findViewById(R.id.tv_landMark);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            foodOrderId = bundle.getString("foodOrderId");
        }
        toolbarTitle.setText("ORDER # "+foodOrderId);
        SupportMapFragment mapFragmentUser = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_restaurent);
        mapFragmentUser.getMapAsync(this);

        SupportMapFragment mapFragmentDriver = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_jobDetails);
        mapFragmentDriver.getMapAsync(this);

        btn_foodPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverJobDetailFoodOrder.this, DriverFoodInProgressActivity.class);
                startActivity(intent);
            }
        });
        viewModel.excuteFoodOrderDetails(foodOrderId, this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap1 = googleMap;
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        mMap.getUiSettings().setZoomGesturesEnabled(false);
    }


    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onSuccess(LiveData<DataResponse<RequestModel>> data) {
        progress.dismiss();
        if (data != null) {
            tv_title.setText(data.getValue().getData().getName());
            tv_price.setText("$ "+data.getValue().getData().getTotalAmount());
            tv_address.setText(data.getValue().getData().getAddress());
            tv_restaurentName.setText(data.getValue().getData().getResturentName());
            tv_restaurentPrice.setText("$ "+data.getValue().getData().getTotalAmount());
            tv_restaurentAdreess.setText(data.getValue().getData().getResturentddress());
            tv_restaurentLandmark.setVisibility(View.GONE);
            tv_landMark.setText("Landmark "+data.getValue().getData().getLandmark()+"  "+data.getValue().getData().getWorkPlace());
            moveMap(new LatLng(Double.parseDouble(data.getValue().getData().getDeliveryLat()),
                    Double.parseDouble(data.getValue().getData().getDeliveryLong())),
                    data.getValue().getData().getName());

//            moveMap1(new LatLng(Double.parseDouble(data.getValue().getData().getResturentlatitude()),
//                            Double.parseDouble(data.getValue().getData().getDeliveryLong())),
//                    data.getValue().getData().getResturentName());

        }

    }


    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialogBack(DriverJobDetailFoodOrder.this, message);
    }

    private void moveMap(LatLng latlng1,String name) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(latlng1)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))
                .title(name));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng1).zoom(15.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng1));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    private void moveMap1(LatLng latlng1,String name) {
        mMap1.clear();
        mMap1.addMarker(new MarkerOptions()
                .position(latlng1)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))
                .title(name));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng1).zoom(15.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap1.moveCamera(cameraUpdate);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng1));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap1.getUiSettings().setZoomControlsEnabled(false);
        mMap1.getUiSettings().setMyLocationButtonEnabled(false);
    }
}
