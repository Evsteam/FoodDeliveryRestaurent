package com.evs.foodexp.location_services;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public class BroadcastLocationChange extends IntentService {

    public BroadcastLocationChange(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
