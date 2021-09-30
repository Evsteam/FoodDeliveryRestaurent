package com.evs.foodexp.restaurentPkg.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.common.activity.ImageViewer;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.commonPkg.viewModel.RestorentViewModel;
import com.evs.foodexp.driverPkg.activity.DriverFoodInProgressActivity;
import com.evs.foodexp.driverPkg.activity.MultipleMap;
import com.evs.foodexp.models.OrderModel;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.userPkg.activity.TrackOrderActivity;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.ImageFilePath;
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
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FoodOrderDetails extends Fragment implements OnMapReadyCallback,
        AuthMsgListener , AuthStatusListener {
    TextView tv_title, tv_price, tv_address, tv_landMark, tv_specilaNotes, tv_restName;
    Button btn_requestPlusDriver;
    GoogleMap map;
    RestorentViewModel viewModel;
    SharedPreferences prefs;
    LoaderProgress progress;
    String driverId = "", bookingId, items, driverName, delLat, delLongs, dropAddress, amount, pickUplat, pickLong, restName, status = "";
    RelativeLayout layout_rating;
    RatingBar ratingBar;
    ImageView img_mapSearch;
    RoundedImageView img_slip,img_complete;
    LinearLayout layoutnotes;
    private static int SELECT_FILE = 11, REQUEST_CAMERA = 12;
    String selectedImagePath = "";

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_delivery_detail_fragment_layout, container, false);
        tv_title = view.findViewById(R.id.tv_title);
        tv_price = view.findViewById(R.id.tv_price);
        tv_address = view.findViewById(R.id.tv_address);
        tv_landMark = view.findViewById(R.id.tv_landMark);
        tv_specilaNotes = view.findViewById(R.id.tv_specilaNotes);
        tv_restName = view.findViewById(R.id.tv_restName);
        layoutnotes = view.findViewById(R.id.layoutnotes);
        img_mapSearch = view.findViewById(R.id.img_mapSearch);
        img_slip = view.findViewById(R.id.img_slip);
        img_complete = view.findViewById(R.id.img_complete);
        layout_rating = view.findViewById(R.id.layout_rating);
        ratingBar = view.findViewById(R.id.ratingBar);
        btn_requestPlusDriver = view.findViewById(R.id.btn_requestPlusDriver);
        viewModel = new ViewModelProvider(getActivity()).get(RestorentViewModel.class);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapLocation);
        mapFragment.getMapAsync(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        progress = new LoaderProgress(getContext());


//        if (OrderDetail.status.equalsIgnoreCase("4")) {
//            btn_requestPlusDriver.setText("Delivered");
//            btn_requestPlusDriver.setBackgroundColor(getResources().getColor(R.color.green));
//        } else if (OrderDetail.status.equalsIgnoreCase("10")) {
//            btn_requestPlusDriver.setText("Driver is not Accepted");
//        } else if (OrderDetail.status.equalsIgnoreCase("1")) {
//            btn_requestPlusDriver.setText("Track Your Order");
//        } else {
//            btn_requestPlusDriver.setText("On the Delivery");
//            btn_requestPlusDriver.setBackgroundColor(getResources().getColor(R.color.green));
//        }

        btn_requestPlusDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SessionManager.get_userType(prefs).equalsIgnoreCase("Member")) {
                    if (viewModel.getOrderDetails().getValue().getPaymentStatus().equalsIgnoreCase("2")) {
                        if (viewModel.getOrderDetails().getValue().getStatus().equalsIgnoreCase("1")) {
                            if (!Global.validatename(viewModel.getOrderDetails().getValue().getImageUpload())) {
                                selectImage();
                            } else {

                                viewModel.excuteUpdateStatus(bookingId,
                                        SessionManager.get_user_id(prefs), "2","", FoodOrderDetails.this);
                            }
                        } else if (status.equalsIgnoreCase("2")) {
                            Intent intent = new Intent(getActivity(), DriverFoodInProgressActivity.class);
                            intent.putExtra("bookingId", viewModel.getOrderDetails().getValue().getFoodorderId());
                            intent.putExtra("address", viewModel.getOrderDetails().getValue().getUserAddress());
                            intent.putExtra("landMark", viewModel.getOrderDetails().getValue().getLandmark());
                            intent.putExtra("name", viewModel.getOrderDetails().getValue().getUserName());
                            intent.putExtra("number", viewModel.getOrderDetails().getValue().getPhone());
                            intent.putExtra("dropuserlong", viewModel.getOrderDetails().getValue().getDeliveryLong());
                            intent.putExtra("dropuserlat", viewModel.getOrderDetails().getValue().getDeliveryLat());
//                            intent.putExtra("pickupuserlong", pickupuserlong);
//                            intent.putExtra("pickupuserlat", pickupuserlat);
//                            intent.putExtra("pickUpaddress", resturentddress);
//                            intent.putExtra("pickUplandMark", "");
                            intent.putExtra("imageUploade", viewModel.getOrderDetails().getValue().getImageUpload());
                            intent.putExtra("orderStatus", viewModel.getOrderDetails().getValue().getStatus());
                            startActivity(intent);
//                        viewModel.excuteUpdateStatus(bookingId,
//                                SessionManager.get_user_id(prefs), "4", FoodOrderDetails.this);
                        }

                    }else if (viewModel.getOrderDetails().getValue().getStatus().equalsIgnoreCase("10")){
//                        viewModel.executeUpdateOrder(SessionManager.get_user_id(prefs), OrderDetail.foodRequestId, OrderDetail.orderId,
//                                "2", FoodOrderDetails.this);
                        CustomDialogClass cdd = new CustomDialogClass(getActivity());
                        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        cdd.show();
                    }
                }else  if (viewModel.getOrderDetails().getValue().getPaymentStatus().equalsIgnoreCase("2")) {
                   if (viewModel.getOrderDetails().getValue().getStatus().equalsIgnoreCase("2")) {
                    Intent intent = new Intent(getActivity(), TrackOrderActivity.class);
                    intent.putExtra("bookingId", viewModel.getOrderDetails().getValue().getFoodorderId());
                    intent.putExtra("driverId", viewModel.getOrderDetails().getValue().getDriverId());
                    intent.putExtra("dropLat", viewModel.getOrderDetails().getValue().getDeliveryLat());
                    intent.putExtra("dropLongs", viewModel.getOrderDetails().getValue().getDeliveryLong());
                    intent.putExtra("dropAddress", viewModel.getOrderDetails().getValue().getUserAddress());
                    intent.putExtra("amount", viewModel.getOrderDetails().getValue().getTotalAmount());
//                    intent.putExtra("items", items);
//                    intent.putExtra("pickLong", pickLong);
//                    intent.putExtra("pickUplat", pickUplat);
//                    intent.putExtra("restName", restName);
                    intent.putExtra("driverName", viewModel.getOrderDetails().getValue().getDriverName());
                    startActivity(intent);
                   }
            }



//                if( !OrderDetail.status.equalsIgnoreCase("delivered")){
//                    viewModel.excuteRequestForDriver(OrderDetail.orderId,
//                            SessionManager.get_user_id(prefs),OrderDeliveryDetailsFragment.this);
//                }
//                if (btn_requestPlusDriver.getText().toString().equalsIgnoreCase("Track Your Order")) {
//                    Intent intent = new Intent(getActivity(), TrackOrderActivity.class);
//                    intent.putExtra("bookingId", bookingId);
//                    intent.putExtra("driverId", driverId);
//                    intent.putExtra("dropLat", delLat);
//                    intent.putExtra("dropLongs", delLongs);
//                    intent.putExtra("dropAddress", dropAddress);
//                    intent.putExtra("amount", amount);
//                    intent.putExtra("items", items);
//                    intent.putExtra("pickLong", pickLong);
//                    intent.putExtra("pickUplat", pickUplat);
//                    intent.putExtra("restName", restName);
//                    intent.putExtra("driverName", driverName);
//                    startActivity(intent);
//                }else if (btn_requestPlusDriver.getText().toString().equalsIgnoreCase("On the Delivery")) {
//                    Intent intent = new Intent(getActivity(), TrackOrderActivity.class);
//                    intent.putExtra("bookingId", bookingId);
//                    intent.putExtra("driverId", driverId);
//                    intent.putExtra("dropLat", delLat);
//                    intent.putExtra("dropLongs", delLongs);
//                    intent.putExtra("dropAddress", dropAddress);
//                    intent.putExtra("amount", amount);
//                    intent.putExtra("items", items);
//                    intent.putExtra("pickLong", pickLong);
//                    intent.putExtra("pickUplat", pickUplat);
//                    intent.putExtra("restName", restName);
//                    intent.putExtra("driverName", driverName);
//                    startActivity(intent);
//                } else
//                    Toast.makeText(getActivity(), btn_requestPlusDriver.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });

        layout_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Global.validatename(driverId)) {
                    Submitreview cdd = new Submitreview(getActivity(), driverId);
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cdd.show();
                }
            }
        });

        img_mapSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertbox = new AlertDialog.Builder(getActivity());
                alertbox.setTitle(getActivity().getResources().getString(R.string.app_name));
                alertbox.setMessage("Please Google to the closest restaurant to you based on this order");
                alertbox.setPositiveButton(getActivity().getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + restName);
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                            }
                        });
                alertbox.show();
            }
        });

        img_slip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),ImageViewer.class);
                intent.putExtra("test",viewModel.getOrderDetails().getValue().getImageUpload());
                startActivity(intent);
            }
        });
        img_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),ImageViewer.class);
                intent.putExtra("test",viewModel.getOrderDetails().getValue().getDeliveryImage());
                startActivity(intent);
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
                progress.dismiss();
                tv_title.setText(orderModel.getUserName());
                tv_price.setText("$ " + orderModel.getTotalAmount());
                tv_address.setText(orderModel.getUserAddress());
                if (Global.validatename(orderModel.getSpecialNote())) {
                    layoutnotes.setVisibility(View.VISIBLE);
                } else layoutnotes.setVisibility(View.GONE);

                tv_specilaNotes.setText(orderModel.getSpecialNote());
                tv_restName.setText(orderModel.getResturentName());
                tv_landMark.setText(orderModel.getWorkPlace() + " " + orderModel.getLandmark());
                if (Global.validatename(orderModel.getWorkPlace())) {
                    if (orderModel.getDeliveryLat().length() > 4) {
                        myLocation(new LatLng(Double.parseDouble(orderModel.getDeliveryLat()), Double.parseDouble(orderModel.getDeliveryLong())));
                    }

                }
//                if (orderModel.getDriverId().equalsIgnoreCase("1")) {
//                    if (Global.validatename(orderModel.getDriverId())) {
//                        btn_requestPlusDriver.setText("Track your Order");
//                    } else btn_requestPlusDriver.setText("Pending");
//                }
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
                Glide.with(getActivity()).load(orderModel.getImageUpload()).into(img_slip);
                Glide.with(getActivity()).load(orderModel.getDeliveryImage()).into(img_complete);
//                if (orderModel.getFoodDetails().size() > 0) {
//                    if (orderModel.getFoodDetails().size() == 1) {
//                        items = orderModel.getFoodDetails().get(0).getQuintity() + " " + orderModel.getFoodDetails().get(0).getName();
//                    } else {
//                        String itemsde = "";
//                        for (int i = 0; i < orderModel.getFoodDetails().size(); i++) {
//                            if (i == 0) {
//                                items = orderModel.getFoodDetails().get(i).getQuintity() + " " + orderModel.getFoodDetails().get(0).getName() + " ";
//                            } else
//                                items = items + orderModel.getFoodDetails().get(i).getQuintity() + " " + orderModel.getFoodDetails().get(0).getName();
//                        }
//                        items = itemsde;
//                    }
//
//                } else items = orderModel.getWhatYouWant();

                if (Global.validatename(orderModel.getDriverAVG())) {
                    ratingBar.setRating(Float.parseFloat(orderModel.getDriverAVG()));
                } else ratingBar.setRating(0);

                if (SessionManager.get_userType(prefs).equalsIgnoreCase(Global.type_person)) {
                    if (orderModel.getStatus().equalsIgnoreCase("4")) {
                        layout_rating.setVisibility(View.VISIBLE);
                    }
                }
                if (!SessionManager.get_userType(prefs).equalsIgnoreCase("Member")) {

                    if (orderModel.getPaymentStatus().equalsIgnoreCase("2")) {
                        if (orderModel.getStatus().equalsIgnoreCase("1")) {
                            btn_requestPlusDriver.setVisibility(View.VISIBLE);
                            if (!Global.validatename(orderModel.getImageUpload())) {
                                img_mapSearch.setVisibility(View.VISIBLE);
                                btn_requestPlusDriver.setText("Upload Slip!!");
                            } else {
                                img_slip.setVisibility(View.VISIBLE);
                                btn_requestPlusDriver.setText("Confirm and Pickup!!");
                            }
                        } else if (orderModel.getStatus().equalsIgnoreCase("2")) {
                            img_slip.setVisibility(View.VISIBLE);
                            btn_requestPlusDriver.setVisibility(View.VISIBLE);
                            btn_requestPlusDriver.setText("Track your Order");

                        } else if (orderModel.getStatus().equalsIgnoreCase("4")) {
                            img_slip.setVisibility(View.VISIBLE);
                            img_complete.setVisibility(View.VISIBLE);
                            btn_requestPlusDriver.setVisibility(View.VISIBLE);
                            btn_requestPlusDriver.setText("Completed!!");
                        }

                    } else if (orderModel.getStatus().equalsIgnoreCase("10")) {
                        btn_requestPlusDriver.setVisibility(View.VISIBLE);
                        btn_requestPlusDriver.setText("Accept");
                    } else if (orderModel.getStatus().equalsIgnoreCase("1")) {
                        btn_requestPlusDriver.setVisibility(View.VISIBLE);
                        btn_requestPlusDriver.setText("Waiting for Payment!!");
                    }


                } else if (orderModel.getPaymentStatus().equalsIgnoreCase("2")) {
                    btn_requestPlusDriver.setVisibility(View.VISIBLE);
                    if (orderModel.getStatus().equalsIgnoreCase("1")) {
                        if (!Global.validatename(orderModel.getImageUpload())) {
                            btn_requestPlusDriver.setText("Waiting For Slip!!");
                        } else {
                            img_slip.setVisibility(View.VISIBLE);
                            btn_requestPlusDriver.setText("Slip Uploaded!!");
                        }
                    } else if (orderModel.getStatus().equalsIgnoreCase("2")) {
                        img_slip.setVisibility(View.VISIBLE);
                        btn_requestPlusDriver.setVisibility(View.VISIBLE);
                        btn_requestPlusDriver.setText("On The Way!!");
                    } else if (orderModel.getStatus().equalsIgnoreCase("4")) {
                        img_slip.setVisibility(View.VISIBLE);
                        img_complete.setVisibility(View.VISIBLE);
                        btn_requestPlusDriver.setVisibility(View.VISIBLE);
                        btn_requestPlusDriver.setText("Completed!!");
                    }
                } else if (orderModel.getStatus().equalsIgnoreCase("10")) {
                    btn_requestPlusDriver.setVisibility(View.VISIBLE);
                    btn_requestPlusDriver.setText("Waiting for Driver!!");
//                } else if (orderModel.getStatus().equalsIgnoreCase("1")) {
//                    btn_requestPlusDriver.setVisibility(View.VISIBLE);
//                    btn_requestPlusDriver.setText("Payment!!");
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
            Global.msgDialog(getActivity(), msgResponce.getMsg());
            viewModel.apifoodDetails(SessionManager.get_user_id(prefs), OrderDetail.orderId, FoodOrderDetails.this);
        } else Global.msgDialog(getActivity(), msgResponce.getMsg());
    }

    public class Submitreview extends Dialog implements
            View.OnClickListener, AuthListener<MsgResponse> {

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
                Global.msgDialog(getActivity(), data.getValue().getMsg());
            }

        }

        @Override
        public void onFailure(String message) {
            progress.dismiss();
            Global.msgDialog(getActivity(), message);
        }
    }


    private void selectImage() {

        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camera")) {
                    openCamera();
                } else if (items[item].equals("Gallery")) {
                    openGalery();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void openGalery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                    ContentResolver resolver = getContext().getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis());
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + System.currentTimeMillis());
                    Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    try {
                        OutputStream fos = resolver.openOutputStream(imageUri);

                        selectedImagePath = ImageFilePath.getPath(getActivity(), imageUri);
                        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        try {
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Log.e("image", selectedImagePath);
                    viewModel.executeImgeUpload(bookingId, selectedImagePath, this);
                }else {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    selectedImagePath = String.valueOf(destination);
                    viewModel.executeImgeUpload(bookingId, selectedImagePath, this);
                }
                } else if (requestCode == SELECT_FILE) {
                Bitmap bm = null;
                if (data != null) {
                    try {
                        Uri selectedImageUri = data.getData();
                        selectedImagePath = ImageFilePath.getPath(getActivity(), selectedImageUri);
                        viewModel.executeImgeUpload(bookingId, selectedImagePath, this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public class CustomDialogClass extends Dialog {

        public Activity c;
        public Dialog d;
        LinearLayout layout_del;
        TextView tv_accept, tv_totalAmount, tv_reject, tv_dropLocation, tv_picupLocation, tv_deliveryFee, tv_tipAmount, tv_time, tv_miles;
        ImageView img_mapSearchd;
        public CustomDialogClass(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.food_delivery_request_alert);
            tv_accept = findViewById(R.id.tv_accept);
            tv_reject = findViewById(R.id.tv_reject);
            tv_dropLocation = findViewById(R.id.tv_dropLocation);
            tv_totalAmount = findViewById(R.id.tv_totalAmount);
            layout_del = findViewById(R.id.layout_del);
            tv_picupLocation = findViewById(R.id.tv_picupLocation);
            tv_deliveryFee = findViewById(R.id.tv_deliveryFee);
            tv_tipAmount = findViewById(R.id.tv_tipAmount);
            tv_time = findViewById(R.id.tv_time);
            tv_miles = findViewById(R.id.tv_miles);
            img_mapSearchd = findViewById(R.id.img_mapSearch);
            img_mapSearchd.setVisibility(View.VISIBLE);

//            tv_miles.setText(miles);
//            tv_time.setText(estTime);
//            tv_tipAmount.setText("$ " + s_tipAmount);
//            tv_totalAmount.setText("$ " + totalAmount);
            tv_dropLocation.setText(dropAddress);
            tv_picupLocation.setText( viewModel.getOrderDetails().getValue().getResturentName());
//            tv_deliveryFee.setText("$ " + deliveryFee);
//            if (Global.validatename(deliveryFee)) {
//                layout_del.setVisibility(View.VISIBLE);
//            } else layout_del.setVisibility(View.GONE);

            tv_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.executeUpdateOrder(SessionManager.get_user_id(prefs), OrderDetail.foodRequestId, OrderDetail.orderId, "2", FoodOrderDetails.this);
                    dismiss();
                }
            });
            tv_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.executeUpdateOrder(SessionManager.get_user_id(prefs), OrderDetail.foodRequestId, OrderDetail.orderId, "3", FoodOrderDetails.this);
                    dismiss();
                }
            });

            img_mapSearchd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(getActivity());
                    alertbox.setTitle(getActivity().getResources().getString(R.string.app_name));
                    alertbox.setMessage("Please Google to the closest restaurant to you based on this order");
                    alertbox.setPositiveButton(getActivity().getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + viewModel.getOrderDetails().getValue().getResturentName());
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                    mapIntent.setPackage("com.google.android.apps.maps");
                                    startActivity(mapIntent);
                                }
                            });
                    alertbox.show();
                }
            });
        }


    }
}
