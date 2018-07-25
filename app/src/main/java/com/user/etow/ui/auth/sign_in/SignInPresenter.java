package com.user.etow.ui.auth.sign_in;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.data.NetworkManager;
import com.user.etow.ui.base.BasePresenter;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class SignInPresenter extends BasePresenter<SignInMVPView> {

    @Inject
    public SignInPresenter(Retrofit mRetrofit, NetworkManager mNetworkManager) {
        super(mRetrofit, mNetworkManager);
    }

    @Override
    public void initialView(SignInMVPView mvpView) {
        super.initialView(mvpView);
    }
}
