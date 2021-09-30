package com.evs.foodexp.userPkg.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import  androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.evs.foodexp.driverPkg.activity.DriverJobToGetDetailActivity;
import com.evs.foodexp.driverPkg.activity.ServiceJobDetailActivity;
import com.evs.foodexp.driverPkg.activity.SpecialDetailsScreen;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.userPkg.fragment.OrderHistoryFoodDeliveryFragment;
import com.evs.foodexp.userPkg.fragment.OrderHistoryFragment;
import com.evs.foodexp.userPkg.fragment.RestaurentListFragment;
import com.evs.foodexp.userPkg.fragment.UserHomeFragment;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;

import java.util.LinkedHashMap;

public class UserHomeActivity extends AppCompatActivity implements AuthMsgListener {

    FragmentManager manager;
    ListView list_nav;
    ListAdapter list_adapter;
    ActionBarDrawerToggle toggle;
    SharedPreferences prefs;
    ProgressDialog progress;
    DrawerLayout drawer;
    TextView toolbarTitle;
    ImageView toolbarImage;
    APIViewModel apiViewModel;
    Intent intent;
    String totalAmount = "", fooOrderId = "", foodOrderGroupId = "",
            driverContect = "", driverName = "", driverImage = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(UserHomeActivity.this);
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        Utills.StatusBarColour(UserHomeActivity.this);
//        databaseHelper = new DatabaseHelper(getApplicationContext());
        setContentView(R.layout.activity_user_home);

        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) toolbar2.findViewById(R.id.text_toolbar);
        toolbarImage = (ImageView) toolbar2.findViewById(R.id.imageToolbar);
        toolbarTitle.setText("");
        //   toolbarImage.setImageResource(R.drawable.ic_shopping_cart_white);
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        manager = getSupportFragmentManager();
//        queue = Volley.newRequestQueue(MainActivity.this);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayoutUserHome);
        progress = new ProgressDialog(UserHomeActivity.this);
        progress.setMessage("Loading");
        progress.setCancelable(false);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        //  final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutRestaurantHome);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar2, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        list_nav = (ListView) findViewById(R.id.list_nav);
        LinearLayout layout_location = findViewById(R.id.layout_location);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        TextView tv_location = (TextView) findViewById(R.id.tv_loaction);
        if (Global.validatename(SessionManager.get_address(prefs))) {
            layout_location.setVisibility(View.VISIBLE);
        } else layout_location.setVisibility(View.GONE);
        tv_title.setText(SessionManager.get_name(prefs));
        tv_location.setText(SessionManager.get_address(prefs));

        nevigationList();

        list_nav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updatedisplay(position);
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_user_home,
                new RestaurentListFragment()).commit();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            totalAmount = bundle.getString("totalAmount");
            fooOrderId = bundle.getString("fooOrderId");
            foodOrderGroupId = bundle.getString("foodOrderGroupId");
            driverContect = bundle.getString("driverContect");
            driverName = bundle.getString("driverName");
            driverImage = bundle.getString("driverImage");
            if (bundle.getString("notificationType").equalsIgnoreCase("foodAccept")) {
                Intent intent = new Intent(UserHomeActivity.this, BookingSuceess.class);
                intent.putExtra("totalAmount", totalAmount);
                intent.putExtra("fooOrderId", fooOrderId);
                intent.putExtra("foodOrderGroupId", foodOrderGroupId);
                intent.putExtra("driverContect", driverContect);
                intent.putExtra("driverName", driverName);
                intent.putExtra("driverImage", driverImage);
                intent.putExtra("type", "order");
                intent.putExtra("transactionFee", bundle.getString("transactionFee"));
                intent.putExtra("AdminAmount", bundle.getString("AdminAmount"));
                intent.putExtra("driverStripeAccount", bundle.getString("driverStripeAccount"));
                intent.putExtra("driverID", bundle.getString("driverID"));
                startActivity(intent);
            } else if (bundle.getString("notificationType").equalsIgnoreCase("ServiceAccept")) {
                Intent intent = new Intent(UserHomeActivity.this, ServiceJobDetailActivity.class);
                intent.putExtra("bookingId", bundle.getString("bookingId"));
                startActivity(intent);
            } else if (bundle.getString("notificationType").equalsIgnoreCase("ToGetRequestAccept")) {
                Intent intent = new Intent(UserHomeActivity.this, DriverJobToGetDetailActivity.class);
                intent.putExtra("togetrequestId", bundle.getString("togetRequestId"));
                startActivity(intent);
            } else if (bundle.getString("notificationType").equalsIgnoreCase("specialAccept")) {
               Double totalPrice=Double.parseDouble(bundle.getString("price"))+
                        Double.parseDouble(bundle.getString("TIP"))+Double.parseDouble(bundle.getString("salesTax"))+Double.parseDouble(bundle.getString("deliveryFee"));
                Intent intent=new Intent(UserHomeActivity.this, SpecialDetailsScreen.class);
                intent.putExtra("specialrequestId", bundle.getString("specialrequestId"));
                intent.putExtra("userId", bundle.getString("userId"));
                intent.putExtra("userName", bundle.getString("userName"));
                intent.putExtra("userPhone", bundle.getString("userPhone"));
                intent.putExtra("userImage", bundle.getString("userImage"));
                intent.putExtra("driverId", bundle.getString("driverId"));
                intent.putExtra("driverName", bundle.getString("driverName"));
                intent.putExtra("driverPhone", bundle.getString("driverPhone"));
                intent.putExtra("driverImage", bundle.getString("driverImage"));
                intent.putExtra("whatYouWant", bundle.getString("whatYouWant"));
                intent.putExtra("transactionFee", bundle.getString("transactionFee"));
                intent.putExtra("price", bundle.getString("price"));
                intent.putExtra("deliveryFee", bundle.getString("deliveryFee"));
                intent.putExtra("TIP", bundle.getString("TIP"));
                intent.putExtra("salesTax", bundle.getString("salesTax"));
                intent.putExtra("latitude", bundle.getString("latitude"));
                intent.putExtra("longitude","");
                intent.putExtra("address", bundle.getString("address"));
                intent.putExtra("totalAmount",totalPrice.toString());
                intent.putExtra("status", bundle.getString("status"));
                intent.putExtra("paymentStatus","1");
                intent.putExtra("created", "");
                startActivity(intent);
            }
        }

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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutUserHome);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        switch (position) {
            case 0:
                fragment = new RestaurentListFragment();
                break;
            case 1:
                fragment = new EditProfileFragment();
                break;
            case 2:
//                fragment = new OrderHistoryFragment();
                fragment = new OrderHistoryFoodDeliveryFragment();
                break;
//            case 3:
//                fragment = new RestaurentListFragment();
//                break;
//            case 4:
//                fragment = new ServiceFragment();
//                break;
//            case 5:
//                fragment = new ToGetActivity();
//                break;
//            case 6:
//                fragment = new FoodAnotherOptionActivity();
//                break;
            case 3:
                fragment = new ChangePasswordFragment();
                break;
            case 4:
                fragment = new HelpFragment();
                break;

            case 5:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("");
                alertDialog.setMessage("Do you really want to Signout?");
                alertDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                logout();

                                logout();
//                                Intent intent = new Intent(UserHomeActivity.this, WelcomeActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                finish();
//                                logout();

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
            manager.beginTransaction().replace(R.id.frame_container_user_home, fragment)
                    .addToBackStack(null).commit();
        }
    }

    private void logout() {
        apiViewModel.logout(SessionManager.get_user_id(prefs), this);
    }

    private void nevigationList() {
        LinkedHashMap<String, Integer> data = new LinkedHashMap<>();
        data.put(getString(R.string.dashboard), R.drawable.home);
        data.put(getString(R.string.edit_profile), R.drawable.edit_profile);
        data.put(getString(R.string.order_history), R.drawable.history);
//        data.put("Food", R.drawable.food_icon);
//        data.put(getString(R.string.service), R.drawable.service_menu);
//        data.put(getString(R.string.toget), R.drawable.tp_get);
//        data.put("Special Order", R.drawable.menu);
        data.put(getString(R.string.change_password), R.drawable.lock);
        data.put(getString(R.string.help), R.drawable.help);
        data.put(getString(R.string.logout), R.drawable.logout);
        list_adapter = new ListAdapter(UserHomeActivity.this, data);
        list_nav.setAdapter(list_adapter);
        list_nav.addFooterView(new View(UserHomeActivity.this), null, true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        prefs = PreferenceManager.getDefaultSharedPreferences(UserHomeActivity.this);
    }


    @Override
    public void setTitle(CharSequence title) {
        toolbarTitle.setText(title);
    }

    @Override
    public void onStarted() {
        progress.show();
    }


    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialog(UserHomeActivity.this, message);
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
//        if (msgResponce.getStatus().equalsIgnoreCase("success")) {
        SessionManager.dataclear(prefs);
        Intent intent = new Intent(UserHomeActivity.this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
//        }
    }
}
