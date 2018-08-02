package com.user.etow.ui.main.home;

/*
 * ******************************************************************************
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/16
 * ******************************************************************************
 */

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.user.etow.constant.Constant;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.listener.IGetDateListener;
import com.user.etow.listener.IGetTimeListener;
import com.user.etow.ui.base.BaseMVPFragmentWithDialog;
import com.user.etow.ui.booking_trip.BookingTripActivity;
import com.user.etow.ui.main.MainActivity;
import com.user.etow.utils.DateTimeUtils;
import com.user.etow.utils.StringUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends BaseMVPFragmentWithDialog implements HomeMVPView, OnMapReadyCallback {

    private final String TAG = HomeFragment.class.getName();

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

    private boolean mIsVehicleNormal = true;
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
    }

    @OnClick(R.id.tv_where_to_go)
    public void onClickLayoutWhereToGo() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.IS_VEHICLE_NORMAL, mIsVehicleNormal);
        bundle.putString(Constant.SCHEDULE_DATE, DateTimeUtils.convertTimeStampToDateFormat4(DateTimeUtils.getCurrentTimeStamp()));
        bundle.putString(Constant.IS_SCHEDULE_TRIP, Constant.IS_NOT_SCHEDULE);
        GlobalFuntion.startActivity(getActivity(), BookingTripActivity.class, bundle);
    }

    @OnClick(R.id.img_date)
    public void onClickDateBooking() {
        showDialogSelectScheduleDate();
    }

    public void showDialogSelectScheduleDate() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_select_schedule_date);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.BOTTOM;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        // Get view
        final TextView tvDate = dialog.findViewById(R.id.tv_date);
        final TextView tvTime = dialog.findViewById(R.id.tv_time);
        final TextView tvSetPickupDate = dialog.findViewById(R.id.tv_set_pickup_date);

        // Set listerer
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalFuntion.showDatePicker(getActivity(), new IGetDateListener() {
                    @Override
                    public void getDate(String date) {
                        tvDate.setText(date);
                    }
                });
            }
        });

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalFuntion.showTimePicker(getActivity(), new IGetTimeListener() {
                    @Override
                    public void getTime(String time) {
                        tvTime.setText(time);
                    }
                });
            }
        });

        tvSetPickupDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = tvDate.getText().toString().trim();
                String time = tvTime.getText().toString().trim();
                if (StringUtil.isEmpty(date)) {
                    showAlert(getString(R.string.please_select_date));
                } else if (StringUtil.isEmpty(time)) {
                    showAlert(getString(R.string.please_select_time));
                } else {
                    String dateTime = date + ", " + time;
                    if (Long.parseLong(DateTimeUtils.convertDateToTimeStampFormat4(dateTime)) <
                            Long.parseLong(DateTimeUtils.getCurrentTimeStamp())) {
                        showAlert(getString(R.string.schedule_booking_invalid));
                    } else {
                        dialog.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Constant.IS_VEHICLE_NORMAL, mIsVehicleNormal);
                        bundle.putString(Constant.SCHEDULE_DATE, dateTime);
                        bundle.putString(Constant.IS_SCHEDULE_TRIP, Constant.IS_SCHEDULE);
                        GlobalFuntion.startActivity(getActivity(), BookingTripActivity.class, bundle);
                    }
                }
            }
        });

        dialog.show();
    }
}
