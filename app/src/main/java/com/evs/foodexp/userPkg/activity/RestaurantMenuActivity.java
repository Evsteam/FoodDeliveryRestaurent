package com.evs.foodexp.userPkg.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import  androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.cart.Cart;
import com.evs.foodexp.cart.CartItemClick;
import com.evs.foodexp.cart.CartViewModel;
import com.evs.foodexp.commonPkg.DTO.MenuListDto;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.restaurentPkg.adapter.CategoryAdapter;
import com.evs.foodexp.restaurentPkg.adapter.PagedItemClick;
import com.evs.foodexp.userPkg.adapter.RestaurentMenuAdapter;
import com.evs.foodexp.models.CategoryModel;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;

import java.util.List;

public class RestaurantMenuActivity extends AppCompatActivity implements AuthListener<MenuListDto>, AuthMsgListener, CartItemClick<MenuListDto.Data>, PagedItemClick<CategoryModel> {
    TextView toolbarTitle;
    ImageView toolbarImage;
    //  View foodSelection;
    RecyclerView foodListRecycle, menuTypeRecycle;
    GridView gridView;
    RestaurentMenuAdapter adapter;
    private ProgressDialog progressDialog;
    APIViewModel apiViewModel;
    Button breakfast, quickByteBtn, desertBtn;
    String restaurantId, name, scoverImage;
    CartViewModel cartViewModel;
    TextView textCartItemCount,tv_title,tv_noDataFound;
    SharedPreferences prefs;
    ImageView coverImage;
    MenuListDto.Data objecct;


    private void populateGridView(List<MenuListDto.Data> spacecraftList) {
        gridView = findViewById(R.id.grid_view);
        adapter = new RestaurentMenuAdapter(this, spacecraftList, this);
        gridView.setAdapter(adapter);
        if(spacecraftList.size()==0){
            tv_noDataFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);
        Utills.StatusBarColour(RestaurantMenuActivity.this);
        // gridView = (GridView)findViewById(R.id.grid_view);
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        prefs = PreferenceManager.getDefaultSharedPreferences(RestaurantMenuActivity.this);
        foodListRecycle = (RecyclerView) findViewById(R.id.normalRestaurantMenuVertical);
        coverImage = (ImageView) findViewById(R.id.coverImage);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_noDataFound = (TextView) findViewById(R.id.tv_noDataFound);
        menuTypeRecycle = (RecyclerView) findViewById(R.id.menuTypeRestaurantBtnRecycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        menuTypeRecycle.setLayoutManager(layoutManager);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            restaurantId = bundle.getString("userId");
            name = bundle.getString("name");
            scoverImage = bundle.getString("image");
        }

        cartViewModel = new ViewModelProvider(RestaurantMenuActivity.this).get(CartViewModel.class);
        foodListRecycle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);
        toolbarImage = (ImageView) toolbar.findViewById(R.id.imageToolbar);
        breakfast = (Button) findViewById(R.id.breakfastBtn);
        quickByteBtn = (Button) findViewById(R.id.quickByteBtn);
        desertBtn = (Button) findViewById(R.id.desertBtn);

        //  foodSelection = (RecyclerView)findViewById(R.id.foodSelectionView);
        toolbarTitle.setText(name);
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(RestaurantMenuActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);


//        apiViewModel.category("category",this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //  foodSelection.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cartViewModel.getAllnotes().observe(this, new Observer<List<Cart>>() {
            @Override
            public void onChanged(List<Cart> carts) {
                setupBadge(carts.size());
            }
        });

        tv_title.setText(name);
//        Glide.with(RestaurantMenuActivity.this).load(scoverImage)
//                .placeholder(R.drawable.restaurent_back).into(coverImage);

    }


    @Override
    public void onStarted() {
        progressDialog.show();
    }

    @Override
    public void onSuccess(LiveData<MenuListDto> data) {
        progressDialog.dismiss();
        populateGridView(data.getValue().getData());
        setupBadge(Integer.parseInt(data.getValue().getTotalCartItem()));
        menuTypeRecycle.setAdapter(new CategoryAdapter(RestaurantMenuActivity.this, data.getValue().getCategoryList(),this));
        // foodListRecycle.setAdapter(new RestaurentMenuAdapter(getApplicationContext(),data.getValue().getData()));

    }

    @Override
    public void onFailure(String message) {
        progressDialog.dismiss();
        Global.msgDialog(RestaurantMenuActivity.this, message);
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progressDialog.dismiss();
        if (msgResponce.getStatus().equalsIgnoreCase("success")) {
//            Toast.makeText(RestaurantMenuActivity.this, msgResponce.getMsg(), Toast.LENGTH_LONG).show();
            setupBadge(Integer.parseInt(msgResponce.getTotalCartItem()));
        }else  if (msgResponce.getStatus().equalsIgnoreCase("deleteCart")) {
            apiViewModel.addToCart(SessionManager.get_user_id(prefs), objecct.getMenuId(), objecct.getQuantity(), this);
      }else  if (!msgResponce.getStatus().equalsIgnoreCase("noteDeleted")) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(RestaurantMenuActivity.this);
            alertbox.setTitle("Replace Cart Item?");
            alertbox.setMessage(msgResponce.getMsg());
            alertbox.setPositiveButton(getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            apiViewModel.deleteCart(SessionManager.get_user_id(prefs), RestaurantMenuActivity.this);
                        }
                    });

            alertbox.setNegativeButton(getResources().getString(R.string.cancle),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            apiViewModel.menuList("menulist", restaurantId, SessionManager.get_user_id(prefs),"", RestaurantMenuActivity.this);
                        }

                    });
            alertbox.show();
        }
    }


    @Override
    public void onItemClick(MenuListDto.Data objecct) {
        this.objecct=objecct;
        if (objecct.getQuantity().equalsIgnoreCase("0")) {
            apiViewModel.addDeleteCartItem(SessionManager.get_user_id(prefs), objecct.getMenuId(), this);
        } else
            apiViewModel.addToCart(SessionManager.get_user_id(prefs), objecct.getMenuId(), objecct.getQuantity(), this);
    }

    //
    @Override
    public void update(String id, String quntity) {
//        cartViewModel.updateCart(quntity, id);
//        if (quntity.equalsIgnoreCase("0")) {
//            cartViewModel.getliveData(id).observe(RestaurantMenuActivity.this, new Observer<Cart>() {
//                @Override
//                public void onChanged(@Nullable Cart note) {
//                    if (note != null) {
//                        cartViewModel.delete(note);
//                    }
//                }
//            });
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cart_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);


        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                if (!textCartItemCount.getText().toString().equalsIgnoreCase("0")) {
                    Intent intent = new Intent(RestaurantMenuActivity.this, CartActivity.class);
                    startActivity(intent);
                } else Toast.makeText(RestaurantMenuActivity.this,"Your Cart Is Empty",Toast.LENGTH_LONG).show();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void setupBadge(int mCartItemCount) {
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                    textCartItemCount.setText("0");
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(Global.isOnline(RestaurantMenuActivity.this)){
            apiViewModel.menuList("menulist", restaurantId, SessionManager.get_user_id(prefs),"", this);
        }else Global.showDialog(RestaurantMenuActivity.this);
    }

    @Override
    public void pagedmClick(CategoryModel object) {
        Intent intent=new Intent(RestaurantMenuActivity.this,CategoryMenuMenuActivity.class);
        intent.putExtra("restaurantId",restaurantId);
        intent.putExtra("categoryID",object.getId());
        intent.putExtra("name",object.getName());
        startActivity(intent);
    }
}
