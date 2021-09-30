package com.evs.foodexp.restaurentPkg.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import  androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.commonPkg.viewModel.RestorentViewModel;
import com.evs.foodexp.models.OrderModel;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.restaurentPkg.activity.CashoutActivity;
import com.evs.foodexp.restaurentPkg.adapter.earning.EarningPagedAdapter;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;

import java.text.DecimalFormat;

public class TodayEarningFragment extends Fragment implements AuthStatusListener , AuthMsgListener {

    Button btn_cashOut;
    private SharedPreferences prefs;
    RecyclerView recylerView;
    LinearLayoutManager layoutManager;
    TextView notfound,restEarnings,restOrderDeliveredNumber;
    LoaderProgress progress;
    LinearLayout earningDetails;
    RestorentViewModel viewModel;
    EarningPagedAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.today_earning_layout, container, false);
        btn_cashOut = (Button) view.findViewById(R.id.btn_cashOut);

        recylerView = view.findViewById(R.id.recentOrdersRecyclerView);
        notfound = view.findViewById(R.id.notfound);
        earningDetails = view.findViewById(R.id.earningDetails);
        restEarnings = view.findViewById(R.id.restEarnings);
        restOrderDeliveredNumber = view.findViewById(R.id.restOrderDeliveredNumber);


        layoutManager = new LinearLayoutManager(getActivity());
        recylerView.setLayoutManager(layoutManager);
        recylerView.setItemAnimator(new DefaultItemAnimator());
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        progress = new LoaderProgress(getContext());
        viewModel = new ViewModelProvider(getActivity()).get(RestorentViewModel.class);


        if (Global.isOnline(getContext())) {
            viewModel.executeEarning("getearningfood",SessionManager.get_user_id(prefs), SessionManager.get_userType(prefs),"Today",this);
            viewModel.apiEarning(SessionManager.get_user_id(prefs), "4", SessionManager.get_userType(prefs),"Today", this);
        } else Global.showDialog(getActivity());

        adapter = new EarningPagedAdapter(getContext());

        viewModel.getEarning().observe(this, new Observer<PagedList<OrderModel>>() {
            @Override
            public void onChanged(PagedList<OrderModel> reviewsModels) {
                adapter.submitList(reviewsModels);

            }
        });
        recylerView.setAdapter(adapter);

        btn_cashOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CashoutActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }


    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progress.dismiss();
            if(Integer.parseInt(msgResponce.getMsg())>0){
                earningDetails.setVisibility(View.VISIBLE);
                restOrderDeliveredNumber.setText(msgResponce.getMsg());
                restEarnings.setText(new DecimalFormat("##.##").format(Double.parseDouble(msgResponce.getTotalCartItem()))+" $");
        }else {
                notfound.setVisibility(View.VISIBLE);
                recylerView.setVisibility(View.GONE);
            }
    }
}
