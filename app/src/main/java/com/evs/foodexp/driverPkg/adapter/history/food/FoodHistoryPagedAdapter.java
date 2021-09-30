package com.evs.foodexp.driverPkg.adapter.history.food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.models.HistoryModel;
import com.evs.foodexp.restaurentPkg.adapter.PagedItemClick;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class FoodHistoryPagedAdapter extends PagedListAdapter<HistoryModel, FoodHistoryPagedAdapter.ItemViewHolder> {

    private Context mCtx;
    private PagedItemClick<HistoryModel> itemClick;

    public FoodHistoryPagedAdapter(Context mCtx, PagedItemClick<HistoryModel> itemClick) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.order_history_layout_adapter, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        final HistoryModel model = getItem(position);

        if (model != null) {
            holder.tv_title.setText(model.getUserName());

            double tamount=Double.parseDouble(model.getEPrice())+Double.parseDouble(model.getTransactionFee());
            holder.tv_price.setText(" - $ "+new DecimalFormat("##.##").format(tamount));
            holder.tv_address.setText(model.getAddress());
            holder.tv_dateTime.setText(model.getCreated());

            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color1 = generator.getRandomColor();
            TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().width(60)
                    .height(60).endConfig().round();

            try {
                TextDrawable drawable2 = builder.build("" + model.getUserName().toUpperCase().charAt(0), color1);
                Glide.with(mCtx).load(model.getUserImage()).placeholder(drawable2).dontAnimate().error(drawable2).dontAnimate().into(holder.profileImage);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(model.getFoodDetails().size()>0) {
                if (model.getFoodDetails().size() == 1) {
                    holder.tv_address.setText(model.getFoodDetails().get(0).getQuintity() + " " + model.getFoodDetails().get(0).getName());
                } else {
                    String items = "";
                    for (int i = 0; i < model.getFoodDetails().size(); i++) {
                        if (i == 0) {
                            items = model.getFoodDetails().get(i).getQuintity() + " " + model.getFoodDetails().get(0).getName() + " ";
                        } else
                            items = items + model.getFoodDetails().get(i).getQuintity() + " " + model.getFoodDetails().get(0).getName();
                    }
                    holder.tv_address.setText(items);
                }


            }else holder.tv_address.setText(model.getWahtUwant());

            holder.main_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick.pagedmClick(model);
                }
            });
        
        }
    }

    private static DiffUtil.ItemCallback<HistoryModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<HistoryModel>() {
                @Override
                public boolean areItemsTheSame(HistoryModel oldItem, HistoryModel newItem) {
                    return oldItem.getFoodorderId().equalsIgnoreCase(newItem.getFoodorderId());
                }

                @Override
                public boolean areContentsTheSame(HistoryModel oldItem, HistoryModel newItem) {
                    return oldItem.equals(newItem);
                }
            };

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title, tv_dateTime, tv_price,tv_address;
        RelativeLayout main_layout;
        CircleImageView profileImage;
        ImageView img_location;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_dateTime = itemView.findViewById(R.id.tv_dateTime);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_price = itemView.findViewById(R.id.tv_price);
            profileImage = itemView.findViewById(R.id.profileImage);
            main_layout = itemView.findViewById(R.id.main_layout);
            img_location = itemView.findViewById(R.id.img_location);
            img_location.setVisibility(View.GONE);
        }
    }

    public String[] slipt(String dateTime) {
        String[] separated = dateTime.split("\\s+");
        return separated;
    }
}
