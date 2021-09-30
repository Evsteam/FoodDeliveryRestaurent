package com.evs.foodexp.driverPkg.adapter.service_earning;

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

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.models.BookingModel;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class ServiceEarningPagedAdapter extends PagedListAdapter<BookingModel, ServiceEarningPagedAdapter.ItemViewHolder> {

    private Context mCtx;

    public ServiceEarningPagedAdapter(Context mCtx) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.today_earning_layout_list_adapter, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        BookingModel model = getItem(position);

        if (model != null) {
            holder.tv_name.setText(model.getFullName());
            holder.tv_price.setText(" $ "+new DecimalFormat("##.##").format(Double.parseDouble(model.getTotalAmount())));
            holder.tv_itemCount.setText(model.getServiceName());
            holder.tv_itemName.setText(model.getAddress());


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

        TextView tv_name, tv_price, tv_itemName,tv_itemCount;
        CircleImageView profileImage;
        RelativeLayout main_layout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_itemName = itemView.findViewById(R.id.tv_itemName);
            tv_itemCount = itemView.findViewById(R.id.tv_itemCount);
            main_layout = itemView.findViewById(R.id.main_layout);
        }
    }

    public String[] slipt(String dateTime) {
        String[] separated = dateTime.split(", ");
        return separated;
    }
}
