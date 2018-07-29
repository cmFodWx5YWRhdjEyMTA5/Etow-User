package com.user.etow.data;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.data.networking.ThinkFitService;
import com.user.etow.models.response.ApiResponse;
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

    public Observable<ApiResponse> register(String fullName, String email, String password, String phone) {
        return mThinkFitService.register(fullName, email, password, phone);
    }

    public Observable<ApiSuccess> resetPassword(String email) {
        return mThinkFitService.resetPassword(email);
    }

    public Observable<ApiResponse> login(String email, String password) {
        return mThinkFitService.login(email, password);
    }

    public Observable<ApiResponse> updateProfile(String fullName, String phone, String email,
                                                 String password, String avatar) {
        return mThinkFitService.updateProfile(fullName, phone, email, password, avatar);
    }

    public Observable<ApiSuccess> logout() {
        return mThinkFitService.logout();
    }
}
