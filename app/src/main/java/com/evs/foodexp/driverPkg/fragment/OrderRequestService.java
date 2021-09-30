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
import com.evs.foodexp.driverPkg.adapter.requestorderlist.services.ServiceRequestPagedAdapter;
import com.evs.foodexp.models.BookingModel;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.restaurentPkg.adapter.PagedItemClick;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;

public class OrderRequestService extends Fragment implements AuthStatusListener, PagedItemClick<BookingModel>, AuthMsgListener {

    RecyclerView recyclerView;
    RestorentViewModel viewModel;
    TextView notfound;
    SharedPreferences prefe;
    LoaderProgress progress;
    ServiceRequestPagedAdapter adapter;
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
//                    adapter = new RequestOrderAdapter(getContext(), requestModels, OrderRequestService.this);
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
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progress.dismiss();
        Global.msgDialogBack(getActivity(), msgResponce.getMsg());

    }

    @Override
    public void pagedmClick(BookingModel object) {

        CustomDialogClass cdd = new CustomDialogClass(getActivity(), object);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }

    public class CustomDialogClass extends Dialog {

        public Activity c;
        public Dialog d;
        BookingModel model;
        TextView tv_accept, tv_reject,tv_date, tv_serviceName, tv_calenderDate, tv_location, tv_servicePrice,
                tv_tipAmount, tv_totalAmount;

        public CustomDialogClass(Activity a, BookingModel model) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
            this.model = model;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.service_request_alert);
            tv_accept = findViewById(R.id.tv_accept);
            tv_reject = findViewById(R.id.tv_reject);
            tv_serviceName = findViewById(R.id.tv_serviceName);
            tv_calenderDate = findViewById(R.id.tv_calenderDate);
            tv_location = findViewById(R.id.tv_location);
            tv_tipAmount = findViewById(R.id.tv_tipAmount);
            tv_servicePrice = findViewById(R.id.tv_servicePrice);
            tv_totalAmount = findViewById(R.id.tv_totalAmount);
            tv_date = findViewById(R.id.tv_date);

            tv_serviceName.setText(model.getServiceName());
            tv_calenderDate.setText(model.getSlote());
            tv_location.setText(model.getAddress());
            tv_servicePrice.setText("$ "+model.getServiceAmount());
            tv_tipAmount.setText("$ "+model.getTIP());
            tv_totalAmount.setText("$ "+model.getTotalAmount());
            tv_date.setText(model.getBookingDate());

            tv_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.executeServiceOrder(SessionManager.get_user_id(prefe), model.getBookingId(),
                            "1", OrderRequestService.this);
                    dismiss();
                }
            });
            tv_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.executeServiceOrder(SessionManager.get_user_id(prefe), model.getBookingId(),
                          "3", OrderRequestService.this);
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
            viewModel.apiOrderReqestService(SessionManager.get_user_id(prefe), "requestbookinglist", "0", SessionManager.get_userType(prefe), this);
        } else Global.showDialog(getActivity());


        adapter = new ServiceRequestPagedAdapter(getContext(), OrderRequestService.this);

        viewModel.getRequest().observe(this, new Observer<PagedList<BookingModel>>() {
            @Override
            public void onChanged(PagedList<BookingModel> requestModels) {
//                progress.dismiss();
                adapter.submitList(requestModels);

            }
        });
        recyclerView.setAdapter(adapter);
    }
}
