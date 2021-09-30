package com.evs.foodexp.driverPkg.adapter.spot.spotservice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.evs.foodexp.R;
import com.evs.foodexp.models.OrderModel;

public class SpotPagedAdapter extends PagedListAdapter<OrderModel, SpotPagedAdapter.ItemViewHolder> {

    private Context mCtx;

    public SpotPagedAdapter(Context mCtx) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.spot_schidule_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        OrderModel model = getItem(position);

        if (model != null) {
            holder.tv_dateTime.setText(model.getUserName());
            holder.tv_status.setText(model.getTotalAmount());
        }
    }

    private static DiffUtil.ItemCallback<OrderModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<OrderModel>() {
                @Override
                public boolean areItemsTheSame(OrderModel oldItem, OrderModel newItem) {
                    return oldItem.getFoodorderId().equalsIgnoreCase(newItem.getFoodorderId());
                }

                @Override
                public boolean areContentsTheSame(OrderModel oldItem, OrderModel newItem) {
                    return oldItem.equals(newItem);
                }
            };

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tv_dateTime, tv_status;
        RelativeLayout main_layout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_dateTime = itemView.findViewById(R.id.tv_dateTime);
            tv_status = itemView.findViewById(R.id.tv_status);

            main_layout = itemView.findViewById(R.id.main_layout);
        }
    }

    public String[] slipt(String dateTime) {
        String[] separated = dateTime.split(", ");
        return separated;
    }
}
