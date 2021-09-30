package com.evs.foodexp.restaurentPkg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evs.foodexp.R;
import com.evs.foodexp.models.SpotSchduleModel;

import java.util.ArrayList;

public class SpotSchudelAdapter extends RecyclerView.Adapter<SpotSchudelAdapter.MyViewHolder> {

    Context context;
    ArrayList<SpotSchduleModel> listData;

    public SpotSchudelAdapter(Context context, ArrayList<SpotSchduleModel> listData) {
        this.context = context;
        this.listData = listData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spot_schidule_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.tv_dateTime.setText(listData.get(position).getReviewId());
            holder.tv_status.setText(listData.get(position).getUserId());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_dateTime,tv_status;
        RelativeLayout main_layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_dateTime=itemView.findViewById(R.id.tv_dateTime);
            tv_status=itemView.findViewById(R.id.tv_status);
            main_layout=itemView.findViewById(R.id.main_layout);
        }
    }

}
