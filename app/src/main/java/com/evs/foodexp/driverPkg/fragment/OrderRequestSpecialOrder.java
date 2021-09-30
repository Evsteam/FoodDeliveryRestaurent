package com.evs.foodexp.driverPkg.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.evs.foodexp.driverPkg.activity.SpecialDetailsScreen;
import com.evs.foodexp.driverPkg.adapter.requestorderlist.food.FoodRequestPagedAdapter;
import com.evs.foodexp.driverPkg.adapter.requestorderlist.specialRequest.SpecialRequestPagedAdapter;
import com.evs.foodexp.models.RequestModel;
import com.evs.foodexp.models.SpecialOrderModel;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.restaurentPkg.adapter.PagedItemClick;
import com.evs.foodexp.restaurentPkg.fragment.OrderDetail;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;

public class OrderRequestSpecialOrder extends Fragment implements AuthStatusListener, PagedItemClick<SpecialOrderModel>, AuthMsgListener {

    RecyclerView recyclerView;
    RestorentViewModel viewModel;
    TextView notfound;
    SharedPreferences prefe;
    LoaderProgress progress;
    SpecialRequestPagedAdapter adapter;
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
//                    adapter = new RequestOrderAdapter(getContext(), requestModels, OrderRequestFood.this);
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
//        if(msgResponce.getStatus().equalsIgnoreCase("success")){
//
//        }else {}
        Global.msgDialogBack(getActivity(), msgResponce.getMsg());


    }

    @Override
    public void pagedmClick(SpecialOrderModel object) {

            Intent intent=new Intent(getActivity(), SpecialDetailsScreen.class);
            intent.putExtra("specialrequestId", object.getSpecialrequestId());
            intent.putExtra("userId", object.getUserId());
            intent.putExtra("userName", object.getUserName());
            intent.putExtra("userPhone", object.getUserPhone());
            intent.putExtra("userImage", object.getUserImage());
            intent.putExtra("driverId", object.getDriverId());
            intent.putExtra("driverName", object.getDriverName());
            intent.putExtra("driverPhone", object.getDriverPhone());
            intent.putExtra("driverImage", object.getDriverImage());
            intent.putExtra("whatYouWant", object.getWhatYouWant());
            intent.putExtra("price", object.getPrice());
            intent.putExtra("deliveryFee", object.getDeliveryFee());
            intent.putExtra("TIP", object.getTIP());
            intent.putExtra("salesTax", object.getSalesTax());
            intent.putExtra("latitude", object.getLatitude());
            intent.putExtra("longitude", object.getLongitude());
            intent.putExtra("address", object.getAddress());
            intent.putExtra("totalAmount", object.getTotalAmount());
            intent.putExtra("status", object.getStatus());
            intent.putExtra("created", object.getCreated());
            intent.putExtra("paymentStatus", object.getPaymentstatus());
            intent.putExtra("imageUpload", object.getImageUpload());
            intent.putExtra("transactionFee", object.getTransactionFee());
            startActivity(intent);

    }

    public class CustomDialogClass extends Dialog {

        public Activity c;
        public Dialog d;
        RequestModel model;
        TextView tv_accept, tv_reject, tv_dropLocation, tv_picupLocation, tv_deliveryFee, tv_tipAmount, tv_time, tv_miles;

        public CustomDialogClass(Activity a, RequestModel model) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
            this.model = model;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.food_delivery_request_alert);
            tv_accept = findViewById(R.id.tv_accept);
            tv_reject = findViewById(R.id.tv_reject);
            tv_dropLocation = findViewById(R.id.tv_dropLocation);
            tv_picupLocation = findViewById(R.id.tv_picupLocation);
            tv_deliveryFee = findViewById(R.id.tv_deliveryFee);
            tv_tipAmount = findViewById(R.id.tv_tipAmount);
            tv_time = findViewById(R.id.tv_time);
            tv_miles = findViewById(R.id.tv_miles);

            tv_miles.setText(model.getMile());
            tv_time.setText(model.getEstTime());
            tv_tipAmount.setText(model.getTIPAmount());

            tv_dropLocation.setText(model.getAddress());
            tv_picupLocation.setText(model.getResturentddress());
            tv_deliveryFee.setText(model.getDeliveryFee());

            tv_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.executeUpdateOrder(SessionManager.get_user_id(prefe), model.getFoodrequestId(),
                            model.getFoodorderId(), "2", OrderRequestSpecialOrder.this);
                    dismiss();
                }
            });
            tv_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.executeUpdateOrder(SessionManager.get_user_id(prefe), model.getFoodrequestId(),
                            model.getFoodorderId(), "3", OrderRequestSpecialOrder.this);
                    dismiss();
                }
            });
        }


    }


    @Override
    public void onResume() {
        super.onResume();
        if (Global.isOnline(getContext())) {
            viewModel.apiOrderSpecialOrder(SessionManager.get_user_id(prefe), "specialrequestlist",SessionManager.get_userType(prefe), this);
        } else Global.showDialog(getActivity());


        adapter = new SpecialRequestPagedAdapter(getContext(), OrderRequestSpecialOrder.this);

        viewModel.getSpecialOrderList().observe(this, new Observer<PagedList<SpecialOrderModel>>() {
            @Override
            public void onChanged(PagedList<SpecialOrderModel> requestModels) {
//                progress.dismiss();
                adapter.submitList(requestModels);

            }
        });
        recyclerView.setAdapter(adapter);
    }
}
