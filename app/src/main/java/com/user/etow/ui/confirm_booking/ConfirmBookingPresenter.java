package com.user.etow.ui.confirm_booking;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.constant.Constant;
import com.user.etow.data.NetworkManager;
import com.user.etow.models.response.EstimateCostResponse;
import com.user.etow.ui.base.BasePresenter;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ConfirmBookingPresenter extends BasePresenter<ConfirmBookingMVPView> {

    @Inject
    public ConfirmBookingPresenter(Retrofit mRetrofit, NetworkManager mNetworkManager) {
        super(mRetrofit, mNetworkManager);
    }

    @Override
    public void initialView(ConfirmBookingMVPView mvpView) {
        super.initialView(mvpView);
    }

    public void getEstimateCost(String distance) {
        if (!isConnectToInternet()) {
            notifyNoNetwork();
        } else {
            mNetworkManager.getEstimateCost(distance)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<EstimateCostResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            getMvpView().onErrorCallApi(getErrorFromHttp(e).getCode());
                        }

                        @Override
                        public void onNext(EstimateCostResponse estimateCostResponse) {
                            if (estimateCostResponse != null) {
                                if (Constant.SUCCESS.equalsIgnoreCase(estimateCostResponse.getStatus())) {
                                    getMvpView().loadEstimateCost(estimateCostResponse.getData());
                                }
                            }
                        }
                    });
        }
    }
}
