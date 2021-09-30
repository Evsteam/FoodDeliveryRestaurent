package com.evs.foodexp.userPkg.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
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
import com.evs.foodexp.commonPkg.DTO.RestaurantListDto;

import com.evs.foodexp.userPkg.activity.RestaurantMenuActivity;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.MyViewHolder> {

    Context context;
    List<RestaurantListDto.Data> list;
    SharedPreferences preferences;

    public RestaurantListAdapter(Context context, List<RestaurantListDto.Data> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.restaurant_list_adapter,null);
        return new RestaurantListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final RestaurantListDto.Data modal =list.get(position);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            holder.foodNameTxt.setText(modal.getFullName());
            holder.foodTypeTxt.setText(modal.getEmail());

            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color1 = generator.getRandomColor();
            TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().width(60)
                    .height(60).endConfig().round();

            try {
                TextDrawable drawable2 = builder.build("" + modal.getFullName().toUpperCase().charAt(0), color1);
                Glide.with(context).load(modal.getImage()).placeholder(drawable2).dontAnimate().error(drawable2).dontAnimate().into(holder.foodImg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        holder.restaurantLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RestaurantMenuActivity.class);
                intent.putExtra("userId",String.valueOf(modal.getUserId()));
                intent.putExtra("name",String.valueOf(modal.getFullName()));
                intent.putExtra("image",String.valueOf(modal.getCompanyBackground()));
                context.startActivity(intent);
            }
        });

        if(modal.getFoodTag().equalsIgnoreCase("Veg")){
            holder.restaurantType.setImageResource(R.drawable.veg);
        } else if(modal.getFoodTag().equalsIgnoreCase("Non-veg")){
            holder.restaurantType.setImageResource(R.drawable.nonveg);
        }else if(modal.getFoodTag().equalsIgnoreCase("None")){
            holder.restaurantType.setImageResource(R.drawable.nonveg);
        }


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
       ImageView foodImg,restaurantType;
       TextView foodNameTxt,foodTypeTxt;
       RelativeLayout restaurantLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foodImg = (ImageView)itemView.findViewById(R.id.img_profile1);
            foodNameTxt = (TextView) itemView.findViewById(R.id.foodTitleTxt);
            foodTypeTxt = (TextView) itemView.findViewById(R.id.availablePlaces);
            restaurantType = (ImageView) itemView.findViewById(R.id.restaurantType);
            restaurantLayout = (RelativeLayout)itemView.findViewById(R.id.foodLayoutRelative);

        }
    }

}
