package com.user.etow.ui.confirm_booking;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.data.DataManager;
import com.user.etow.ui.base.BasePresenter;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class ConfirmBookingPresenter extends BasePresenter<ConfirmBookingMVPView> {

    @Inject
    public ConfirmBookingPresenter(Retrofit mRetrofit, DataManager mDataManager) {
        super(mRetrofit, mDataManager);
    }

    @Override
    public void initialView(ConfirmBookingMVPView mvpView) {
        super.initialView(mvpView);
    }
}
