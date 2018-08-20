package com.user.etow.ui.trip_process;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.models.Driver;
import com.user.etow.models.Trip;
import com.user.etow.ui.base.BaseScreenMvpView;

import java.util.ArrayList;

interface TripProcessMVPView extends BaseScreenMvpView {

    void getStatusDriverAvailable(ArrayList<Driver> listDriver);

    void getTripDetail(Trip trip);
}
