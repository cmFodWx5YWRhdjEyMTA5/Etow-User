package com.user.etow.ui.splash;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;

import com.user.etow.R;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.data.prefs.DataStoreManager;
import com.user.etow.ui.auth.user_start.UserStartActivity;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.ui.main.MainActivity;
import com.user.etow.ui.trip_process.TripProcessActivity;
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
        // init font text
        Utils.getTahomaRegularTypeFace(SplashActivity.this);
        // Request permissions
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        // Get setting app
        presenter.getSetting(this);
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
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int idTripProcess = DataStoreManager.getPrefIdTripProcess();
                if (idTripProcess != 0) {
                    GlobalFuntion.startActivity(SplashActivity.this, TripProcessActivity.class);
                } else {
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
                }
                finish();
            }
        }, 1000);
    }

    private void settingGPS() {
        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            GlobalFuntion.showDialogNoGPS(this);
        } else if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            GlobalFuntion.getCurrentLocation(this, mLocationManager);
            goToActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                settingGPS();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }
}
