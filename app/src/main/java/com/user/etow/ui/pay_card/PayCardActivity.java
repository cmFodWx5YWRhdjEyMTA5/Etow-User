package com.user.etow.ui.pay_card;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.user.etow.R;
import com.user.etow.adapter.CountryPayCardAdapter;
import com.user.etow.constant.Constant;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.data.prefs.DataStoreManager;
import com.user.etow.models.CountryCode;
import com.user.etow.models.Trip;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.ui.pay_card_result.PayCardResultActivity;
import com.user.etow.ui.rate_trip.RateTripActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayCardActivity extends BaseMVPDialogActivity implements PayCardMVPView {

    @Inject
    PayCardPresenter presenter;

    @BindView(R.id.spinner_country)
    Spinner spnCountry;

    private CountryPayCardAdapter countryPayCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this);
        presenter.initialView(this);

        initUi();
        presenter.getTripDetail(this, DataStoreManager.getPrefIdTripProcess());
    }

    @Override
    protected boolean bindView() {
        return true;
    }

    @Override
    protected int addContextView() {
        return R.layout.activity_pay_card;
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

    private void initUi() {
        ArrayList<CountryCode> listCountryCode = GlobalFuntion.getListCountryCode(this);
        countryPayCardAdapter = new CountryPayCardAdapter(this, R.layout.item_choose_country_pay_card,
                listCountryCode);
        spnCountry.setAdapter(countryPayCardAdapter);
        spnCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryCode countryCode = countryPayCardAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.tv_continue)
    public void onClickContinue() {
        presenter.updatePaymentStatus(DataStoreManager.getPrefIdTripProcess(), Constant.TYPE_PAYMENT_CARD,
                Constant.PAYMENT_STATUS_PAYMENT_FAIL);
    }

    @Override
    public void updateStatusTrip(Trip trip) {
        if (Constant.PAYMENT_STATUS_PAYMENT_SUCCESS.equals(trip.getPayment_status()) && trip.getIs_rate() == 0) {
            GlobalFuntion.startActivity(this, RateTripActivity.class);
            finish();
        } else if (Constant.PAYMENT_STATUS_PAYMENT_FAIL.equals(trip.getPayment_status())) {
            GlobalFuntion.startActivity(this, PayCardResultActivity.class);
            finish();
        }
    }
}
