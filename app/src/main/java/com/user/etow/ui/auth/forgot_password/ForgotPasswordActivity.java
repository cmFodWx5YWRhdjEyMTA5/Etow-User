package com.user.etow.ui.auth.forgot_password;

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
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.utils.StringUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity extends BaseMVPDialogActivity implements ForgotPasswordMVPView {

    @Inject
    ForgotPasswordPresenter presenter;

    @BindView(R.id.edt_email)
    EditText edtEmail;

    @BindView(R.id.tv_request_password)
    TextView tvRequestPassword;

    private boolean mIsClickRequestPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this);
        presenter.initialView(this);

        setListener();
    }

    @Override
    protected boolean bindView() {
        return true;
    }

    @Override
    protected int addContextView() {
        return R.layout.activity_forgot_password;
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
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtil.isValidEmail(s)) {
                    mIsClickRequestPassword = true;
                    tvRequestPassword.setBackgroundResource(R.drawable.bg_black_corner);
                    tvRequestPassword.setTextColor(getResources().getColor(R.color.white));
                } else {
                    mIsClickRequestPassword = false;
                    tvRequestPassword.setBackgroundResource(R.drawable.bg_grey_corner);
                    tvRequestPassword.setTextColor(getResources().getColor(R.color.textColorAccent));
                }
            }
        });
    }

    @OnClick(R.id.tv_request_password)
    public void onClickRequestPassword() {
        if (mIsClickRequestPassword) {
            presenter.resetPassword(edtEmail.getText().toString().trim());
        }
    }

    @Override
    public void getStatusResetPassword() {
        edtEmail.setText("");
        onBackPressed();
    }
}
