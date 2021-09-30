package com.evs.foodexp.driverPkg.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.common.fragment.EditProfileFragment;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.stripe.Stripe;
import com.evs.foodexp.stripe.TokenCallback;
import com.evs.foodexp.stripe.dialog.ErrorDialogFragment;
import com.evs.foodexp.stripe.model.Card;
import com.evs.foodexp.stripe.model.Token;
import com.evs.foodexp.userPkg.activity.PaymentActivity;
import com.evs.foodexp.userPkg.fragment.OrderHistoryFoodDeliveryFragment;
import com.evs.foodexp.userPkg.fragment.OrderHistoryFragment;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
import com.stripe.model.Charge;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class DriverHomeFragment extends Fragment implements AuthMsgListener {

    Fragment fragment = null;
    TextView spotSchedulingTxt, tv_address, tv_name;
    ImageView editProfileImg, img_userProfile, img_history;
    SharedPreferences prefs;
    APIViewModel apiViewModel;
    LoaderProgress progress;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_home_layout, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("Dashboard");

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        spotSchedulingTxt = (TextView) view.findViewById(R.id.spotScheduling);
        editProfileImg = (ImageView) view.findViewById(R.id.editProfileImgDriver);
        img_userProfile = (ImageView) view.findViewById(R.id.img_userProfile);
        img_history = (ImageView) view.findViewById(R.id.img_history);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        progress=new LoaderProgress(getContext());
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        spotSchedulingTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new SpotScduleFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_driver_home, fragment)
                        .addToBackStack(null).commit();
            }
        });

        editProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_driver_home, new EditProfileFragment())
                        .addToBackStack(null).commit();
            }
        });
        img_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_driver_home, new OrderHistoryFoodDeliveryFragment())
                        .addToBackStack(null).commit();

            }
        });


        Glide.with(getActivity()).load(SessionManager.get_image(prefs)).placeholder(R.drawable.driver_img).into(img_userProfile);
        tv_name.setText(SessionManager.get_name(prefs));
        tv_address.setText(SessionManager.get_address(prefs));
        if (!Global.validatename(SessionManager.get_address(prefs))) {
            tv_address.setVisibility(View.GONE);
        } else tv_address.setVisibility(View.VISIBLE);
//        paynow("4242424242424242 ",4,23,"424",1000);

        return view;


    }

    @Override
    public void onResume() {
        super.onResume();
        if (SessionManager.get_StripeStatus(prefs).equalsIgnoreCase("0")) {
            apiViewModel.checkStripAccount(SessionManager.get_user_id(prefs),SessionManager.get_StripeAccountNo(prefs),DriverHomeFragment.this);
        }
    }

    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialogBack(getActivity(),message);
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progress.dismiss();
        if(msgResponce.getStatus().equalsIgnoreCase("success")){
            if(msgResponce.getMsg().equalsIgnoreCase("0")) {
                CreateStripeAccount stDailog = new CreateStripeAccount(getActivity(), msgResponce.getTotalCartItem());
                stDailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                stDailog.show();
            }else SessionManager.save_StripeStatus(prefs,msgResponce.getMsg());
        }else Global.msgDialogBack(getActivity(),msgResponce.getMsg());

    }


    public class CreateStripeAccount extends Dialog {

        public Activity c;
        public Dialog d;
        String url;


        public CreateStripeAccount(Activity a,String url) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
            this.url = url;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.create_custom_dailog);
            Button btn_create = (Button) findViewById(R.id.btn_create);
            btn_create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    dismiss();
                }
            });
        }

    }

    public void paynow(String cardval, int monthval, int yearval, String cvcval, final double amount) {

        Card card = new Card(cardval, monthval, yearval, cvcval);
        card.setCurrency("USD");
        boolean validation = card.validateCard();
        progress.show();
        if (validation) {

            new Stripe().createToken(card, Global.PUBLISHABLE_KEY, new TokenCallback() {
                public void onSuccess(Token token) {
                    progress.dismiss();
                    try {
                        com.stripe.Stripe.apiKey = Global.SECRET_KEY;
                        String TokenIds = token.getId();
                        Map<String, Object> chargeParams = new HashMap<String, Object>();


                        Double usd = Double.parseDouble(new DecimalFormat("##.##")
                                .format(amount));

//                        chargeParams.put("amount", usd);
                            chargeParams.put("amount", (int) Math.round(usd * 100));
                            chargeParams.put("currency", "USD");
                            chargeParams.put("source", token.getId());
                            Map<String, String> initialMetadata = new HashMap<String, String>();
                            initialMetadata.put("order_id", "" + TokenIds);
                            chargeParams.put("metadata", initialMetadata);
                            Map<String, Object> transferDataParams = new HashMap<>();
                            transferDataParams.put("amount", 999);
                            transferDataParams.put("destination", "acct_1IJbzZHtOoK8hcOb");
                            chargeParams.put("transfer_data", transferDataParams);
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        try {
                            Charge.create(chargeParams);
                            progress.dismiss();

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.print("" + e.getMessage());
                            progress.dismiss();
                            Global.msgDialog(getActivity(), e.getMessage());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progress.dismiss();
                    }
                }

                public void onError(Exception error) {
                    handleError(error.getLocalizedMessage());
                }

            });
        } else if (!card.validateNumber()) {
            handleError("The card number that you entered is invalid");
        } else if (!card.validateExpiryDate()) {
            handleError("The expiration date that you entered is invalid");
        } else if (!card.validateCVC()) {
            handleError("The CVC code that you entered is invalid");
        } else {
            handleError("The card details that you entered are invalid");
        }
    }

    private void handleError(String error) {
        DialogFragment fragment = ErrorDialogFragment.newInstance(R.string.validationErrors, error);
        fragment.show(getChildFragmentManager(), "error");
        progress.dismiss();
    }

}
