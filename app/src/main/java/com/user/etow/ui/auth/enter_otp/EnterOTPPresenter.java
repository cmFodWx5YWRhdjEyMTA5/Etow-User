package com.user.etow.ui.auth.enter_otp;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.data.NetworkManager;
import com.user.etow.ui.base.BasePresenter;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class EnterOTPPresenter extends BasePresenter<EnterOTPMVPView> {

    @Inject
    public EnterOTPPresenter(Retrofit mRetrofit, NetworkManager mNetworkManager) {
        super(mRetrofit, mNetworkManager);
    }

    @Override
    public void initialView(EnterOTPMVPView mvpView) {
        super.initialView(mvpView);
    }

    /*public void verifyOTP(String otp, String phone) {
        if (!isConnectToInternet()) {
            notifyNoNetwork();
        } else {
            getMvpView().showProgressDialog(true);
            mNetworkManager.verifyOTP(otp)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ApiSuccess>() {
                        @Override
                        public void onCompleted() {
                            getMvpView().showProgressDialog(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            getMvpView().showProgressDialog(false);
                            getMvpView().onErrorCallApi(getErrorFromHttp(e).getCode());
                        }

                        @Override
                        public void onNext(ApiSuccess apiSuccess) {
                            if (apiSuccess != null) {
                                if (Constant.SUCCESS.equalsIgnoreCase(apiSuccess.getStatus())) {
                                    getMvpView().getStatusVerifyOTP(phone);
                                }
                            }
                        }
                    });
        }
    }*/

    /*public void getOTP(String phone) {
        if (!isConnectToInternet()) {
            notifyNoNetwork();
        } else {
            getMvpView().showProgressDialog(true);
            mNetworkManager.getOTP(phone)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ApiSuccess>() {
                        @Override
                        public void onCompleted() {
                            getMvpView().showProgressDialog(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            getMvpView().showProgressDialog(false);
                            getMvpView().onErrorCallApi(getErrorFromHttp(e).getCode());
                        }

                        @Override
                        public void onNext(ApiSuccess apiSuccess) {
                            if (apiSuccess != null) {
                                if (Constant.SUCCESS.equalsIgnoreCase(apiSuccess.getStatus())) {
                                    getMvpView().getStatusCodeOTP();
                                }
                            }
                        }
                    });
        }
    }*/
}
