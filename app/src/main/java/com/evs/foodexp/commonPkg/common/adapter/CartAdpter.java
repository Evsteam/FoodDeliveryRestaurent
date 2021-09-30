package com.evs.foodexp.commonPkg.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.cart.CartItemClick;
import com.evs.foodexp.commonPkg.DTO.MenuListDto;
import com.evs.foodexp.utils.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;


public class CartAdpter extends RecyclerView.Adapter<CartAdpter.myViewHolder> {
    Context mcContext;
    List<MenuListDto.Data> dataList;
    CartItemClick<MenuListDto.Data> itemClick;

    public CartAdpter(Context mContext, List<MenuListDto.Data> dataList, CartItemClick<MenuListDto.Data> itemClick) {
        this.mcContext = mContext;
        this.dataList = dataList;
        this.itemClick = itemClick;
    }

    public double getTotalAmount() {
        double totalAmoount = 0;
        for (int i = 0; i < dataList.size(); i++) {
            if (Global.validatename(dataList.get(i).getSpecialPrice())) {
                if (Double.parseDouble(dataList.get(i).getSpecialPrice()) > 0) {
                    totalAmoount = totalAmoount + Double.parseDouble(dataList.get(i).getSpecialPrice()) * Double.parseDouble(dataList.get(i).getQuantity());
                } else
                    totalAmoount = totalAmoount + Double.parseDouble(dataList.get(i).getPrice()) * Double.parseDouble(dataList.get(i).getQuantity());
            } else
                totalAmoount = totalAmoount + Double.parseDouble(dataList.get(i).getSpecialPrice()) * Double.parseDouble(dataList.get(i).getQuantity());
        }
        return totalAmoount;

    }

    public String getlist() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < dataList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                DecimalFormat formater = new DecimalFormat("#.##");
                jsonObject.put("id", dataList.get(i).getMenuId());
                jsonObject.put("name", dataList.get(i).getFoodName());
                if (Global.validatename(dataList.get(i).getSpecialPrice())) {
                    if (Double.parseDouble(dataList.get(i).getSpecialPrice()) > 0) {

                        jsonObject.put("price", formater.format(Double.parseDouble(dataList.get(i).getSpecialPrice())));
                    } else
                        jsonObject.put("price", formater.format(Double.parseDouble(dataList.get(i).getPrice())));
                } else
                    jsonObject.put("price", formater.format(Double.parseDouble(dataList.get(i).getPrice())));

                jsonObject.put("quantity", dataList.get(i).getQuantity());
                jsonObject.put("resturentId", dataList.get(i).getResturentId());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return jsonArray.toString();

    }


    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_menu_list_adapter, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final myViewHolder holder, int position) {

        holder.setData(dataList.get(position));


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    class myViewHolder extends RecyclerView.ViewHolder {
        TextView cartProductName, priceFoodCart, tv_quintity;
        RelativeLayout main_layout;
        ImageView cartItemProductImg1, img_minus, img_plus, cartItemProductType;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            cartProductName = itemView.findViewById(R.id.cartProductName);
            priceFoodCart = itemView.findViewById(R.id.priceFoodCart);
            cartItemProductType = itemView.findViewById(R.id.cartItemProductType);
            tv_quintity = itemView.findViewById(R.id.tv_quintity);
            cartItemProductImg1 = itemView.findViewById(R.id.cartItemProductImg1);
            img_minus = itemView.findViewById(R.id.img_minus);
            img_plus = itemView.findViewById(R.id.img_plus);
            main_layout = itemView.findViewById(R.id.main_layout);


        }

        private void setData(final MenuListDto.Data model) {
            DecimalFormat formater = new DecimalFormat("#.##");
            cartProductName.setText(model.getFoodName());

            if (Global.validatename(model.getSpecialPrice())) {
                if (Double.parseDouble(model.getSpecialPrice()) > 0) {
                    priceFoodCart.setText("Price:  $ " +formater.format(Double.parseDouble( model.getSpecialPrice())));
                } else priceFoodCart.setText("Prize:  $ " + formater.format(Double.parseDouble(model.getPrice())));

            } else priceFoodCart.setText("Price:  $ " + formater.format(Double.parseDouble(model.getPrice())));

            tv_quintity.setText(model.getQuantity());

            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color1 = generator.getRandomColor();
            TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().width(60)
                    .height(60).endConfig().round();

            try {
                TextDrawable drawable2 = builder.build("" + model.getFoodName().toUpperCase().charAt(0), color1);
                Glide.with(mcContext).load(model.getImage1()).placeholder(drawable2).dontAnimate().error(drawable2).dontAnimate().into(cartItemProductImg1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            img_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    model.setQuantity(String.valueOf(Integer.parseInt(model.getQuantity()) + 1));
                    itemClick.onItemClick(model);
                    notifyDataSetChanged();
                }
            });
            img_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    model.setQuantity(String.valueOf(Integer.parseInt(model.getQuantity()) - 1));
                    itemClick.onItemClick(model);
                    notifyDataSetChanged();

                }
            });

            if (model.getFoodType().equalsIgnoreCase("Non-veg")) {
                cartItemProductType.setImageResource(R.drawable.nonveg);
            } else cartItemProductType.setImageResource(R.drawable.veg);


        }
    }
}