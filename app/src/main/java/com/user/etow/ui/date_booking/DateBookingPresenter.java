package com.user.etow.ui.date_booking;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.data.NetworkManager;
import com.user.etow.ui.base.BasePresenter;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class DateBookingPresenter extends BasePresenter<DateBookingMVPView> {

    @Inject
    public DateBookingPresenter(Retrofit mRetrofit, NetworkManager mNetworkManager) {
        super(mRetrofit, mNetworkManager);
    }

    @Override
    public void initialView(DateBookingMVPView mvpView) {
        super.initialView(mvpView);
    }
}
