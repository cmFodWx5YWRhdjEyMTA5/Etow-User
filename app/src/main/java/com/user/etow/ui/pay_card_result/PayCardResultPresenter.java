package com.user.etow.ui.pay_card_result;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.data.NetworkManager;
import com.user.etow.ui.base.BasePresenter;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class PayCardResultPresenter extends BasePresenter<PayCardResultMVPView> {

    @Inject
    public PayCardResultPresenter(Retrofit mRetrofit, NetworkManager mNetworkManager) {
        super(mRetrofit, mNetworkManager);
    }

    @Override
    public void initialView(PayCardResultMVPView mvpView) {
        super.initialView(mvpView);
    }
}
