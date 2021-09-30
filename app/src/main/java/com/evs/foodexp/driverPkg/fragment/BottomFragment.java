package com.evs.foodexp.driverPkg.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import  androidx.lifecycle.ViewModelProvider;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.commonPkg.viewModel.RestorentViewModel;
import com.evs.foodexp.models.RequestModel;
import com.evs.foodexp.repositry.DataResponse;
import com.evs.foodexp.restaurentPkg.fragment.OrderDetail;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class BottomFragment extends Fragment implements OnMapReadyCallback, AuthListener<DataResponse<RequestModel>> {
    LoaderProgress progress;
    RestorentViewModel viewModel;
    TextView tv_restaurentName, tv_restaurentPrice, tv_restaurentAdreess,
            tv_restaurentLandmark;
    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_layout, container, false);
//        tv_restaurentName=view.findViewById(R.id.tv_restaurentName);
//        tv_restaurentName.setText("Second");

        SupportMapFragment mapFragmentUser = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_restaurent);
        mapFragmentUser.getMapAsync(this);

        progress = new LoaderProgress(getContext());
        viewModel = new ViewModelProvider(this).get(RestorentViewModel.class);

        tv_restaurentName = (TextView) view.findViewById(R.id.tv_restaurentName);
        tv_restaurentPrice = (TextView) view.findViewById(R.id.tv_restaurentPrice);
        tv_restaurentAdreess = (TextView) view.findViewById(R.id.tv_restaurentAdreess);
        tv_restaurentLandmark = (TextView) view.findViewById(R.id.tv_restaurentLandmark);
        viewModel.excuteFoodOrderDetails(OrderDetail.orderId, this);
        return view;
    }

    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onSuccess(LiveData<DataResponse<RequestModel>> data) {
        progress.dismiss();
        if (data != null) {
            tv_restaurentName.setText(data.getValue().getData().getResturentName());
            tv_restaurentPrice.setText("$ " + data.getValue().getData().getTotalAmount());
            tv_restaurentAdreess.setText(data.getValue().getData().getResturentddress());
            tv_restaurentLandmark.setVisibility(View.GONE);
            if (Global.validatename(data.getValue().getData().getResturentlatitude())) {
                if (Global.validatename(data.getValue().getData().getResturentlongitude())) {
                    moveMap(new LatLng(Double.parseDouble(data.getValue().getData().getResturentlatitude()),
                                    Double.parseDouble(data.getValue().getData().getResturentlongitude())),
                            data.getValue().getData().getResturentName());
                }
            }


        }

    }


    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialogBack(getActivity(), message);
    }

    private void moveMap(LatLng latlng1, String name) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(latlng1)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))
                .title(name));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng1).zoom(15.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
