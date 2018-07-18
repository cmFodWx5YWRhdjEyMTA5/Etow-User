package com.user.etow.ui.trip_completed;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.user.etow.R;
import com.user.etow.constant.Constant;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.ui.pay_card.PayCardActivity;
import com.user.etow.ui.rate_trip.RateTripActivity;

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

    private String mTypePayment;

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
            mTypePayment = bundle.getString(Constant.TYPE_PAYMENT);
        }
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

    private void initUi() {
        imgBack.setVisibility(View.GONE);
        tvTitleToolbar.setText(getString(R.string.trip_completed));
        if (Constant.TYPE_PAYMENT_CASH.equals(mTypePayment)) {
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
    }

    @OnClick(R.id.tv_action)
    public void onClickAction() {
        if (Constant.TYPE_PAYMENT_CASH.equals(mTypePayment)) {
            GlobalFuntion.startActivity(this, RateTripActivity.class);
        } else {
            GlobalFuntion.startActivity(this, PayCardActivity.class);
        }
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
