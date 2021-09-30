package com.evs.foodexp.driverPkg.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import  androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.common.activity.TrackInProgress;
import com.evs.foodexp.commonPkg.common.activity.TrackOrder1;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.commonPkg.viewModel.RestorentViewModel;
import com.evs.foodexp.models.BookingModel;
import com.evs.foodexp.repositry.DataResponse;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.userPkg.activity.PaymentActivity;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
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
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DecimalFormat;
import java.text.Format;

import de.hdodenhof.circleimageview.CircleImageView;

public class ServiceJobDetailActivity extends AppCompatActivity implements
        OnMapReadyCallback, AuthListener<DataResponse<BookingModel>>, AuthMsgListener {

    private GoogleMap mMap;
    int PERMISSION_ALL = 1;
    TextView tv_title, tv_specialNotess, tv_transactionFee, tv_sateTax, tv_phonenumber, tv_Datetime, tv_address, tv_tipAmount, tv_totalAmount, tv_servicePrice, tv_serviceName;
    CircleImageView imag_profile;
    RecyclerView serviceList;
    String bookingId, vendorId = "";
    RestorentViewModel viewModel;
    LoaderProgress progress;
    SharedPreferences prefs;
    Button btn_ServiceConfrim;
    RelativeLayout layout_rating;
    RatingBar ratingBar;
    RelativeLayout specialNotes;
    String paymentStatus = "0", status,AdminAmount,driverStripeAccount;
    String tipAmount = "", totalAmount = "", discount = "", serviceAmount = "", services = "", groupId = "",
            lats, longs, address, name, number, driverId, driverName, amount, transactionFee = "";
    String uploadedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_job_detail);

        Utills.StatusBarColour(ServiceJobDetailActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("SERVICE JOB DETAIL ");
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
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bookingId = bundle.getString("bookingId");
        }
        viewModel = new ViewModelProvider(ServiceJobDetailActivity.this).get(RestorentViewModel.class);
        progress = new LoaderProgress(ServiceJobDetailActivity.this);
        prefs = PreferenceManager.getDefaultSharedPreferences(ServiceJobDetailActivity.this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btn_ServiceConfrim = findViewById(R.id.btn_ServiceConfrim);
        ratingBar = findViewById(R.id.ratingBar);
        specialNotes = findViewById(R.id.specialNotes);
        tv_sateTax = findViewById(R.id.tv_sateTax);
        tv_transactionFee = findViewById(R.id.tv_transactionFee);
        layout_rating = findViewById(R.id.layout_rating);
        tv_serviceName = findViewById(R.id.tv_serviceName);
        tv_servicePrice = findViewById(R.id.tv_servicePrice);
        tv_title = findViewById(R.id.tv_title);
        tv_phonenumber = findViewById(R.id.tv_phonenumber);
        tv_Datetime = findViewById(R.id.tv_Datetime);
        tv_specialNotess = findViewById(R.id.tv_specialNotess);
        tv_address = findViewById(R.id.tv_address);
        tv_tipAmount = findViewById(R.id.tv_tipAmount);
        tv_totalAmount = findViewById(R.id.tv_totalAmount);
        imag_profile = findViewById(R.id.imag_profile);
        serviceList = findViewById(R.id.serviceList);


        btn_ServiceConfrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SessionManager.get_userType(prefs).equalsIgnoreCase(Global.type_person)) {
                    if (paymentStatus.equalsIgnoreCase("1")) {
                        Intent intent = new Intent(ServiceJobDetailActivity.this, PaymentActivity.class);
                        intent.putExtra("groupId", groupId);
                        intent.putExtra("totalAmount", "" + totalAmount);
                        intent.putExtra("tipAmount", "" + tipAmount);
                        intent.putExtra("discount", "" + discount);
                        intent.putExtra("serviceAmount", "" + serviceAmount);
                        intent.putExtra("service", "" + services);
                        intent.putExtra("transactionFee", "" + transactionFee);
                        intent.putExtra("AdminAmount", AdminAmount);
                        intent.putExtra("driverStripeAccount",  driverStripeAccount);
                        startActivity(intent);

                    } else if (status.equalsIgnoreCase("1")) {
                        Intent intent = new Intent(ServiceJobDetailActivity.this, TrackOrder1.class);
                        intent.putExtra("bookingId", bookingId);
                        intent.putExtra("driverId", driverId);
                        intent.putExtra("amount", amount);
                        intent.putExtra("driverName", driverName);
                        intent.putExtra("dropLat", lats);
                        intent.putExtra("dropLongs", longs);
                        intent.putExtra("dropAddress", address);
                        intent.putExtra("uploadedImage", uploadedImage);
                        startActivity(intent);
                        finish();
                    }

                } else if (status.equalsIgnoreCase("1")) {
                    Intent intent = new Intent(ServiceJobDetailActivity.this, TrackInProgress.class);
                    intent.putExtra("bookingId", bookingId);
                    intent.putExtra("address", address);
                    intent.putExtra("name", name);
                    intent.putExtra("number", number);
                    intent.putExtra("dropuserlat", lats);
                    intent.putExtra("dropuserlong", longs);
                    intent.putExtra("type", "Bookings");
                    intent.putExtra("uploadedImage", uploadedImage);
                    startActivity(intent);
                    finish();
//
//                    btn_ServiceConfrim.setEnabled(false);
                }

            }
        });
        layout_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Global.validatename(vendorId)) {
                    Submitreview cdd = new Submitreview(ServiceJobDetailActivity.this, vendorId);
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cdd.show();
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        mMap.getUiSettings().setZoomGesturesEnabled(false);
//        initMap();
    }

//    private void initMap() {
//
//        LocationManager locationManager = (LocationManager) ServiceJobDetailActivity.this.getSystemService(LOCATION_SERVICE);
//        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        if (isGPSEnabled) {
//            googleApiClient = new GoogleApiClient.Builder(ServiceJobDetailActivity.this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .build();
//            googleApiClient.connect();
//            getCurrentLocation();
//        } else {
//            gps_on();
//        }
//
//        if (!Places.isInitialized()) {
//            Places.initialize(getApplicationContext(),getResources().getString(R.string.location_key));
//
//        }
//     //   PlacesClient placesClient = Places.createClient(this);
//       /* places.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
//
//        ((EditText) places.getView().findViewById(R.id.places_autocomplete_search_input)).setBackground(getResources().getDrawable(R.drawable.corner_round_white));
//        ((ImageButton) places.getView().findViewById(R.id.places_autocomplete_search_button)).setVisibility(View.GONE);
//        ((ImageButton) places.getView().findViewById(R.id.places_autocomplete_clear_button)).setVisibility(View.GONE);
//        ((EditText) places.getView().findViewById(R.id.places_autocomplete_search_input)).setTextSize(16.0f);
//
//        ((EditText) places.getView().findViewById(R.id.places_autocomplete_search_input)).setText(address);
//        //((ImageButton) place_autocomplete_pickup_loc.getView().findViewById(R.id.place_autocomplete_clear_button)).setImageResource(R.drawable.xblack);
//        ((ImageButton) places.getView().findViewById(R.id.places_autocomplete_clear_button)).setBackgroundColor(Color.WHITE);
//
//        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(@NonNull Place place) {
//                // TODO: Get info about the selected place.
//                mMap.clear();
//                ((ImageButton) places.getView().findViewById(R.id.places_autocomplete_clear_button)).setVisibility(View.GONE);
//
//                Log.i(TAG, "Place: " + place.getName());
//                address = place.getAddress();
////                Log.d("searched location", Str_pickLocET);
////                Log.d("searched location", String.valueOf(place.getLatLng()));
//                // pickLocET.setText(Str_pickLocET);
//                latitude = place.getLatLng().latitude;
//                longitude = place.getLatLng().longitude;
//
//                moveMap(place.getLatLng());
//            }
//
//            @Override
//            public void onError(Status status) {
//                // TODO: Handle the error.
//                Log.i(TAG, "An error occurred: " + status);
//            }
//        });
//
//        places.getView().findViewById(R.id.places_autocomplete_clear_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.i(TAG, "Place: " + "");
////                Str_pickLocET = "";
////                places.setText(Str_pickLocET);
////                Log.d("searched location", Str_pickLocET);
//
//                view.setVisibility(View.GONE);
//            }
//        });*/
//
//
//    }

    public void gps_on() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ServiceJobDetailActivity.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Turn On Location Services ");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                        startActivityForResult(intent, 14);
                    }
                });
        alertDialog.setNegativeButton("Not Now",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        alertDialog.show();

    }

//    private void getCurrentLocation() {
//
////        if (ActivityCompat.checkSelfPermission(SelectLocation.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
////                && ActivityCompat.checkSelfPermission(SelectLocation.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////            checkLocation();
////            return;
////        }
//        if (!checkLocation()) {
//            return;
//        }
//        try {
//
//            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//            if (location != null) {
//                //Getting longitude and latitude
//                longitude = location.getLongitude();
//                latitude = location.getLatitude();
//
//
//                mMap.setMyLocationEnabled(true);
//
//                //moving the map to location
//                moveMap(new LatLng(latitude, longitude));
//            //    ((EditText) places.getView().findViewById(R.id.places_autocomplete_search_input)).setText(getAddress(new LatLng(latitude, longitude)));
//            } else {
//
//                LocationManager locationManager = (LocationManager) ServiceJobDetailActivity.this.getSystemService(LOCATION_SERVICE);
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                        1000 * 60 * 1,
//                        10, this
//                );
//                if (locationManager != null) {
//                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                    latitude = location.getLatitude();
//                    longitude = location.getLongitude();
//                    moveMap(new LatLng(latitude, longitude));
//              //      ((EditText) places.getView().findViewById(R.id.places_autocomplete_search_input)).setText(getAddress(new LatLng(latitude, longitude)));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void moveMap(LatLng latlng1, String aname) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(latlng1)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))
                .title(aname));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng1).zoom(15.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng1));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        //  ((EditText) places.getView().findViewById(R.id.places_autocomplete_search_input)).setText(address);
    }


    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onSuccess(LiveData<DataResponse<BookingModel>> data) {
        progress.dismiss();
        if (data != null) {
            lats = data.getValue().getData().getLatidude();
            longs = data.getValue().getData().getLongitude();
            address = data.getValue().getData().getAddress();
            name = data.getValue().getData().getFullName();
            number = data.getValue().getData().getUserPhone();
            driverId = data.getValue().getData().getVendorId();
            driverName = data.getValue().getData().getVendorName();
            amount = data.getValue().getData().getTotalAmount();
            uploadedImage = data.getValue().getData().getImageUpload();

            paymentStatus = data.getValue().getData().getPaymentStatus();
            tipAmount = data.getValue().getData().getTIP();
            totalAmount = data.getValue().getData().getTotalAmount();
            discount = data.getValue().getData().getDiscountAmount();
            serviceAmount = data.getValue().getData().getServiceAmount();
            services = data.getValue().getData().getServiceName();
            groupId = data.getValue().getData().getGroupId();
            status = data.getValue().getData().getStatus();
            driverStripeAccount = data.getValue().getData().getDriverStripeAccount();
            AdminAmount = data.getValue().getData().getAdminAmount();

            tv_Datetime.setText(data.getValue().getData().getBookingDate());
            tv_tipAmount.setText("$ " + data.getValue().getData().getTIP());
            tv_transactionFee.setText("$ " + data.getValue().getData().getTransactionFee());

            tv_servicePrice.setText("$ " + data.getValue().getData().getServiceAmount());
            tv_serviceName.setText(data.getValue().getData().getServiceName());
            tv_specialNotess.setText(data.getValue().getData().getSpecialNote());
            tv_sateTax.setText("$ " + data.getValue().getData().getSalesTax());

            totalAmount = String.valueOf(Double.parseDouble(data.getValue().getData().getTotalAmount()));
            transactionFee = String.valueOf(Double.parseDouble(data.getValue().getData().getTransactionFee()));


            tv_totalAmount.setText("$ " + new DecimalFormat("##.##").format(Double.parseDouble(totalAmount)+Double.parseDouble(transactionFee)));

            vendorId = data.getValue().getData().getVendorId();
            if (!Global.validatename(data.getValue().getData().getSpecialNote())) {
                specialNotes.setVisibility(View.GONE);
            }

            if (Global.validatename(data.getValue().getData().getLatidude())) {
                moveMap(new LatLng(Double.parseDouble(data.getValue().getData().getLatidude()),
                                Double.parseDouble(data.getValue().getData().getLongitude())),
                        data.getValue().getData().getAddress());
            }
            if (data.getValue().getData().getStatus().equalsIgnoreCase("1")) {
                btn_ServiceConfrim.setText("Track Your Service");
            } else if (data.getValue().getData().getStatus().equalsIgnoreCase("0")) {
                btn_ServiceConfrim.setText("Pending");
            } else btn_ServiceConfrim.setText("Service Completed");

            if (SessionManager.get_userType(prefs).equalsIgnoreCase(Global.type_driver)) {
                tv_title.setText(data.getValue().getData().getFullName());
                tv_phonenumber.setText("Phone : " + data.getValue().getData().getUserPhone());
                tv_address.setText(data.getValue().getData().getAddress());
                Glide.with(ServiceJobDetailActivity.this).load(data.getValue().getData().getImage())
                        .placeholder(R.drawable.placeholder_user).into(imag_profile);

            } else {
                if (data.getValue().getData().getStatus().equalsIgnoreCase("0")) {
                    tv_title.setText(data.getValue().getData().getFullName());
                    tv_phonenumber.setText("Phone : " + data.getValue().getData().getUserPhone());
                    tv_address.setText(data.getValue().getData().getAddress());
                    Glide.with(ServiceJobDetailActivity.this).load(data.getValue().getData().getImage())
                            .placeholder(R.drawable.placeholder_user).into(imag_profile);
                } else {
                    tv_title.setText(data.getValue().getData().getVendorName());
                    tv_phonenumber.setText("Phone : " + data.getValue().getData().getVendorPhone());
                    tv_address.setText(data.getValue().getData().getVendorAddress());
                    Glide.with(ServiceJobDetailActivity.this).load(data.getValue().getData().getVendorImage())
                            .placeholder(R.drawable.placeholder_user).into(imag_profile);
                }
            }
            if (Global.validatename(data.getValue().getData().getVendorAVGRating())) {
                ratingBar.setRating(Float.parseFloat(data.getValue().getData().getVendorAVGRating()));
            } else ratingBar.setRating(0);

            if (SessionManager.get_userType(prefs).equalsIgnoreCase(Global.type_person)) {
                if (data.getValue().getData().getStatus().equalsIgnoreCase("2")) {
                    layout_rating.setVisibility(View.VISIBLE);
                }
            }

            if (SessionManager.get_userType(prefs).equalsIgnoreCase(Global.type_person)) {

                switch (data.getValue().getData().getPaymentStatus()) {
                    case "0":
                        btn_ServiceConfrim.setText("Waiting For Driver!!");
                        break;
                    case "1":
                        btn_ServiceConfrim.setText(getString(R.string.confrimPyament));
                        break;

                }
            } else {
                switch (data.getValue().getData().getPaymentStatus()) {
                    case "1":
                        btn_ServiceConfrim.setText("Waiting For Payment!!");

                        break;

                }
            }

        }
    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialogBack(ServiceJobDetailActivity.this, message);
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        if (msgResponce.getStatus().equalsIgnoreCase("success")) {
            Global.msgDialogBack(ServiceJobDetailActivity.this, msgResponce.getMsg());
        }
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
                Global.msgDialog(ServiceJobDetailActivity.this, data.getValue().getMsg());
            }

        }

        @Override
        public void onFailure(String message) {
            progress.dismiss();
            Global.msgDialog(ServiceJobDetailActivity.this, message);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.excuteServiceDetails(bookingId, this);
    }
}
