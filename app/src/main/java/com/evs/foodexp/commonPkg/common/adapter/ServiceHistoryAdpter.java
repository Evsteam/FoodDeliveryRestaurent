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
import com.evs.foodexp.restaurentPkg.adapter.PagedItemClick;
import com.evs.foodexp.models.HistoryModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ServiceHistoryAdpter extends RecyclerView.Adapter<ServiceHistoryAdpter.myViewHolder> {
    Context mcContext;
    List<HistoryModel> dataList;
    PagedItemClick<HistoryModel> itemClick;

    public ServiceHistoryAdpter(Context mContext, List<HistoryModel> dataList , PagedItemClick<HistoryModel> itemClick) {
        this.mcContext = mContext;
        this.dataList = dataList;
        this.itemClick = itemClick;
    }




    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_layout_adapter, parent, false);
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
        TextView tv_title, tv_dateTime, tv_price,tv_address;
        RelativeLayout main_layout;
        CircleImageView profileImage;
        ImageView img_location;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_dateTime = itemView.findViewById(R.id.tv_dateTime);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_price = itemView.findViewById(R.id.tv_price);
            img_location = itemView.findViewById(R.id.img_location);
            profileImage = itemView.findViewById(R.id.profileImage);
            main_layout = itemView.findViewById(R.id.main_layout);
            img_location.setVisibility(View.GONE);

        }

        private void setData(final HistoryModel model) {
            tv_title.setText(model.getUserName());
            tv_price.setText(" - $ "+model.getEPrice());
            tv_address.setText(model.getServiceName());
            tv_dateTime.setText(model.getBookingDate()+"-"+model.getSlote());

            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color1 = generator.getRandomColor();
            TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().width(60)
                    .height(60).endConfig().round();

            try {
                TextDrawable drawable2 = builder.build("" + model.getUserName().toUpperCase().charAt(0), color1);
                Glide.with(mcContext).load(model.getUserImage()).placeholder(drawable2).dontAnimate().error(drawable2).dontAnimate().into(profileImage);
            } catch (Exception e) {
                e.printStackTrace();
            }

            main_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick.pagedmClick(model);
                }
            });

        }
    }
}