package com.user.etow.ui.auth.sign_up;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.constant.Constant;
import com.user.etow.data.NetworkManager;
import com.user.etow.data.prefs.DataStoreManager;
import com.user.etow.models.User;
import com.user.etow.models.response.ApiResponse;
import com.user.etow.ui.base.BasePresenter;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignUpPresenter extends BasePresenter<SignUpMVPView> {

    @Inject
    public SignUpPresenter(Retrofit mRetrofit, NetworkManager mNetworkManager) {
        super(mRetrofit, mNetworkManager);
    }

    @Override
    public void initialView(SignUpMVPView mvpView) {
        super.initialView(mvpView);
    }

    public void register(String fullName, String email, String password, String phone) {
        if (!isConnectToInternet()) {
            notifyNoNetwork();
        } else {
            getMvpView().showProgressDialog(true);
            mNetworkManager.register(fullName, email, password, phone)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ApiResponse>() {
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
                        public void onNext(ApiResponse apiResponse) {
                            if (apiResponse != null) {
                                if (Constant.SUCCESS.equalsIgnoreCase(apiResponse.getStatus())) {
                                    User user = apiResponse.getDataObject(User.class);
                                    if (user != null) {
                                        DataStoreManager.setIsLogin(true);
                                        DataStoreManager.setUserToken(user.getToken());
                                        DataStoreManager.setUser(user);
                                        getMvpView().updateStatusRegister();
                                    }
                                }
                            }
                        }
                    });
        }
    }
}
