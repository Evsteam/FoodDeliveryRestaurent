package com.evs.foodexp.driverPkg.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import  androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.common.activity.TrackInProgress;
import com.evs.foodexp.commonPkg.common.activity.TrackOrder1;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.commonPkg.viewModel.RestorentViewModel;
import com.evs.foodexp.models.GoGetModel;
import com.evs.foodexp.repositry.DataResponse;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.userPkg.activity.MorePayment;
import com.evs.foodexp.userPkg.activity.PaymentToGetActivity;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DecimalFormat;

public class DriverJobToGetDetailActivity extends AppCompatActivity implements
        AuthListener<DataResponse<GoGetModel>>, AuthMsgListener {
    RoundedImageView img_profileImage, img_msg;
    TextView tv_product, tv_whatStore, requestMoney, tv_transation,transation,
            tv_requestMoney, tv_sateTax, tv_tip, tv_estimatePrice, tv_DevilryFee, tv_Notes, tv_title, tv_address;
    Button btn_totalAmount, btn_paymentStatus, btn_sendMoney, btn_requestForMoney, btn_markDeliverd, btn_deliverd;
    String togetrequestId, ePrice = "", contactNumbe = "", driverId = "", requestAmount = "0", requestStauts = "0";
    RestorentViewModel viewModel;
    LoaderProgress progress;
    SharedPreferences prefe;
    RelativeLayout devider, layout_rating;
    RatingBar ratingBar;
    ImageView img_location;
    LinearLayout layoutButton;
    String paymentStatus = "0", totalAmount = "", tip = "0",AdminAmount,driverStripeAccount;
    String lats, longs, name, driverName, address, number, amount,imageUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_job_detail);

        Utills.StatusBarColour(DriverJobToGetDetailActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("To get details ");
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
            togetrequestId = bundle.getString("togetrequestId");
        }
        prefe = PreferenceManager.getDefaultSharedPreferences(DriverJobToGetDetailActivity.this);

        tv_product = findViewById(R.id.tv_product);
        tv_transation = findViewById(R.id.tv_transation);
        transation = findViewById(R.id.transation);
        devider = findViewById(R.id.devider);
        tv_requestMoney = findViewById(R.id.tv_requestMoney);
        requestMoney = findViewById(R.id.requestMoney);
        tv_sateTax = findViewById(R.id.tv_sateTax);
        layoutButton = findViewById(R.id.layoutButton);
        btn_sendMoney = findViewById(R.id.btn_sendMoney);
        ratingBar = findViewById(R.id.ratingBar);
        img_location = findViewById(R.id.img_location);
        layout_rating = findViewById(R.id.layout_rating);
        layout_rating = findViewById(R.id.layout_rating);
        img_msg = findViewById(R.id.img_msg);
        btn_deliverd = findViewById(R.id.btn_deliverd);
        tv_address = findViewById(R.id.tv_address);
        btn_markDeliverd = findViewById(R.id.btn_markDeliverd);
        btn_paymentStatus = findViewById(R.id.btn_paymentStatus);
        tv_title = findViewById(R.id.tv_title);
        tv_whatStore = findViewById(R.id.tv_whatStore);
        tv_tip = findViewById(R.id.tv_tip);
        tv_estimatePrice = findViewById(R.id.tv_estimatePrice);
        tv_DevilryFee = findViewById(R.id.tv_DevilryFee);
        tv_Notes = findViewById(R.id.tv_Notes);
        btn_totalAmount = findViewById(R.id.btn_totalAmount);
        btn_requestForMoney = findViewById(R.id.btn_requestForMoney);
        img_profileImage = findViewById(R.id.img_profileImage);
        btn_markDeliverd.setVisibility(View.VISIBLE);
        btn_requestForMoney.setVisibility(View.VISIBLE);
        viewModel = new ViewModelProvider(DriverJobToGetDetailActivity.this).get(RestorentViewModel.class);
        progress = new LoaderProgress(DriverJobToGetDetailActivity.this);

        transation.setVisibility(View.VISIBLE);
        tv_transation.setVisibility(View.VISIBLE);

        btn_requestForMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!requestStauts.equalsIgnoreCase("2")) {
                    morePrice(ePrice);
                } else Global.msgDialog(DriverJobToGetDetailActivity.this, "Already Requested !!");

            }
        });
        btn_markDeliverd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!btn_markDeliverd.getText().toString().equalsIgnoreCase("Delivered")) {
                    Intent intent = new Intent(DriverJobToGetDetailActivity.this, TrackInProgress.class);
                    intent.putExtra("bookingId", togetrequestId);
                    intent.putExtra("address", address);
                    intent.putExtra("name", name);
                    intent.putExtra("number", number);
                    intent.putExtra("dropuserlat", lats);
                    intent.putExtra("dropuserlong", longs);
                    intent.putExtra("type", "Togetrequests");
                    intent.putExtra("uploadedImage", imageUpload);
                    startActivity(intent);
//                    viewModel.excuteTogetStatus(togetrequestId, SessionManager.get_user_id(prefe),"2",DriverJobToGetDetailActivity.this);
                }
            }
        });
        img_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Global.validatename(contactNumbe)) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + contactNumbe));
                    startActivity(callIntent);
                } else
                    Toast.makeText(DriverJobToGetDetailActivity.this, "Contact number is not Available!!", Toast.LENGTH_LONG).show();
            }
        });
        layout_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Global.validatename(driverId)) {
                    Submitreview cdd = new Submitreview(DriverJobToGetDetailActivity.this, driverId);
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cdd.show();
                }
            }
        });
        btn_sendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverJobToGetDetailActivity.this, MorePayment.class);
                intent.putExtra("togetRequestId", togetrequestId);
                intent.putExtra("amount", requestAmount);
                startActivity(intent);
                finish();
            }
        });
        btn_paymentStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SessionManager.get_userType(prefe).equalsIgnoreCase(Global.type_person)) {
                    if (paymentStatus.equalsIgnoreCase("1")) {
                        Intent intent = new Intent(DriverJobToGetDetailActivity.this, PaymentToGetActivity.class);
                        intent.putExtra("togetrequestsId", togetrequestId);
                        intent.putExtra("totalAmount", totalAmount);
                        intent.putExtra("TIP", tip);
                        intent.putExtra("AdminAmount", AdminAmount);
                        intent.putExtra("driverStripeAccount",  driverStripeAccount);
                        startActivity(intent);
                    }
                }
            }
        });

        btn_deliverd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SessionManager.get_userType(prefe).equalsIgnoreCase(Global.type_person)) {
                    if (!btn_deliverd.getText().toString().equalsIgnoreCase("Delivered")) {
                        Intent intent=new Intent(DriverJobToGetDetailActivity.this, TrackOrder1.class);
                        intent.putExtra("bookingId",togetrequestId);
                        intent.putExtra("driverId",driverId);
                        intent.putExtra("amount",amount);
                        intent.putExtra("driverName",driverName);
                        intent.putExtra("dropLat",lats);
                        intent.putExtra("dropLongs",longs);
                        intent.putExtra("dropAddress",address);
                        intent.putExtra("uploadedImage", imageUpload);
                        startActivity(intent);
                    }
                }


            }
        });


    }

    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onSuccess(LiveData<DataResponse<GoGetModel>> data) {
        progress.dismiss();
        if (data != null) {

            lats = data.getValue().getData().getLatidude();
            longs = data.getValue().getData().getLongitude();
            address = data.getValue().getData().getAddress();
            name = data.getValue().getData().getUserName();
            number = data.getValue().getData().getUserPhone();
            driverId = data.getValue().getData().getVendorId();
            driverName = data.getValue().getData().getVendorName();
            amount = data.getValue().getData().getTotalAmount();
            imageUpload = data.getValue().getData().getImageUpload();
            AdminAmount=data.getValue().getData().getAdminAmount();
            driverStripeAccount=data.getValue().getData().getDriverStripeAccount();
            paymentStatus = data.getValue().getData().getPaymentStatus();
            totalAmount=String.valueOf(Double.parseDouble(data.getValue().getData().getTotalAmount())+
                    Double.parseDouble(data.getValue().getData().getTransactionFee()));
//            totalAmount = data.getValue().getData().getTotalAmount();
            tip = data.getValue().getData().getTIP();
            tv_address.setText(data.getValue().getData().getAddress());
            tv_title.setText(data.getValue().getData().getUserName());
            tv_product.setText(data.getValue().getData().getWahtUwant());
            tv_whatStore.setText(data.getValue().getData().getStoreCity());
            tv_tip.setText("$ " + data.getValue().getData().getTIP());
            tv_transation.setText("$ " + new DecimalFormat("##.##").format(Double.parseDouble(data.getValue().getData().getTransactionFee())));
            tv_estimatePrice.setText("$ " + data.getValue().getData().getEPrice());
            tv_DevilryFee.setText("$ " + data.getValue().getData().getDeliveryFee());
            tv_Notes.setText(data.getValue().getData().getNotes());
            tv_sateTax.setText("$ " + data.getValue().getData().getSalesTax());
            btn_totalAmount.setText("Total Amount $ " +  new DecimalFormat("##.##").format(Double.parseDouble(totalAmount )));
            ePrice = data.getValue().getData().getEPrice();
            driverId = data.getValue().getData().getDriverId();
            requestStauts = data.getValue().getData().getRequestStatus();

            Glide.with(DriverJobToGetDetailActivity.this)
                    .load(data.getValue().getData().getUserImage())
                    .placeholder(R.drawable.placeholder_user)
                    .into(img_profileImage);
            if (!data.getValue().getData().getStatus().equalsIgnoreCase("1")) {
                btn_deliverd.setVisibility(View.VISIBLE);
                btn_deliverd.setText("Delivered");
                btn_markDeliverd.setVisibility(View.GONE);
                btn_requestForMoney.setVisibility(View.GONE);
                btn_markDeliverd.setText("Delivered");
            }

            if (SessionManager.get_userType(prefe).equalsIgnoreCase(Global.type_driver)) {
                tv_title.setText(data.getValue().getData().getUserName());
                tv_address.setText(data.getValue().getData().getAddress());
                Glide.with(DriverJobToGetDetailActivity.this).load(data.getValue().getData().getUserImage())
                        .placeholder(R.drawable.placeholder_user).into(img_profileImage);
                contactNumbe = data.getValue().getData().getPhone();
            } else {
                if (Global.validatename(data.getValue().getData().getVendorName())) {
                    tv_title.setText(data.getValue().getData().getVendorName());
                    tv_address.setText(data.getValue().getData().getVendorPhone());
                    Glide.with(DriverJobToGetDetailActivity.this).load(data.getValue().getData().getVendorImage())
                            .placeholder(R.drawable.placeholder_user).into(img_profileImage);
                    contactNumbe = data.getValue().getData().getVendorPhone();
                    img_location.setVisibility(View.GONE);
                } else {
                    tv_title.setText(data.getValue().getData().getUserName());
                    tv_address.setText(data.getValue().getData().getAddress());
                    Glide.with(DriverJobToGetDetailActivity.this).load(data.getValue().getData().getUserImage())
                            .placeholder(R.drawable.placeholder_user).into(img_profileImage);
                    contactNumbe = data.getValue().getData().getPhone();
                }

            }

            if (Global.validatename(data.getValue().getData().getVendorAVGRating())) {
                ratingBar.setRating(Float.parseFloat(data.getValue().getData().getVendorAVGRating()));
            } else ratingBar.setRating(0);

            if (SessionManager.get_userType(prefe).equalsIgnoreCase(Global.type_person)) {
                layoutButton.setVisibility(View.GONE);
                if (data.getValue().getData().getStatus().equalsIgnoreCase("2")) {
                    layout_rating.setVisibility(View.VISIBLE);
                }
                if (data.getValue().getData().getRequestStatus().equalsIgnoreCase("1")) {
                    btn_sendMoney.setVisibility(View.VISIBLE);
                    requestAmount = data.getValue().getData().getRequestMoney();
                } else btn_sendMoney.setVisibility(View.GONE);

                switch (paymentStatus) {
                    case "0":
                        btn_paymentStatus.setText("Waiting for Driver!!");
                        btn_paymentStatus.setVisibility(View.VISIBLE);
                        break;
                    case "1":
                        btn_paymentStatus.setText(getResources().getString(R.string.confirm_pay));
                        btn_paymentStatus.setVisibility(View.VISIBLE);
                        break;
                    case "2":
                        btn_paymentStatus.setVisibility(View.GONE);
                        btn_deliverd.setVisibility(View.VISIBLE);
                        btn_deliverd.setText("Track Your Order!!");
                        break;
                }
            } else {
                if (paymentStatus.equalsIgnoreCase("1")) {
                    btn_paymentStatus.setText("Waiting For Payment!!!");
                    btn_paymentStatus.setVisibility(View.VISIBLE);
                    layoutButton.setVisibility(View.GONE);
                    btn_deliverd.setVisibility(View.GONE);
                } else if (paymentStatus.equalsIgnoreCase("2")) {
                    layoutButton.setVisibility(View.VISIBLE);
                    btn_deliverd.setVisibility(View.GONE);
                }
                if (data.getValue().getData().getRequestStatus().equalsIgnoreCase("1")) {
                    layoutButton.setVisibility(View.GONE);
                }

            }
            if (Global.validatename(data.getValue().getData().getRequestMoney())) {
                if (Double.parseDouble(data.getValue().getData().getRequestMoney()) > 0) {
                    devider.setVisibility(View.VISIBLE);
                    requestMoney.setVisibility(View.VISIBLE);
                    tv_requestMoney.setVisibility(View.VISIBLE);
                    tv_requestMoney.setText("$ " + data.getValue().getData().getRequestMoney());
//                    Double toalaAmount=Double.parseDouble(data.getValue().getData().getTotalAmount())+Double.parseDouble(data.getValue().getData().getRequestMoney());
//                    btn_totalAmount.setText("Total Amount $ " + toalaAmount);
                }
            }

        }

    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialogBack(DriverJobToGetDetailActivity.this, message);
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progress.dismiss();
        Global.msgDialogBack(DriverJobToGetDetailActivity.this, msgResponce.getMsg());
    }

    private void morePrice(String ePrice) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DriverJobToGetDetailActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.request_more_money_alert, null);
        TextView tv_estimatePrice = dialogView.findViewById(R.id.tv_estimatePrice);
        Button btn_send = dialogView.findViewById(R.id.btn_send);
        final EditText et_price = dialogView.findViewById(R.id.et_price);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        tv_estimatePrice.setText("Estimate Price: $ " + ePrice);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Global.validatename(et_price.getText().toString())) {
                    viewModel.excuteTogetupdatePayment(togetrequestId, SessionManager.get_user_id(prefe), et_price.getText().toString(), DriverJobToGetDetailActivity.this);
                } else
                    Toast.makeText(DriverJobToGetDetailActivity.this, "Please Enter Amount!!", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getApplicationContext(),UploadImageDeliveryActivity.class);
//                startActivity(intent);
            }
        });

        alertDialog.show();
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
                    viewModel.reviewRating(SessionManager.get_user_id(prefe), id, String.valueOf(ratingBar.getRating()), et_message.getText().toString(), this);
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
                Global.msgDialog(DriverJobToGetDetailActivity.this, data.getValue().getMsg());
            }

        }

        @Override
        public void onFailure(String message) {
            progress.dismiss();
            Global.msgDialog(DriverJobToGetDetailActivity.this, message);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.excuteTogetDetails(togetrequestId, this);
    }
}
