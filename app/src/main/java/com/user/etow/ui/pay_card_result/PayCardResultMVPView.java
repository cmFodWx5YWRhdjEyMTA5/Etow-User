package com.user.etow.ui.pay_card_result;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.models.Trip;
import com.user.etow.ui.base.BaseScreenMvpView;

interface PayCardResultMVPView extends BaseScreenMvpView {

    void updateStatusTrip(Trip trip);
}
