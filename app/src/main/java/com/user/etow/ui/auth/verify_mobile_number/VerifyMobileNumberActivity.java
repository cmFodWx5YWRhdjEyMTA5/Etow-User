package com.user.etow.ui.auth.verify_mobile_number;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.user.etow.R;
import com.user.etow.adapter.CountryCodeAdapter;
import com.user.etow.constant.Constant;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.models.CountryCode;
import com.user.etow.ui.auth.enter_otp.EnterOTPActivity;
import com.user.etow.ui.auth.term_and_condition.TermAndConditionActivity;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.utils.StringUtil;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerifyMobileNumberActivity extends BaseMVPDialogActivity implements VerifyMobileNumberMVPView {

    @Inject
    VerifyMobileNumberPresenter presenter;

    @BindView(R.id.spinner_country_code)
    Spinner spinnerCountryCode;

    @BindView(R.id.tv_get_code)
    TextView tvGetCode;

    @BindView(R.id.edt_mobile_number)
    EditText edtMobileNumber;

    @BindView(R.id.checkbox_terms_conditions)
    CheckBox chbTermsConditions;

    private CountryCodeAdapter countryCodeAdapter;
    private CountryCode mCountryCode;
    private boolean mIsUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this);
        presenter.initialView(this);

        getDataIntent();
        initUi();
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mIsUpdate = bundle.getBoolean(Constant.IS_UPDATE);
        }
    }

    @Override
    protected boolean bindView() {
        return true;
    }

    @Override
    protected int addContextView() {
        return R.layout.activity_verify_mobile_number;
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
        countryCodeAdapter = new CountryCodeAdapter(this, R.layout.item_choose_country_code,
                listCountryCode);
        spinnerCountryCode.setAdapter(countryCodeAdapter);
        spinnerCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCountryCode = countryCodeAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        chbTermsConditions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!StringUtil.isEmpty(edtMobileNumber.getText().toString().trim())) {
                        tvGetCode.setBackgroundResource(R.drawable.bg_black_corner);
                        tvGetCode.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        tvGetCode.setBackgroundResource(R.drawable.bg_grey_corner);
                        tvGetCode.setTextColor(getResources().getColor(R.color.textColorAccent));
                    }
                } else {
                    tvGetCode.setBackgroundResource(R.drawable.bg_grey_corner);
                    tvGetCode.setTextColor(getResources().getColor(R.color.textColorAccent));
                }
            }
        });

        edtMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtil.isEmpty(s.toString())) {
                    if (chbTermsConditions.isChecked()) {
                        tvGetCode.setBackgroundResource(R.drawable.bg_black_corner);
                        tvGetCode.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        tvGetCode.setBackgroundResource(R.drawable.bg_grey_corner);
                        tvGetCode.setTextColor(getResources().getColor(R.color.textColorAccent));
                    }
                } else {
                    tvGetCode.setBackgroundResource(R.drawable.bg_grey_corner);
                    tvGetCode.setTextColor(getResources().getColor(R.color.textColorAccent));
                }
            }
        });
    }

    @OnClick(R.id.tv_get_code)
    public void onClickGetCode() {
        String strMobileNumber = edtMobileNumber.getText().toString().trim();
        if (StringUtil.isEmpty(edtMobileNumber.getText().toString().trim())) {
            showAlert(getString(R.string.please_enter_your_mobile_number));
        } else if (!chbTermsConditions.isChecked()) {
            showAlert(getString(R.string.please_agree_the_terms_and_conditions));
        } else if (!GlobalFuntion.checkMobileNumber(this, strMobileNumber, mCountryCode.getCode())) {
            showAlert(getString(R.string.msg_mobile_number_invalid));
        } else {
            presenter.getOTP(mCountryCode.getDialCode() + strMobileNumber);
        }
    }

    @OnClick(R.id.tv_terms_and_conditions)
    public void onClickTermsAndConditions() {
        GlobalFuntion.startActivity(this, TermAndConditionActivity.class);
    }

    @Override
    public void getStatusCodeOTP(String phone) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.IS_UPDATE, mIsUpdate);
        bundle.putString(Constant.PHONE_NUMBER, phone);
        GlobalFuntion.startActivity(this, EnterOTPActivity.class, bundle);
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
