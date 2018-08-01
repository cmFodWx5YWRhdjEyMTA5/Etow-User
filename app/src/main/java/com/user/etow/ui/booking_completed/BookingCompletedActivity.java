package com.user.etow.ui.booking_completed;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.user.etow.R;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.messages.GoToUpcomingTrip;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.ui.main.MainActivity;
import com.user.etow.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookingCompletedActivity extends BaseMVPDialogActivity implements BookingCompletedMVPView {

    @Inject
    BookingCompletedPresenter presenter;

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
        return R.layout.activity_booking_completed;
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

    @OnClick(R.id.img_call_us)
    public void onClickCallUs() {
        Utils.callPhoneNumber(this, getString(R.string.phone_number_call_us));
    }

    @OnClick(R.id.tv_understand)
    public void onClickUnderStand() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @OnClick(R.id.tv_upcoming_trip)
    public void onClickUpcomingTrip() {
        EventBus.getDefault().post(new GoToUpcomingTrip());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Utils.callPhoneNumber(this, getString(R.string.phone_number_call_us));
                } else {
                    Log.e("Permission", "Permission not Granted");
                }
                break;
        }
    }
}
