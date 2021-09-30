package com.evs.foodexp.driverPkg.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import  androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.common.activity.CompleteProfileActivity;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.commonPkg.viewModel.RestorentViewModel;
import com.evs.foodexp.driverPkg.fragment.BottomFragment;
import com.evs.foodexp.driverPkg.fragment.TopFragment;
import com.evs.foodexp.models.RequestModel;
import com.evs.foodexp.repositry.DataResponse;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.restaurentPkg.fragment.OrderDetail;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.ImageFilePath;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MultipleMap extends Fragment implements AuthMsgListener, AuthListener<DataResponse<RequestModel>> {
    Button btn_foodPickUp;
    LoaderProgress progress;
    RestorentViewModel viewModel;
    SharedPreferences prefes;
    String miles, estTime, tip, resturentddress,resturentName, dropAddress, deliveryFee,
            totalAmount, number, lanMark, imageUploaded = "", name,
            dropuserlong, dropuserlat, pickupuserlong, pickupuserlat, selectedImagePath = "";
    private static int SELECT_FILE = 11, REQUEST_CAMERA = 12;


//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.multiple_map);
//
//        TopFragment frg=new TopFragment();//create the fragment instance for the top fragment
//        BottomFragment frg1=new BottomFragment();//create the fragment instance for the middle fragment
//
//        FragmentManager manager=getSupportFragmentManager();//create an instance of fragment manager
//        FragmentTransaction transaction=manager.beginTransaction();//create an instance of Fragment-transaction
//
//        transaction.add(R.id.fm_userInfromation, frg, "Frag_Top_tag");
//        transaction.add(R.id.fm_restaurentInfromation, frg1, "Frag_Middle_tag");
//
//
//        transaction.commit();
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.multiple_map, container, false);

        TopFragment frg = new TopFragment();//create the fragment instance for the top fragment
        BottomFragment frg1 = new BottomFragment();//create the fragment instance for the middle fragment

        FragmentManager manager = getActivity().getSupportFragmentManager();//create an instance of fragment manager
        FragmentTransaction transaction = manager.beginTransaction();//create an instance of Fragment-transaction

        transaction.add(R.id.fm_userInfromation, frg, "Frag_Top_tag");
        transaction.add(R.id.fm_restaurentInfromation, frg1, "Frag_Middle_tag");

        prefes = PreferenceManager.getDefaultSharedPreferences(getContext());
        progress = new LoaderProgress(getContext());
        viewModel = new ViewModelProvider(getActivity()).get(RestorentViewModel.class);
        viewModel.excuteFoodOrderDetails(OrderDetail.orderId, this);

        transaction.commit();
        btn_foodPickUp = view.findViewById(R.id.btn_foodPickUp);


        if (OrderDetail.status.equalsIgnoreCase("4")) {
            btn_foodPickUp.setText("Delivered");
            btn_foodPickUp.setBackgroundColor(getResources().getColor(R.color.green));
        } else if (OrderDetail.status.equalsIgnoreCase("10")) {
            btn_foodPickUp.setText("Accept");
        } else if (OrderDetail.status.equalsIgnoreCase("1")) {

            btn_foodPickUp.setText("Confirm food pick up");

        } else {
            btn_foodPickUp.setText("On the Delivery");
            btn_foodPickUp.setBackgroundColor(getResources().getColor(R.color.green));
        }

        btn_foodPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (OrderDetail.status.equalsIgnoreCase("10")) {
                    CustomDialogClass cdd = new CustomDialogClass(getActivity(), OrderDetail.foodRequestId, OrderDetail.orderId,
                            dropAddress, resturentName, deliveryFee, tip, estTime, miles, totalAmount);
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cdd.show();
                } else if (OrderDetail.status.equalsIgnoreCase("1")) {
                    if (OrderDetail.PaymentStatus.equalsIgnoreCase("2")) {

                        Intent intent = new Intent(getActivity(), DriverFoodInProgressActivity.class);
                        intent.putExtra("bookingId", OrderDetail.orderId);
                        intent.putExtra("address", dropAddress);
                        intent.putExtra("landMark", lanMark);
                        intent.putExtra("name", name);
                        intent.putExtra("number", number);
                        intent.putExtra("dropuserlong", dropuserlong);
                        intent.putExtra("dropuserlat", dropuserlat);
                        intent.putExtra("pickupuserlong", pickupuserlong);
                        intent.putExtra("pickupuserlat", pickupuserlat);
                        intent.putExtra("pickUpaddress", resturentddress);
                        intent.putExtra("pickUplandMark", "");
                        intent.putExtra("imageUploade",imageUploaded);
                        intent.putExtra("orderStatus",OrderDetail.status);
                        startActivity(intent);
//                        viewModel.excuteUpdateStatus(OrderDetail.orderId,
//                                SessionManager.get_user_id(prefes), "2", MultipleMap.this);

                    } else
                        Toast.makeText(getContext(), "Payment Pending by User Side", Toast.LENGTH_LONG).show();

//                } else if(OrderDetail.status.equalsIgnoreCase("2")){
//                    viewModel.excuteUpdateStatus(OrderDetail.orderId,
//                            SessionManager.get_user_id(prefes), "4", MultipleMap.this);
                } else {
                    if (btn_foodPickUp.getText().toString().equalsIgnoreCase("On the Delivery")) {
                        if (Global.validatename(dropuserlat) && Global.validatename(dropuserlong)) {
                            if (Global.validatename(pickupuserlong) && Global.validatename(pickupuserlat)) {
                                Intent intent = new Intent(getActivity(), DriverFoodInProgressActivity.class);
                                intent.putExtra("bookingId", OrderDetail.orderId);
                                intent.putExtra("address", dropAddress);
                                intent.putExtra("landMark", lanMark);
                                intent.putExtra("pickUpaddress", resturentddress);
                                intent.putExtra("pickUplandMark", "");
                                intent.putExtra("name", name);
                                intent.putExtra("number", number);
                                intent.putExtra("dropuserlong", dropuserlong);
                                intent.putExtra("dropuserlat", dropuserlat);
                                intent.putExtra("pickupuserlong", pickupuserlong);
                                intent.putExtra("pickupuserlat", pickupuserlat);
                                intent.putExtra("imageUploade",imageUploaded);
                                intent.putExtra("orderStatus",OrderDetail.status);
                                startActivity(intent);
                            } else
                                Toast.makeText(getContext(), "Restaurent location is not valid", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(getContext(), "User  location is not valid", Toast.LENGTH_LONG).show();

                    }
                }


            }
        });

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
            miles = data.getValue().getData().getMile();
            imageUploaded = data.getValue().getData().getImageUpload();
            estTime = data.getValue().getData().getEstTime();
            tip = data.getValue().getData().getTIPAmount();
            resturentName = data.getValue().getData().getResturentName();
            resturentddress = data.getValue().getData().getResturentddress();
            dropAddress = data.getValue().getData().getAddress();
            deliveryFee = data.getValue().getData().getDeliveryFee();
            totalAmount = data.getValue().getData().getTotalAmount();
            number = data.getValue().getData().getPhone();
            name = data.getValue().getData().getUserName();
            lanMark = data.getValue().getData().getLandmark();
            dropuserlong = data.getValue().getData().getDeliveryLong();
            dropuserlat = data.getValue().getData().getDeliveryLat();
            pickupuserlong = data.getValue().getData().getResturentlongitude();
            pickupuserlat = data.getValue().getData().getResturentlatitude();
        }
    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();

        Global.msgDialogBack(getActivity(), message);
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progress.dismiss();

        if (btn_foodPickUp.getText().equals("Confirm food pick up")) {
            if (Global.validatename(dropuserlat) && Global.validatename(dropuserlong)) {
                if (Global.validatename(pickupuserlong) && Global.validatename(pickupuserlat)) {
                    Intent intent = new Intent(getActivity(), DriverFoodInProgressActivity.class);
                    intent.putExtra("bookingId", OrderDetail.orderId);
                    intent.putExtra("address", dropAddress);
                    intent.putExtra("landMark", lanMark);
                    intent.putExtra("name", name);
                    intent.putExtra("number", number);
                    intent.putExtra("dropuserlong", dropuserlong);
                    intent.putExtra("dropuserlat", dropuserlat);
                    intent.putExtra("pickupuserlong", pickupuserlong);
                    intent.putExtra("pickupuserlat", pickupuserlat);
                    intent.putExtra("pickUpaddress", resturentddress);
                    intent.putExtra("pickUplandMark", "");
                    startActivity(intent);

                } else
                    Toast.makeText(getContext(), "Restaurent location is not valid", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(getContext(), "User  location is not valid", Toast.LENGTH_LONG).show();
        } else if (msgResponce.getTotalCartItem().equalsIgnoreCase("2")) {
            if (OrderDetail.status.equalsIgnoreCase("10")) {
                OrderDetail.status = "1";
                btn_foodPickUp.setText("Confirm food pick up");
            } else {
                btn_foodPickUp.setText("On the Delivery");
                btn_foodPickUp.setBackgroundColor(getResources().getColor(R.color.green));
            }

        } else Global.msgDialogBack(getActivity(), msgResponce.getMsg());
    }

    public class CustomDialogClass extends Dialog {

        public Activity c;
        public Dialog d;
        String requstId;
        String oederId;
        LinearLayout layout_del;
        TextView tv_accept, tv_totalAmount, tv_reject, tv_dropLocation, tv_picupLocation, tv_deliveryFee, tv_tipAmount, tv_time, tv_miles;
        String s_dropLocation, s_picupLocation, s_deliveryFee, s_tipAmount, s_time, s_miles, totalAmount;

        public CustomDialogClass(Activity a, String requstId, String oederId,
                                 String s_dropLocation, String s_picupLocation, String s_deliveryFee, String s_tipAmount, String s_time, String s_miles, String totalAmount) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
            this.oederId = oederId;
            this.requstId = requstId;
            this.s_dropLocation = s_dropLocation;
            this.s_picupLocation = s_picupLocation;
            this.s_deliveryFee = s_deliveryFee;
            this.s_tipAmount = s_tipAmount;
            this.s_time = s_time;
            this.s_miles = s_miles;
            this.totalAmount = totalAmount;
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

            tv_miles.setText(miles);
            tv_time.setText(estTime);
            tv_tipAmount.setText("$ " + s_tipAmount);
            tv_totalAmount.setText("$ " + totalAmount);
            tv_dropLocation.setText(dropAddress);
            tv_picupLocation.setText(resturentName);
            tv_deliveryFee.setText("$ " + deliveryFee);
            if (Global.validatename(deliveryFee)) {
                layout_del.setVisibility(View.VISIBLE);
            } else layout_del.setVisibility(View.GONE);

            tv_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.executeUpdateOrder(SessionManager.get_user_id(prefes), requstId,
                            oederId, "2", MultipleMap.this);
                    dismiss();
                }
            });
            tv_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.executeUpdateOrder(SessionManager.get_user_id(prefes), requstId,
                            oederId, "3", MultipleMap.this);
                    dismiss();
                }
            });
        }


    }

    private void selectImage() {

        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
//                Glide.with(getContext()).load(selectedImagePath).error(R.drawable.image).dontAnimate().into(img_CoverImage);
            } else if (requestCode == SELECT_FILE) {
                Bitmap bm = null;
                if (data != null) {
                    try {
                        Uri selectedImageUri = data.getData();
                        selectedImagePath = ImageFilePath.getPath(getContext(), selectedImageUri);
//                        Glide.with(getContext()).load(selectedImagePath).error(R.drawable.image).dontAnimate().into(img_CoverImage);
//viewModel.executeImgeUpload();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
