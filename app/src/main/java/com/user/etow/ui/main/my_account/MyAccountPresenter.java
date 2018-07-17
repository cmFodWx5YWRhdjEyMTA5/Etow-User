package com.user.etow.ui.main.my_account;

/*
 * ******************************************************************************
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/16
 * ******************************************************************************
 */

import com.user.etow.data.DataManager;
import com.user.etow.injection.PerActivity;
import com.user.etow.ui.base.BasePresenter;

import javax.inject.Inject;

import retrofit2.Retrofit;

@PerActivity
public class MyAccountPresenter extends BasePresenter<MyAccountMVPView> {

    @Inject
    public MyAccountPresenter(Retrofit mRetrofit, DataManager mDataManager) {
        super(mRetrofit, mDataManager);
    }

    @Override
    public void initialView(MyAccountMVPView mvpView) {
        super.initialView(mvpView);
    }

    @Override
    public void destroyView() {
        super.destroyView();
    }
}
