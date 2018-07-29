package com.user.etow.ui.auth.forgot_password;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.constant.Constant;
import com.user.etow.data.NetworkManager;
import com.user.etow.models.response.ApiSuccess;
import com.user.etow.ui.base.BasePresenter;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ForgotPasswordPresenter extends BasePresenter<ForgotPasswordMVPView> {

    @Inject
    public ForgotPasswordPresenter(Retrofit mRetrofit, NetworkManager mNetworkManager) {
        super(mRetrofit, mNetworkManager);
    }

    @Override
    public void initialView(ForgotPasswordMVPView mvpView) {
        super.initialView(mvpView);
    }

    public void resetPassword(String email) {
        if (!isConnectToInternet()) {
            notifyNoNetwork();
        } else {
            getMvpView().showProgressDialog(true);
            mNetworkManager.resetPassword(email)
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
                                    getMvpView().showAlert(apiSuccess.getMessage());
                                    getMvpView().getStatusResetPassword();
                                }
                            }
                        }
                    });
        }
    }
}
