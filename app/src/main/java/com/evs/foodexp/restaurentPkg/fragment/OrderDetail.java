package com.evs.foodexp.restaurentPkg.fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.commonPkg.viewModel.RestorentViewModel;
import com.evs.foodexp.driverPkg.activity.MultipleMap;
import com.evs.foodexp.models.HistoryModel;
import com.evs.foodexp.models.OrderModel;
import com.evs.foodexp.repositry.ListResponse;
import com.evs.foodexp.userPkg.listeners.AuthStatusListener;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;
import com.google.android.material.tabs.TabLayout;

public class OrderDetail extends AppCompatActivity implements AuthStatusListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static String orderId = "", status = "",foodRequestId="",PaymentStatus="0";
    SharedPreferences prefe;
    LoaderProgress progress;
    RestorentViewModel viewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail_fragment_layout);

        Utills.StatusBarColour(OrderDetail.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderId = bundle.getString("orderId");
            status = bundle.getString("status");
            foodRequestId = bundle.getString("foodRequestId");
            PaymentStatus = bundle.getString("PaymentStatus");
        }
        viewModel = new ViewModelProvider(OrderDetail.this).get(RestorentViewModel.class);
        prefe = PreferenceManager.getDefaultSharedPreferences(OrderDetail.this);
        progress=new LoaderProgress(OrderDetail.this);
        toolbarTitle.setText("ORDER # " + orderId);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_item_detail);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.itemDetail)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.deliveryDetail)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.BLACK);
        drawable.setSize(1, 1);
        linearLayout.setDividerPadding(2);
        linearLayout.setDividerDrawable(drawable);


        viewPager = (ViewPager) findViewById(R.id.pager_item_detail);
        CharSequence Titles[] = {getResources().getString(R.string.itemDetail), getResources().getString(R.string.deliveryDetail)};
        VendorFoodOrdersPagerAdapter adapter2 = new VendorFoodOrdersPagerAdapter(getSupportFragmentManager(), Titles, tabLayout.getTabCount());
        viewPager.setAdapter(adapter2);
        tabLayout.setupWithViewPager(viewPager);


        viewModel.getOrderDetails().observe(OrderDetail.this, new Observer<OrderModel>() {
            @Override
            public void onChanged(OrderModel orderModel) {
                progress.dismiss();
            }
        });


    }

    @Override
    public void onStarted() {
        progress.show();
    }


    @Override
    public void onFailure(String message) {
        progress.dismiss();
    }


    public class VendorFoodOrdersPagerAdapter extends FragmentStatePagerAdapter {
        CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when FriendsPagerAdapter is created
        int NumbOfTabs; // Store the number of tabs, this will also be passed when the FriendsPagerAdapter is created


        // Build a Constructor and assign the passed Values to appropriate values in the class
        public VendorFoodOrdersPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
            super(fm);
            this.Titles = mTitles;
            this.NumbOfTabs = mNumbOfTabsumb;

        }

        //This method return the fragment for the every position in the View Pager
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new OrderItemDetailsFragment();
                case 1:
                    return new  FoodOrderDetails();
                default:
                    return null;
            }
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        // This method return the titles for the Tabs in the Tab Strip
        @Override
        public CharSequence getPageTitle(int position) {
            return Titles[position];
        }

        // This method return the Number of tabs for the tabs Strip

        @Override
        public int getCount() {
            return NumbOfTabs;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Global.isOnline(OrderDetail.this)){
            viewModel.apifoodDetails(SessionManager.get_user_id(prefe), orderId, this);
        }else Global.showDialog(OrderDetail.this);
    }
}
