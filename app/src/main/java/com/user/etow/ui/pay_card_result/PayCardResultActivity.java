package com.user.etow.ui.pay_card_result;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.user.etow.R;
import com.user.etow.constant.Constant;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.data.prefs.DataStoreManager;
import com.user.etow.models.Trip;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.ui.rate_trip.RateTripActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayCardResultActivity extends BaseMVPDialogActivity implements PayCardResultMVPView {

    @Inject
    PayCardResultPresenter presenter;

    @BindView(R.id.layout_pay_success)
    RelativeLayout layoutPaySuccess;

    @BindView(R.id.layout_pay_fail)
    RelativeLayout layoutPayFail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this);
        presenter.initialView(this);

        presenter.getTripDetail(this, DataStoreManager.getPrefIdTripProcess());
    }

    @Override
    protected boolean bindView() {
        return true;
    }

    @Override
    protected int addContextView() {
        return R.layout.activity_pay_card_result;
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

    @OnClick(R.id.tv_retry)
    public void onClickRetry() {
        onBackPressed();
    }

    @OnClick(R.id.tv_pay_cash)
    public void onClickPayCash() {
        presenter.updatePaymentStatus(DataStoreManager.getPrefIdTripProcess(), Constant.TYPE_PAYMENT_CASH,
                Constant.PAYMENT_STATUS_PAYMENT_SUCCESS);
    }

    @Override
    public void updateStatusTrip(Trip trip) {
        if (Constant.PAYMENT_STATUS_PAYMENT_SUCCESS.equals(trip.getPayment_status())) {
            layoutPayFail.setVisibility(View.GONE);
            layoutPaySuccess.setVisibility(View.VISIBLE);
        }

        if (Constant.TRIP_STATUS_COMPLETE == trip.getStatus()) {
            GlobalFuntion.startActivity(this, RateTripActivity.class);
            finish();
        }
    }
}
