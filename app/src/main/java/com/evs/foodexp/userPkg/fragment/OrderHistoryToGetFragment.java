package com.evs.foodexp.userPkg.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import  androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.driverPkg.activity.DriverJobToGetDetailActivity;
import com.evs.foodexp.driverPkg.adapter.history.toget.TogetPagedAdapter;
import com.evs.foodexp.repositry.ListResponse;
import com.evs.foodexp.restaurentPkg.adapter.PagedItemClick;
import com.evs.foodexp.models.HistoryModel;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryToGetFragment extends Fragment implements PagedItemClick<HistoryModel>,
        AuthListener<ListResponse<HistoryModel>> , AuthStatusListener {

    RecyclerView historyList;
    TextView tv_notFound;
    LinearLayoutManager mLayoutManager;
    List<HistoryModel> list;
    LoaderProgress progress;
    SharedPreferences prefs;
    APIViewModel viewModel;
    //    TogetHistoryAdpter adapter;
    TogetPagedAdapter adapter;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    int page = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_history_service_layout, container, false);
        historyList = view.findViewById(R.id.historyList);
        tv_notFound = view.findViewById(R.id.tv_notFound);

        progress = new LoaderProgress(getContext());
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        viewModel = new ViewModelProvider(getActivity()).get(APIViewModel.class);

        historyList.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        historyList.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(historyList.getContext(),
                mLayoutManager.getOrientation());
        historyList.addItemDecoration(dividerItemDecoration);
        list = new ArrayList<>();
//        adapter = new TogetHistoryAdpter(getContext(), list, OrderHistoryToGetFragment.this);
//        historyList.setAdapter(adapter);
        adapter = new TogetPagedAdapter(getContext(), OrderHistoryToGetFragment.this);

//        if (Global.isOnline(getContext())) {
////            viewModel.getTogetList("gogetlist", SessionManager.get_user_id(prefs),
////                    SessionManager.get_userType(prefs), "", "" + page, this);
//            viewModel.apiTogetHistory("gogetlist", SessionManager.get_user_id(prefs),
//                    "", SessionManager.get_userType(prefs), this);
//        } else Global.showDialog(getActivity());

//        historyList.addOnScrollListener(new RecycleViewPaginationScrollListener(mLayoutManager) {
//            @Override
//            protected void loadMoreItems() {
//                isLoading = true;
//                page++;
//            }
//
//            @Override
//            public boolean isLastPage() {
//                return isLastPage;
//            }
//
//            @Override
//            public boolean isLoading() {
//                return isLoading;
//            }
//
//        });

//        viewModel.getTogetHistoryist().observe(this, new Observer<PagedList<HistoryModel>>() {
//            @Override
//            public void onChanged(PagedList<HistoryModel> historyModels) {
//                adapter.submitList(historyModels);
//            }
//        });
//        historyList.setAdapter(adapter);

        return view;
    }


    @Override
    public void pagedmClick(HistoryModel object) {
        Intent intent = new Intent(getContext(), DriverJobToGetDetailActivity.class);
        intent.putExtra("togetrequestId", object.getTogetrequestId());
        startActivity(intent);
    }

    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onSuccess(LiveData<ListResponse<HistoryModel>> data) {
        progress.dismiss();
        if (data.getValue().getStatus().equalsIgnoreCase("success")) {
            list.addAll(data.getValue().getList());
            adapter.notifyDataSetChanged();
            if (list.size() > 0) {
                tv_notFound.setVisibility(View.GONE);
            } else tv_notFound.setVisibility(View.VISIBLE);
        } else Global.msgDialog(getActivity(), data.getValue().getMsg());


    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();
        if (message.equalsIgnoreCase("0")) {
            tv_notFound.setVisibility(View.GONE);
        }else if (message.equalsIgnoreCase("1")){
            tv_notFound.setVisibility(View.VISIBLE);
        }else Global.msgDialog(getActivity(),message);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Global.isOnline(getContext())) {
//            viewModel.getTogetList("gogetlist", SessionManager.get_user_id(prefs),
//                    SessionManager.get_userType(prefs), "", "" + page, this);
            viewModel.apiTogetHistory("gogetlist", SessionManager.get_user_id(prefs),
                    "", SessionManager.get_userType(prefs), this);
        } else Global.showDialog(getActivity());
        viewModel.getTogetHistoryist().observe(this, new Observer<PagedList<HistoryModel>>() {
            @Override
            public void onChanged(PagedList<HistoryModel> historyModels) {
                adapter.submitList(historyModels);
            }
        });
        historyList.setAdapter(adapter);
    }
}