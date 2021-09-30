package com.evs.foodexp.driverPkg.adapter.requestorderlist.specialRequest;

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
import com.evs.foodexp.models.SpecialOrderModel;
import com.evs.foodexp.restaurentPkg.adapter.PagedItemClick;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class SpecialRequestPagedAdapter extends PagedListAdapter<SpecialOrderModel, SpecialRequestPagedAdapter.ItemViewHolder> {

    private Context mCtx;
    private PagedItemClick<SpecialOrderModel> itemClick;

    public SpecialRequestPagedAdapter(Context mCtx, PagedItemClick<SpecialOrderModel> itemClick) {
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

        final SpecialOrderModel model = getItem(position);

        if (model != null) {
            holder.tv_title.setText(model.getWhatYouWant());
            String totalAmount=new DecimalFormat("##.##").format(Double.parseDouble(model.getTotalAmount())+
                    Double.parseDouble(model.getTransactionFee()));
            holder.tv_price.setText(" $ "+totalAmount);
            holder.tv_address.setText(model.getAddress());
            holder.tv_date.setText(slipt(model.getCreated())[0]);
            holder.tv_time.setText(slipt(model.getCreated())[1]);
            holder.locationHistoryImg.setVisibility(View.GONE);

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

    private static DiffUtil.ItemCallback<SpecialOrderModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<SpecialOrderModel>() {
                @Override
                public boolean areItemsTheSame(SpecialOrderModel oldItem, SpecialOrderModel newItem) {
                    return oldItem.getSpecialrequestId().equalsIgnoreCase(newItem.getSpecialrequestId());
                }

                @Override
                public boolean areContentsTheSame(SpecialOrderModel oldItem, SpecialOrderModel newItem) {
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
