package com.evs.foodexp.restaurentPkg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.models.ItemModel;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderItemDetailAdapter extends RecyclerView.Adapter<OrderItemDetailAdapter.MyViewHolder> {
    Context context;
    ArrayList<ItemModel> list;
    String restorentName;

    public OrderItemDetailAdapter(Context context, ArrayList<ItemModel> list,String restorentName) {
        this.context = context;
        this.list = list;
        this.restorentName = restorentName;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_detail_adapter,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DecimalFormat formater = new DecimalFormat("#.##");
            holder.tv_itemName.setText(list.get(position).getName());
            holder.tv_restaurentName.setText(restorentName);
            holder.tv_itemPrice.setText("$ "+formater.format(Double.parseDouble(list.get(position).getPrize())));
            holder.tv_quintity.setText(list.get(position).getQuintity());
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().width(60)
                .height(60).endConfig().round();
        try {
            TextDrawable drawable2 = builder.build("" + list.get(position).getName().toUpperCase().charAt(0), color1);
            Glide.with(context).load(list.get(position).getImage_1()).placeholder(drawable2).dontAnimate().error(drawable2).dontAnimate().into(holder.item_image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(list.get(position).getFoodTag().equalsIgnoreCase("Non-veg")){
            holder.img_foodType.setImageResource(R.drawable.nonveg);
        }else  holder.img_foodType.setImageResource(R.drawable.veg);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView item_image;
        TextView tv_itemName,tv_restaurentName,tv_itemPrice,tv_quintity;
        ImageView img_foodType;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_image=itemView.findViewById(R.id.item_image);
            tv_itemName=itemView.findViewById(R.id.tv_itemName);
            tv_restaurentName=itemView.findViewById(R.id.tv_restaurentName);
            tv_itemPrice=itemView.findViewById(R.id.tv_itemPrice);
            tv_quintity=itemView.findViewById(R.id.tv_quintity);
            img_foodType=itemView.findViewById(R.id.img_foodType);
        }
    }

}
