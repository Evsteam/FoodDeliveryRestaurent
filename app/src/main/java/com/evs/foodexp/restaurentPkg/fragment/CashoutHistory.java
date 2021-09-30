package com.evs.foodexp.restaurentPkg.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import  androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.viewModel.RestorentViewModel;
import com.evs.foodexp.models.CashoutModel;
import com.evs.foodexp.restaurentPkg.adapter.cashout.CashoutPagedAdapter;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;

public class CashoutHistory extends Fragment implements AuthStatusListener {

    private SharedPreferences prefs;
    RecyclerView recylerView;
    LinearLayoutManager layoutManager;
    LoaderProgress progress;
    LinearLayout earningDetails;
    RestorentViewModel viewModel;
    CashoutPagedAdapter adapter;
    TextView tv_notFound;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cashout_history, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        mTitle.setText("Cashout History");

        recylerView = view.findViewById(R.id.recentOrdersRecyclerView);
        tv_notFound = view.findViewById(R.id.tv_notFound);


        layoutManager = new LinearLayoutManager(getActivity());
        recylerView.setLayoutManager(layoutManager);
        recylerView.setItemAnimator(new DefaultItemAnimator());
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        progress = new LoaderProgress(getContext());
        viewModel = new ViewModelProvider(getActivity()).get(RestorentViewModel.class);

        recylerView.setLayoutManager(layoutManager);
        recylerView.setItemAnimator(new DefaultItemAnimator());
        if (Global.isOnline(getContext())) {
//            viewModel.executeEarning(SessionManager.get_user_id(prefs), SessionManager.get_userType(prefs),"Today",this);
            viewModel.apiCashout(SessionManager.get_user_id(prefs),  this);
        } else Global.showDialog(getActivity());

        adapter = new CashoutPagedAdapter(getContext());

        viewModel.getCahouts().observe(this, new Observer<PagedList<CashoutModel>>() {
            @Override
            public void onChanged(PagedList<CashoutModel> reviewsModels) {
                progress.dismiss();
                adapter.submitList(reviewsModels);

            }
        });
        recylerView.setAdapter(adapter);

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


}
