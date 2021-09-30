package com.evs.foodexp.restaurentPkg.fragment;

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
import com.evs.foodexp.restaurentPkg.adapter.OrderInProgressAdapter;
import com.evs.foodexp.restaurentPkg.adapter.PagedItemClick;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;

import java.util.List;

public class OrderDeliveredFragment extends Fragment implements AuthStatusListener , PagedItemClick<OrderModel> {
    RecyclerView recyclerView;
    RestorentViewModel viewModel;
    TextView notfound;
    SharedPreferences prefe;
    LoaderProgress progress;
    OrderInProgressAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.in_progress_order_layout,container,false);
        recyclerView=view.findViewById(R.id.recyclerView);
        notfound=view.findViewById(R.id.notfound);

        prefe= PreferenceManager.getDefaultSharedPreferences(getContext());
        viewModel= new ViewModelProvider(getActivity()).get(RestorentViewModel.class);
        progress=new LoaderProgress(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                new LinearLayoutManager(getContext()).getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.recycler_devider));
        recyclerView.addItemDecoration(dividerItemDecoration);


        if(Global.isOnline(getContext())){
            viewModel.apiOrderList(SessionManager.get_user_id(prefe),SessionManager.get_userType(prefe),"4",this);
        }else Global.showDialog(getActivity());

        viewModel.getDelivredOrdersList().observe(this, new Observer<List<OrderModel>>() {
            @Override
            public void onChanged(List<OrderModel> orderModels) {
                progress.dismiss();
                if(orderModels.size()>0){
                    notfound.setVisibility(View.GONE);
                    adapter=new OrderInProgressAdapter(getContext(),orderModels,OrderDeliveredFragment.this);
                    recyclerView.setAdapter(adapter);
                }else notfound.setVisibility(View.VISIBLE);

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
        Global.msgDialog(getActivity(),message);
    }

    @Override
    public void pagedmClick(OrderModel object) {
        Intent intent=new Intent(getContext(),OrderDetail.class);
        intent.putExtra("orderId",object.getFoodorderId());
        intent.putExtra("status","delivered");
        intent.putExtra("foodRequestId","");
        startActivity(intent);
    }
}
