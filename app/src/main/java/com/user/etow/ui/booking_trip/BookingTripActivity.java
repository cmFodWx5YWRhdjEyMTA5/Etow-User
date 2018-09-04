package com.user.etow.ui.booking_trip;

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
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.user.etow.adapter.autocompleteaddress.PlacesAutoCompleteAdapter;
import com.user.etow.constant.Constant;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.models.Trip;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.ui.confirm_booking.ConfirmBookingActivity;
import com.user.etow.utils.DateTimeUtils;
import com.user.etow.utils.StringUtil;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookingTripActivity extends BaseMVPDialogActivity implements BookingTripMVPView, OnMapReadyCallback {

    private final String TAG = BookingTripActivity.class.getName();

    @Inject
    BookingTripPresenter presenter;

    @BindView(R.id.img_vehicle_normal)
    ImageView imgVehicleNormal;

    @BindView(R.id.img_vehicle_flatbed)
    ImageView imgVehicleFlatbed;

    @BindView(R.id.tv_vehicle_normal)
    TextView tvVehicleNormal;

    @BindView(R.id.tv_vehicle_flatbed)
    TextView tvVehicleFlatbed;

    @BindView(R.id.layout_vehicle_normal)
    LinearLayout layoutVehicleNormal;

    @BindView(R.id.layout_vehicle_flatbed)
    LinearLayout layoutVehicleFlatbed;

    @BindView(R.id.tv_pick_up)
    AutoCompleteTextView tvPickUp;

    @BindView(R.id.tv_drop_off)
    AutoCompleteTextView tvDropOff;

    @BindView(R.id.img_clear_pick_up)
    ImageView imgClearPickUp;

    @BindView(R.id.img_clear_drop_off)
    ImageView imgClearDropOff;

    @BindView(R.id.tv_current_location)
    TextView tvCurrentLocation;

    @BindView(R.id.layout_date_time_booking)
    LinearLayout layoutDateTimeBooking;

    @BindView(R.id.tv_date_time_booking)
    TextView tvDateTimeBooking;

    private boolean mIsVehicleNormal;
    private int countClick = 1;
    private GoogleMap mMap;
    private PlacesAutoCompleteAdapter mAdapter;
    private HandlerThread mHandlerThread;
    private Handler mThreadHandler;
    private String mScheduleDate;
    private Trip mTripBooking;
    private int mIsScheduleTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this);
        presenter.initialView(this);

        getDataIntent();
        // init map
        SupportMapFragment mMapFragment = new SupportMapFragment();
        mMapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_view_map));
        mMapFragment.getMapAsync(this);

        selectCurrenLocation();
    }

    private void getDataIntent() {
        // init trip
        mTripBooking = new Trip();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mIsVehicleNormal = bundle.getBoolean(Constant.IS_VEHICLE_NORMAL);
            setSelectVehicle(mIsVehicleNormal);

            mIsScheduleTrip = bundle.getInt(Constant.IS_SCHEDULE_TRIP);
            mTripBooking.setIs_schedule(mIsScheduleTrip);

            if (bundle.getString(Constant.SCHEDULE_DATE) != null) {
                mScheduleDate = bundle.getString(Constant.SCHEDULE_DATE);
                mTripBooking.setPickup_date(DateTimeUtils.convertDateToTimeStampFormat4(mScheduleDate));
            }
            if (Constant.IS_SCHEDULE == mIsScheduleTrip) {
                layoutDateTimeBooking.setVisibility(View.VISIBLE);
                tvDateTimeBooking.setText(mScheduleDate);
            } else {
                layoutDateTimeBooking.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected boolean bindView() {
        return true;
    }

    @Override
    protected int addContextView() {
        return R.layout.activity_booking_trip;
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

    @OnClick(R.id.img_back)
    public void onClickBack() {
        GlobalFuntion.hideSoftKeyboard(this);
        onBackPressed();
    }

    @OnClick(R.id.layout_vehicle_normal)
    public void onClickLayoutVehicleNormal() {
        if (!mIsVehicleNormal) {
            mIsVehicleNormal = true;
            setSelectVehicle(mIsVehicleNormal);
        }
    }

    @OnClick(R.id.layout_vehicle_flatbed)
    public void onClickLayoutVehicleFlatbed() {
        if (mIsVehicleNormal) {
            mIsVehicleNormal = false;
            setSelectVehicle(mIsVehicleNormal);
        }
    }

    private void setSelectVehicle(boolean isNormal) {
        if (isNormal) {
            imgVehicleNormal.setImageResource(R.drawable.ic_vehicle_normal_white);
            layoutVehicleNormal.setBackgroundResource(R.drawable.bg_black_corner_left_bottom);
            tvVehicleNormal.setTextColor(getResources().getColor(R.color.white));

            imgVehicleFlatbed.setImageResource(R.drawable.ic_vehicle_flatbed_black);
            layoutVehicleFlatbed.setBackgroundResource(R.drawable.bg_grey_corner_right_bottom);
            tvVehicleFlatbed.setTextColor(getResources().getColor(R.color.textColorAccent));
        } else {
            imgVehicleFlatbed.setImageResource(R.drawable.ic_vehicle_flatbed_white);
            layoutVehicleFlatbed.setBackgroundResource(R.drawable.bg_black_corner_right_bottom);
            tvVehicleFlatbed.setTextColor(getResources().getColor(R.color.white));

            imgVehicleNormal.setImageResource(R.drawable.ic_vehicle_normal_black);
            layoutVehicleNormal.setBackgroundResource(R.drawable.bg_grey_corner_left_bottom);
            tvVehicleNormal.setTextColor(getResources().getColor(R.color.textColorAccent));
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

        setupAutoComplete();
    }

    @OnClick(R.id.tv_current_location)
    public void onClickCurrentLocation() {
        if (countClick == 1) {
            tvCurrentLocation.setTextColor(getResources().getColor(R.color.textColorPrimary));
            selectCurrenLocation();
        } else {
            tvCurrentLocation.setVisibility(View.GONE);
            tvPickUp.setVisibility(View.VISIBLE);
            tvPickUp.setHint(getString(R.string.current_location));
            tvPickUp.requestFocus();
            GlobalFuntion.showSoftKeyboard(this, tvPickUp);
        }
        countClick++;
    }

    private void selectCurrenLocation() {
        mTripBooking.setPickup_latitude(GlobalFuntion.LATITUDE + "");
        mTripBooking.setPickup_longitude(GlobalFuntion.LONGITUDE + "");
        String currentAddress = GlobalFuntion.getCompleteAddressString(this, GlobalFuntion.LATITUDE,
                GlobalFuntion.LONGITUDE);
        if (StringUtil.isEmpty(currentAddress)) {
            showAlert(getString(R.string.unble_trace_location));
        } else {
            mTripBooking.setPick_up(currentAddress);
        }
    }

    public void setupAutoComplete() {
        if (mThreadHandler == null) {
            mHandlerThread = new HandlerThread(TAG, android.os.Process.THREAD_PRIORITY_BACKGROUND);
            mHandlerThread.start();

            // Initialize the Handler
            mThreadHandler = new Handler(mHandlerThread.getLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 1) {
                        ArrayList<String> results = mAdapter.resultList;

                        if (results != null && results.size() > 0) {
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mAdapter.notifyDataSetInvalidated();
                        }
                    }
                }
            };
        }
        // set auto complete address pick up
        tvPickUp.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.item_auto_place, true));
        tvPickUp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                GlobalFuntion.hideSoftKeyboard(BookingTripActivity.this, tvPickUp);
                // Get data associated with the specified position
                // in the list (AdapterView)
                final String description = (String) parent.getItemAtPosition(position);
                if (!getString(R.string.current_location).equals(tvPickUp.getText().toString().trim())) {
                    LatLng latLng = GlobalFuntion.getLocationFromAddress(BookingTripActivity.this, description);
                    if (latLng != null) {
                        mTripBooking.setPickup_latitude(latLng.latitude + "");
                        mTripBooking.setPickup_longitude(latLng.longitude + "");
                        mTripBooking.setPick_up(tvPickUp.getText().toString().trim());
                    } else {
                        showAlert(getString(R.string.error_select_pick_up_location));
                    }
                }
            }
        });

        tvPickUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String value = s.toString();
                if (value.length() > 0) {
                    if (getString(R.string.current_location).equals(value)) {
                        selectCurrenLocation();
                    }
                    imgClearPickUp.setVisibility(View.VISIBLE);
                    // Remove all callbacks and messages
                    mThreadHandler.removeCallbacksAndMessages(null);
                    // Now add a new one
                    mThreadHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (mAdapter == null) {
                                mAdapter = new PlacesAutoCompleteAdapter(BookingTripActivity.this,
                                        R.layout.item_auto_place, true);
                            }
                            // Background thread
                            mAdapter.resultList = mAdapter.mPlaceAPI.autocomplete(value);
                            // Post to Main Thread
                            mThreadHandler.sendEmptyMessage(1);
                        }
                    }, 500);
                } else {
                    imgClearPickUp.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvPickUp.setDropDownHeight(DrawerLayout.LayoutParams.WRAP_CONTENT);
            }
        });

        // set auto complete address drop off
        tvDropOff.setAdapter(new PlacesAutoCompleteAdapter(BookingTripActivity.this,
                R.layout.item_auto_place, false));
        tvDropOff.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                GlobalFuntion.hideSoftKeyboard(BookingTripActivity.this, tvDropOff);
                // Get data associated with the specified position
                // in the list (AdapterView)
                final String description = (String) parent.getItemAtPosition(position);
                LatLng latLng = GlobalFuntion.getLocationFromAddress(BookingTripActivity.this, description);
                if (latLng != null) {
                    mTripBooking.setDropoff_latitude(latLng.latitude + "");
                    mTripBooking.setDropoff_longitude(latLng.longitude + "");
                    mTripBooking.setDrop_off(tvDropOff.getText().toString().trim());

                    if (StringUtil.isEmpty(mTripBooking.getPick_up()) ||
                            StringUtil.isEmpty(mTripBooking.getPickup_latitude()) ||
                            StringUtil.isEmpty(mTripBooking.getPickup_longitude())) {
                        // Not do anything
                    } else {
                        goToConfirmBooking();
                    }
                } else {
                    showAlert(getString(R.string.error_select_drop_off_location));
                }
            }
        });

        tvDropOff.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String value = s.toString();
                if (value.length() > 0) {
                    imgClearDropOff.setVisibility(View.VISIBLE);
                    // Remove all callbacks and messages
                    mThreadHandler.removeCallbacksAndMessages(null);
                    // Now add a new one
                    mThreadHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (mAdapter == null) {
                                mAdapter = new PlacesAutoCompleteAdapter(BookingTripActivity.this,
                                        R.layout.item_auto_place, false);
                            }
                            // Background thread
                            mAdapter.resultList = mAdapter.mPlaceAPI.autocomplete(value);
                            // Post to Main Thread
                            mThreadHandler.sendEmptyMessage(1);
                        }
                    }, 500);
                } else {
                    imgClearDropOff.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvDropOff.setDropDownHeight(DrawerLayout.LayoutParams.WRAP_CONTENT);
            }
        });
    }

    private void goToConfirmBooking() {
        if (mIsVehicleNormal) {
            mTripBooking.setVehicle_type(Constant.TYPE_VEHICLE_NORMAL);
        } else {
            mTripBooking.setVehicle_type(Constant.TYPE_VEHICLE_FLATBED);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.OBJECT_TRIP, mTripBooking);
        GlobalFuntion.startActivity(BookingTripActivity.this, ConfirmBookingActivity.class, bundle);
    }

    @OnClick(R.id.img_clear_pick_up)
    public void onClickClearPickUp() {
        tvPickUp.setText("");
        selectCurrenLocation();
    }

    @OnClick(R.id.img_clear_drop_off)
    public void onClickClearDropOff() {
        tvDropOff.setText("");
    }
}
