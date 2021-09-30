package com.evs.foodexp.restaurentPkg.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import  androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evs.foodexp.R;
import com.evs.foodexp.cart.Cart;
import com.evs.foodexp.cart.CartItemClick;
import com.evs.foodexp.cart.CartViewModel;
import com.evs.foodexp.commonPkg.DTO.MenuListDto;
import com.evs.foodexp.commonPkg.viewModel.APIViewModel;
import com.evs.foodexp.commonPkg.viewModel.AuthListener;
import com.evs.foodexp.commonPkg.viewModel.AuthMsgListener;
import com.evs.foodexp.repositry.MsgResponse;
import com.evs.foodexp.userPkg.adapter.RestaurentMenuRestAdapter;
import com.evs.foodexp.utils.Global;
import com.evs.foodexp.utils.LoaderProgress;
import com.evs.foodexp.utils.SessionManager;
import com.evs.foodexp.utils.Utills;

import java.util.List;

public class RestCategoryMenuMenuActivity extends AppCompatActivity implements
        AuthListener<MenuListDto>, AuthMsgListener, CartItemClick<MenuListDto.Data> {
    TextView toolbarTitle;
    ImageView toolbarImage;
    //  View foodSelection;
    RecyclerView foodListRecycle, menuTypeRecycle;
    GridView gridView;
    RestaurentMenuRestAdapter adapter;
    LoaderProgress progress;
    APIViewModel apiViewModel;
    Button breakfast, quickByteBtn, desertBtn;
    String restaurantId, name,categoryID;
    CartViewModel cartViewModel;
    TextView textCartItemCount;
    SharedPreferences prefs;
    TextView tv_notFound;

    private void populateGridView(List<MenuListDto.Data> spacecraftList) {
        gridView = findViewById(R.id.grid_view);
        tv_notFound = (TextView) findViewById(R.id.tv_notFound);
        adapter = new RestaurentMenuRestAdapter(this, spacecraftList);
        gridView.setAdapter(adapter);
        if (spacecraftList.size()>0) {
            tv_notFound.setVisibility(View.GONE);
        }else tv_notFound.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_category_menu);
        Utills.StatusBarColour(RestCategoryMenuMenuActivity.this);
        // gridView = (GridView)findViewById(R.id.grid_view);
        apiViewModel = new ViewModelProvider(this).get(APIViewModel.class);
        prefs = PreferenceManager.getDefaultSharedPreferences(RestCategoryMenuMenuActivity.this);
        foodListRecycle = (RecyclerView) findViewById(R.id.normalRestaurantMenuVertical);
        menuTypeRecycle = (RecyclerView) findViewById(R.id.menuTypeRestaurantBtnRecycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        menuTypeRecycle.setLayoutManager(layoutManager);
        progress=new LoaderProgress(RestCategoryMenuMenuActivity.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            restaurantId = bundle.getString("restaurantId");
            categoryID = bundle.getString("categoryID");
            name = bundle.getString("name");
        }
        cartViewModel = new ViewModelProvider(RestCategoryMenuMenuActivity.this).get(CartViewModel.class);
        foodListRecycle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRestaurantMenu);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.text_toolbar);

        toolbarImage = (ImageView) toolbar.findViewById(R.id.imageToolbar);

        breakfast = (Button) findViewById(R.id.breakfastBtn);
        quickByteBtn = (Button) findViewById(R.id.quickByteBtn);
        desertBtn = (Button) findViewById(R.id.desertBtn);
        //  foodSelection = (RecyclerView)findViewById(R.id.foodSelectionView);
        toolbarTitle.setText(name);
        setSupportActionBar(toolbar);


        apiViewModel.menuList("menulist", restaurantId, SessionManager.get_user_id(prefs), categoryID,this);
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

    }


    @Override
    public void onStarted() {
        progress.show();
    }

    @Override
    public void onSuccess(LiveData<MenuListDto> data) {
        progress.dismiss();
        populateGridView(data.getValue().getData());
        setupBadge(Integer.parseInt(data.getValue().getTotalCartItem()));
//        menuTypeRecycle.setAdapter(new CategoryAdapter(CategoryMenuMenuActivity.this, data.getValue().getCategoryList()));
        // foodListRecycle.setAdapter(new RestaurentMenuAdapter(getApplicationContext(),data.getValue().getData()));

    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Global.msgDialog(RestCategoryMenuMenuActivity.this, message);
    }

    @Override
    public void onSuccessMsg(MsgResponse msgResponce) {
        progress.dismiss();
        if (msgResponce.getStatus().equalsIgnoreCase("success")) {
            Toast.makeText(RestCategoryMenuMenuActivity.this, msgResponce.getMsg(), Toast.LENGTH_LONG).show();
            setupBadge(Integer.parseInt(msgResponce.getTotalCartItem()));
        } else Global.msgDialog(RestCategoryMenuMenuActivity.this, msgResponce.getMsg());
    }


    @Override
    public void onItemClick(MenuListDto.Data objecct) {
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.cart_menu, menu);
//
//        final MenuItem menuItem = menu.findItem(R.id.action_cart);
//
//        View actionView = MenuItemCompat.getActionView(menuItem);
//        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
//
//
//        actionView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onOptionsItemSelected(menuItem);
//            }
//        });
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_cart:
//                if (!textCartItemCount.getText().toString().equalsIgnoreCase("0")) {
//                    Intent intent = new Intent(RestCategoryMenuMenuActivity.this, CartActivity.class);
//                    startActivity(intent);
//                } else Toast.makeText(RestCategoryMenuMenuActivity.this,"Your Cart Is Empty",Toast.LENGTH_LONG).show();
//                break;
//        }
//
//
//        return super.onOptionsItemSelected(item);
//    }

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
}
