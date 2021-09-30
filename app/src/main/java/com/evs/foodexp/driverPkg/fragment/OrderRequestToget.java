package com.evs.foodexp.driverPkg.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import  androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.commonPkg.viewModel.RestorentViewModel;
import com.evs.foodexp.driverPkg.adapter.requestorderlist.toget.TogetRequestPagedAdapter;
import com.evs.foodexp.models.GoGetModel;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.restaurentPkg.adapter.PagedItemClick;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;

public class OrderRequestToget extends Fragment implements AuthStatusListener, PagedItemClick<GoGetModel> , AuthMsgListener {

    RecyclerView recyclerView;
    RestorentViewModel viewModel;
    TextView notfound;
    SharedPreferences prefe;
    LoaderProgress progress;
    TogetRequestPagedAdapter adapter;
//    RequestOrderAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.in_progress_order_layout, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("ORDERS");

        recyclerView = view.findViewById(R.id.recyclerView);
        notfound = view.findViewById(R.id.notfound);

        prefe = PreferenceManager.getDefaultSharedPreferences(getContext());
        viewModel = new ViewModelProvider(getActivity()).get(RestorentViewModel.class);
        progress = new LoaderProgress(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                new LinearLayoutManager(getContext()).getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.recycler_devider));
        recyclerView.addItemDecoration(dividerItemDecoration);




//        viewModel.getRequestList().observe(this, new Observer<List<RequestModel>>() {
//            @Override
//            public void onChanged(List<RequestModel> requestModels) {
//                progress.dismiss();
//                if (requestModels.size() > 0) {
//                    adapter = new RequestOrderAdapter(getContext(), requestModels, OrderRequestToget.this);
//                    recyclerView.setAdapter(adapter);
//                } else notfound.setVisibility(View.VISIBLE);
//            }
//        });

        return view;
    }

    @Override
    public void onStarted() {
        progress.show();
    }


    @Override
    public void onFailure(String message) {
        progress.dismiss();
        if (message.equalsIgnoreCase("0")) {
            notfound.setVisibility(View.GONE);
        }else if (message.equalsIgnoreCase("1")){
            notfound.setVisibility(View.VISIBLE);
        }else Global.msgDialog(getActivity(),message);
//
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progress.dismiss();
        Global.msgDialogBack(getActivity(),msgResponce.getMsg());

    }

    @Override
    public void pagedmClick(GoGetModel object) {

        CustomDialogClass cdd = new CustomDialogClass(getActivity(), object);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }

    public class CustomDialogClass extends Dialog {

        public Activity c;
        public Dialog d;
        GoGetModel model;
        TextView tv_accept, tv_reject, tv_title, tv_price,tv_stoes, tv_tipAmount, tv_delilry, tv_adress;

        public CustomDialogClass(Activity a, GoGetModel model) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
            this.model = model;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.new_job_request_to_get_driver_alert);
            tv_accept = findViewById(R.id.tv_accept);
            tv_stoes = findViewById(R.id.tv_stoes);
            tv_reject = findViewById(R.id.tv_reject);
            tv_title = findViewById(R.id.tv_title);
            tv_price = findViewById(R.id.tv_price);
            tv_delilry = findViewById(R.id.tv_delilry);
            tv_tipAmount = findViewById(R.id.tv_tipAmount);
            tv_adress = findViewById(R.id.tv_adress);

            tv_adress.setText(model.getAddress());
            tv_title.setText(model.getWahtUwant());
            tv_price.setText("$ "+model.getTotalAmount());
            tv_tipAmount.setText("$ "+model.getTIP());
            tv_delilry.setText(model.getStoreCity());
            tv_stoes.setText("Store name ");


            tv_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.executeTogetOrder(SessionManager.get_user_id(prefe),model.getTogetDriverRequestId(),
                            model.getTogetRequestId(),"2", OrderRequestToget.this);
                    dismiss();
                }
            });
            tv_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.executeUpdateOrder(SessionManager.get_user_id(prefe),model.getTogetDriverRequestId(),
                            model.getTogetRequestId(),"3", OrderRequestToget.this);
                    dismiss();
                }
            });
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        if (Global.isOnline(getContext())) {
//            viewModel.apiRequestOrder(SessionManager.get_user_id(prefe), this);
            viewModel.apiOrderReqestToget(SessionManager.get_user_id(prefe),"togetdriverrequestlist", "0",SessionManager.get_userType(prefe),this);
        } else Global.showDialog(getActivity());


        adapter=new TogetRequestPagedAdapter(getContext(),OrderRequestToget.this);

        viewModel.getTogetRequest().observe(this, new Observer<PagedList<GoGetModel>>() {
            @Override
            public void onChanged(PagedList<GoGetModel> requestModels) {
//                progress.dismiss();
                adapter.submitList(requestModels);

            }
        });
        recyclerView.setAdapter(adapter);
    }
}
