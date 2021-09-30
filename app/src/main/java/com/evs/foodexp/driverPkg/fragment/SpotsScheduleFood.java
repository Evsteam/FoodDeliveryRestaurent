package com.evs.foodexp.driverPkg.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import  androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.viewModel.RestorentViewModel;
import com.evs.foodexp.driverPkg.adapter.spot.spotservice.SpotPagedAdapter;
import com.evs.foodexp.models.OrderModel;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SpotsScheduleFood extends Fragment implements AuthStatusListener {

    Button updateScheduleBtn;
    RecyclerView recylerView;
    LinearLayoutManager layoutManager;
    TextView tv_notfound, tv_dateTime;
    LoaderProgress progress;
    RestorentViewModel viewModel;
    SharedPreferences prefs;
    SpotPagedAdapter adapter;
    Calendar myCalendar;
    ImageView imgCalender;
    String myFormat = "yyyy-MM-dd";
    SimpleDateFormat sdf;
    Date today;
    String selectedDate = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.spote_schedule_food, container, false);

        updateScheduleBtn = (Button) view.findViewById(R.id.updateScheduleBtn);
        tv_notfound = (TextView) view.findViewById(R.id.notfound);
        tv_dateTime = (TextView) view.findViewById(R.id.tv_dateTime);
        imgCalender = (ImageView) view.findViewById(R.id.imgCalender);
        recylerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        progress = new LoaderProgress(getContext());
        layoutManager = new LinearLayoutManager(getActivity());
        recylerView.setLayoutManager(layoutManager);
        recylerView.setHasFixedSize(false);
        myCalendar = Calendar.getInstance();
        today = new Date();
        recylerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                new LinearLayoutManager(getContext()).getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.recycler_devider));
        recylerView.addItemDecoration(dividerItemDecoration);
        sdf = new SimpleDateFormat(myFormat);
        viewModel = new ViewModelProvider(getActivity()).get(RestorentViewModel.class);
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        selectedDate = sdf.format(today.getTime());
        tv_dateTime.setText(new SimpleDateFormat("MMM dd, yyyy").format(today.getTime()));

        layoutManager = new LinearLayoutManager(getActivity());
        recylerView.setLayoutManager(layoutManager);
        recylerView.addItemDecoration(new DividerItemDecoration(getActivity(), new LinearLayoutManager(getContext()).getOrientation()));

        if (Global.isOnline(getContext())) {
//            viewModel.apiSpotScdule("foodorderlist", SessionManager.get_user_id(prefs), "1", SessionManager.get_userType(prefs),
//                    selectedDate, this);
        } else Global.showDialog(getActivity());

        adapter = new SpotPagedAdapter(getContext());

        viewModel.getSpots().observe(this, new Observer<PagedList<OrderModel>>() {
            @Override
            public void onChanged(PagedList<OrderModel> reviewsModels) {
                progress.dismiss();
                adapter.submitList(reviewsModels);

            }
        });
        recylerView.setAdapter(adapter);


//        scheduleLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                LayoutInflater factory = LayoutInflater.from(getActivity());
//                final View deleteDialogView = factory.inflate(R.layout.new_job_request_to_get_driver_alert, null);
//                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
//                alertDialog.setView(deleteDialogView);
//                alertDialog.getWindow().setLayout(400, 800);
//                deleteDialogView.findViewById(R.id.acceptJobRequest).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                    }
//                });
//
//                alertDialog.show();
//
//            }
//        });

//        updateScheduleBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LayoutInflater factory = LayoutInflater.from(getActivity());
//                final View deleteDialogView = factory.inflate(R.layout.food_delivery_request_alert, null);
//                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
//                alertDialog.setView(deleteDialogView);
//                alertDialog.getWindow().setLayout(400, 800);
//                deleteDialogView.findViewById(R.id.foodDeliveryRequestAccept).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                    }
//                });
//
//                alertDialog.show();
//            }
//        });

//        scheduleOverlapLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LayoutInflater factory = LayoutInflater.from(getActivity());
//                final View deleteDialogView = factory.inflate(R.layout.service_request_alert, null);
//                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
//                alertDialog.setView(deleteDialogView);
//                alertDialog.getWindow().setLayout(400, 800);
//                deleteDialogView.findViewById(R.id.serviceRequestAccept).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                    }
//                });
//
//                alertDialog.show();
//            }
//        });


        imgCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePic();
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
        if(message.equalsIgnoreCase("0")){
            tv_notfound.setVisibility(View.VISIBLE);
        }else Global.msgDialogBack(getActivity(),message);
    }

    private void DatePic() {
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(),
                AlertDialog.THEME_TRADITIONAL, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                // TODO Auto-generated method stub
                /*      Your code   to get date and time    */

                myCalendar.set(Calendar.YEAR, selectedyear);
                myCalendar.set(Calendar.MONTH, selectedmonth);
                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
//

                tv_dateTime.setText(updateCalanderDate());
                selectedDate = sdf.format(myCalendar.getTime());
//                viewModel.apiSpotScdule("bookinglist", SessionManager.get_user_id(prefs), "1", SessionManager.get_userType(prefs),
//                selectedDate, SpotsScheduleFood.this);

            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select date");
        mDatePicker.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
        Log.e("DatePic: ", "" + mDatePicker.getDatePicker().getMinDate());
        mDatePicker.show();
    }

    private String updateCalanderDate() {
        // TODO Auto-generated method stub
        return new SimpleDateFormat("MMM dd, yyyy", Locale.US).format(myCalendar.getTime());
    }

}
