package com.evs.foodexp.driverPkg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.models.RequestModel;
import com.evs.foodexp.restaurentPkg.adapter.PagedItemClick;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestOrderAdapter extends RecyclerView.Adapter<RequestOrderAdapter.MyViewHolder> {
    Context context;
    List<RequestModel> list1;
    PagedItemClick<RequestModel> itemClick;

    public RequestOrderAdapter(Context context, List<RequestModel> list1,PagedItemClick<RequestModel> itemClick) {
        this.context = context;
        this.list1 = list1;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.in_progress_order_adapter,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.tv_name.setText(list1.get(position).getResturentName());
        holder.tv_amount.setText("$ "+list1.get(position).getTotalAmount());
        holder.tv_time.setText(Time24To12(list1.get(position).getCreated()));


        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().width(60)
                .height(60).endConfig().round();
        try {
            TextDrawable drawable2 = builder.build("" + list1.get(position).getResturentName().toUpperCase().charAt(0), color1);
            Glide.with(context).load(list1.get(position).getResturentImage())
                    .placeholder(drawable2).dontAnimate().error(drawable2).dontAnimate().into(holder.profileImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(list1.get(position).getFoodDetails().size()==1){
            holder.itemName.setText(list1.get(position).getFoodDetails().get(0).getQuintity()+" "+list1.get(position).getFoodDetails().get(0).getName());
        }else {
            String items="";
            for (int i = 0; i <list1.get(position).getFoodDetails().size() ; i++) {
                if(i==0){
                    items= list1.get(position).getFoodDetails().get(i).getQuintity()+" "+list1.get(position).getFoodDetails().get(0).getName()+" ";
                }else items= items+list1.get(position).getFoodDetails().get(i).getQuintity()+" "+list1.get(position).getFoodDetails().get(0).getName();
            }
            holder.itemName.setText(items);
        }
        holder.main_layout.setId(position);
        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.pagedmClick(list1.get( holder.main_layout.getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list1.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView tv_name, tv_amount, itemName, tv_time;
        RelativeLayout main_layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            main_layout = itemView.findViewById(R.id.main_layout);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            itemName = itemView.findViewById(R.id.itemName);
            tv_time = itemView.findViewById(R.id.tv_time);

        }
    }

    private String Time24To12(String time) {
        String timeD = "";
        String[] arrOfStr = time.split("\\s+");
        if (arrOfStr.length == 2) {
            try {
                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                Date _24HourDt = _24HourSDF.parse(arrOfStr[1]);
                System.out.println(_12HourSDF.format(_24HourDt));
                timeD = _12HourSDF.format(_24HourDt);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else timeD = time;

        return timeD;
    }
}
