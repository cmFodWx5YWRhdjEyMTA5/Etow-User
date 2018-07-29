package com.user.etow.ui.splash;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.os.Bundle;
import android.os.Handler;

import com.user.etow.R;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.data.prefs.DataStoreManager;
import com.user.etow.ui.auth.user_start.UserStartActivity;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.ui.main.MainActivity;
import com.user.etow.utils.Utils;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class SplashActivity extends BaseMVPDialogActivity implements SplashMVPView {

    @Inject
    SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this);
        presenter.initialView(this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Utils.getTahomaRegularTypeFace(SplashActivity.this);

                goToActivity();
            }
        }, 1000);
    }

    @Override
    protected boolean bindView() {
        return true;
    }

    @Override
    protected int addContextView() {
        return R.layout.activity_splash;
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

    private void goToActivity() {
        if (!DataStoreManager.getFirstInstallApp()) {
            DataStoreManager.setFirstInstallApp(true);
            DataStoreManager.removeUser();
            GlobalFuntion.startActivity(SplashActivity.this, UserStartActivity.class);
        } else {
            if (DataStoreManager.getIsLogin()) {
                GlobalFuntion.startActivity(SplashActivity.this, MainActivity.class);
            } else {
                GlobalFuntion.startActivity(SplashActivity.this, UserStartActivity.class);
            }
        }
        finish();
    }
}
