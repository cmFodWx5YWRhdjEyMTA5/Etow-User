package com.user.etow.ui.pay_card;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.data.NetworkManager;
import com.user.etow.ui.base.BasePresenter;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class PayCardPresenter extends BasePresenter<PayCardMVPView> {

    @Inject
    public PayCardPresenter(Retrofit mRetrofit, NetworkManager mNetworkManager) {
        super(mRetrofit, mNetworkManager);
    }

    @Override
    public void initialView(PayCardMVPView mvpView) {
        super.initialView(mvpView);
    }
}
