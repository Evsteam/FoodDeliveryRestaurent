package com.evs.foodexp.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.evs.foodexp.R;
import com.github.ybq.android.spinkit.style.DoubleBounce;

public class LoaderProgress {
    private Context mContext;
    private Dialog dialog;
    private View view;

    public LoaderProgress(Context context) {
        this.mContext = context;
        view = View.inflate(mContext, R.layout.pricing_final, null);
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        try {
            ProgressBar bar = (ProgressBar) dialog.findViewById(R.id.spin_kit11);
            bar.setVisibility(View.GONE);
            ImageView image = (ImageView) dialog.findViewById(R.id.image);


            Glide.with(mContext)
                    .load(R.drawable.car_loader)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(new DrawableImageViewTarget(image));
            DoubleBounce doubleBounce = new DoubleBounce();
            bar.setIndeterminateDrawable(doubleBounce);
        } catch (Exception e) {

        }


        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // Prevent dialog close on back press button
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void show() {
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }
}
