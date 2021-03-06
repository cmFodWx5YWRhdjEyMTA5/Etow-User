package com.user.etow.ui.trip_process;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.content.Context;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.user.etow.ETowApplication;
import com.user.etow.constant.Constant;
import com.user.etow.data.NetworkManager;
import com.user.etow.models.Driver;
import com.user.etow.models.Trip;
import com.user.etow.models.response.ApiSuccess;
import com.user.etow.ui.base.BasePresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TripProcessPresenter extends BasePresenter<TripProcessMVPView> {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private String mReference;
    private ArrayList<Driver> mListDriver = new ArrayList<>();

    @Inject
    public TripProcessPresenter(Retrofit mRetrofit, NetworkManager mNetworkManager) {
        super(mRetrofit, mNetworkManager);
    }

    @Override
    public void initialView(TripProcessMVPView mvpView) {
        super.initialView(mvpView);
    }

    public void initFirebase() {
        mReference = "/account";
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference(mReference);
    }

    public void checkDriverAvailable() {
        if (getMvpView() != null) getMvpView().showProgressDialog(true);
        mDatabaseReference.orderByChild("type").equalTo(Constant.TYPE_DRIVER)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (getMvpView() != null) getMvpView().showProgressDialog(false);
                        Driver driver = dataSnapshot.getValue(Driver.class);
                        if (driver != null) mListDriver.add(driver);
                        getMvpView().getStatusDriverAvailable(mListDriver);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

    public void getTripDetail(Context context, int tripId) {
        getMvpView().showProgressDialog(true);
        ETowApplication.get(context).getDatabaseReference().orderByChild("id").equalTo(tripId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        getMvpView().showProgressDialog(false);
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

    public void updateTrip(int tripId, String status, String note) {
        if (!isConnectToInternet()) {
            notifyNoNetwork();
        } else {
            getMvpView().showProgressDialog(true);
            mNetworkManager.updateTrip(tripId, status, note)
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

                        }
                    });
        }
    }
}
