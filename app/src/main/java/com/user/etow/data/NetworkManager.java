package com.user.etow.data;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.data.networking.ThinkFitService;
import com.user.etow.models.response.ApiSuccess;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class NetworkManager {

    private final ThinkFitService mThinkFitService;

    @Inject
    public NetworkManager(ThinkFitService thinkFitService) {
        this.mThinkFitService = thinkFitService;
    }

    public Observable<ApiSuccess> getOTP(String phone) {
        return mThinkFitService.getOTP(phone);
    }

    public Observable<ApiSuccess> verifyOTP(String otp) {
        return mThinkFitService.verifyOTP(otp);
    }
}
