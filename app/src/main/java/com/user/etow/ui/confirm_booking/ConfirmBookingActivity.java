package com.user.etow.ui.confirm_booking;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.user.etow.R;
import com.user.etow.constant.Constant;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.models.Trip;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.ui.trip_process.TripProcessActivity;
import com.user.etow.utils.StringUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmBookingActivity extends BaseMVPDialogActivity implements ConfirmBookingMVPView {

    @Inject
    ConfirmBookingPresenter presenter;

    @BindView(R.id.tv_title_toolbar)
    TextView tvTitleToolbar;

    @BindView(R.id.view_divider)
    View viewDivider;

    @BindView(R.id.tv_label_date_time)
    TextView tvLabelDateTime;

    @BindView(R.id.layout_estimate_time)
    LinearLayout layoutEstimateTime;

    private Trip mTripBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this);
        presenter.initialView(this);

        getDataIntent();
        initUi();
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mTripBooking = (Trip) bundle.get(Constant.OBJECT_TRIP);
        }
    }

    @Override
    protected boolean bindView() {
        return true;
    }

    @Override
    protected int addContextView() {
        return R.layout.activity_confirm_booking;
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

    private void initUi() {
        tvTitleToolbar.setText(getString(R.string.confirm_booking));
        if (!StringUtil.isEmpty(mTripBooking.getPickup_date())) {
            tvLabelDateTime.setText(getString(R.string.scheduled_booking_date_and_time));
            layoutEstimateTime.setVisibility(View.GONE);
            viewDivider.setVisibility(View.GONE);
        } else {
            tvLabelDateTime.setText(getString(R.string.booking_date_and_time));
            layoutEstimateTime.setVisibility(View.VISIBLE);
            viewDivider.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.img_back)
    public void onClickBack() {
        onBackPressed();
    }

    @OnClick(R.id.tv_confirm_booking)
    public void onClickConfirmBooking() {
        GlobalFuntion.startActivity(this, TripProcessActivity.class);
    }
}
