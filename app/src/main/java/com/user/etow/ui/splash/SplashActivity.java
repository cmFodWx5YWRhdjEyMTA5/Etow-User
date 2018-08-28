package com.user.etow.ui.splash;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.user.etow.R;
import com.user.etow.constant.Constant;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.data.prefs.DataStoreManager;
import com.user.etow.models.Trip;
import com.user.etow.ui.auth.user_start.UserStartActivity;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.ui.main.MainActivity;
import com.user.etow.ui.rate_trip.RateTripActivity;
import com.user.etow.ui.trip_completed.TripCompletedActivity;
import com.user.etow.ui.trip_process.TripProcessActivity;
import com.user.etow.utils.Utils;

import java.util.Locale;

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
        setLocale("en");
        // init font text
        Utils.getTahomaRegularTypeFace(SplashActivity.this);
        // Get setting app
        presenter.getSetting();
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

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        if (Build.VERSION.SDK_INT >= 17) {
            conf.setLayoutDirection(myLocale);
        }
        res.updateConfiguration(conf, dm);
    }

    private void goToActivity() {
        int idTripProcess = DataStoreManager.getPrefIdTripProcess();
        if (idTripProcess != 0) {
            presenter.getTripDetail(SplashActivity.this, idTripProcess);
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
            finish();
        }
    }

    private void settingGPS() {
        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showDialogSettingGps();
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

    @Override
    public void getTripDetail(Trip trip) {
        if (Constant.PAYMENT_STATUS_PAYMENT_SUCCESS.equals(trip.getPayment_status()) && trip.getIs_rate() == 0) {
            GlobalFuntion.startActivity(this, RateTripActivity.class);
        } else {
            if (Constant.TRIP_STATUS_JOURNEY_COMPLETED.equals(trip.getStatus())) {
                GlobalFuntion.startActivity(this, TripCompletedActivity.class);
            } else {
                GlobalFuntion.startActivity(SplashActivity.this, TripProcessActivity.class);
            }
        }
        finish();
    }

    @Override
    public void getSettingApp() {
        // Request permissions
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    public void showDialogSettingGps() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_setting_gps);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        // Get view
        final TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        final TextView tvReload = dialog.findViewById(R.id.tv_reload);
        final TextView tvOk = dialog.findViewById(R.id.tv_ok);

        // Get listener
        tvReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
                startActivity(getIntent());
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        // show dialog
        dialog.show();
    }
}
