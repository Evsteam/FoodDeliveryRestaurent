package com.evs.foodexp.driverPkg.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import  androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.common.activity.StipeAccounteWeb;
import com.evs.foodexp.commonPkg.common.activity.WelcomeActivity;
import com.evs.foodexp.commonPkg.common.adapter.ListAdapter;
import com.evs.foodexp.commonPkg.common.fragment.ChangePasswordFragment;
import com.evs.foodexp.commonPkg.common.fragment.EditProfileFragment;
import com.evs.foodexp.commonPkg.common.fragment.HelpFragment;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;

import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.driverPkg.fragment.BagPurchase;
import com.evs.foodexp.driverPkg.fragment.Cards;
import com.evs.foodexp.driverPkg.fragment.DriverCashoutRequestFragment;
import com.evs.foodexp.driverPkg.fragment.OrderRequestFood;
import com.evs.foodexp.driverPkg.fragment.ServicesEarningFragment;
import com.evs.foodexp.driverPkg.fragment.DriverHomeFragment;
import com.evs.foodexp.driverPkg.fragment.OrdersFragment;
import com.evs.foodexp.driverPkg.fragment.SpecialEarningFragment;
import com.evs.foodexp.driverPkg.fragment.TogetEarningFragment;
import com.evs.foodexp.driverPkg.fragment.WNineForm;
import com.evs.foodexp.driverPkg.request_popup.NotificationDialogFood;
import com.evs.foodexp.driverPkg.request_popup.NotificationDialogService;
import com.evs.foodexp.driverPkg.request_popup.NotificationDialogSpecialRequest;
import com.evs.foodexp.driverPkg.request_popup.NotificationDialogToget;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.restaurentPkg.fragment.ReviewsFragment;
import com.evs.foodexp.restaurentPkg.fragment.TotalEarningFragment;
import com.evs.foodexp.userPkg.fragment.OrderHistoryFoodDeliveryFragment;
import com.evs.foodexp.userPkg.fragment.OrderHistoryFragment;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;

import java.util.LinkedHashMap;


public class DriverHomeActivity extends AppCompatActivity implements AuthListener<MsgResponse>, AuthMsgListener {

    FragmentManager manager;
    ListView list_nav;
    ListAdapter list_adapter;
    ActionBarDrawerToggle toggle;
    SharedPreferences prefs;
    LoaderProgress progress;
    DrawerLayout drawer;
    TextView toolbarTitle;
    Switch switch_btn;
    APIViewModel apiViewModel;
    LinearLayout layout_location;
    String notificationType, serviceName, bookingId, time, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(DriverHomeActivity.this);
        Utills.StatusBarColour(DriverHomeActivity.this);
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        setContentView(R.layout.activity_driver_home);
        progress = new LoaderProgress(DriverHomeActivity.this);
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) toolbar2.findViewById(R.id.text_toolbar);
        switch_btn = toolbar2.findViewById(R.id.switch_btn);

        switch_btn.setVisibility(View.VISIBLE);
        switch_btn.setChecked(SessionManager.get_onOff(prefs));
        toolbarTitle.setText("DASHBOARD");
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        manager = getSupportFragmentManager();
        drawer = (DrawerLayout) findViewById(R.id.drawerLayoutDriverHome);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        switch_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (SessionManager.get_onOff(prefs)) {
                    SessionManager.save_onOff(prefs, false);
                    apiViewModel.callStatusOnOff(SessionManager.get_user_id(prefs), "0", DriverHomeActivity.this);
                } else {
                    SessionManager.save_onOff(prefs, true);
                    apiViewModel.callStatusOnOff(SessionManager.get_user_id(prefs), "1", DriverHomeActivity.this);
                }

            }
        });

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar2, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        list_nav = (ListView) findViewById(R.id.list_nav_driver_side);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        TextView tv_loaction = (TextView) findViewById(R.id.tv_loaction);
        layout_location = (LinearLayout) findViewById(R.id.layout_location);

        tv_title.setText(SessionManager.get_name(prefs));
        tv_loaction.setText(SessionManager.get_address(prefs));
        if (!Global.validatename(SessionManager.get_address(prefs))) {
            layout_location.setVisibility(View.GONE);
        } else layout_location.setVisibility(View.VISIBLE);
        nevigationList();

        list_nav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updatedisplay(position);
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_driver_home,
                new DriverHomeFragment()).commit();

        Bundle extras = getIntent().getExtras();
        {
            if (extras != null) {
                notificationType = extras.getString("notificationType");
                if (notificationType != null) {
                    if (notificationType.equalsIgnoreCase("ServiceRequest")) {
                        servicePopup(extras.getString("serviceName"), extras.getString("bookingId"),
                                extras.getString("time"), extras.getString("address"), extras.getString("bookingDate"));
                    } else if (notificationType.equalsIgnoreCase("ToGetRequest")) {
                        toGetRequest(extras.getString("togetRequestId"), extras.getString("togetDriverRequestId"),
                                extras.getString("title"), extras.getString("address"), extras.getString("storeCity"));

                    } else if (notificationType.equalsIgnoreCase("SpecialRequest")) {
                        SpecialRequest(extras.getString("specialrequestId"), extras.getString("whatYouWant")
                                , extras.getString("address")
                                , extras.getString("userId")
                                , extras.getString("userName")
                                , extras.getString("userPhone")
                                , extras.getString("userImage")
                                , extras.getString("driverId")
                                , extras.getString("driverName")
                                , extras.getString("driverPhone")
                                , extras.getString("driverImage")
                                , extras.getString("price")
                                , extras.getString("deliveryFee")
                                , extras.getString("TIP")
                                , extras.getString("salesTax")
                                , extras.getString("latitude")
                                , extras.getString("totalAmount")
                                , extras.getString("status")
                                , extras.getString("transactionFee"));
                    } else if (notificationType.equalsIgnoreCase("FoodRequest")) {
                        foodOrderPopup(extras.getString("foodorderId"), extras.getString("foodrequestId"),
                                extras.getString("estimateDistace"), extras.getString("estimateTime"), extras.getString("dropLocation"),
                                extras.getString("pickupLoaction"), extras.getString("resturentName"));
                    }
                }


            }
        }

    }

    private void foodOrderPopup(String foodorderId, String foodrequestId,
                                String estimateDistace, String estimateTime,
                                String dropLocation, String pickupLoaction, String resturentName) {
        NotificationDialogFood newFragment = new NotificationDialogFood();
        Bundle bundle = new Bundle();
        newFragment.setArguments(bundle);
        bundle.putString("foodorderId", foodorderId);
        bundle.putString("foodrequestId", foodrequestId);
        bundle.putString("estimateDistace", estimateDistace);
        bundle.putString("estimateTime", estimateTime);
        bundle.putString("dropLocation", dropLocation);
        bundle.putString("pickupLoaction", pickupLoaction);
        bundle.putString("pickupLoaction", pickupLoaction);
        bundle.putString("resturentName", resturentName);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    private void toGetRequest(String togetRequestId, String togetDriverRequestId,
                              String title, String address, String storeCity) {
        NotificationDialogToget newFragment = new NotificationDialogToget();
        Bundle bundle = new Bundle();
        bundle.putString("togetRequestId", togetRequestId);
        bundle.putString("togetDriverRequestId", togetDriverRequestId);
        bundle.putString("title", title);
        bundle.putString("address", address);
        bundle.putString("storeCity", storeCity);
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    private void SpecialRequest(String specialrequestId, String whatYouWant,
                                String address
            , String userId
            , String userName
            , String userPhone
            , String userImage
            , String driverId
            , String driverName
            , String driverPhone
            , String driverImage
            , String price
            , String deliveryFee
            , String TIP
            , String salesTax
            , String latitude
            , String totalAmount
            , String status
            , String transactionFee) {
        NotificationDialogSpecialRequest newFragment = new NotificationDialogSpecialRequest();
        Bundle bundle = new Bundle();
        bundle.putString("specialrequestId", specialrequestId);
        bundle.putString("whatYouWant", whatYouWant);
        bundle.putString("address", address);
        bundle.putString("userName", userName);
        bundle.putString("userId", userId);
        bundle.putString("userPhone", userPhone);
        bundle.putString("userImage", userImage);
        bundle.putString("driverId", driverId);
        bundle.putString("driverName", driverName);
        bundle.putString("driverPhone", driverPhone);
        bundle.putString("driverImage", driverImage);
        bundle.putString("price", price);
        bundle.putString("deliveryFee", deliveryFee);
        bundle.putString("TIP", TIP);
        bundle.putString("salesTax", salesTax);
        bundle.putString("latitude", latitude);
        bundle.putString("status", status);
        bundle.putString("totalAmount", totalAmount);
        bundle.putString("transactionFee", transactionFee);
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "dialog");
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutDriverHome);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        switch (position) {
            case 0:
                fragment = new DriverHomeFragment();
                break;
            case 1:
                fragment = new EditProfileFragment();
                break;
            case 2:
//                fragment = new OrderHistoryFragment();
                fragment = new OrderHistoryFoodDeliveryFragment();
                break;
            case 3:
//                fragment = new OrdersFragment();
                fragment = new OrderRequestFood();
                break;
            case 4:
                fragment = new TotalEarningFragment();
                break;
//            case 5:
//                fragment = new ServicesEarningFragment();
//                break;
//            case 6:
//                fragment = new TogetEarningFragment();
//                break;
//            case 7:
//                fragment = new SpecialEarningFragment();
//                break;
            case 5:
                fragment = new ReviewsFragment();
                break;
            case 6:
                fragment = new DriverCashoutRequestFragment();
                break;
            case 7:
                fragment = new BagPurchase();
                break;
            case 8:
                fragment = new Cards();
                break;
            case 9:
                fragment = new WNineForm();
                break;
            case 10:
                fragment = new ChangePasswordFragment();
                break;
            case 11:
                fragment = new HelpFragment();
                break;

            case 12:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("");
                alertDialog.setMessage("Do you really want to Signout?");
                alertDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                apiViewModel.logout(SessionManager.get_user_id(prefs), DriverHomeActivity.this);

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
            manager.beginTransaction().replace(R.id.frame_container_driver_home, fragment)
                    .addToBackStack(null).commit();
        }
    }


    private void nevigationList() {
        LinkedHashMap<String, Integer> data = new LinkedHashMap<>();
        data.put(getString(R.string.dashboard), R.drawable.home);
        data.put(getString(R.string.edit_profile), R.drawable.edit_profile);
        data.put(getString(R.string.job_histoy), R.drawable.history);
        data.put(getString(R.string.orders), R.drawable.orders);
        data.put(getString(R.string.fooearnings), R.drawable.earnings);
//        data.put(getString(R.string.servceearnings), R.drawable.earnings);
//        data.put(getString(R.string.togetearnings), R.drawable.earnings);
//        data.put(getString(R.string.speOrderEr), R.drawable.earnings);
        data.put(getString(R.string.reviews), R.drawable.star_white);
        data.put(getString(R.string.cashouts), R.drawable.cashout);
        data.put(getString(R.string.bagPurchase), R.drawable.bag);
        data.put("Card", R.drawable.cash_card);
        data.put(getString(R.string.w_nine), R.drawable.w_nine);
        data.put(getString(R.string.change_password), R.drawable.lock);
        data.put(getString(R.string.help), R.drawable.help);
        data.put(getString(R.string.logout), R.drawable.logout);
        list_adapter = new ListAdapter(DriverHomeActivity.this, data);
        list_nav.setAdapter(list_adapter);
        list_nav.addFooterView(new View(DriverHomeActivity.this), null, true);
    }


    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onSuccess(LiveData<MsgResponse> data) {

        progress.dismiss();
        if (data.getValue().getStatus().equalsIgnoreCase("success")) {
            Toast.makeText(DriverHomeActivity.this, data.getValue().getMsg(), Toast.LENGTH_LONG).show();
        } else Global.msgDialog(DriverHomeActivity.this, data.getValue().getMsg());

    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialog(DriverHomeActivity.this, message);
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        SessionManager.dataclear(prefs);
        Intent intent = new Intent(DriverHomeActivity.this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    private void servicePopup(String serviceName, String bookingId, String time, String address, String bookingDate) {
        NotificationDialogService newFragment = new NotificationDialogService();
        Bundle bundle = new Bundle();
        bundle.putString("serviceName", serviceName);
        bundle.putString("bookingId", bookingId);
        bundle.putString("time", time);
        bundle.putString("address", address);
        bundle.putString("bookingDate", bookingDate);
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }


}
