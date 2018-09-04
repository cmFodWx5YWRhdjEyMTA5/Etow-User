package com.user.etow.ui.trip_process;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.user.etow.R;
import com.user.etow.constant.Constant;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.data.prefs.DataStoreManager;
import com.user.etow.direction.DirectionFinder;
import com.user.etow.direction.DirectionFinderListener;
import com.user.etow.direction.Route;
import com.user.etow.models.Driver;
import com.user.etow.models.Trip;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.ui.main.MainActivity;
import com.user.etow.ui.trip_completed.TripCompletedActivity;
import com.user.etow.utils.StringUtil;
import com.user.etow.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TripProcessActivity extends BaseMVPDialogActivity implements TripProcessMVPView,
        OnMapReadyCallback, DirectionFinderListener {

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

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    private ArrayList<LatLng> mListPoints = new ArrayList<LatLng>();
    private Polyline mPolyline;
    private boolean mIsLoadMap;

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
        /*int distanceRequest = 0;
        if (GlobalFuntion.mSetting != null) {
            if (!StringUtil.isEmpty(GlobalFuntion.mSetting.getDistanceRequest())) {
                distanceRequest = Integer.parseInt(GlobalFuntion.mSetting.getDistanceRequest());
            }
        }*/
        if (listDriver != null && listDriver.size() > 0) {
            for (int i = 0; i < listDriver.size(); i++) {
                if (Constant.IS_DRIVER_FREE == listDriver.get(i).getIs_free()) {
                    mIsDriverAvailable = true;
                    break;
                }
            }
        }

        if (!mIsDriverAvailable) {
            layoutDriverAreAway.setVisibility(View.VISIBLE);
            layoutWaitDriver.setVisibility(View.GONE);
        } else {
            layoutDriverAreAway.setVisibility(View.GONE);
            layoutWaitDriver.setVisibility(View.VISIBLE);
        }
        layoutBookingAccepted.setVisibility(View.GONE);
    }

    @Override
    public void getTripDetail(Trip trip) {
        mTrip = trip;
        if (Constant.TRIP_STATUS_NEW == trip.getStatus()) {
            presenter.initFirebase();
            presenter.checkDriverAvailable();
            loadMapCurrentLocaetion();
        } else if (Constant.TRIP_STATUS_REJECT == trip.getStatus()) {
            showDialogRejected();
        } else if (Constant.TRIP_STATUS_CANCEL == trip.getStatus()) {
            DataStoreManager.setPrefIdTripProcess(0);
            DataStoreManager.setEstimateTimeArrived("");
            GlobalFuntion.startActivity(this, MainActivity.class);
            finishAffinity();
        } else if (Constant.TRIP_STATUS_ACCEPT == trip.getStatus()) {
            layoutDriverAreAway.setVisibility(View.GONE);
            layoutWaitDriver.setVisibility(View.GONE);
            layoutBookingAccepted.setVisibility(View.VISIBLE);
            mMap.clear();
            LatLng latLng = new LatLng(Double.parseDouble(trip.getCurrent_latitude()),
                    Double.parseDouble(trip.getCurrent_longitude()));
            showMarkerOnMap(latLng);
            mListPoints.add(latLng);
            redrawLine();
        } else if (Constant.TRIP_STATUS_ARRIVED == trip.getStatus()) {
            layoutBookingAccepted.setVisibility(View.GONE);
            layoutDriverArrived.setVisibility(View.VISIBLE);
            if (Constant.IS_SCHEDULE == trip.getStatus()) {
                mMap.clear();
                LatLng latLng = new LatLng(Double.parseDouble(trip.getCurrent_latitude()),
                        Double.parseDouble(trip.getCurrent_longitude()));
                showMarkerOnMap(latLng);
                mListPoints.add(latLng);
                redrawLine();
            }
        } else if (Constant.TRIP_STATUS_ON_GOING == trip.getStatus()) {
            layoutDriverArrived.setVisibility(View.GONE);
            layoutTripOngoing.setVisibility(View.VISIBLE);
            if (!mIsLoadMap) {
                loadMap(trip);
                mListPoints = new ArrayList<>();
            }
            // Draw road on map
            LatLng latLng = new LatLng(Double.parseDouble(trip.getCurrent_latitude()),
                    Double.parseDouble(trip.getCurrent_longitude()));
            mListPoints.add(latLng);
            redrawLine();
        } else if (Constant.TRIP_STATUS_JOURNEY_COMPLETED == trip.getStatus()) {
            GlobalFuntion.startActivity(this, TripCompletedActivity.class);
            finish();
        }

        initData();
    }

    private void loadMap(Trip trip) {
        mMap.clear();
        String strCurrentLocation = GlobalFuntion.getCompleteAddressString(this, GlobalFuntion.LATITUDE, GlobalFuntion.LONGITUDE);
        String strDropOffLocation = trip.getDrop_off();
        if (StringUtil.isEmpty(strCurrentLocation)) {
            showAlert(getString(R.string.unble_trace_location));
        } else {
            sendRequestDirection(strCurrentLocation, strDropOffLocation);
        }
        mIsLoadMap = true;
    }

    private void initData() {
        // Set up estimate time arrive.
        String estimateTimeArrived = "";
        if (!StringUtil.isEmpty(DataStoreManager.getEstimateTimeArrived())) {
            estimateTimeArrived = DataStoreManager.getEstimateTimeArrived();
            tvCurrentEstimateTime.setText(estimateTimeArrived);
            tvTimeDriverReach.setText(estimateTimeArrived);
        }
        /*else {
            int minFrom = 1;
            int maxFrom = 10;
            int minTo = 15;
            int maxTo = 25;
            Random randomFrom = new Random();
            Random randomTo = new Random();
            int fromTime = randomFrom.nextInt(maxFrom - minFrom + 1) + minFrom;
            int toTime = randomTo.nextInt(maxTo - minTo + 1) + minTo;
            estimateTimeArrived = fromTime + " - " + toTime + " " + getString(R.string.unit_time);
        }*/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        loadMapCurrentLocaetion();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        presenter.getTripDetail(this, DataStoreManager.getPrefIdTripProcess());
    }

    private void loadMapCurrentLocaetion() {
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
        presenter.updateTrip(DataStoreManager.getPrefIdTripProcess(), Constant.TRIP_STATUS_CANCEL + "",
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
        presenter.updateTrip(DataStoreManager.getPrefIdTripProcess(), Constant.TRIP_STATUS_CANCEL + "",
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
                        DataStoreManager.setEstimateTimeArrived("");
                        GlobalFuntion.startActivity(TripProcessActivity.this, MainActivity.class);
                        finishAffinity();
                    }
                })
                .cancelable(false)
                .show();
    }

    private void sendRequestDirection(String origin, String destination) {
        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDirectionFinderStart() {
        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            // ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            // ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);
            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_black))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_black))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLACK).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    private void redrawLine() {
        // googleMap.clear();  //clears all Markers and Polylines
        PolylineOptions options = new PolylineOptions().width(10)
                .color(getResources().getColor(R.color.colorRouteLine)).geodesic(true);
        for (int i = 0; i < mListPoints.size(); i++) {
            LatLng point = mListPoints.get(i);
            options.add(point);
        }
        mPolyline = mMap.addPolyline(options); //add Polyline
    }

    private void showMarkerOnMap(LatLng latLng) {
        // create marker
        MarkerOptions marker = new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_black));
        // adding marker
        mMap.addMarker(marker);
        CameraUpdate myLoc = CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(latLng).zoom(13).build());
        mMap.moveCamera(myLoc);
    }
}
