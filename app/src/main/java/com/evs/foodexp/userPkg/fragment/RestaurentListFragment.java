package com.evs.foodexp.userPkg.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.DTO.RestaurantListDto;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.userPkg.activity.UserAddressActivity;
import com.evs.foodexp.userPkg.adapter.RestaurantListAdapter;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class RestaurentListFragment extends Fragment implements View.OnClickListener, AuthListener<RestaurantListDto> {

    // View restaurantFood;
    private LoaderProgress progressDialog;
    private RecyclerView foodListRecycle;
    private APIViewModel apiViewModel;
    ImageView img_serach;
    EditText et_search;
    TextView tv_noFound;
    String latitude = "", longitude = "";
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;
    FusedLocationProviderClient mFusedLocationProviderClient;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    final static int PERMISSION_ID = 12;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.restaurent_list_layout, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("Restaurants");

        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        // restaurantFood = view.findViewById(R.id.listRestaurantForUser);
        foodListRecycle = (RecyclerView) view.findViewById(R.id.foodListRecycle);
        img_serach = (ImageView) view.findViewById(R.id.img_serach);
        et_search = (EditText) view.findViewById(R.id.et_search);
        tv_noFound = (TextView) view.findViewById(R.id.tv_noFound);
        foodListRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        progressDialog = new LoaderProgress(getContext());

        //   restaurantFood.setOnClickListener(this);


        img_serach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Global.validatename(et_search.getText().toString())) {
//                    apiViewModel.restaurantList("resturentlist", "Restaurant", et_search.getText().toString(), latitude, longitude, RestaurentListFragment.this);
                    apiViewModel.restaurantList("resturentlistnew", "Restaurant", et_search.getText().toString(), latitude, longitude, RestaurentListFragment.this);
                } else
                    Toast.makeText(getContext(), "Please key for Search", Toast.LENGTH_LONG).show();

            }
        });
        if (Global.isOnline(getActivity())) {
//            if (requestPermissions()) {
//                if (Global.GpsEnable(getActivity())) {
//                    createLocationRequest();
//                } else Global.showGPSDisabledAlertToUser(getActivity());
//            }
//            apiViewModel.restaurantList("resturentlist", "Restaurant", "", "", "" , RestaurentListFragment.this);
            apiViewModel.restaurantList("resturentlistnew", "Restaurant", "", "", "" , RestaurentListFragment.this);
        } else Global.showDialog(getActivity());

        return view;
    }

//    @Override
//    public void onResume() {
//        getActivity().setTitle("FOODS");
//        super.onResume();
//    }

    @Override
    public void onClick(View v) {
        /*if(v == restaurantFood){
            Intent intent = new Intent(getActivity(), FoodAnotherOptionActivity.class);
            startActivity(intent);
        }*/
    }

    @Override
    public void onStarted() {
        progressDialog.show();
    }

    @Override
    public void onSuccess(LiveData<RestaurantListDto> data) {
        progressDialog.dismiss();
        if (data.getValue().getStatus().equalsIgnoreCase("success")) {
            foodListRecycle.setAdapter(new RestaurantListAdapter(getActivity(), data.getValue().getData()));
            if (data.getValue().getData().size() == 0) {
                tv_noFound.setVisibility(View.VISIBLE);
                tv_noFound.setText("Restaurant\n is not Available");
            } else {
                tv_noFound.setText(data.getValue().getStatus());
                tv_noFound.setVisibility(View.GONE);
                tv_noFound.setText("Restaurant\n is not Available");
            }
        }else {
            tv_noFound.setVisibility(View.VISIBLE);
            tv_noFound.setText("Restaurant\n is not Available");
        }

    }

    @Override
    public void onFailure(String message) {
        progressDialog.dismiss();
        System.out.println("Error ======= " + message);

    }

    private void setMyLastLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    latitude = "" + location.getLatitude();
                    longitude = "" + location.getLongitude();
                }
            }
        });
    }

    protected void createLocationRequest() {
        progressDialog.show();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(1500);
        mLocationRequest.setSmallestDisplacement(50);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                progressDialog.dismiss();
                startLocationUpdates();
            }
        });
        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(getActivity(),
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    private void stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult.getLocations() != null) {
                    if (locationResult.getLocations() != null) {
                        if (Global.isOnline(getActivity())) {
                            apiViewModel.restaurantList("resturentlist", "Restaurant", "", "" + locationResult.getLocations().get(0).getLatitude(), "" + locationResult.getLocations().get(0).getLongitude(), RestaurentListFragment.this);
                            stopLocationUpdates();
                        } else Global.showDialog(getActivity());
                    }
                }
            }

        };
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);
    }

    private boolean requestPermissions() {
        int Access_fine_location = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int Access_course_location = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (Access_fine_location != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }


        if (Access_course_location != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_ID);
            return false;
        }
        return true;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_ID: {
                if (Global.GpsEnable(getActivity())) {
                    createLocationRequest();
                } else Global.showGPSDisabledAlertToUser(getActivity());
                return;
            }
            default:
        }
    }

}
