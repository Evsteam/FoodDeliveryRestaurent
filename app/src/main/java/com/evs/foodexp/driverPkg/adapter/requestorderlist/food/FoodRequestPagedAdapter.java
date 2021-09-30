package com.evs.foodexp.driverPkg.adapter.requestorderlist.food;

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
import com.evs.foodexp.models.RequestModel;
import com.evs.foodexp.restaurentPkg.adapter.PagedItemClick;

import de.hdodenhof.circleimageview.CircleImageView;

public class FoodRequestPagedAdapter extends PagedListAdapter<RequestModel, FoodRequestPagedAdapter.ItemViewHolder> {

    private Context mCtx;
    private PagedItemClick<RequestModel> itemClick;

    public FoodRequestPagedAdapter(Context mCtx, PagedItemClick<RequestModel> itemClick) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.food_delivery_history_adapter_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        final RequestModel model = getItem(position);

        if (model != null) {
            holder.tv_title.setText(model.getResturentName());
            holder.tv_price.setText(" $ "+model.getTotalAmount());
            holder.tv_address.setText(model.getAddress());
            holder.tv_date.setText(slipt(model.getCreated())[0]);
            holder.tv_time.setText(slipt(model.getCreated())[1]);
            holder.locationHistoryImg.setVisibility(View.GONE);

            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color1 = generator.getRandomColor();
            TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().width(60)
                    .height(60).endConfig().round();

            try {
                TextDrawable drawable2 = builder.build("" + model.getResturentName().toUpperCase().charAt(0), color1);
                Glide.with(mCtx).load(model.getResturentImage()).placeholder(drawable2).dontAnimate().error(drawable2).dontAnimate().into(holder.profileImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            if(model.getFoodDetails().size()==1){
//                holder.tv_itemName.setText(model.getFoodDetails().get(0).getQuintity()+" "+model.getFoodDetails().get(0).getName());
//            }else {
//                String items="";
//                for (int i = 0; i <model.getFoodDetails().size() ; i++) {
//                    if(i==0){
//                        items= model.getFoodDetails().get(i).getQuintity()+" "+model.getFoodDetails().get(0).getName()+" ";
//                    }else items= items+model.getFoodDetails().get(i).getQuintity()+" "+model.getFoodDetails().get(0).getName();
//                }
//                holder.tv_itemName.setText(items);
//            }

            holder.main_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick.pagedmClick(model);
                }
            });
        }
    }

    private static DiffUtil.ItemCallback<RequestModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<RequestModel>() {
                @Override
                public boolean areItemsTheSame(RequestModel oldItem, RequestModel newItem) {
                    return oldItem.getFoodrequestId().equalsIgnoreCase(newItem.getFoodrequestId());
                }

                @Override
                public boolean areContentsTheSame(RequestModel oldItem, RequestModel newItem) {
                    return oldItem.equals(newItem);
                }
            };

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title, tv_price, tv_date,tv_time,tv_address,tv_product;
        CircleImageView profileImage;
        RelativeLayout main_layout;
        ImageView timeImg,locationHistoryImg;

        public ItemViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_product = itemView.findViewById(R.id.tv_product);
            locationHistoryImg = itemView.findViewById(R.id.locationHistoryImg);
            timeImg = itemView.findViewById(R.id.timeImg);
            main_layout = itemView.findViewById(R.id.main_layout);
        }
    }

    public String[] slipt(String dateTime) {
        String[] separated = dateTime.split("\\s+");
        return separated;
    }
}
