package com.user.etow.ui.confirm_booking;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.models.Trip;
import com.user.etow.ui.base.BaseScreenMvpView;

interface ConfirmBookingMVPView extends BaseScreenMvpView {

    void loadEstimateCost(String cost);

    void getStatusCreateTrip(Trip trip);
}
