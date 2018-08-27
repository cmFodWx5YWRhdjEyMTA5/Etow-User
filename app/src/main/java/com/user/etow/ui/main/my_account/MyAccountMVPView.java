package com.user.etow.ui.main.my_account;

/*
 * ******************************************************************************
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/16
 * ******************************************************************************
 */

import com.user.etow.ui.base.BaseScreenMvpView;

interface MyAccountMVPView extends BaseScreenMvpView {

    void updateStatusUpdateProfile();

    void updatePhoneNumber(String phone);

    void updateAvatar();
}
