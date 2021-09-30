package com.evs.foodexp.commonPkg.common.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;


import com.bumptech.glide.Glide;
import com.evs.foodexp.R;

import java.util.ArrayList;


public class ImageViewer extends AppCompatActivity {
    ImageView uplodedSlip;
    String UploadeImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView mTitle = toolbar.findViewById(R.id.text_toolbar);
        mTitle.setText("IMAGE");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle= getIntent().getExtras();
        if(bundle!=null) {
            UploadeImage = bundle.getString("test");
        }


        uplodedSlip = (ImageView) findViewById(R.id.uplodedSlip);
        Glide.with(ImageViewer.this).load(UploadeImage).into(uplodedSlip);

    }


}
