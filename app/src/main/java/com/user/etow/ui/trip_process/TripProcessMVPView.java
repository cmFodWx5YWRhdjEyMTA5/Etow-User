package com.user.etow.ui.trip_process;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.ui.base.BaseScreenMvpView;

interface TripProcessMVPView extends BaseScreenMvpView {

    void getStatusDriverAvailable(boolean isAvailable);

    void updateStatusCancelTrip();
}
