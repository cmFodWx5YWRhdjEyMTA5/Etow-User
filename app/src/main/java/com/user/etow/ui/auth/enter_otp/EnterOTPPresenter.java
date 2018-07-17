package com.user.etow.ui.auth.enter_otp;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.data.DataManager;
import com.user.etow.ui.base.BasePresenter;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class EnterOTPPresenter extends BasePresenter<EnterOTPMVPView> {

    @Inject
    public EnterOTPPresenter(Retrofit mRetrofit, DataManager mDataManager) {
        super(mRetrofit, mDataManager);
    }

    @Override
    public void initialView(EnterOTPMVPView mvpView) {
        super.initialView(mvpView);
    }
}
