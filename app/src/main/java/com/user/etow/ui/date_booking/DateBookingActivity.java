package com.user.etow.ui.date_booking;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.user.etow.R;
import com.user.etow.constant.Constant;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.listener.IGetDateListener;
import com.user.etow.listener.IGetTimeListener;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.utils.StringUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DateBookingActivity extends BaseMVPDialogActivity implements DateBookingMVPView {

    @Inject
    DateBookingPresenter presenter;

    @BindView(R.id.tv_date)
    TextView tvDate;

    @BindView(R.id.tv_time)
    TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this);
        presenter.initialView(this);
    }

    @Override
    protected boolean bindView() {
        return true;
    }

    @Override
    protected int addContextView() {
        return R.layout.activity_date_booking;
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

    @OnClick(R.id.img_back)
    public void onClickBack() {
        onBackPressed();
    }

    @OnClick(R.id.tv_date)
    public void onClickSelectDate() {
        GlobalFuntion.showDatePicker(this, new IGetDateListener() {
            @Override
            public void getDate(String date) {
                tvDate.setText(date);
            }
        });
    }

    @OnClick(R.id.tv_time)
    public void onClickSelectTime() {
        GlobalFuntion.showTimePicker(this, new IGetTimeListener() {
            @Override
            public void getTime(String time) {
                tvTime.setText(time);
            }
        });
    }

    @OnClick(R.id.tv_set_pickup_date)
    public void onClickSetPickUpDate() {
        String date = tvDate.getText().toString().trim();
        String time = tvTime.getText().toString().trim();
        if (StringUtil.isEmpty(date)) {
            showAlert(getString(R.string.please_select_date));
        } else if (StringUtil.isEmpty(time)) {
            showAlert(getString(R.string.please_select_time));
        } else {
            String dateTime = date + ", " + time;
            Intent intent = new Intent();
            intent.putExtra(Constant.DATE_BOOKING, dateTime);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
