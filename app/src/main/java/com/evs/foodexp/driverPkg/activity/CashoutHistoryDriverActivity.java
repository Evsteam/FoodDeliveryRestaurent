package com.evs.foodexp.driverPkg.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import  androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.viewModel.RestorentViewModel;
import com.evs.foodexp.models.CashoutModel;
import com.evs.foodexp.restaurentPkg.adapter.cashout.CashoutPagedAdapter;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;

public class CashoutHistoryDriverActivity extends AppCompatActivity implements AuthStatusListener {
    RestorentViewModel viewModel;
    LoaderProgress progress;
    SharedPreferences prefs;
    TextView tv_totalAmount;
    RecyclerView recylerView;
    LinearLayoutManager layoutManager;
    CashoutPagedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashout_history_driver);

        Utills.StatusBarColour(CashoutHistoryDriverActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCashout_history);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("CASHOUT HISTORY");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tv_totalAmount = findViewById(R.id.tv_totalAmount);
        recylerView = findViewById(R.id.recyclerView);

        prefs = PreferenceManager.getDefaultSharedPreferences(CashoutHistoryDriverActivity.this);
        progress = new LoaderProgress(CashoutHistoryDriverActivity.this);
        viewModel = new ViewModelProvider(CashoutHistoryDriverActivity.this).get(RestorentViewModel.class);
        tv_totalAmount.setText("$ "+SessionManager.get_wallet(prefs));


        layoutManager = new LinearLayoutManager(CashoutHistoryDriverActivity.this);
        recylerView.setLayoutManager(layoutManager);
        recylerView.setHasFixedSize(false);
        recylerView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(CashoutHistoryDriverActivity.this,
                new LinearLayoutManager(CashoutHistoryDriverActivity.this).getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(CashoutHistoryDriverActivity.this, R.drawable.recycler_devider));
        recylerView.addItemDecoration(dividerItemDecoration);

        if(Global.isOnline(CashoutHistoryDriverActivity.this)){
            viewModel.apiCashout(SessionManager.get_user_id(prefs),this);
        }else Global.showDialog(CashoutHistoryDriverActivity.this);


        adapter = new CashoutPagedAdapter(CashoutHistoryDriverActivity.this);

        viewModel.getCahouts().observe(this, new Observer<PagedList<CashoutModel>>() {
            @Override
            public void onChanged(PagedList<CashoutModel> reviewsModels) {
                progress.dismiss();
                adapter.submitList(reviewsModels);

            }
        });
        recylerView.setAdapter(adapter);
    }


    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onFailure(String message) {
        progress.show();
    }
}
