package com.user.etow.ui.confirm_booking;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.user.etow.R;
import com.user.etow.constant.Constant;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.models.Trip;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.ui.trip_process.TripProcessActivity;
import com.user.etow.utils.DateTimeUtils;
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

    @BindView(R.id.tv_pick_up)
    TextView tvPickUp;

    @BindView(R.id.tv_drop_off)
    TextView tvDropOff;

    @BindView(R.id.img_vehicle)
    ImageView imgVehicle;

    @BindView(R.id.tv_vehicle)
    TextView tvVehicle;

    @BindView(R.id.tv_date_time)
    TextView tvDateTime;

    @BindView(R.id.tv_cost)
    TextView tvCost;

    @BindView(R.id.layout_cash)
    LinearLayout layoutCash;

    @BindView(R.id.layout_card)
    LinearLayout layoutCard;

    @BindView(R.id.img_payment_cash)
    ImageView imgPaymentCash;

    @BindView(R.id.img_payment_card)
    ImageView imgPaymentCard;

    @BindView(R.id.tv_payment_cash)
    TextView tvPaymentCash;

    @BindView(R.id.tv_payment_card)
    TextView tvPaymentCard;

    private Trip mTripBooking;
    private boolean mIsPaymentCash = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this);
        presenter.initialView(this);

        getDataIntent();
        initUi();
        initData();
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

    private void initData() {
        tvPickUp.setText(mTripBooking.getPick_up());
        tvDropOff.setText(mTripBooking.getDrop_off());
        if (Constant.TYPE_VEHICLE_NORMAL.equals(mTripBooking.getVehicle_type())) {
            imgVehicle.setImageResource(R.drawable.ic_car_normal_black);
            tvVehicle.setText(getString(R.string.type_vehicle_normal));
        } else {
            Drawable myIcon = getResources().getDrawable(R.drawable.ic_vehicle_flatbed_white);
            ColorFilter filter = new LightingColorFilter(Color.BLACK, Color.BLACK);
            myIcon.setColorFilter(filter);
            imgVehicle.setImageDrawable(myIcon);
            tvVehicle.setText(getString(R.string.type_vehicle_flatbed));
        }
        if (!StringUtil.isEmpty(mTripBooking.getPickup_date())) {
            tvDateTime.setText(DateTimeUtils.parseDateFormat2(mTripBooking.getPickup_date()));
        }
        getEstimateCost();
    }

    @OnClick(R.id.img_back)
    public void onClickBack() {
        onBackPressed();
    }

    @OnClick(R.id.tv_confirm_booking)
    public void onClickConfirmBooking() {
        GlobalFuntion.startActivity(this, TripProcessActivity.class);
    }

    private void getEstimateCost() {
        float[] result = new float[3];
        Location.distanceBetween(Double.parseDouble(mTripBooking.getPick_up_latitude()),
                Double.parseDouble(mTripBooking.getPick_up_longitude()),
                Double.parseDouble(mTripBooking.getDrop_off_latitude()),
                Double.parseDouble(mTripBooking.getDrop_off_longitude()), result);
        int distance = (int) (result[0] / 1000);
        presenter.getEstimateCost(distance + "");
    }

    @Override
    public void loadEstimateCost(String cost) {
        tvCost.setText(cost + " " + getString(R.string.unit_price));
    }

    @OnClick(R.id.layout_cash)
    public void onClickLayoutCash() {
        if (!mIsPaymentCash) {
            mIsPaymentCash = true;

            layoutCash.setBackgroundResource(R.drawable.bg_white_corner_border_grey_radius_8);
            imgPaymentCash.setImageResource(R.drawable.ic_cash_black);
            tvPaymentCash.setTextColor(getResources().getColor(R.color.textColorPrimary));

            layoutCard.setBackgroundResource(R.drawable.bg_grey_corner_radius_6);
            imgPaymentCard.setImageResource(R.drawable.ic_card_grey);
            tvPaymentCard.setTextColor(getResources().getColor(R.color.textColorAccent));
        }
    }

    @OnClick(R.id.layout_card)
    public void onClickLayoutCard() {
        if (mIsPaymentCash) {
            mIsPaymentCash = false;

            layoutCard.setBackgroundResource(R.drawable.bg_white_corner_border_grey_radius_8);
            imgPaymentCard.setImageResource(R.drawable.ic_card_black);
            tvPaymentCard.setTextColor(getResources().getColor(R.color.textColorPrimary));

            layoutCash.setBackgroundResource(R.drawable.bg_grey_corner_radius_6);
            imgPaymentCash.setImageResource(R.drawable.ic_cash_grey);
            tvPaymentCash.setTextColor(getResources().getColor(R.color.textColorAccent));
        }
    }
}
