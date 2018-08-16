package com.user.etow.ui.main.my_bookings;

/*
 * ******************************************************************************
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/16
 * ******************************************************************************
 */

import android.content.Context;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.user.etow.ETowApplication;
import com.user.etow.adapter.TripUpcomingAdapter;
import com.user.etow.constant.Constant;
import com.user.etow.data.NetworkManager;
import com.user.etow.injection.PerActivity;
import com.user.etow.models.Trip;
import com.user.etow.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Retrofit;

@PerActivity
public class MyBookingsPresenter extends BasePresenter<MyBookingsMVPView> {

    ArrayList<Trip> listTripUpcoming = new ArrayList<>();

    @Inject
    public MyBookingsPresenter(Retrofit mRetrofit, NetworkManager mNetworkManager) {
        super(mRetrofit, mNetworkManager);
    }

    @Override
    public void initialView(MyBookingsMVPView mvpView) {
        super.initialView(mvpView);
    }

    @Override
    public void destroyView() {
        super.destroyView();
    }

    public void getListTripCompleted() {
        List<Trip> list = new ArrayList<>();
        getMvpView().loadListTripCompleted(list);
    }

    public void getTripSchedules(Context context, TripUpcomingAdapter tripUpcomingAdapter) {
        getMvpView().showProgressDialog(true);
        ETowApplication.get(context).getDatabaseReference().orderByChild("is_schedule").equalTo(Constant.IS_SCHEDULE)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        getMvpView().showProgressDialog(false);
                        Trip trip = dataSnapshot.getValue(Trip.class);
                        listTripUpcoming.add(trip);
                        tripUpcomingAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Trip trip = dataSnapshot.getValue(Trip.class);
                        if (listTripUpcoming != null && listTripUpcoming.size() > 0) {
                            for (int i = 0; i < listTripUpcoming.size(); i++) {
                                if (trip.getId() == listTripUpcoming.get(i).getId()) {
                                    listTripUpcoming.set(i, trip);
                                    break;
                                }
                            }
                        }
                        tripUpcomingAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Trip trip = dataSnapshot.getValue(Trip.class);
                        if (listTripUpcoming != null && listTripUpcoming.size() > 0) {
                            for (int i = 0; i < listTripUpcoming.size(); i++) {
                                if (trip.getId() == listTripUpcoming.get(i).getId()) {
                                    listTripUpcoming.remove(i);
                                    break;
                                }
                            }
                        }
                        tripUpcomingAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public ArrayList<Trip> getListTripUpcoming() {
        return listTripUpcoming;
    }
}
