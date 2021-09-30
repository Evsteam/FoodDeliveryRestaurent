package com.evs.foodexp.restaurentPkg.adapter.cashout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.evs.foodexp.R;
import com.evs.foodexp.models.CashoutModel;
import com.evs.foodexp.utils.Global;

public class CashoutPagedAdapter extends PagedListAdapter<CashoutModel,CashoutPagedAdapter.CashoutViewHolder> {

    private Context mCtx;

    public CashoutPagedAdapter(Context mCtx) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public CashoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.cashout_history_adaper_layout, parent, false);
        return new CashoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CashoutViewHolder holder, int position) {

        CashoutModel model = getItem(position);

        if (model != null) {
                holder.tv_dateTime.setText(model.getCreated());
                holder.tv_totalAmount.setText("Amount $ "+model.getRequestAmount());
                holder.btn_status.setText("Pending");
                if(Global.validatename(model.getSendAmount())){
                    holder.tv_dateTime.setText(model.getSendDate());
                    holder.tv_totalAmount.setText("Amount $ "+model.getSendAmount());
                    holder.btn_status.setText("Paid");
                }
        }
    }

    private static DiffUtil.ItemCallback<CashoutModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<CashoutModel>() {
                @Override
                public boolean areItemsTheSame(CashoutModel oldItem, CashoutModel newItem) {
                    return oldItem.getRequestId().equalsIgnoreCase(newItem.getRequestId());
                }

                @Override
                public boolean areContentsTheSame(CashoutModel oldItem, CashoutModel newItem) {
                    return oldItem.equals(newItem);
                }
            };

      class CashoutViewHolder extends RecyclerView.ViewHolder {

        TextView tv_dateTime, tv_totalAmount;
        Button  btn_status;
        RelativeLayout main_layout;

        public CashoutViewHolder(View itemView) {
            super(itemView);
            tv_dateTime = itemView.findViewById(R.id.tv_dateTime);
            tv_totalAmount = itemView.findViewById(R.id.tv_totalAmount);
            btn_status = itemView.findViewById(R.id.btn_status);
            main_layout = itemView.findViewById(R.id.main_layout);
        }
    }

//    public String dateFomate(String dateTime) {
//        Date date=null;
//        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
//        try {
//             date = dt.parse(dateTime);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        SimpleDateFormat showingDf = new SimpleDateFormat("MMMM d, yyyy");
//        return showingDf.format(date);
//    }
}
