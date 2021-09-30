package com.evs.foodexp.driverPkg.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import  androidx.lifecycle.ViewModelProvider;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.common.activity.PaymentInfoActivity;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.commonPkg.viewModel.RestorentViewModel;
import com.evs.foodexp.driverPkg.activity.CashoutHistoryDriverActivity;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;

public class DriverCashoutRequestFragment extends Fragment implements AuthMsgListener {
    TextView changeBank, account_number, bank_name, wallet_balance, withdraw_balance;
    EditText edttxt_amount;
    Button btn_submit;
    RestorentViewModel viewModel;
    LoaderProgress progress;
    SharedPreferences prefs;
    Double earningAmount = 0.0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.cash_out, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("Cashout");

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        progress = new LoaderProgress(getActivity());
        viewModel = new ViewModelProvider(getActivity()).get(RestorentViewModel.class);


        changeBank = view.findViewById(R.id.changeBank);
        withdraw_balance = view.findViewById(R.id.withdraw_balance);
        wallet_balance = view.findViewById(R.id.wallet_balance);
        account_number = view.findViewById(R.id.account_number);
        bank_name = view.findViewById(R.id.bank_name);
        edttxt_amount = view.findViewById(R.id.edttxt_amount);
        btn_submit = view.findViewById(R.id.btn_submit);
        if (Global.validatename(SessionManager.get_AccountNo(prefs))) {
            String nmber = SessionManager.get_AccountNo(prefs).replaceAll("\\w(?=\\w{4})", "*");
            account_number.setText(nmber);
            bank_name.setText(SessionManager.get_BankName(prefs));
        } else {
            Toast.makeText(getActivity(), "Your Bank Infromation not Updated please update", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), PaymentInfoActivity.class);
            startActivity(intent);
        }


        changeBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PaymentInfoActivity.class);
                startActivity(intent);
            }
        });


        if (Global.isOnline(getActivity())) {
            viewModel.excutewallet(SessionManager.get_user_id(prefs), this);
        } else Global.showDialog(getActivity());


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Global.isOnline(getContext())) {
                    try {
                        if (Double.parseDouble(SessionManager.get_wallet(prefs)) > 0) {
                            if (edttxt_amount.getText().toString().length() > 0) {
                                if (Double.parseDouble(edttxt_amount.getText().toString()) > 0) {
                                    if (Double.parseDouble(SessionManager.get_wallet(prefs)) >= Double.parseDouble(edttxt_amount.getText().toString())) {
                                        viewModel.excutecahout(SessionManager.get_user_id(prefs),
                                                edttxt_amount.getText().toString(), DriverCashoutRequestFragment.this);
                                    } else
                                        Toast.makeText(getContext(), "Please Enter Amount Low", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(getContext(), "Please Enter Greater Then  0", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getContext(), "Please Enter Amount ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Wallet Balance Low", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }

        });

        return view;
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
        Global.msgDialogBack(getActivity(), message);
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progress.dismiss();
        if (msgResponce.getStatus().equalsIgnoreCase("wallet")) {
            earningAmount = Double.parseDouble(msgResponce.getTotalCartItem());
            SessionManager.save_wallet(prefs, msgResponce.getTotalCartItem());
            setProfileInfo();
        } else Global.msgDialogBack(getActivity(), msgResponce.getMsg());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.history, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_history){
            Intent intent=new Intent(getContext(),CashoutHistoryDriverActivity.class);
            startActivity(intent);
        }

        return false;
    }
}
