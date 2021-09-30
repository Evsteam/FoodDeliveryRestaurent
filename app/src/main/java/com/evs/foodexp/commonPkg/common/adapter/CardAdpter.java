package com.evs.foodexp.commonPkg.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evs.foodexp.R;
import com.evs.foodexp.models.CardModel;
import com.evs.foodexp.restaurentPkg.adapter.AdapterItemClick;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class CardAdpter extends RecyclerView.Adapter<CardAdpter.MyViewHolder> {
    Context mcContext;
    List<CardModel> dataList;
    AdapterItemClick itemClick;

    public CardAdpter(Context mContext, List<CardModel> dataList , AdapterItemClick itemClick) {
        this.mcContext = mContext;
        this.dataList = dataList;
        this.itemClick = itemClick;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.setData(dataList.get(position));

        holder.main_layout.setId(position);
        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.main_layout.getId();
                itemClick.onItemClick(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_serviceName, tv_location, tv_status,tv_time;
        CircleImageView img_profile;
        RelativeLayout main_layout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_profile = itemView.findViewById(R.id.img_profile);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_serviceName = itemView.findViewById(R.id.tv_serviceName);
            tv_location = itemView.findViewById(R.id.tv_location);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_status = itemView.findViewById(R.id.tv_status);
            main_layout = itemView.findViewById(R.id.main_layout);


        }

        private void setData(CardModel model) {
//            tv_title.setText(model.getFullName());
//            tv_serviceName.setText(model.getServiceName());
//            tv_location.setText(model.getAddress());
//            tv_time.setText(model.getCreated());

//            ColorGenerator generator = ColorGenerator.MATERIAL;
//            int color1 = generator.getRandomColor();
//            TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().width(60)
//                    .height(60).endConfig().round();
//
//            try {
//                TextDrawable drawable2 = builder.build("" + model.getFullName().toUpperCase().charAt(0), color1);
//                Glide.with(mcContext).load(model.getImage()).placeholder(drawable2).dontAnimate().error(drawable2).dontAnimate().into(img_profile);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

//            if(model.getStatus().equalsIgnoreCase("0")){
//               tv_status.setText("Pending");
//            }else if(model.getStatus().equalsIgnoreCase("1")){
//                tv_status.setText("Accepted");
//            }else if(model.getStatus().equalsIgnoreCase("2")){
//                tv_status.setText("Completed");
//            }else if(model.getStatus().equalsIgnoreCase("3")){
//                tv_status.setText("Decline");
//            }


        }
    }
}