package com.user.etow.data;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.util.Log;

import com.user.etow.data.networking.EtowService;
import com.user.etow.models.Trip;
import com.user.etow.models.response.ApiResponse;
import com.user.etow.models.response.ApiSuccess;
import com.user.etow.models.response.EstimateCostResponse;
import com.user.etow.utils.DateTimeUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class NetworkManager {

    private final EtowService mEtowService;

    @Inject
    public NetworkManager(EtowService etowService) {
        this.mEtowService = etowService;
    }

    public Observable<ApiSuccess> getOTP(String phone) {
        return mEtowService.getOTP(phone);
    }

    public Observable<ApiSuccess> verifyOTP(String otp) {
        return mEtowService.verifyOTP(otp);
    }

    public Observable<ApiResponse> register(String fullName, String email, String password, String phone) {
        return mEtowService.register(fullName, email, password, phone);
    }

    public Observable<ApiSuccess> resetPassword(String email) {
        return mEtowService.resetPassword(email);
    }

    public Observable<ApiResponse> login(String email, String password) {
        return mEtowService.login(email, password);
    }

    public Observable<ApiResponse> updateProfile(String fullName, String phone, String email,
                                                 String password, String avatar) {
        return mEtowService.updateProfile(fullName, phone, email, password, avatar);
    }

    public Observable<ApiSuccess> logout() {
        return mEtowService.logout();
    }

    public Observable<EstimateCostResponse> getEstimateCost(String distance) {
        return mEtowService.getEstimateCost(distance);
    }

    public Observable<ApiResponse> createTrip(Trip trip) {
        return mEtowService.createTrip(trip.toJSon());
    }

    public Observable<ApiSuccess> sendFeedback(String comment) {
        return mEtowService.sendFeedback(comment);
    }

    public Observable<ApiSuccess> updateTrip(int tripId, String status, String note) {
        return mEtowService.updateTrip(tripId, status, note);
    }

    public Observable<ApiResponse> getSetting() {
        return mEtowService.getSetting();
    }

    public Observable<ApiSuccess> updatePaymentStatus(int tripId, String type, String status) {
        return mEtowService.updatePaymentStatus(tripId, type, status);
    }

    public Observable<ApiSuccess> rateTrip(int tripId, int rate) {
        return mEtowService.rateTrip(tripId, rate);
    }
}
