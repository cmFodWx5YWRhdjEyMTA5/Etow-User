package com.user.etow.ui.rate_trip;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.data.DataManager;
import com.user.etow.ui.base.BasePresenter;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class RateTripPresenter extends BasePresenter<RateTripMVPView> {

    @Inject
    public RateTripPresenter(Retrofit mRetrofit, DataManager mDataManager) {
        super(mRetrofit, mDataManager);
    }

    @Override
    public void initialView(RateTripMVPView mvpView) {
        super.initialView(mvpView);
    }
}
