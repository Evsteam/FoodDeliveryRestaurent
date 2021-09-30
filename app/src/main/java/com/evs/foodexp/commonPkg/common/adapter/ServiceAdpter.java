package com.evs.foodexp.commonPkg.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evs.foodexp.R;
import com.evs.foodexp.restaurentPkg.adapter.PagedItemClick;
import com.evs.foodexp.models.SubService;

import java.util.List;


public class ServiceAdpter extends RecyclerView.Adapter<ServiceAdpter.myViewHolder> {
    Context mcContext;
    List<SubService> dataList;
    int selectedvalue = -1;
    PagedItemClick<SubService> itemClick;


    public ServiceAdpter(Context mContext, List<SubService> dataList,PagedItemClick<SubService> itemClick) {
        this.mcContext = mContext;
        this.dataList = dataList;
        this.itemClick = itemClick;
    }

//    public double getTotalAmount(){
//        double totalAmoount=0;
//        for (int i = 0; i <dataList.size() ; i++) {
//            totalAmoount=totalAmoount+Double.parseDouble(dataList.get(i).getSpecialPrice())*Double.parseDouble(dataList.get(i).getQuantity());
//        }
//        return totalAmoount;
//
//    }
//    public String getlist(){
//        JSONArray jsonArray = new JSONArray();
//        for (int i = 0; i <dataList.size() ; i++) {
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("id", dataList.get(i).getMenuId());
//                jsonObject.put("name", dataList.get(i).getFoodName());
//                jsonObject.put("price", dataList.get(i).getSpecialPrice());
//                jsonObject.put("quantity", dataList.get(i).getQuantity());
//                jsonObject.put("resturentId", dataList.get(i).getResturentId());
//                jsonArray.put(jsonObject);
//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//
//        return jsonArray.toString();
//
//    }


    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expanded_itemview, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final myViewHolder holder, final int position) {


        holder.tv_subtitle.setText(dataList.get(position).getName());
        holder.img_check.setChecked(selectedvalue==position);

        holder.main_layout.setId(position);
        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedvalue =holder.main_layout.getId();
                itemClick.pagedmClick(dataList.get(position));
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    class myViewHolder extends RecyclerView.ViewHolder {
        TextView tv_subtitle;
        LinearLayout main_layout;
        RadioButton img_check;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_subtitle = itemView.findViewById(R.id.tv_subtitle);
            img_check = itemView.findViewById(R.id.img_check);
            main_layout = itemView.findViewById(R.id.main_layout);


        }


    }
}