package com.evs.foodexp.userPkg.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import  androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.common.adapter.ServiceAdpter;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.repositry.ListResponse;
import com.evs.foodexp.restaurentPkg.adapter.PagedItemClick;
import com.evs.foodexp.userPkg.adapter.ExpandableListAdapter;
import com.evs.foodexp.models.SubService;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;

import java.util.ArrayList;
import java.util.List;

public class ServiceFragment extends Fragment implements
        AuthListener<ListResponse<SubService>>, PagedItemClick<SubService> {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    SharedPreferences preferences;
    APIViewModel apiViewModel;
    private Button serviceContinue;
    EditText otherService, et_search;
    String otherServiceStr = "",serviceAmount;
    LoaderProgress progress;
    LinearLayoutManager layoutManager;
    RecyclerView serviceLayout;
    int selectedItem = -1;
    RelativeLayout other_service;
    List<SubService> dataList, filterList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_service, container, false);
        getActivity().setTitle("Services");

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        progress = new LoaderProgress(getContext());
        serviceContinue = (Button) view.findViewById(R.id.serviceContinueBtn);
        other_service = (RelativeLayout) view.findViewById(R.id.other_service);
        serviceLayout = (RecyclerView) view.findViewById(R.id.serviceLayout);
        expListView = (ExpandableListView) view.findViewById(R.id.expandableServiceList);
        et_search = (EditText) view.findViewById(R.id.et_search);
        otherService = (EditText) view.findViewById(R.id.editTxt_otherService);
        apiViewModel.services("servicelist", this);

        dataList = new ArrayList<>();
        filterList = new ArrayList<>();

        //     prepareListData();


        layoutManager = new LinearLayoutManager(getContext());
        serviceLayout.setLayoutManager(layoutManager);
        serviceLayout.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                new LinearLayoutManager(getContext()).getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.recycler_devider));
        serviceLayout.addItemDecoration(dividerItemDecoration);

        serviceContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                otherServiceStr = otherService.getText().toString().trim();
                if (selectedItem > -1) {
                    if (selectedItem == 8) {
                        if (Global.validatename(otherServiceStr)) {
                            Intent intent = new Intent(getContext(), DisclosuresActivity.class);
                            intent.putExtra("service", selectedItem+"");
                            intent.putExtra("desc", otherServiceStr);
                            intent.putExtra("serviceAmount", serviceAmount);
                            startActivity(intent);
                        } else
                            Toast.makeText(getContext(), "Please enter Detials fro other service", Toast.LENGTH_LONG).show();

                    } else {
                        Intent intent = new Intent(getContext(), DisclosuresActivity.class);
                        intent.putExtra("service", selectedItem+"");
                        intent.putExtra("desc", otherServiceStr);
                        intent.putExtra("serviceAmount", serviceAmount);
                        startActivity(intent);
                    }
                } else
                    Toast.makeText(getContext(), "Please select Service", Toast.LENGTH_LONG).show();
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("    value   ", "" + s);
                if (s.length() > 0) {
                    //recyclerView_list.setVisibility(View.VISIBLE);
                    filterList = new ArrayList<SubService>();
                    for (int i = 0; i < dataList.size(); i++) {
                        if (dataList.get(i).getName().toLowerCase().contains(s.toString().toLowerCase())) {
                            filterList.add(dataList.get(i));
                        }

                    }
                    ServiceAdpter adapter = new ServiceAdpter(getContext(), filterList, ServiceFragment.this);
                    serviceLayout.setAdapter(adapter);
                } else {
//                    list.setVisibility(View.GONE);
                    ServiceAdpter adapter = new ServiceAdpter(getContext(), filterList, ServiceFragment.this);
                    serviceLayout.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_service);
//        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
//        Utills.StatusBarColour(ServiceFragment.this);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarServiceForUser);
//        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
//        otherService = (EditText)findViewById(R.id.editTxt_otherService);
//        preferences = PreferenceManager.getDefaultSharedPreferences(ServiceFragment.this);
//        toolbarTitle.setText("SERVICE");
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//        serviceContinue = (Button)findViewById(R.id.serviceContinueBtn);
//        other_service = (RelativeLayout) findViewById(R.id.other_service);
//        serviceLayout = (RecyclerView) findViewById(R.id.serviceLayout);
//        progress = new LoaderProgress(ServiceFragment.this);
//
//        apiViewModel.services("servicelist",this);
//        expListView = (ExpandableListView) findViewById(R.id.expandableServiceList);
//   //     prepareListData();
//
//        serviceContinue.setOnClickListener(this);
//
//
//        layoutManager = new LinearLayoutManager(ServiceFragment.this);
//        serviceLayout.setLayoutManager(layoutManager);
//        serviceLayout.setItemAnimator(new DefaultItemAnimator());
//
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ServiceFragment.this,
//                new LinearLayoutManager(ServiceFragment.this).getOrientation());
//        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(ServiceFragment.this, R.drawable.recycler_devider));
//        serviceLayout.addItemDecoration(dividerItemDecoration);
//
//    }


    /*private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Diverse App Service");



        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("YARD WORK");
        top250.add("HOUSE CLEANING");
        top250.add("BABY SITTING");
        top250.add("PET WALKING");
        top250.add("MUSIC SOCIAL");
        top250.add("PIMER SOCIAL");
        top250.add("CAR WASHING");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data

    }*/

    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onSuccess(LiveData<ListResponse<SubService>> data) {
        progress.dismiss();
        if (data.getValue().getStatus().equalsIgnoreCase("success")) {
//            ArrayList<ServiceModel> list=new ArrayList();
//            ServiceModel model=new ServiceModel();
//            model.setId("0");
//            model.setName("Diverse App Services");
//            model.setCategory(data.getValue().getList());
//            list.add(model);
//            listAdapter = new ExpandableListAdapter(this,list);
//            expListView.setAdapter(listAdapter);
            dataList = data.getValue().getList();
            ServiceAdpter adapter = new ServiceAdpter(getContext(), data.getValue().getList(), this);
            serviceLayout.setAdapter(adapter);
        } else Global.msgDialog(getActivity(), data.getValue().getMsg());

    }

//    @Override
//    public void onSuccess(LiveData<ServiceDto> data) {
//        progressDialog.dismiss();
////        listDataHeader = new ArrayList<String>();
////        listDataChild = new HashMap<String, List<String>>();
////        System.out.println("My Service Data zero poss === "+data.getValue().getData().get(0).getName());
////        // Adding child data
////        listDataHeader.add("Diverse App Service");
////
////        // Adding child data
////
////
////        for(int i=0;i<data.getValue().getData().size();i++){
////            System.out.println("My Service Data === "+data.getValue().getData().get(i).getName());
////            top250.add(data.getValue().getData().get(i).getName());
////
////
////        }
//       // top250.add(myService);
//        /*top250.add("YARD WORK");
//        top250.add("HOUSE CLEANING");
//        top250.add("BABY SITTING");
//        top250.add("PET WALKING");
//        top250.add("MUSIC SOCIAL");
//        top250.add("PIMER SOCIAL");
//        top250.add("CAR WASHING");*/
//
//        listDataChild.put(listDataHeader.get(0), top250);
//
//
//        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//
//                Toast.makeText(ServiceActivity.this, "You Selected "+top250.get(childPosition), Toast.LENGTH_SHORT).show();
//
//                MySharedPreference.setServicePosition(preferences,String.valueOf(top250.get(childPosition)));
//                return false;
//            }
//        });
//
//    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialog(getActivity(), message);
    }

//    @Override
//    public void onItemClick(int pos) {
//        selectedItem = pos;
//        if (selectedItem == 8) {
//            other_service.setVisibility(View.VISIBLE);
//        } else other_service.setVisibility(View.GONE);
//    }

    @Override
    public void pagedmClick(SubService object) {
        selectedItem = Integer.parseInt(object.getId());
        serviceAmount=object.getPrice();
        if (selectedItem == 8) {
            other_service.setVisibility(View.VISIBLE);
        } else other_service.setVisibility(View.GONE);
    }


}
