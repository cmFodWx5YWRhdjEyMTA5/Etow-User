package com.user.etow.ui.auth.verify_mobile_number;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.data.DataManager;
import com.user.etow.ui.base.BasePresenter;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class VerifyMobileNumberPresenter extends BasePresenter<VerifyMobileNumberMVPView> {

    @Inject
    public VerifyMobileNumberPresenter(Retrofit mRetrofit, DataManager mDataManager) {
        super(mRetrofit, mDataManager);
    }

    @Override
    public void initialView(VerifyMobileNumberMVPView mvpView) {
        super.initialView(mvpView);
    }
}
