package com.user.etow.ui.rate_trip;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.os.Bundle;

import com.user.etow.R;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.ui.base.BaseMVPDialogActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class RateTripActivity extends BaseMVPDialogActivity implements RateTripMVPView {

    @Inject
    RateTripPresenter presenter;

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
        return R.layout.activity_rate_trip;
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
}
