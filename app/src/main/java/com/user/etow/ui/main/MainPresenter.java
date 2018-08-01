package com.user.etow.ui.main;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.data.NetworkManager;
import com.user.etow.data.prefs.DataStoreManager;
import com.user.etow.messages.GoToUpcomingTrip;
import com.user.etow.models.response.ApiSuccess;
import com.user.etow.ui.base.BasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainPresenter extends BasePresenter<MainMVPView> {

    @Inject
    public MainPresenter(Retrofit mRetrofit, NetworkManager mNetworkManager) {
        super(mRetrofit, mNetworkManager);
    }

    @Override
    public void initialView(MainMVPView mvpView) {
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

    public void logout() {
        if (!isConnectToInternet()) {
            notifyNoNetwork();
        } else {
            getMvpView().showProgressDialog(true);
            mNetworkManager.logout()
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
                                DataStoreManager.setIsLogin(false);
                                DataStoreManager.setUserToken("");
                                DataStoreManager.removeUser();
                                getMvpView().logout();
                            }
                        }
                    });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGoToUpcomingTrip(GoToUpcomingTrip goToUpcomingTrip) {
        getMvpView().goToUpcomingTrip();
    }
}
