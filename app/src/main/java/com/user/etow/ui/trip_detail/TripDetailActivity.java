package com.user.etow.ui.trip_detail;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.user.etow.R;
import com.user.etow.constant.Constant;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.models.Trip;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.utils.DateTimeUtils;
import com.user.etow.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TripDetailActivity extends BaseMVPDialogActivity implements TripDetailMVPView {

    @Inject
    TripDetailPresenter presenter;

    @BindView(R.id.tv_title_toolbar)
    TextView tvTitleToolbar;

    @BindView(R.id.tv_label_date_time)
    TextView tvLabelDateTime;

    @BindView(R.id.layout_booking_status)
    LinearLayout layoutBookingStatus;

    @BindView(R.id.tv_status)
    TextView tvStatus;

    @BindView(R.id.tv_trip_no)
    TextView tvTripNo;

    @BindView(R.id.tv_trip_amount)
    TextView tvTripAmount;

    @BindView(R.id.tv_date_time)
    TextView tvDateTime; // Monday 19 Sep 2018 17:46 PM

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

    @BindView(R.id.img_payment_type)
    ImageView imgPaymentType;

    @BindView(R.id.tv_payment_type)
    TextView tvPaymentType;

    private Trip mTrip;
    private boolean mIsTripCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this);
        presenter.initialView(this);

        getDataIntent();
        if (mIsTripCompleted) {
            tvTitleToolbar.setText(getString(R.string.completed_trips));
        } else {
            tvTitleToolbar.setText(getString(R.string.upcoming_trips));
        }

        initUi();
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mTrip = (Trip) bundle.get(Constant.OBJECT_TRIP);
            mIsTripCompleted = bundle.getBoolean(Constant.IS_TRIP_COMPLETED);
        }
    }

    @Override
    protected boolean bindView() {
        return true;
    }

    @Override
    protected int addContextView() {
        return R.layout.activity_trip_detail;
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
        tvTripNo.setText(mTrip.getId() + "");
        tvTripAmount.setText(mTrip.getPrice() + " " + getString(R.string.unit_price));
        tvDateTime.setText(DateTimeUtils.convertTimeStampToDateFormat6(mTrip.getPickup_date()));
        if (mIsTripCompleted) {
            tvLabelDateTime.setText(getString(R.string.date_and_time));
            layoutBookingStatus.setVisibility(View.GONE);
        } else {
            tvLabelDateTime.setText(getString(R.string.scheduled_date_and_time));
            layoutBookingStatus.setVisibility(View.VISIBLE);
            if (Constant.TRIP_STATUS_NEW == mTrip.getStatus()) {
                tvStatus.setText(getString(R.string.pending));
                tvStatus.setTextColor(getResources().getColor(R.color.orange));
            } else if (Constant.TRIP_STATUS_REJECT == mTrip.getStatus()) {
                tvStatus.setText(getString(R.string.no_driver_available));
                tvStatus.setTextColor(getResources().getColor(R.color.button_red));
            } else if (Constant.TRIP_STATUS_ACCEPT == mTrip.getStatus()) {
                tvStatus.setText(getString(R.string.confirmed));
                tvStatus.setTextColor(getResources().getColor(R.color.button_green));
            }
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
        if (Constant.TYPE_PAYMENT_CASH.equals(mTrip.getPayment_type())) {
            imgPaymentType.setImageResource(R.drawable.ic_cash_black);
            tvPaymentType.setText(getString(R.string.cash));
        } else {
            imgPaymentType.setImageResource(R.drawable.ic_card_black);
            tvPaymentType.setText(getString(R.string.card));
        }
    }

    @OnClick(R.id.img_back)
    public void onClickBack() {
        onBackPressed();
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
                Utils.callPhoneNumber(TripDetailActivity.this, phoneNumber);
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
}
