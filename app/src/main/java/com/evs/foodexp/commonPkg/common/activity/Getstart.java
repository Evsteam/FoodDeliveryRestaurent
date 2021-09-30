package com.evs.foodexp.commonPkg.common.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.common.fragment.LoginFragment;
import com.evs.foodexp.commonPkg.common.fragment.SignUpFragment;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.Utills;
import com.google.android.material.tabs.TabLayout;

public class Getstart extends AppCompatActivity implements TabLayout.OnTabSelectedListener , View.OnTouchListener{

    SharedPreferences prefs;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    CharSequence Titles[] = {"Sign In", "Sign Up"};
    ScrollView mScrollView;
    int fragmentPosition =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getstart);

        prefs= PreferenceManager.getDefaultSharedPreferences(Getstart.this);
        Utills.StatusBarColour(Getstart.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("Get Started Now");
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
        mScrollView = (ScrollView) findViewById(R.id.mScrollView);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Sign in"));
        tabLayout.addTab(tabLayout.newTab().setText("Sign Up Here"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.pager);
        BarberDashboardPagerAdapter adapter = new BarberDashboardPagerAdapter(getSupportFragmentManager(), Titles, tabLayout.getTabCount());
        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        //Adding onTabSelectedListener to swipe views
        tabLayout.addOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            fragmentPosition=Integer.parseInt(bundle.getString("pos"));
        }
        viewPager.setCurrentItem(fragmentPosition);

        if(!Global.GpsEnable(Getstart.this)){
            Global.showGPSDisabledAlertToUser(Getstart.this);
        }

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public class BarberDashboardPagerAdapter extends FragmentStatePagerAdapter {
        CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when FriendsPagerAdapter is created
        int NumbOfTabs; // Store the number of tabs, this will also be passed when the FriendsPagerAdapter is created


        // Build a Constructor and assign the passed Values to appropriate values in the class
        public BarberDashboardPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
            super(fm);
            this.Titles = mTitles;
            this.NumbOfTabs = mNumbOfTabsumb;

        }

        //This method return the fragment for the every position in the View Pager
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new LoginFragment();
                case 1:
                    return new SignUpFragment();
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
    protected void onDestroy() {
        tabLayout.removeOnTabSelectedListener(this);
        super.onDestroy();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int dragthreshold = 30;

        int downX = 0;

        int downY = 0;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getRawX();

                downY = (int) event.getRawY();

                break;

            case MotionEvent.ACTION_MOVE:
                int distanceX = Math.abs((int) event.getRawX() - downX);

                int distanceY = Math.abs((int) event.getRawY() - downY);

                if (distanceY > distanceX && distanceY > dragthreshold) {
                    viewPager.getParent().requestDisallowInterceptTouchEvent(false);

                    mScrollView.getParent().requestDisallowInterceptTouchEvent(true);
                } else if (distanceX > distanceY && distanceX > dragthreshold) {
                    viewPager.getParent().requestDisallowInterceptTouchEvent(true);

                    mScrollView.getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
            case MotionEvent.ACTION_UP:
                mScrollView.getParent().requestDisallowInterceptTouchEvent(false);

                viewPager.getParent().requestDisallowInterceptTouchEvent(false);

                break;
        }

        return false;

    }
}
