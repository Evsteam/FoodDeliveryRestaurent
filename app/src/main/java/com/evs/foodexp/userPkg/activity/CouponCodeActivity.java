package com.evs.foodexp.userPkg.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import  androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.DTO.CouponCodeRequest;
import com.evs.foodexp.commonPkg.common.adapter.ItemAdpter;

import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.models.ItemModel;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CouponCodeActivity extends AppCompatActivity implements View.OnClickListener, AuthListener<MsgResponse> {

    Button orderPlaceBtn, applyCouponCodeBtn;
    EditText couponCodeEdit, other_amount;
    APIViewModel apiViewModel;
    String couponCodeStr, specialNotes = "", amount = "", noConctact;
    CouponCodeRequest codeRequest;
    SharedPreferences preferences;
    double tipAmount = 0.0, transactionfee = 0, esttotalAmount, discount = 0;
    TextView callDriver, tv_tipAmount, tv_totalAmount,tv_transactionfee;
    RadioGroup tip_group;
    LoaderProgress progress;
    String itemList;
    RecyclerView recylerView;
    LinearLayoutManager layoutManager;
    double coupenPercent = 0.0;
    DecimalFormat formater = new DecimalFormat("#.##");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_coupon_code_payment);
        Utills.StatusBarColour(CouponCodeActivity.this);
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(CouponCodeActivity.this);
        codeRequest = new CouponCodeRequest();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCartCouponCode);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        recylerView = findViewById(R.id.listView);
        couponCodeEdit = (EditText) findViewById(R.id.couponCode);
        tv_transactionfee = (TextView) findViewById(R.id.tv_transactionfee);
        applyCouponCodeBtn = (Button) findViewById(R.id.applyCouponCodeBtn);
        callDriver = (TextView) findViewById(R.id.callDriver);
        tv_tipAmount = (TextView) findViewById(R.id.tv_tipAmount);
        progress = new LoaderProgress(CouponCodeActivity.this);
        toolbarTitle.setText("CART");
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
        orderPlaceBtn = (Button) findViewById(R.id.placeOrderByCouponCodeBtn);
        applyCouponCodeBtn = (Button) findViewById(R.id.applyCouponCodeBtn);
        tip_group = (RadioGroup) findViewById(R.id.tip_group);
        other_amount = (EditText) findViewById(R.id.other_amount);
        tv_totalAmount = (TextView) findViewById(R.id.tv_totalAmount);
        orderPlaceBtn.setOnClickListener(this);
        applyCouponCodeBtn.setOnClickListener(this);

        layoutManager = new LinearLayoutManager(CouponCodeActivity.this);
        recylerView.setLayoutManager(layoutManager);
        recylerView.setItemAnimator(new DefaultItemAnimator());
        recylerView.setLayoutManager(layoutManager);
        recylerView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(CouponCodeActivity.this,
                new LinearLayoutManager(CouponCodeActivity.this).getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(CouponCodeActivity.this, R.drawable.recycler_devider));
        recylerView.addItemDecoration(dividerItemDecoration);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            amount = bundle.getString("totalAmount");
            itemList = bundle.getString("itemList");
            specialNotes = bundle.getString("specialNotes");
            noConctact = bundle.getString("noConctact");
        }

        try {
            ArrayList<ItemModel> list = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(itemList);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ItemModel model = new ItemModel();
                model.setId(jsonObject.optString("id"));
                model.setPrize(jsonObject.optString("price"));
                model.setName(jsonObject.optString("name"));
                model.setQuintity(jsonObject.optString("quantity"));
                list.add(model);

            }
            recylerView.setAdapter(new ItemAdpter(CouponCodeActivity.this, list));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        esttotalAmount = Double.parseDouble(amount) + 6;
        transactionfee =esttotalAmount * 5.8 / 100;
        transactionfee=transactionfee+0.60;
        tv_transactionfee.setText("$ " +formater.format(transactionfee));

        tv_totalAmount.setText("$ " + formater.format(esttotalAmount+transactionfee));
        callDriver.setText("$ " + formater.format(Double.parseDouble(amount)));


        tv_tipAmount.setText("$ 0");
        tip_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_five:
                        other_amount.setText("");
                        tipAmount = 5;
//                        tipAmount = Double.parseDouble(amount) * 5 / 100;
                        other_amount.setVisibility(View.GONE);
                        break;
                    case R.id.rb_ten:
                        other_amount.setText("");
                        tipAmount = 10;
//                        tipAmount = Double.parseDouble(amount) * 10 / 100;
                        other_amount.setVisibility(View.GONE);
                        break;
                    case R.id.rb_fifteen:
                        other_amount.setText("");
                        tipAmount = 15;
//                        tipAmount = Double.parseDouble(amount) * 15 / 100;
                        other_amount.setVisibility(View.GONE);
                        break;
                    case R.id.rb_twenty:
                        other_amount.setText("");
//                        tipAmount = Double.parseDouble(amount) * 20/ 100;
                        tipAmount = 20;
                        other_amount.setVisibility(View.GONE);
                        break;
                    case R.id.rb_other:
                        other_amount.setVisibility(View.VISIBLE);
                        tipAmount = 0;
                        break;

                }
                esttotalAmount = Double.parseDouble(amount) + tipAmount+6;
                esttotalAmount = esttotalAmount - discount;
                tv_tipAmount.setText("$ " + formater.format(tipAmount));
                transactionfee =esttotalAmount * 5.8 / 100;
                transactionfee=transactionfee+0.60;
                tv_transactionfee.setText("$ " +formater.format(transactionfee));
                tv_totalAmount.setText("$ " + formater.format(esttotalAmount+transactionfee));
            }
        });

        TextWatcher mDateEntryWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
//                    tipAmount = Double.parseDouble(amount) * Integer.parseInt(String.valueOf(s))/ 100;
                    tipAmount = Integer.parseInt(String.valueOf(s));
                    esttotalAmount = Double.parseDouble(amount) + tipAmount+6;
                    esttotalAmount = esttotalAmount - discount;
                    tv_tipAmount.setText("$ " + formater.format(tipAmount));


                    transactionfee =esttotalAmount * 5.8 / 100;
                    transactionfee=transactionfee+0.60;
                    tv_transactionfee.setText("$ " +formater.format(transactionfee));
                    tv_totalAmount.setText("$ " + formater.format(esttotalAmount+transactionfee));
                } else {
                    tipAmount = 0;
                    esttotalAmount = Double.parseDouble(amount) + tipAmount+6;
                    esttotalAmount = esttotalAmount - discount;

                    tv_tipAmount.setText("$ " +formater.format( tipAmount));

                    transactionfee =esttotalAmount * 5.8 / 100;
                    transactionfee=transactionfee+0.60;
                    tv_transactionfee.setText("$ " +formater.format(transactionfee));
                    tv_totalAmount.setText("$ " + formater.format(esttotalAmount+transactionfee));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

        };
        other_amount.addTextChangedListener(mDateEntryWatcher);


        if (!Global.GpsEnable(CouponCodeActivity.this)) {
            Global.showGPSDisabledAlertToUser(CouponCodeActivity.this);
        }

    }

    @Override
    public void onClick(View v) {
        if (v == orderPlaceBtn) {
            if (!Global.GpsEnable(CouponCodeActivity.this)) {
                Global.showGPSDisabledAlertToUser(CouponCodeActivity.this);
            } else if (tipAmount > 0) {
                Intent intent = new Intent(CouponCodeActivity.this, UserAddressActivity.class);
                intent.putExtra("totalAmount", "" + esttotalAmount);
                intent.putExtra("itemList", "" + itemList);
                intent.putExtra("tipAmount", "" + tipAmount);
                intent.putExtra("specialNotes", specialNotes);
                intent.putExtra("discount", "" + discount);
                intent.putExtra("couponCode", couponCodeEdit.getText().toString());
                intent.putExtra("noConctact", noConctact);
                intent.putExtra("transactionfee", ""+transactionfee);
                startActivity(intent);
            } else
                Toast.makeText(CouponCodeActivity.this, "Please select Tip Amount", Toast.LENGTH_LONG).show();

        } else if (v == applyCouponCodeBtn) {
            couponCodeStr = couponCodeEdit.getText().toString().trim();
            if(Global.validatename(couponCodeStr)){
                codeRequest.setAction("checkcoupon");
                codeRequest.setCode(couponCodeStr);
                codeRequest.setUserId(SessionManager.get_user_id(preferences));
                apiViewModel.couponCode(codeRequest, this);
            }else couponCodeEdit.setError("Enter valid Coupon Code!!!");

        }


    }

    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onSuccess(LiveData<MsgResponse> data) {
        progress.dismiss();
        if (data.getValue().getStatus().equalsIgnoreCase("success")) {

        } else if (data.getValue().getStatus().equalsIgnoreCase("codeFaild")) {
            Global.msgDialog(CouponCodeActivity.this, data.getValue().getMsg());
        } else if (data.getValue().getStatus().equalsIgnoreCase("codeApplyed")) {
            applyCouponCodeBtn.setText("Applied");
            applyCouponCodeBtn.setEnabled(false);
            couponCodeEdit.setFocusable(false);
            coupenPercent = Double.parseDouble(data.getValue().getTotalCartItem());
            discount = Double.parseDouble(amount) * coupenPercent / 100;
            esttotalAmount = Double.parseDouble(amount) - discount;
            callDriver.setText("$ " + esttotalAmount);
            Log.e("dis", amount + "  " + discount);
            esttotalAmount = esttotalAmount + tipAmount+6;

            transactionfee =esttotalAmount * 5.8 / 100;
            transactionfee=transactionfee+0.60;
            tv_transactionfee.setText("$ " +formater.format(transactionfee));
            tv_totalAmount.setText("$ " + formater.format(esttotalAmount+transactionfee));
        }
    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialog(CouponCodeActivity.this, message);
    }


}
