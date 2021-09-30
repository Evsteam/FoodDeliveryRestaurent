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
import com.evs.foodexp.models.ItemModel;

import java.util.ArrayList;

public class ItemAdpter extends RecyclerView.Adapter<ItemAdpter.myViewHolder> {
    Context mcContext;
    ArrayList<ItemModel> dataList;

    public ItemAdpter(Context mContext, ArrayList<ItemModel> dataListk) {
        this.mcContext = mContext;
        this.dataList = dataListk;
    }


    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
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
        TextView tv_title, tv_quintity, tv_prize;
        RelativeLayout main_layout;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_quintity = itemView.findViewById(R.id.tv_quintity);
            tv_prize = itemView.findViewById(R.id.tv_prize);
            main_layout = itemView.findViewById(R.id.main_layout);


        }

        private void setData(final ItemModel model) {
            tv_title.setText(model.getName());
            tv_prize.setText("$ "+model.getPrize());
            tv_quintity.setText(model.getQuintity());


        }
    }
}

