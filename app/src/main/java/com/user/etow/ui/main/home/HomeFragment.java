package com.user.etow.ui.main.home;

/*
 * ******************************************************************************
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/16
 * ******************************************************************************
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.user.etow.listener.IMaps;
import com.user.etow.ui.base.BaseMVPFragmentWithDialog;
import com.user.etow.ui.confirm_booking.ConfirmBookingActivity;
import com.user.etow.ui.date_booking.DateBookingActivity;
import com.user.etow.ui.main.MainActivity;
import com.user.etow.utils.MapsUtil;
import com.user.etow.utils.StringUtil;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends BaseMVPFragmentWithDialog implements HomeMVPView, OnMapReadyCallback {

    private final String TAG = HomeFragment.class.getName();

    private MainActivity mMainActivity;

    @Inject
    HomePresenter presenter;

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

    @BindView(R.id.layout_where_to_go)
    RelativeLayout layoutWhereToGo;

    @BindView(R.id.layout_select_location)
    LinearLayout layoutSelectLocation;

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

    private boolean mIsVehicleNormal = true;
    private int countClick = 1;
    private GoogleMap mMap;
    private PlacesAutoCompleteAdapter mAdapter;
    private HandlerThread mHandlerThread;
    private Handler mThreadHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMainActivity = (MainActivity) getActivity();
        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this, view);
        presenter.initialView(this);
        ((MainActivity) getActivity()).showAndHiddenItemToolbar("");

        SupportMapFragment mMapFragment = new SupportMapFragment();
        this.getChildFragmentManager().beginTransaction()
                .add(R.id.fragment_view_map, mMapFragment).commit();
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!StringUtil.isEmpty(mMainActivity.getDateBooking())) {
            layoutDateTimeBooking.setVisibility(View.VISIBLE);
            tvDateTimeBooking.setText(mMainActivity.getDateBooking());
            onClickLayoutWhereToGo();
        } else {
            layoutDateTimeBooking.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroyView();
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    public void onErrorCallApi(int code) {
        GlobalFuntion.showMessageError(getActivity(), code);
    }

    @OnClick(R.id.layout_vehicle_normal)
    public void onClickLayoutVehicleNormal() {
        if (!mIsVehicleNormal) {
            mIsVehicleNormal = true;
            imgVehicleNormal.setImageResource(R.drawable.ic_vehicle_normal_white);
            layoutVehicleNormal.setBackgroundResource(R.drawable.bg_black_corner_left_bottom);
            tvVehicleNormal.setTextColor(getResources().getColor(R.color.white));

            imgVehicleFlatbed.setImageResource(R.drawable.ic_vehicle_flatbed_black);
            layoutVehicleFlatbed.setBackgroundResource(R.drawable.bg_grey_corner_right_bottom);
            tvVehicleFlatbed.setTextColor(getResources().getColor(R.color.textColorAccent));
        }
    }

    @OnClick(R.id.layout_vehicle_flatbed)
    public void onClickLayoutVehicleFlatbed() {
        if (mIsVehicleNormal) {
            mIsVehicleNormal = false;
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

        setupAutoComplete();
    }

    @OnClick(R.id.layout_where_to_go)
    public void onClickLayoutWhereToGo() {
        layoutWhereToGo.setVisibility(View.GONE);
        layoutSelectLocation.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_current_location)
    public void onClickCurrentLocation() {
        if (countClick == 1) {
            // Todo Get current location
            tvCurrentLocation.setTextColor(getResources().getColor(R.color.textColorPrimary));
        } else {
            tvCurrentLocation.setVisibility(View.GONE);
            tvPickUp.setVisibility(View.VISIBLE);
            tvPickUp.requestFocus();
        }
        countClick++;
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
        tvPickUp.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.item_auto_place, true));
        tvPickUp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                GlobalFuntion.hideSoftKeyboard(getActivity(), tvPickUp);
                // Get data associated with the specified position
                // in the list (AdapterView)
                final String description = (String) parent
                        .getItemAtPosition(position);

                // Move camera to new address.
                new MapsUtil.GetLatLngByAddress(new IMaps() {

                    @Override
                    public void processFinished(Object obj) {
                        try {
                            // Move camera smoothly
                            LatLng latLng = (LatLng) obj;
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                            MarkerOptions marker = new MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_black));
                            mMap.addMarker(marker);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    @Override
                    public void getLatLong(String latitude, String longitude) {

                    }
                }).execute(description);
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
                    /*if (Constant.CURRENT_LOCATION.equals(value)) {
                        tvPickUp.setVisibility(View.GONE);
                        tvCurrentLocation.setVisibility(View.VISIBLE);
                        tvCurrentLocation.setTextColor(getResources().getColor(R.color.textColorPrimary));
                    }*/
                    imgClearPickUp.setVisibility(View.VISIBLE);
                    // Remove all callbacks and messages
                    mThreadHandler.removeCallbacksAndMessages(null);
                    // Now add a new one
                    mThreadHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (mAdapter == null) {
                                mAdapter = new PlacesAutoCompleteAdapter(getActivity(), R.layout.item_auto_place, true);
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
        tvDropOff.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.item_auto_place, false));
        tvDropOff.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                GlobalFuntion.hideSoftKeyboard(getActivity(), tvDropOff);
                // Get data associated with the specified position
                // in the list (AdapterView)
                final String description = (String) parent
                        .getItemAtPosition(position);

                // Move camera to new address.
                new MapsUtil.GetLatLngByAddress(new IMaps() {

                    @Override
                    public void processFinished(Object obj) {
                        try {
                            // Move camera smoothly
                            LatLng latLng = (LatLng) obj;
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                            MarkerOptions marker = new MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_black));
                            mMap.addMarker(marker);

                            // Go to Confirm Booking
                            Bundle bundle = new Bundle();
                            bundle.putBoolean(Constant.IS_DATE_SCHEDULED, false);
                            GlobalFuntion.startActivity(getActivity(), ConfirmBookingActivity.class, bundle);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    @Override
                    public void getLatLong(String latitude, String longitude) {

                    }
                }).execute(description);
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
                                mAdapter = new PlacesAutoCompleteAdapter(getActivity(),
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

    @OnClick(R.id.img_clear_pick_up)
    public void onClickClearPickUp() {
        tvPickUp.setText("");
    }

    @OnClick(R.id.img_clear_drop_off)
    public void onClickClearDropOff() {
        tvDropOff.setText("");
    }

    @OnClick(R.id.img_date)
    public void onClickDateBooking() {
        Intent intent = new Intent(getActivity(), DateBookingActivity.class);
        startActivityForResult(intent, GlobalFuntion.PICK_SCHEDULE_DATE);
    }
}
