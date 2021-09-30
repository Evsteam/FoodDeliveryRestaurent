package com.evs.foodexp.commonPkg.viewModel;

import com.evs.foodexp.repositry.MsgResponse;

public interface AuthMsgListener {

    void onStarted();

    void onFailure(String message);

    void onSuccessMsg(MsgResponse msgResponce);

}