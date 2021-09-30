package com.evs.foodexp.restaurentPkg.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import  androidx.lifecycle.ViewModelProvider;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.common.activity.PaymentInfoActivity;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.commonPkg.viewModel.RestorentViewModel;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;

public class CashoutActivity extends AppCompatActivity implements AuthMsgListener {
    TextView changeBank, account_number, bank_name, wallet_balance, withdraw_balance;
    EditText edttxt_amount;
    Button btn_submit;
    RestorentViewModel viewModel;
    LoaderProgress progress;
    SharedPreferences prefs;
    Double earningAmount = 0.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cash_out);
        Utills.StatusBarColour(CashoutActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("CASHOUT");
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


        prefs = PreferenceManager.getDefaultSharedPreferences(CashoutActivity.this);
        progress = new LoaderProgress(CashoutActivity.this);
        viewModel = new ViewModelProvider(CashoutActivity.this).get(RestorentViewModel.class);


        changeBank = findViewById(R.id.changeBank);
        withdraw_balance = findViewById(R.id.withdraw_balance);
        wallet_balance = findViewById(R.id.wallet_balance);
        account_number = findViewById(R.id.account_number);
        bank_name = findViewById(R.id.bank_name);
        edttxt_amount = findViewById(R.id.edttxt_amount);
        btn_submit = findViewById(R.id.btn_submit);
        if (Global.validatename(SessionManager.get_AccountNo(prefs))) {
            String nmber = SessionManager.get_AccountNo(prefs).replaceAll("\\w(?=\\w{4})", "*");
            account_number.setText(nmber);
            bank_name.setText(SessionManager.get_BankName(prefs));
        }else {
            Toast.makeText(CashoutActivity.this,"Your Bank Infromation not Updated please update",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(CashoutActivity.this, PaymentInfoActivity.class);
            startActivity(intent);
        }


        changeBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CashoutActivity.this, PaymentInfoActivity.class);
                startActivity(intent);
            }
        });


        if (Global.isOnline(CashoutActivity.this)) {
            viewModel.excutewallet(SessionManager.get_user_id(prefs), this);
        } else Global.showDialog(CashoutActivity.this);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Global.isOnline(CashoutActivity.this)) {
                    try {
                        if (Double.parseDouble(SessionManager.get_wallet(prefs)) > 0) {
                            if (edttxt_amount.getText().toString().length() > 0) {
                                if (Double.parseDouble(edttxt_amount.getText().toString()) > 0) {
                                    if (Double.parseDouble(SessionManager.get_wallet(prefs)) >= Double.parseDouble(edttxt_amount.getText().toString())) {
                                        viewModel.excutecahout(SessionManager.get_user_id(prefs),
                                                edttxt_amount.getText().toString(), CashoutActivity.this);
                                    } else
                                        Toast.makeText(CashoutActivity.this, "Please Enter Amount Low", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(CashoutActivity.this, "Please Enter Greater Then  0", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(CashoutActivity.this, "Please Enter Amount ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CashoutActivity.this, "Wallet Balance Low", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(CashoutActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }

        });
    }


    void setProfileInfo() {
        if (SessionManager.get_wallet(prefs).equals("")) {
            wallet_balance.setText("$ 0");
            withdraw_balance.setText("Withdrawable Balance: $ 0");
        } else {
            wallet_balance.setText("$" + String.format("%.2f", Double.parseDouble(SessionManager.get_wallet(prefs))));
            withdraw_balance.setText("Withdrawable Balance: $" + String.format("%.2f", Double.parseDouble(SessionManager.get_wallet(prefs))));
        }
//        bank_name.setText(SessionManager.get_name(prefs));
//        account_number.setText(SessionManager.get_mobile(prefs));
    }


    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialogBack(CashoutActivity.this, message);
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progress.dismiss();
        if (msgResponce.getStatus().equalsIgnoreCase("wallet")) {
            earningAmount = Double.parseDouble(msgResponce.getTotalCartItem());
            SessionManager.save_wallet(prefs, msgResponce.getTotalCartItem());
            setProfileInfo();
        } else Global.msgDialogBack(CashoutActivity.this, msgResponce.getMsg());
    }
}
