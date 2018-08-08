package com.user.etow.ui.trip_process;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.user.etow.R;
import com.user.etow.constant.Constant;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.ui.main.MainActivity;
import com.user.etow.ui.trip_completed.TripCompletedActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TripProcessActivity extends BaseMVPDialogActivity implements TripProcessMVPView {

    @Inject
    TripProcessPresenter presenter;

    @BindView(R.id.layout_driver_are_away)
    LinearLayout layoutDriverAreAway;

    @BindView(R.id.layout_wait_driver)
    LinearLayout layoutWaitDriver;

    private int mTripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this);
        presenter.initialView(this);

        //Todo check driver available in current estimate time
        presenter.checkDriverAvailable();
    }

    @Override
    protected boolean bindView() {
        return true;
    }

    @Override
    protected int addContextView() {
        return R.layout.activity_trip_process;
    }

    @Override
    protected void onDestroy() {
        presenter.destroyView();
        super.onDestroy();
    }

    @Override
    public void showNoNetworkAlert() {
        showAlert(getString(R.string.error_not_connect_to_internet));
    }

    @Override
    public void onErrorCallApi(int code) {
        GlobalFuntion.showMessageError(this, code);
    }

    @Override
    public void getStatusDriverAvailable(boolean isAvailable) {
        if (isAvailable) {
            layoutDriverAreAway.setVisibility(View.VISIBLE);
        } else {
            layoutWaitDriver.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateStatusCancelTrip() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @OnClick(R.id.tv_confirm)
    public void onClickConfirm() {
        layoutDriverAreAway.setVisibility(View.GONE);
        layoutWaitDriver.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.layout_trip_ongoing)
    public void onClickFakeClick() {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.TYPE_PAYMENT, Constant.TYPE_PAYMENT_CARD);
        GlobalFuntion.startActivity(this, TripCompletedActivity.class, bundle);
    }

    @OnClick(R.id.tv_cancel_driver_away)
    public void onClickCancelDriverAway() {
        presenter.updateTrip(mTripId, Constant.TRIP_STATUS_CANCEL);
    }
}
