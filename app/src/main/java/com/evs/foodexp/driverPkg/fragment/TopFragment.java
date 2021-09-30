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

public class TopFragment extends Fragment  implements OnMapReadyCallback, AuthListener<DataResponse<RequestModel>> {

    LoaderProgress progress;
    RestorentViewModel viewModel;

    TextView tv_title, tv_price, tv_address,
            tv_landMark,tv_specialNotess;
    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.top_layout,container,false);
        SupportMapFragment mapFragmentUser = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_jobDetails);
        mapFragmentUser.getMapAsync(this);

        progress = new LoaderProgress(getContext());
        viewModel = new ViewModelProvider(this).get(RestorentViewModel.class);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_price = (TextView) view.findViewById(R.id.tv_price);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        tv_specialNotess = (TextView) view.findViewById(R.id.tv_specialNotess);
        tv_landMark = (TextView) view.findViewById(R.id.tv_landMark);
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



            tv_title.setText(data.getValue().getData().getName());
            tv_price.setText("$ "+data.getValue().getData().getTotalAmount());
            tv_specialNotess.setText("Special Note : "+data.getValue().getData().getSpecialNote());
            tv_address.setText(data.getValue().getData().getAddress());
            tv_landMark.setText("Landmark "+data.getValue().getData().getLandmark()+"  "+data.getValue().getData().getWorkPlace());
            if (Global.validatename(data.getValue().getData().getDeliveryLat())) {
                moveMap(new LatLng(Double.parseDouble(data.getValue().getData().getDeliveryLat()),
                                Double.parseDouble(data.getValue().getData().getDeliveryLong())),
                        data.getValue().getData().getName());
            }
        }

    }


    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialogBack(getActivity(), message);
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



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
