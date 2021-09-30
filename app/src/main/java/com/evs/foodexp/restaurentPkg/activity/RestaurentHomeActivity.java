package com.evs.foodexp.restaurentPkg.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.common.activity.WelcomeActivity;
import com.evs.foodexp.commonPkg.common.adapter.ListAdapter;
import com.evs.foodexp.commonPkg.common.fragment.ChangePasswordFragment;
import com.evs.foodexp.commonPkg.common.fragment.EditProfileFragment;
import com.evs.foodexp.commonPkg.common.fragment.HelpFragment;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.restaurentPkg.fragment.ManageMenu;
import com.evs.foodexp.restaurentPkg.fragment.OrdersFragment;
import com.evs.foodexp.restaurentPkg.fragment.RestaurentHomeFragment;
import com.evs.foodexp.restaurentPkg.fragment.ReviewsFragment;
import com.evs.foodexp.restaurentPkg.fragment.TotalEarningFragment;
import com.evs.foodexp.userPkg.fragment.OrderHistoryFoodDeliveryFragment;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import  androidx.lifecycle.ViewModelProvider;

import java.util.LinkedHashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class RestaurentHomeActivity extends AppCompatActivity implements AuthMsgListener {

    FragmentManager manager;
    ListView list_nav;
    ListAdapter list_adapter;
    ActionBarDrawerToggle toggle;
    SharedPreferences prefs;
    ProgressDialog progress;
    TextView tv_title;
    CircleImageView u_img;
    DrawerLayout drawer;
    TextView toolbarTitle;
    APIViewModel apiViewModel;
    LinearLayout layout_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(RestaurentHomeActivity.this);
        Utills.StatusBarColour(RestaurentHomeActivity.this);
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        setContentView(R.layout.activity_restaurent_home);

        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) toolbar2.findViewById(R.id.text_toolbar);
        //toolbarTitle.setText("HOME");
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        manager = getSupportFragmentManager();
//        queue = Volley.newRequestQueue(MainActivity.this);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayoutRestaurantHome);
        progress = new ProgressDialog(RestaurentHomeActivity.this);
        progress.setMessage("Loading");
        progress.setCancelable(false);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        //  final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutRestaurantHome);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar2, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        list_nav = (ListView) findViewById(R.id.list_nav);
        layout_location = (LinearLayout) findViewById(R.id.layout_location);
        TextView tv_loaction = (TextView) findViewById(R.id.tv_loaction);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(SessionManager.get_name(prefs));
        tv_loaction.setText(SessionManager.get_address(prefs));
        //  u_img = (CircleImageView) findViewById(R.id.u_img);
            if(Global.validatename(SessionManager.get_address(prefs))){
                layout_location.setVisibility(View.VISIBLE);
            }else   layout_location.setVisibility(View.GONE);
        nevigationList();

        list_nav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updatedisplay(position);
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_restaurant_home,
                new RestaurentHomeFragment()).commit();


    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (manager.getBackStackEntryCount() > 0) {
                super.onBackPressed();
            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("");
                alertDialog.setMessage("Do you really want to Exit?");
                alertDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                alertDialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alertDialog.show();
            }
        }
    }

    public void updatedisplay(int position) {
        Fragment fragment = null;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutRestaurantHome);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        switch (position) {
            case 0:
                fragment = new RestaurentHomeFragment();
                break;
            case 1:
                fragment = new EditProfileFragment();
                break;
            case 2:
                fragment = new ManageMenu();
                break;
            case 3:
                fragment = new OrdersFragment();
                break;
            case 4:
                fragment = new OrderHistoryFoodDeliveryFragment();
                break;
            case 5:
                fragment = new TotalEarningFragment();
                break;
            case 6:
                fragment = new ReviewsFragment();
                break;
            case 7:
                Intent intentCashout = new Intent(RestaurentHomeActivity.this, CashoutActivity.class);
                startActivity(intentCashout);
                break;
            case 8:
                fragment = new ChangePasswordFragment();
                break;
            case 9:
                fragment = new HelpFragment();
                break;

            case 10:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("");
                alertDialog.setMessage("Do you really want to Signout?");
                alertDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                logout();

//                                Intent intent = new Intent(RestaurentHomeActivity.this, WelcomeActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                finish();
                                logout();

                            }
                        });
                alertDialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alertDialog.show();
                break;
        }
        if (fragment != null) {
            manager.beginTransaction().replace(R.id.frame_container_restaurant_home, fragment)
                    .addToBackStack(null).commit();
        }
    }

    private void logout() {
        apiViewModel.logout(SessionManager.get_user_id(prefs), RestaurentHomeActivity.this);
    }

    private void nevigationList() {
        LinkedHashMap<String, Integer> data = new LinkedHashMap<>();
        data.put(getString(R.string.dashboard), R.drawable.home);
        data.put(getString(R.string.edit_profile), R.drawable.edit_profile);
        data.put(getString(R.string.manage_menu), R.drawable.menu);
        data.put(getString(R.string.orders), R.drawable.orders);
        data.put(getString(R.string.order_history), R.drawable.history);
        data.put(getString(R.string.total_earnings), R.drawable.earnings);
        data.put(getString(R.string.reviews), R.drawable.star_white);
        data.put(getString(R.string.cashouts), R.drawable.cashout);
        data.put(getString(R.string.change_password), R.drawable.lock);
        data.put(getString(R.string.help), R.drawable.help);
        data.put(getString(R.string.logout), R.drawable.logout);
        list_adapter = new ListAdapter(RestaurentHomeActivity.this, data);
        list_nav.setAdapter(list_adapter);
        list_nav.addFooterView(new View(RestaurentHomeActivity.this), null, true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        prefs = PreferenceManager.getDefaultSharedPreferences(RestaurentHomeActivity.this);
        //tv_title.setText(SessionManager.get_fname(prefs));
        //   Glide.with(RestaurentHomeActivity.this).load(SessionManager.get_image(prefs)).placeholder(R.drawable.placeholder_user).into(u_img);
    }


    @Override
    public void onStarted() {
        progress.show();
    }


    @Override
    public void onFailure(String message) {
        progress.show();
        Global.msgDialog(RestaurentHomeActivity.this,message);
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        SessionManager.dataclear(prefs);
        Intent intent = new Intent(RestaurentHomeActivity.this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
