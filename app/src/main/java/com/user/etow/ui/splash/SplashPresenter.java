package com.user.etow.ui.splash;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.content.Context;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.user.etow.ETowApplication;
import com.user.etow.constant.Constant;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.data.NetworkManager;
import com.user.etow.models.Setting;
import com.user.etow.models.Trip;
import com.user.etow.models.response.ApiResponse;
import com.user.etow.ui.base.BasePresenter;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashPresenter extends BasePresenter<SplashMVPView> {

    @Inject
    public SplashPresenter(Retrofit mRetrofit, NetworkManager mNetworkManager) {
        super(mRetrofit, mNetworkManager);
    }

    @Override
    public void initialView(SplashMVPView mvpView) {
        super.initialView(mvpView);
    }

    public void getTripDetail(Context context, int tripId) {
        ETowApplication.get(context).getDatabaseReference().orderByChild("id").equalTo(tripId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Trip trip = dataSnapshot.getValue(Trip.class);
                        if (getMvpView() != null && trip != null) getMvpView().getTripDetail(trip);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Trip trip = dataSnapshot.getValue(Trip.class);
                        if (getMvpView() != null && trip != null) getMvpView().getTripDetail(trip);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void getSetting() {
        if (!isConnectToInternet()) {
            notifyNoNetwork();
        } else {
            mNetworkManager.getSetting()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ApiResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            getMvpView().onErrorCallApi(getErrorFromHttp(e).getCode());
                        }

                        @Override
                        public void onNext(ApiResponse apiResponse) {
                            if (apiResponse != null) {
                                if (Constant.SUCCESS.equalsIgnoreCase(apiResponse.getStatus())) {
                                    Setting setting = apiResponse.getDataObject(Setting.class);
                                    if (setting != null) {
                                        GlobalFuntion.mSetting = setting;
                                        getMvpView().getSettingApp();
                                    }
                                }
                            }
                        }
                    });
        }
    }
}
