package com.evs.foodexp.restaurentPkg.adapter.reviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.evs.foodexp.R;
import com.evs.foodexp.models.ReviewModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewPagedAdapter extends PagedListAdapter<ReviewModel, ReviewPagedAdapter.ReviewViewHolder> {

    private Context mCtx;

    public ReviewPagedAdapter(Context mCtx) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.reviews_item_view, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        ReviewModel model = getItem(position);

        if (model != null) {
            holder.tv_name.setText(model.getUserName());
            holder.tv_massage.setText(model.getMessage());


            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color1 = generator.getRandomColor();
            TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().width(60)
                    .height(60).endConfig().round();

            try {
                TextDrawable drawable2 = builder.build("" + model.getUserName().toUpperCase().charAt(0), color1);
                Glide.with(mCtx).load(model.getProfile_picture()).placeholder(drawable2).dontAnimate().error(drawable2).dontAnimate().into(holder.profileImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                holder.ratingBar.setRating(Float.parseFloat(model.getStar()));
            } catch (NumberFormatException e) {
            }


        }
    }

    private static DiffUtil.ItemCallback<ReviewModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ReviewModel>() {
                @Override
                public boolean areItemsTheSame(ReviewModel oldItem, ReviewModel newItem) {
                    return oldItem.getReviewId().equalsIgnoreCase(newItem.getReviewId());
                }

                @Override
                public boolean areContentsTheSame(ReviewModel oldItem, ReviewModel newItem) {
                    return oldItem.equals(newItem);
                }
            };

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_massage;
        CircleImageView profileImage;
        RatingBar ratingBar;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_massage = itemView.findViewById(R.id.tv_massage);
            ratingBar = itemView.findViewById(R.id.ratingBar);

        }
    }


}
