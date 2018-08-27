package com.user.etow.ui.rate_trip;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

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
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.data.prefs.DataStoreManager;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.ui.main.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RateTripActivity extends BaseMVPDialogActivity implements RateTripMVPView, OnMapReadyCallback {

    @Inject
    RateTripPresenter presenter;

    @BindView(R.id.layout_thanks_service)
    LinearLayout layoutThanksService;

    @BindView(R.id.layout_rate_service)
    LinearLayout layoutRateService;

    @BindView(R.id.ratingbar)
    RatingBar ratingbar;

    @BindView(R.id.tv_rate_your_trip)
    TextView tvRateYourTrip;

    private GoogleMap mMap;
    private boolean mIsActiveRateTrip;
    private int mRate;

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

        setListener();
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

    @OnClick(R.id.tv_ok_thank_you)
    public void onClickOkThankYou() {
        layoutThanksService.setVisibility(View.GONE);
        layoutRateService.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_skip)
    public void onClickSkipRate() {
        presenter.rateTrip(DataStoreManager.getPrefIdTripProcess(), 0);
    }

    @OnClick(R.id.tv_rate_your_trip)
    public void onClickRateTrip() {
        if (mIsActiveRateTrip) {
            presenter.rateTrip(DataStoreManager.getPrefIdTripProcess(), mRate);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setListener() {
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (ratingBar.getRating() > 0) {
                    tvRateYourTrip.setBackgroundResource(R.drawable.bg_black_corner);
                    tvRateYourTrip.setTextColor(getResources().getColor(R.color.white));
                    mIsActiveRateTrip = true;
                    mRate = (int) (ratingBar.getRating() * 2);
                } else {
                    tvRateYourTrip.setBackgroundResource(R.drawable.bg_grey_corner);
                    tvRateYourTrip.setTextColor(getResources().getColor(R.color.textColorAccent));
                    mIsActiveRateTrip = false;
                }
            }
        });
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void getStatusRateTrip() {
        DataStoreManager.setPrefIdTripProcess(0);
        GlobalFuntion.startActivity(this, MainActivity.class);
        finishAffinity();
    }
}
