package com.user.etow.ui.main.my_bookings;

/*
 * ******************************************************************************
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/16
 * ******************************************************************************
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.user.etow.R;
import com.user.etow.adapter.TripCompletedAdapter;
import com.user.etow.adapter.TripUpcomingAdapter;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.models.Trip;
import com.user.etow.ui.base.BaseMVPFragmentWithDialog;
import com.user.etow.ui.main.MainActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyBookingsFragment extends BaseMVPFragmentWithDialog implements MyBookingsMVPView {

    @Inject
    MyBookingsPresenter presenter;

    @BindView(R.id.tv_completed)
    TextView tvCompleted;

    @BindView(R.id.tv_upcoming)
    TextView tvUpcoming;

    @BindView(R.id.rcv_completed)
    RecyclerView rcvCompleted;

    @BindView(R.id.rcv_upcoming)
    RecyclerView rcvUpcoming;

    private boolean mIsTabCompleted = true;
    private MainActivity mMainActivity;

    private TripCompletedAdapter tripCompletedAdapter;
    private TripUpcomingAdapter tripUpcomingAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_bookings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this, view);
        presenter.initialView(this);
        mMainActivity = (MainActivity) getActivity();
        ((MainActivity)getActivity()).showAndHiddenItemToolbar(getString(R.string.my_bookings));

        mIsTabCompleted = mMainActivity.isTabCompleted();
        initUi();

        tripCompletedAdapter = new TripCompletedAdapter(getActivity());
        tripCompletedAdapter.injectInto(rcvCompleted);

        tripUpcomingAdapter = new TripUpcomingAdapter(getActivity(), presenter.getListTripUpcoming());
        tripUpcomingAdapter.injectInto(rcvUpcoming);

        presenter.initFirebase();
        presenter.getListTripCompleted();
        presenter.getTripSchedules();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroyView();

        if (tripCompletedAdapter != null) {
            tripCompletedAdapter.release();
        }

        if (tripUpcomingAdapter != null) {
            tripUpcomingAdapter.release();
        }
    }

    @Override
    protected void initToolbar() {}

    @Override
    public void onErrorCallApi(int code) {
        GlobalFuntion.showMessageError(getActivity(), code);
    }

    private void initUi() {
        if (mIsTabCompleted) {
            tvCompleted.setBackgroundResource(R.color.black);
            tvCompleted.setTextColor(getResources().getColor(R.color.white));
            tvUpcoming.setBackgroundResource(R.color.button_grey);
            tvUpcoming.setTextColor(getResources().getColor(R.color.textColorSecondary));
            rcvCompleted.setVisibility(View.VISIBLE);
            rcvUpcoming.setVisibility(View.GONE);
        } else {
            tvUpcoming.setBackgroundResource(R.color.black);
            tvUpcoming.setTextColor(getResources().getColor(R.color.white));
            tvCompleted.setBackgroundResource(R.color.button_grey);
            tvCompleted.setTextColor(getResources().getColor(R.color.textColorSecondary));
            rcvCompleted.setVisibility(View.GONE);
            rcvUpcoming.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_completed)
    public void onClickSelectTabCompleted() {
        if (!mIsTabCompleted) {
            mIsTabCompleted = true;
            tvCompleted.setBackgroundResource(R.color.black);
            tvCompleted.setTextColor(getResources().getColor(R.color.white));
            tvUpcoming.setBackgroundResource(R.color.button_grey);
            tvUpcoming.setTextColor(getResources().getColor(R.color.textColorSecondary));
            rcvCompleted.setVisibility(View.VISIBLE);
            rcvUpcoming.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tv_upcoming)
    public void onClickSelectTabUpcoming() {
        if (mIsTabCompleted) {
            mIsTabCompleted = false;
            tvUpcoming.setBackgroundResource(R.color.black);
            tvUpcoming.setTextColor(getResources().getColor(R.color.white));
            tvCompleted.setBackgroundResource(R.color.button_grey);
            tvCompleted.setTextColor(getResources().getColor(R.color.textColorSecondary));
            rcvCompleted.setVisibility(View.GONE);
            rcvUpcoming.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadListTripCompleted(List<Trip> listTripCompleted) {
        listTripCompleted.add(new Trip());
        listTripCompleted.add(new Trip());
        listTripCompleted.add(new Trip());
        listTripCompleted.add(new Trip());
        listTripCompleted.add(new Trip());
        listTripCompleted.add(new Trip());

        tripCompletedAdapter.setListData(listTripCompleted);
    }

    @Override
    public void loadListTripUpcoming() {
        tripUpcomingAdapter.notifyDataSetChanged();
    }
}
