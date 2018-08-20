package com.user.etow.ui.trip_process;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.user.etow.R;
import com.user.etow.constant.Constant;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.data.prefs.DataStoreManager;
import com.user.etow.models.Driver;
import com.user.etow.models.Trip;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.ui.main.MainActivity;
import com.user.etow.ui.trip_completed.TripCompletedActivity;
import com.user.etow.utils.Utils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TripProcessActivity extends BaseMVPDialogActivity implements TripProcessMVPView,
        OnMapReadyCallback {

    @Inject
    TripProcessPresenter presenter;

    @BindView(R.id.layout_driver_are_away)
    LinearLayout layoutDriverAreAway;

    @BindView(R.id.layout_wait_driver)
    LinearLayout layoutWaitDriver;

    @BindView(R.id.layout_cancel_trip)
    LinearLayout layoutCancelTrip;

    @BindView(R.id.layout_booking_accepted)
    LinearLayout layoutBookingAccepted;

    @BindView(R.id.layout_driver_arrived)
    LinearLayout layoutDriverArrived;

    @BindView(R.id.layout_trip_ongoing)
    LinearLayout layoutTripOngoing;

    @BindView(R.id.tv_current_estimate_time)
    TextView tvCurrentEstimateTime;

    @BindView(R.id.tv_time_driver_reach)
    TextView tvTimeDriverReach;

    private GoogleMap mMap;
    private Trip mTrip;
    private boolean mIsDriverAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this);
        presenter.initialView(this);

        // init map
        SupportMapFragment mMapFragment = new SupportMapFragment();
        mMapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_view_map));
        mMapFragment.getMapAsync(this);

        presenter.getTripDetail(this, DataStoreManager.getPrefIdTripProcess());
    }

    @Override
    protected boolean bindView() {
        return true;
    }

    @Override
    protected int addContextView() {
        return R.layout.activity_trip_process;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GlobalFuntion.getCurrentLocation(this, mLocationManager);
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
    public void getStatusDriverAvailable(ArrayList<Driver> listDriver) {
        if (listDriver != null && listDriver.size() > 0) {
            for (int i = 0; i < listDriver.size(); i++) {
                if (Constant.IS_DRIVER_FREE.equals(listDriver.get(i).getIs_free())) {
                    mIsDriverAvailable = true;
                    break;
                }
            }
        }

        if (!mIsDriverAvailable) {
            layoutDriverAreAway.setVisibility(View.VISIBLE);
        } else {
            layoutWaitDriver.setVisibility(View.VISIBLE);
        }
        layoutBookingAccepted.setVisibility(View.GONE);
    }

    @Override
    public void getTripDetail(Trip trip) {
        mTrip = trip;
        if (Constant.TRIP_STATUS_NEW.equals(trip.getStatus())) {
            presenter.initFirebase();
            presenter.checkDriverAvailable();
        } else if (Constant.TRIP_STATUS_REJECT.equals(trip.getStatus())) {
            showDialogRejected();
        } else if (Constant.TRIP_STATUS_CANCEL.equals(trip.getStatus())) {
            DataStoreManager.setPrefIdTripProcess(0);
            GlobalFuntion.startActivity(this, MainActivity.class);
            finishAffinity();
        } else if (Constant.TRIP_STATUS_ACCEPT.equals(trip.getStatus())) {
            layoutDriverAreAway.setVisibility(View.GONE);
            layoutWaitDriver.setVisibility(View.GONE);
            layoutBookingAccepted.setVisibility(View.VISIBLE);
        } else if (Constant.TRIP_STATUS_ARRIVED.equals(trip.getStatus())) {
            layoutBookingAccepted.setVisibility(View.GONE);
            layoutDriverArrived.setVisibility(View.VISIBLE);
        } else if (Constant.TRIP_STATUS_ON_GOING.equals(trip.getStatus())) {
            layoutDriverArrived.setVisibility(View.GONE);
            layoutTripOngoing.setVisibility(View.VISIBLE);
        } else if (Constant.TRIP_STATUS_JOURNEY_COMPLETED.equals(trip.getStatus())) {
            GlobalFuntion.startActivity(this, TripCompletedActivity.class);
            finish();
        }

        initData();
    }

    private void initData() {
        if (GlobalFuntion.mSetting != null) {
            int timeEstimateArrive = Integer.parseInt(GlobalFuntion.mSetting.getEstimateTimeArrive());
            int timeEstimateArrive01 = timeEstimateArrive - Integer.parseInt(GlobalFuntion.mSetting.getTimeBuffer());
            if (timeEstimateArrive01 < 0) timeEstimateArrive01 = 1;
            int timeEstimateArrive02 = timeEstimateArrive + Integer.parseInt(GlobalFuntion.mSetting.getTimeBuffer());
            tvCurrentEstimateTime.setText(timeEstimateArrive01 + " - " + timeEstimateArrive02 + " " + getString(R.string.unit_time));
            tvTimeDriverReach.setText(timeEstimateArrive01 + " - " + timeEstimateArrive02 + " " + getString(R.string.unit_time));
        } else {
            tvCurrentEstimateTime.setText("");
            tvTimeDriverReach.setText("");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (GlobalFuntion.LATITUDE > 0 && GlobalFuntion.LONGITUDE > 0) {
            // Add a marker in Sydney, Australia, and move the camera.
            LatLng currentLocation = new LatLng(GlobalFuntion.LATITUDE, GlobalFuntion.LONGITUDE);
            // create marker
            MarkerOptions marker = new MarkerOptions().position(currentLocation)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_black));
            // adding marker
            mMap.addMarker(marker);
            CameraUpdate myLoc = CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                    .target(currentLocation).zoom(13).build());
            mMap.moveCamera(myLoc);
        }
    }

    @OnClick(R.id.tv_confirm)
    public void onClickConfirm() {
        layoutDriverAreAway.setVisibility(View.GONE);
        layoutWaitDriver.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_cancel_driver_away)
    public void onClickCancelDriverAway() {
        presenter.updateTrip(DataStoreManager.getPrefIdTripProcess(), Constant.TRIP_STATUS_CANCEL,
                getString(R.string.no_driver_accepted));
    }

    @OnClick(R.id.tv_cancel_wait)
    public void onClickCancelWait() {
        layoutWaitDriver.setVisibility(View.GONE);
        layoutCancelTrip.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_no_cancel)
    public void onClickNoCancel() {
        layoutCancelTrip.setVisibility(View.GONE);
        layoutWaitDriver.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_yes_cancel)
    public void onClickYesCancel() {
        presenter.updateTrip(DataStoreManager.getPrefIdTripProcess(), Constant.TRIP_STATUS_CANCEL,
                getString(R.string.no_driver_accepted));
    }

    @OnClick(R.id.tv_call_driver)
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

        // Set data
        tvPhomeNumber.setText(phoneNumber);

        // Get listener
        tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.callPhoneNumber(TripProcessActivity.this, phoneNumber);
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

    private void showDialogRejected() {
        MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                .content(getString(R.string.message_trip_have_been_rejected))
                .positiveText(getString(R.string.action_ok))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        DataStoreManager.setPrefIdTripProcess(0);
                        GlobalFuntion.startActivity(TripProcessActivity.this, MainActivity.class);
                        finishAffinity();
                    }
                })
                .cancelable(false)
                .show();
    }
}
