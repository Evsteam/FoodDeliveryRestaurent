package com.evs.foodexp.commonPkg.viewModel;

import androidx.lifecycle.LiveData;

public interface AuthCategoryListener<T> {

    void onCategoryStarted();

    void onCategorySuccess(LiveData<T> data);

    void onCategoryFailure(String message);

}
