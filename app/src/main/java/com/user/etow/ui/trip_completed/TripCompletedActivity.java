package com.user.etow.ui.trip_completed;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.user.etow.R;
import com.user.etow.constant.Constant;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.data.prefs.DataStoreManager;
import com.user.etow.models.Trip;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.ui.pay_card.PayCardActivity;
import com.user.etow.ui.rate_trip.RateTripActivity;
import com.user.etow.utils.DateTimeUtils;
import com.user.etow.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TripCompletedActivity extends BaseMVPDialogActivity implements TripCompletedMVPView {

    @Inject
    TripCompletedPresenter presenter;

    @BindView(R.id.tv_title_toolbar)
    TextView tvTitleToolbar;

    @BindView(R.id.img_back)
    ImageView imgBack;

    @BindView(R.id.img_payment_type)
    ImageView imgPaymentType;

    @BindView(R.id.tv_payment_type)
    TextView tvPaymentType;

    @BindView(R.id.tv_mesage_pay_cash)
    TextView tvMesagePayCash;

    @BindView(R.id.tv_action)
    TextView tvAction;

    @BindView(R.id.tv_pick_up)
    TextView tvPickUp;

    @BindView(R.id.tv_drop_off)
    TextView tvDropOff;

    @BindView(R.id.img_vehicle)
    ImageView imgVehicle;

    @BindView(R.id.tv_vehicle)
    TextView tvVehicle;

    @BindView(R.id.tv_driver_name)
    TextView tvDriverName;

    @BindView(R.id.tv_vehicle_number)
    TextView tvVehicleNumber;

    @BindView(R.id.tv_date_time)
    TextView tvDateTime;

    @BindView(R.id.tv_price)
    TextView tvPrice;

    private Trip mTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this);
        presenter.initialView(this);

        imgBack.setVisibility(View.GONE);
        tvTitleToolbar.setText(getString(R.string.trip_completed));

        presenter.getTripDetail(this, DataStoreManager.getPrefIdTripProcess());
    }

    @Override
    protected boolean bindView() {
        return true;
    }

    @Override
    protected int addContextView() {
        return R.layout.activity_trip_completed;
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
    public void updateStatusTrip(Trip trip) {
        mTrip = trip;
        if (Constant.PAYMENT_STATUS_PAYMENT_SUCCESS.equals(trip.getPayment_status())) {
            GlobalFuntion.startActivity(this, RateTripActivity.class);
            finish();
        } else {
            if (Constant.TRIP_STATUS_JOURNEY_COMPLETED.equals(trip.getStatus())) {
                initUi();
            }
        }
    }

    private void initUi() {
        if (Constant.TYPE_PAYMENT_CASH.equals(mTrip.getPayment_type())) {
            imgPaymentType.setImageResource(R.drawable.ic_cash_black);
            tvPaymentType.setText(getString(R.string.cash));
            tvMesagePayCash.setVisibility(View.VISIBLE);
            tvAction.setText(getString(R.string.action_done));
        } else {
            imgPaymentType.setImageResource(R.drawable.ic_card_black);
            tvPaymentType.setText(getString(R.string.card));
            tvMesagePayCash.setVisibility(View.GONE);
            tvAction.setText(getString(R.string.pay_now));
        }
        tvPickUp.setText(mTrip.getPick_up());
        tvDropOff.setText(mTrip.getDrop_off());
        if (Constant.TYPE_VEHICLE_NORMAL.equals(mTrip.getVehicle_type())) {
            imgVehicle.setImageResource(R.drawable.ic_car_normal_black);
            tvVehicle.setText(getString(R.string.type_vehicle_normal));
        } else {
            Drawable myIcon = getResources().getDrawable(R.drawable.ic_vehicle_flatbed_white);
            ColorFilter filter = new LightingColorFilter(Color.BLACK, Color.BLACK);
            myIcon.setColorFilter(filter);
            imgVehicle.setImageDrawable(myIcon);
            tvVehicle.setText(getString(R.string.type_vehicle_flatbed));
        }
        tvDriverName.setText(mTrip.getDriver().getName());
        tvVehicleNumber.setText(mTrip.getDriver().getVehicle_number());
        tvDateTime.setText(DateTimeUtils.convertTimeStampToDateFormat5(mTrip.getPickup_date()));
        tvPrice.setText(mTrip.getPrice() + " " + getString(R.string.unit_price));
    }

    @OnClick(R.id.tv_action)
    public void onClickAction() {
        if (Constant.TYPE_PAYMENT_CASH.equals(mTrip.getPayment_type())) {
            if (Constant.PAYMENT_STATUS_PAYMENT_SUCCESS.equals(mTrip.getPayment_status())) {
                GlobalFuntion.startActivity(this, RateTripActivity.class);
                finish();
            } else {
                presenter.updatePaymentStatus(DataStoreManager.getPrefIdTripProcess(), Constant.TYPE_PAYMENT_CASH,
                        Constant.PAYMENT_STATUS_PAYMENT_SUCCESS);
            }
        } else {
            GlobalFuntion.startActivity(this, PayCardActivity.class);
        }
    }

    @OnClick(R.id.img_call_phone)
    public void onClickCallDriver() {
        showDialogCallDriver(mTrip.getDriver().getPhone());
    }

    public void showDialogCallDriver(String phoneNumber) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_call_driver);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setCancelable(false);

        // Get view
        final TextView tvPhomeNumber = dialog.findViewById(R.id.tv_phome_number);
        final TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        final TextView tvCall = dialog.findViewById(R.id.tv_call);

        // set data
        tvPhomeNumber.setText(phoneNumber);

        // Get listener
        tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.callPhoneNumber(TripCompletedActivity.this, phoneNumber);
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
