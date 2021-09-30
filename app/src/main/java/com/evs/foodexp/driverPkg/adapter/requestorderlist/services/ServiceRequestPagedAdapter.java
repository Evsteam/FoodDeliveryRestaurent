package com.evs.foodexp.driverPkg.adapter.requestorderlist.services;

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
import com.evs.foodexp.models.BookingModel;
import com.evs.foodexp.restaurentPkg.adapter.PagedItemClick;

import de.hdodenhof.circleimageview.CircleImageView;

public class ServiceRequestPagedAdapter extends PagedListAdapter<BookingModel, ServiceRequestPagedAdapter.ItemViewHolder> {

    private Context mCtx;
    private PagedItemClick<BookingModel> itemClick;

    public ServiceRequestPagedAdapter(Context mCtx, PagedItemClick<BookingModel> itemClick) {
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

        final BookingModel model = getItem(position);

        if (model != null) {
            holder.tv_title.setText(model.getFullName());
            holder.tv_price.setText(" $ "+model.getTotalAmount());
            holder.tv_address.setText(model.getServiceName());
            holder.tv_date.setText(model.getBookingDate());
            holder.tv_time.setText(model.getSlote());
            holder.locationHistoryImg.setVisibility(View.GONE);

            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color1 = generator.getRandomColor();
            TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().width(60)
                    .height(60).endConfig().round();

            try {
                TextDrawable drawable2 = builder.build("" + model.getFullName().toUpperCase().charAt(0), color1);
                Glide.with(mCtx).load(model.getImage()).placeholder(drawable2).dontAnimate().error(drawable2).dontAnimate().into(holder.profileImage);
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

    private static DiffUtil.ItemCallback<BookingModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<BookingModel>() {
                @Override
                public boolean areItemsTheSame(BookingModel oldItem, BookingModel newItem) {
                    return oldItem.getBookingId().equalsIgnoreCase(newItem.getBookingId());
                }

                @Override
                public boolean areContentsTheSame(BookingModel oldItem, BookingModel newItem) {
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
        String[] separated = dateTime.split(", ");
        return separated;
    }
}
