package com.user.etow.ui.main.my_account;

/*
 * ******************************************************************************
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/16
 * ******************************************************************************
 */

import com.user.etow.constant.Constant;
import com.user.etow.data.NetworkManager;
import com.user.etow.data.prefs.DataStoreManager;
import com.user.etow.injection.PerActivity;
import com.user.etow.messages.EditPhoneNumberSuccess;
import com.user.etow.messages.SelectAvatarSuccess;
import com.user.etow.models.User;
import com.user.etow.models.response.ApiResponse;
import com.user.etow.ui.base.BasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@PerActivity
public class MyAccountPresenter extends BasePresenter<MyAccountMVPView> {

    @Inject
    public MyAccountPresenter(Retrofit mRetrofit, NetworkManager mNetworkManager) {
        super(mRetrofit, mNetworkManager);
    }

    @Override
    public void initialView(MyAccountMVPView mvpView) {
        super.initialView(mvpView);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void destroyView() {
        super.destroyView();
        EventBus.getDefault().unregister(this);
    }

    public void updateProfile(String fullName, String phone, String email, String password, String avatar) {
        if (!isConnectToInternet()) {
            notifyNoNetwork();
        } else {
            getMvpView().showProgressDialog(true);
            mNetworkManager.updateProfile(fullName, phone, email, password, avatar)
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
                                    getMvpView().showAlert(apiResponse.getMessage());
                                    User user = apiResponse.getDataObject(User.class);
                                    if (user != null) {
                                        DataStoreManager.setUser(user);
                                        getMvpView().updateStatusUpdateProfile();
                                    }
                                }
                            }
                        }
                    });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEditPhoneNumberSuccess(EditPhoneNumberSuccess editPhoneNumberSuccess) {
        getMvpView().updatePhoneNumber(editPhoneNumberSuccess.getPhone());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectAvatarSuccess(SelectAvatarSuccess selectAvatarSuccess) {
        getMvpView().updateAvatar();
    }
}
