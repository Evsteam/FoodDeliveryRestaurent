package com.evs.foodexp.restaurentPkg.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import com.evs.foodexp.restaurentPkg.activity.AddMenuListActivity;
import com.evs.foodexp.utils.Global;

import java.util.List;

public class ManageMenuAdapter extends RecyclerView.Adapter<ManageMenuAdapter.MyViewHolder> {

    Context context;
    List<MenuListDto.Data> list;
    PagedItemClick<MenuListDto.Data> itemClick;

    public ManageMenuAdapter(Context context, List<MenuListDto.Data> list, PagedItemClick<MenuListDto.Data> itemClick) {
        this.context = context;
        this.list = list;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_menu_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.img_del.setVisibility(View.VISIBLE);
        holder.img_edit.setVisibility(View.VISIBLE);
        holder.dishNameRestaurant1.setText(list.get(position).getFoodName());
        holder.tv_quintity.setText(list.get(position).getQuantity());
        holder.itemOldPrice.setText("$ " + list.get(position).getPrice());
//        holder.itemOldPrice.setPaintFlags(holder.itemOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (Global.validatename(list.get(position).getSpecialPrice())) {
            if(Double.parseDouble(list.get(position).getPrice())>Double.parseDouble(list.get(position).getSpecialPrice())){
                holder.itemOldPrice.setTextColor(context.getResources().getColor(R.color.google_back));
                holder.itemOldPrice.setPaintFlags(holder.itemOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.itemSpecialPrice.setText("$ " + list.get(position).getSpecialPrice());
            }else
                holder.itemSpecialPrice.setText("$ " + list.get(position).getSpecialPrice());


        } else holder.itemSpecialPrice.setVisibility(View.GONE);

        Glide.with(context).load(list.get(position).getImage1()).into(holder.restaurentMenuItemImg1);
        if (list.get(position).getFoodType().equalsIgnoreCase("Non-veg")) {
            holder.foodType.setImageResource(R.drawable.nonveg);
        } else holder.foodType.setImageResource(R.drawable.veg);

        holder.img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.pagedmClick(list.get(position));
            }
        });

        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, AddMenuListActivity.class);
                intent.putExtra("categoryId",list.get(position).getCategoryId());
                intent.putExtra("foodId",list.get(position).getMenuId());
                intent.putExtra("foodName",list.get(position).getFoodName());
                intent.putExtra("price",list.get(position).getPrice());
                intent.putExtra("specialPrice",list.get(position).getSpecialPrice());
                intent.putExtra("description",list.get(position).getDescription());
                intent.putExtra("foodImage",list.get(position).getImage1());
                intent.putExtra("foodType",list.get(position).getFoodType());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dishNameRestaurant1, itemOldPrice, tv_quintity, itemSpecialPrice;
        ImageView restaurentMenuItemImg1, foodType, img_del,img_edit;
        RelativeLayout layout_addMenu;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            layout_addMenu = itemView.findViewById(R.id.layout_addMenu);
            dishNameRestaurant1 = itemView.findViewById(R.id.dishNameRestaurant1);
            itemOldPrice = itemView.findViewById(R.id.itemOldPrice);
            tv_quintity = itemView.findViewById(R.id.tv_quintity);
            itemSpecialPrice = itemView.findViewById(R.id.itemSpecialPrice);
            restaurentMenuItemImg1 = itemView.findViewById(R.id.restaurentMenuItemImg1);
            img_del = itemView.findViewById(R.id.img_del);
            foodType = itemView.findViewById(R.id.foodType);
            img_edit = itemView.findViewById(R.id.img_edit);


        }
    }
}
