package com.evs.foodexp.restaurentPkg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evs.foodexp.R;
import com.evs.foodexp.models.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    Context context;
    List<CategoryModel> list;
   PagedItemClick<CategoryModel> itemClick;

    public CategoryAdapter(Context context,List<CategoryModel> list,PagedItemClick<CategoryModel> itemClick) {
        this.context = context;
        this.list = list;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_menu_type_btn_layout_adapter,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
//        /CategoryDto.Data response = list.get(position);

        holder.categoryBtn.setText(String.valueOf(list.get(position).getName()));
        holder.categoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.pagedmClick(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView categoryBtn;
        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            categoryBtn = (TextView)itemView.findViewById(R.id.categoryTypeBtn);




        }
    }
}
