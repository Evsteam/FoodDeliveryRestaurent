package com.evs.foodexp.driverPkg.fragment;

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
import com.evs.foodexp.driverPkg.adapter.toget_earning.TogetEarningPagedAdapter;
import com.evs.foodexp.models.GoGetModel;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.restaurentPkg.activity.CashoutActivity;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;

import java.text.DecimalFormat;

public class TogetTodayEarningFragment extends Fragment implements AuthStatusListener, AuthMsgListener {


    Button btn_cashOut;
    private SharedPreferences prefs;
    RecyclerView recylerView;
    LinearLayoutManager layoutManager;
    TextView notfound,restEarnings,restOrderDeliveredNumber;
    LoaderProgress progress;
    LinearLayout earningDetails;
    RestorentViewModel viewModel;
    TogetEarningPagedAdapter adapter;
//    Button cashoutBtn;
//    FragmentManager manager;
//    Fragment fragment = null;
//
//    @Nullable
//    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.today_earning_driver_layout,container,false);
//        manager = getActivity().getSupportFragmentManager();
//        cashoutBtn = (Button)view.findViewById(R.id.cashoutDriverTodayBtn);
//
//        cashoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fragment = new DriverCashoutRequestFragment();
//                manager.beginTransaction().replace(R.id.frame_container_driver_home, fragment)
//                        .addToBackStack(null).commit();
//            }
//        });
//        return view;
//    }
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
            viewModel.executeEarning("togetearning",SessionManager.get_user_id(prefs), SessionManager.get_userType(prefs), "Today", this);
            viewModel.apiTogetEarning("gogetlist",SessionManager.get_user_id(prefs), "2", SessionManager.get_userType(prefs), "Today", this);
        } else Global.showDialog(getActivity());

        adapter = new TogetEarningPagedAdapter(getContext());

        viewModel.getTogetEarnigs().observe(this, new Observer<PagedList<GoGetModel>>() {
            @Override
            public void onChanged(PagedList<GoGetModel> reviewsModels) {
//                progress.dismiss();
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
