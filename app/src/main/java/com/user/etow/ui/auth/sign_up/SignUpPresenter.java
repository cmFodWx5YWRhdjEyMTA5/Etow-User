package com.user.etow.ui.auth.sign_up;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.data.DataManager;
import com.user.etow.ui.base.BasePresenter;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class SignUpPresenter extends BasePresenter<SignUpMVPView> {

    @Inject
    public SignUpPresenter(Retrofit mRetrofit, DataManager mDataManager) {
        super(mRetrofit, mDataManager);
    }

    @Override
    public void initialView(SignUpMVPView mvpView) {
        super.initialView(mvpView);
    }
}
