package com.evs.foodexp.driverPkg.adapter.toget_earning;

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
import com.evs.foodexp.models.GoGetModel;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class TogetEarningPagedAdapter extends PagedListAdapter<GoGetModel, TogetEarningPagedAdapter.ItemViewHolder> {

    private Context mCtx;

    public TogetEarningPagedAdapter(Context mCtx) {
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

        GoGetModel model = getItem(position);

        if (model != null) {
            holder.tv_name.setText(model.getUserName());

            holder.tv_price.setText("   $ "+new DecimalFormat("##.##").format(Double.parseDouble(model.getTotalAmount())));
            holder.tv_itemCount.setText(model.getWahtUwant());
            holder.tv_itemName.setText(model.getAddress());


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
        }
    }

    private static DiffUtil.ItemCallback<GoGetModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<GoGetModel>() {
                @Override
                public boolean areItemsTheSame(GoGetModel oldItem, GoGetModel newItem) {
                    return oldItem.getTogetRequestId().equalsIgnoreCase(newItem.getTogetRequestId());
                }

                @Override
                public boolean areContentsTheSame(GoGetModel oldItem, GoGetModel newItem) {
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
