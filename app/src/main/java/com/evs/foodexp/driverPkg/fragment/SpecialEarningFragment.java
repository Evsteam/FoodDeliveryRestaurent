package com.evs.foodexp.driverPkg.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.evs.foodexp.R;
import com.google.android.material.tabs.TabLayout;

public class SpecialEarningFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_earning_layout,container,false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarTitle.setText("Earning");
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout_driver_earnings);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.today)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.weekly)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.WHITE);
        drawable.setSize(1, 1);
        linearLayout.setDividerPadding(10);
        linearLayout.setDividerDrawable(drawable);


        viewPager = (ViewPager) view.findViewById(R.id.pager_earnings_driver);
        CharSequence Titles[] = {getResources().getString(R.string.today), getResources().getString(R.string.weekly)};
        VendorFoodOrdersPagerAdapter adapter2 = new VendorFoodOrdersPagerAdapter(getChildFragmentManager(), Titles, tabLayout.getTabCount());
        viewPager.setAdapter(adapter2);
        tabLayout.setupWithViewPager(viewPager);


        return view;
    }

    @Override
    public void onResume() {
        getActivity().setTitle("EARNINGS");
        super.onResume();
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
                    return new SpecialOrderTodayEarningFragment();
                case 1:
                    return new SpecialOrderWeeklyEarningFragment();
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


}
