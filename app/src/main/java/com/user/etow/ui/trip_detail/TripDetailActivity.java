package com.user.etow.ui.trip_detail;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.user.etow.R;
import com.user.etow.constant.Constant;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.models.Trip;
import com.user.etow.ui.base.BaseMVPDialogActivity;

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
        if (mIsTripCompleted) {
            tvLabelDateTime.setText(getString(R.string.date_and_time));
            layoutBookingStatus.setVisibility(View.GONE);
        } else {
            tvLabelDateTime.setText(getString(R.string.scheduled_date_and_time));
            layoutBookingStatus.setVisibility(View.VISIBLE);
            if (mTrip != null) {
                if (mTrip.getStatus() == 1) {
                    tvStatus.setText(getString(R.string.pending));
                    tvStatus.setTextColor(getResources().getColor(R.color.orange));
                } else if (mTrip.getStatus() == 2) {
                    tvStatus.setText(getString(R.string.confirmed));
                    tvStatus.setTextColor(getResources().getColor(R.color.button_green));
                } else {
                    tvStatus.setText(getString(R.string.no_driver_available));
                    tvStatus.setTextColor(getResources().getColor(R.color.button_red));
                }
            }
        }
    }

    @OnClick(R.id.img_back)
    public void onClickBack() {
        onBackPressed();
    }

    @OnClick(R.id.img_call_phone)
    public void onClickCallDriver() {
        showDialogCallDriver();
    }

    public void showDialogCallDriver() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_call_driver);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setCancelable(false);

        // Get view
        final TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        final TextView tvCall = dialog.findViewById(R.id.tv_call);

        // Get listener
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
