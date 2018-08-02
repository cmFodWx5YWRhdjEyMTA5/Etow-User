package com.user.etow.ui.main.my_bookings;

/*
 * ******************************************************************************
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/16
 * ******************************************************************************
 */

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    String mReference;
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

    public void initFirebase() {
        mReference = "/trip";
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference(mReference);
    }

    public void getTripSchedules() {
        getMvpView().showProgressDialog(true);
        mDatabaseReference.orderByChild("is_schedule").equalTo(Constant.IS_SCHEDULE)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        getMvpView().showProgressDialog(false);
                        Trip trip = dataSnapshot.getValue(Trip.class);
                        listTripUpcoming.add(trip);
                        getMvpView().loadListTripUpcoming();
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
                        getMvpView().loadListTripUpcoming();
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
                        getMvpView().loadListTripUpcoming();
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
