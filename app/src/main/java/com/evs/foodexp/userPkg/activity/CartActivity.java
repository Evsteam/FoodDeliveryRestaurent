package com.evs.foodexp.userPkg.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.evs.foodexp.R;
import com.evs.foodexp.cart.CartItemClick;
import com.evs.foodexp.commonPkg.DTO.MenuListDto;
import com.evs.foodexp.commonPkg.common.adapter.CartAdpter;

import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;

public class CartActivity extends AppCompatActivity implements AuthListener<MenuListDto>,
        CartItemClick<MenuListDto.Data>, AuthMsgListener {

    private Button placeOrderFromCartBtn;
    TextView tv_totalProduct;
    SharedPreferences preferences;
    RecyclerView recylerView;
    LinearLayoutManager layoutManager;
    LoaderProgress progress;
    APIViewModel viewModel;
    CartAdpter adpter;
    EditText et_specialNotes;
    CheckBox cb_check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Utills.StatusBarColour(CartActivity.this);
        preferences = PreferenceManager.getDefaultSharedPreferences(CartActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCartMenu);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        tv_totalProduct = (TextView) findViewById(R.id.tv_totalProduct);
        toolbarTitle.setText("CART");
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
        recylerView = findViewById(R.id.listView);
        cb_check = findViewById(R.id.cb_check);
        et_specialNotes = findViewById(R.id.et_specialNotes);
        placeOrderFromCartBtn = (Button) findViewById(R.id.placeOrderFromCartBtn);
        progress=new LoaderProgress(CartActivity.this);

        layoutManager = new LinearLayoutManager(CartActivity.this);
        recylerView.setLayoutManager(layoutManager);
        recylerView.setItemAnimator(new DefaultItemAnimator());



        viewModel = new ViewModelProvider(CartActivity.this).get(APIViewModel.class);

        recylerView.setLayoutManager(layoutManager);
        recylerView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(CartActivity.this,
                new LinearLayoutManager(CartActivity.this).getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(CartActivity.this, R.drawable.recycler_devider));
        recylerView.addItemDecoration(dividerItemDecoration);





        placeOrderFromCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("itemList",adpter.getlist());
                Log.e("amount",""+adpter.getTotalAmount());
                String noConctact="0";
                if(cb_check.isChecked()){
                    noConctact="1";
                }else noConctact="0";
                Intent intent = new Intent(CartActivity.this, CouponCodeActivity.class);
                intent.putExtra("totalAmount",""+adpter.getTotalAmount());
                intent.putExtra("itemList",adpter.getlist());
                intent.putExtra("specialNotes",et_specialNotes.getText().toString());
                intent.putExtra("noConctact",noConctact);
                startActivity(intent);

            }
        });


    }


    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onSuccess(LiveData<MenuListDto> data) {
        progress.dismiss();
        if (data.getValue().getStatus().equalsIgnoreCase("success")) {
            if(data.getValue().getData().size()>0) {
                 adpter = new CartAdpter(CartActivity.this, data.getValue().getData(), this);
                recylerView.setAdapter(adpter);
                tv_totalProduct.setText(data.getValue().getData().size()+" ITEM");
            }else onBackPressed();
        }

    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progress.dismiss();
//        if (msgResponce.getStatus().equalsIgnoreCase("success")) {
//            Toast.makeText(CartActivity.this, msgResponce.getMsg(), Toast.LENGTH_LONG).show();
//        } else Global.msgDialog(CartActivity.this, msgResponce.getMsg());
    }

    @Override
    public void onItemClick(MenuListDto.Data objecct) {
        if (objecct.getQuantity().equalsIgnoreCase("0")) {
            viewModel.addDeleteCartItem(SessionManager.get_user_id(preferences), objecct.getMenuId(), this);
            viewModel.getCart(SessionManager.get_user_id(preferences), this);
        } else
            viewModel.addToCart(SessionManager.get_user_id(preferences), objecct.getMenuId(), objecct.getQuantity(), this);

    }

    @Override
    public void update(String id, String quntity) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Global.isOnline(CartActivity.this)) {
            viewModel.getCart(SessionManager.get_user_id(preferences), this);
        } else Global.showDialog(CartActivity.this);
    }
}
