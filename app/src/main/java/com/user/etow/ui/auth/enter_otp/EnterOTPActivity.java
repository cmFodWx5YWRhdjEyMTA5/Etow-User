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
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.ui.auth.sign_up.SignUpActivity;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.utils.StringUtil;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this);
        presenter.initialView(this);

        tvMessage.setText(getString(R.string.have_sent_sms) + " " + "+84 87564656");
        setListener();
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
            GlobalFuntion.startActivity(this, SignUpActivity.class);
        }
    }

    @OnClick(R.id.tv_not_get_code)
    public void onClickDidNotGetTheCode() {

    }
}
