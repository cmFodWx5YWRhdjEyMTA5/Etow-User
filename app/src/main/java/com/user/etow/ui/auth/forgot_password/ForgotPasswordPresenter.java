package com.user.etow.ui.auth.forgot_password;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.data.NetworkManager;
import com.user.etow.ui.base.BasePresenter;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class ForgotPasswordPresenter extends BasePresenter<ForgotPasswordMVPView> {

    @Inject
    public ForgotPasswordPresenter(Retrofit mRetrofit, NetworkManager mNetworkManager) {
        super(mRetrofit, mNetworkManager);
    }

    @Override
    public void initialView(ForgotPasswordMVPView mvpView) {
        super.initialView(mvpView);
    }
}
