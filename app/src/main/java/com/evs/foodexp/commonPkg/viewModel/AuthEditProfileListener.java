package com.evs.foodexp.commonPkg.viewModel;

import androidx.lifecycle.LiveData;

public interface AuthEditProfileListener<T> {
    void onEditProfileStarted();

    void onEditProfileSuccess(LiveData<T> data);

    void onEditProfileFailure(String message);
}
