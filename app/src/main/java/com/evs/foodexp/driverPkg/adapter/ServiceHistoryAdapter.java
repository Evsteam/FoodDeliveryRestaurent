package com.evs.foodexp.driverPkg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.commonPkg.DTO.MenuListDto;

import java.util.List;

public class ServiceHistoryAdapter extends RecyclerView.Adapter<ServiceHistoryAdapter.MyViewHolder> {
    Context context;
    List<MenuListDto.Data> list1;

    public ServiceHistoryAdapter(Context context, List<MenuListDto.Data> list1) {
        this.context = context;
        this.list1 = list1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.food_delivery_request_alert,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final MenuListDto.Data modal =list1.get(position);
        try {
            holder.foodNameTxt.setText(modal.getFoodName());
            holder.foodTypeTxt.setText(modal.getDescription());
            holder.foodPrice.setText(modal.getSpecialPrice());
            Glide.with(context).load(modal.getImage1()).into(holder.foodImg);
            //   Picasso.with(context).load(modal.getImage()).into(holder.foodImg);
            //  Picasso.get().load(modal.getImage()).placeholder(R.drawable.user).into(holder.foodImg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list1.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImg;
        TextView foodNameTxt,foodTypeTxt, foodPrice;
        RelativeLayout restaurantLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foodImg = (ImageView)itemView.findViewById(R.id.orderHistoryImg);
            foodNameTxt = (TextView) itemView.findViewById(R.id.orderHistoryRestaurantName);
            foodTypeTxt = (TextView) itemView.findViewById(R.id.dateTxtOrderHistory);
            restaurantLayout = (RelativeLayout)itemView.findViewById(R.id.orderHistoryData);
            foodPrice = (TextView)itemView.findViewById(R.id.orderHistoryRestaurantPrice);

        }
    }

}
