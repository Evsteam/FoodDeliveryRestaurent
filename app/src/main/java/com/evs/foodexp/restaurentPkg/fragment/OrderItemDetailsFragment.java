package com.evs.foodexp.restaurentPkg.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import  androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.viewModel.RestorentViewModel;
import com.evs.foodexp.models.OrderModel;
import com.evs.foodexp.restaurentPkg.adapter.OrderItemDetailAdapter;
import com.evs.foodexp.userPkg.activity.BookingSuceess;
import com.evs.foodexp.userPkg.activity.PaymentUserActivity;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;

import java.text.DecimalFormat;

public class OrderItemDetailsFragment extends Fragment implements AuthStatusListener {
    RecyclerView itemDetailRecycle;
    RestorentViewModel viewModel;
    TextView tv_tipAmount, tv_totalAmount,tv_transactionfee, tv_cardNumber, tv_sateTax, paymentInformationHeading;
    SharedPreferences prefe;
    LoaderProgress progress;
    Button btn_payment;
    RelativeLayout paymentTypeRelativeLayout;
    String totalAmount="0",AdminAmount="",driverStripeAccount="";
    DecimalFormat formater = new DecimalFormat("#.##");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_details_layout, container, false);
        itemDetailRecycle = view.findViewById(R.id.itemDetailRecycle);
        paymentInformationHeading = view.findViewById(R.id.paymentInformationHeading);
        paymentTypeRelativeLayout = view.findViewById(R.id.paymentTypeRelativeLayout);
        btn_payment = view.findViewById(R.id.btn_payment);
        tv_tipAmount = view.findViewById(R.id.tv_tipAmount);
        tv_totalAmount = view.findViewById(R.id.tv_totalAmount);
        tv_cardNumber = view.findViewById(R.id.tv_cardNumber);
        tv_sateTax = view.findViewById(R.id.tv_sateTax);
        tv_transactionfee = view.findViewById(R.id.tv_transactionfee);

        prefe = PreferenceManager.getDefaultSharedPreferences(getContext());
        viewModel = new ViewModelProvider(getActivity()).get(RestorentViewModel.class);
        progress = new LoaderProgress(getContext());

        itemDetailRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        itemDetailRecycle.setHasFixedSize(false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                new LinearLayoutManager(getContext()).getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.recycler_devider));
        itemDetailRecycle.addItemDecoration(dividerItemDecoration);




        viewModel.getOrderDetails().observe(getActivity(), new Observer<OrderModel>() {
            @Override
            public void onChanged(OrderModel orderModel) {
                progress.dismiss();
                OrderItemDetailAdapter adapter = new OrderItemDetailAdapter(getContext(), orderModel.getFoodDetails(), orderModel.getResturentName());
                itemDetailRecycle.setAdapter(adapter);
                if (orderModel.getCardNo() != null) {
                    tv_cardNumber.setText(orderModel.getCardNo().replaceAll("\\w(?=\\w{4})", "X"));
                }

                tv_tipAmount.setText("$ " + formater.format(Double.parseDouble(orderModel.getTIP())));
                AdminAmount= formater.format(Double.parseDouble(orderModel.getAdminAmount()));
                driverStripeAccount=orderModel.getDriverStripeAccount();
                tv_sateTax.setText("$ " + formater.format(Double.parseDouble(orderModel.getSalesTax())));
                tv_transactionfee.setText("$ " + formater.format(Double.parseDouble(orderModel.getTransactionFee())));
                totalAmount=String.valueOf(Double.parseDouble(orderModel.getTotalAmount())
                        +Double.parseDouble(orderModel.getTransactionFee()));

                tv_totalAmount.setText("$ " + formater.format(Double.parseDouble(totalAmount)));

                if (SessionManager.get_userType(prefe).equalsIgnoreCase("Member")) {
                    btn_payment.setText("Waiting For Driver!!");

                    if (orderModel.getPaymentStatus().equalsIgnoreCase("2")) {
                        paymentInformationHeading.setVisibility(View.VISIBLE);
                        paymentTypeRelativeLayout.setVisibility(View.VISIBLE);
                        btn_payment.setVisibility(View.GONE);
                    } else if (orderModel.getPaymentStatus().equalsIgnoreCase("1")) {
                        btn_payment.setText(getResources().getString(R.string.confirm_pay));
                        paymentInformationHeading.setVisibility(View.GONE);
                        paymentTypeRelativeLayout.setVisibility(View.GONE);
                        btn_payment.setVisibility(View.VISIBLE);
                    } else {
                        paymentInformationHeading.setVisibility(View.VISIBLE);
                        paymentTypeRelativeLayout.setVisibility(View.VISIBLE);
                        btn_payment.setVisibility(View.GONE);
                    }
                } else if (orderModel.getPaymentStatus().equalsIgnoreCase("2")) {
                    paymentInformationHeading.setVisibility(View.VISIBLE);
                    paymentTypeRelativeLayout.setVisibility(View.VISIBLE);
                    btn_payment.setVisibility(View.GONE);
                }

            }
        });

        if (SessionManager.get_userType(prefe).equalsIgnoreCase("Member")) {
            btn_payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewModel.getOrderDetails().getValue().getPaymentStatus().equalsIgnoreCase("1")) {
                        Intent intent = new Intent(getContext(), PaymentUserActivity.class);
                        intent.putExtra("foodOrderGroupId", viewModel.getOrderDetails().getValue().getFoodOrderGroupId());
                        intent.putExtra("totalAmount", totalAmount);
                        intent.putExtra("AdminAmount", AdminAmount);
                        intent.putExtra("driverStripeAccount", driverStripeAccount);
                        startActivity(intent);
                    }
                }
            });
        }


        return view;
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
}
