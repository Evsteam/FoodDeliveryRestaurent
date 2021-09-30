package com.evs.foodexp.userPkg.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import androidx.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.cart.CartItemClick;
import com.evs.foodexp.commonPkg.DTO.MenuListDto;
import com.evs.foodexp.utils.Global;

import java.util.List;


public class RestaurentMenuAdapter extends BaseAdapter {

    List<MenuListDto.Data> list;
    Context context;
    SharedPreferences preferences;
    CartItemClick<MenuListDto.Data> itemClick;

    public RestaurentMenuAdapter(Context context, List<MenuListDto.Data> list, CartItemClick<MenuListDto.Data> itemClick) {
        this.context = context;
        this.list = list;
        this.itemClick = itemClick;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.restaurant_menu_list_adapter, viewGroup, false);
        }

        final TextView foodName = (TextView) view.findViewById(R.id.dishNameRestaurant1);
        TextView price = (TextView) view.findViewById(R.id.itemOldPrice);
        final TextView addItemInMenu1 = (TextView) view.findViewById(R.id.addItemInMenu1);

        final TextView tv_quintity = (TextView) view.findViewById(R.id.tv_quintity);
        final TextView specialPrice = (TextView) view.findViewById(R.id.itemSpecialPrice);
        final LinearLayout quntity_layout = (LinearLayout) view.findViewById(R.id.quntity_layout);
        ImageView foodImg = (ImageView) view.findViewById(R.id.restaurentMenuItemImg1);
        ImageView img_minus = (ImageView) view.findViewById(R.id.img_minus);
        ImageView img_plus = (ImageView) view.findViewById(R.id.img_plus);
        ImageView foodType = (ImageView) view.findViewById(R.id.foodType);
        RelativeLayout foodLayout = (RelativeLayout) view.findViewById(R.id.particularFoodLayout);

        final MenuListDto.Data modal = list.get(i);
//////////////////////////////////////////////////////////////////////////////////
        try {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            foodName.setText(modal.getFoodName());
            tv_quintity.setText(modal.getQuantity());
            price.setText("$ " + modal.getPrice());

            Glide.with(context).load(modal.getImage1()).into(foodImg);
            if (modal.getFoodType() != null) {
                if (modal.getFoodType().equalsIgnoreCase("Non-veg")) {
                    foodType.setImageResource(R.drawable.nonveg);
                } else foodType.setImageResource(R.drawable.veg);
            }

            if (Global.validatename(modal.getSpecialPrice())) {
                price.setTextColor(context.getResources().getColor(R.color.google_back));
                price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                specialPrice.setText("$ " + modal.getSpecialPrice());
            } else specialPrice.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (modal.getQuantity().equalsIgnoreCase("0")) {
            addItemInMenu1.setVisibility(View.VISIBLE);
            quntity_layout.setVisibility(View.GONE);
        } else {
            addItemInMenu1.setVisibility(View.GONE);
            quntity_layout.setVisibility(View.VISIBLE);
        }

//        foodLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, CouponCodeActivity.class);
//                intent.putExtra("menuId", modal.getMenuId());
//                intent.putExtra("foodName", modal.getFoodName());
//                intent.putExtra("prize", modal.getPrice());
//                intent.putExtra("sPrize", modal.getSpecialPrice());
//                context.startActivity(intent);
//            }
//        });


        addItemInMenu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemInMenu1.setVisibility(View.GONE);
                quntity_layout.setVisibility(View.VISIBLE);
                modal.setQuantity("1");
                itemClick.onItemClick(modal);
                notifyDataSetChanged();

            }
        });
        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                modal.setQuantity(String.valueOf(Integer.parseInt(modal.getQuantity()) + 1));
                itemClick.onItemClick(modal);
                notifyDataSetChanged();
            }
        });
        img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(modal.getQuantity()) > 1) {
                    modal.setQuantity(String.valueOf(Integer.parseInt(modal.getQuantity()) - 1));
                    itemClick.onItemClick(modal);
                    notifyDataSetChanged();
                } else {
                    addItemInMenu1.setVisibility(View.VISIBLE);
                    quntity_layout.setVisibility(View.GONE);
                    modal.setQuantity(String.valueOf(Integer.parseInt(modal.getQuantity()) - 1));
                    itemClick.onItemClick(modal);
                    notifyDataSetChanged();
                }

            }
        });

        return view;
    }


}
