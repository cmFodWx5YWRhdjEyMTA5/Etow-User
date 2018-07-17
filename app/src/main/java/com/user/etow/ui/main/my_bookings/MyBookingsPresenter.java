package com.user.etow.ui.main.my_bookings;

/*
 * ******************************************************************************
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/16
 * ******************************************************************************
 */

import com.user.etow.data.DataManager;
import com.user.etow.injection.PerActivity;
import com.user.etow.models.Trip;
import com.user.etow.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Retrofit;

@PerActivity
public class MyBookingsPresenter extends BasePresenter<MyBookingsMVPView> {

    @Inject
    public MyBookingsPresenter(Retrofit mRetrofit, DataManager mDataManager) {
        super(mRetrofit, mDataManager);
    }

    @Override
    public void initialView(MyBookingsMVPView mvpView) {
        super.initialView(mvpView);
    }

    @Override
    public void destroyView() {
        super.destroyView();
    }

    public void getListTripCompleted() {
        List<Trip> list = new ArrayList<>();
        getMvpView().loadListTripCompleted(list);
    }

    public void getListTripUpcoming() {
        List<Trip> list = new ArrayList<>();
        getMvpView().loadListTripUpcoming(list);
    }
}
