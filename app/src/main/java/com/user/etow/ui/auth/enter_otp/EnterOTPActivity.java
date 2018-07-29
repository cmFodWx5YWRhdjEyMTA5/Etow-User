package com.user.etow.ui.auth.enter_otp;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.user.etow.R;
import com.user.etow.constant.Constant;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.messages.EditPhoneNumberSuccess;
import com.user.etow.ui.auth.sign_up.SignUpActivity;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EnterOTPActivity extends BaseMVPDialogActivity implements EnterOTPMVPView {

    @Inject
    EnterOTPPresenter presenter;

    @BindView(R.id.tv_message)
    TextView tvMessage;

    @BindView(R.id.tv_done)
    TextView tvDone;

    @BindView(R.id.edt_otp)
    EditText edtOtp;

    private boolean mIsActiveDone;
    private String mPhoneNumber;
    private boolean mIsUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this);
        presenter.initialView(this);

        getDataIntent();

        tvMessage.setText(getString(R.string.have_sent_sms) + " " + mPhoneNumber);
        setListener();
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPhoneNumber = bundle.getString(Constant.PHONE_NUMBER);
            mIsUpdate = bundle.getBoolean(Constant.IS_UPDATE);
        }
    }

    @Override
    protected boolean bindView() {
        return true;
    }

    @Override
    protected int addContextView() {
        return R.layout.activity_enter_otp;
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

    private void setListener() {
        edtOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtil.isEmpty(s.toString().trim())) {
                    mIsActiveDone = true;
                    tvDone.setBackgroundResource(R.drawable.bg_black_corner);
                    tvDone.setTextColor(getResources().getColor(R.color.white));
                } else {
                    mIsActiveDone = false;
                    tvDone.setBackgroundResource(R.drawable.bg_grey_corner);
                    tvDone.setTextColor(getResources().getColor(R.color.textColorAccent));
                }
            }
        });
    }

    @OnClick(R.id.tv_done)
    public void onClickDone() {
        if (mIsActiveDone) {
            presenter.verifyOTP(edtOtp.getText().toString().trim(), mPhoneNumber);
        }
    }

    @OnClick(R.id.tv_not_get_code)
    public void onClickDidNotGetTheCode() {
        onBackPressed();
    }

    @Override
    public void getStatusVerifyOTP(String phone) {
        if (mIsUpdate) {
            EventBus.getDefault().post(new EditPhoneNumberSuccess(mPhoneNumber));
            finish();
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.PHONE_NUMBER, mPhoneNumber);
            GlobalFuntion.startActivity(this, SignUpActivity.class, bundle);
        }
    }
}
