package com.user.etow.ui.auth.sign_in;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.os.Bundle;

import com.user.etow.R;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.ui.auth.forgot_password.ForgotPasswordActivity;
import com.user.etow.ui.auth.verify_mobile_number.VerifyMobileNumberActivity;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.ui.main.MainActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends BaseMVPDialogActivity implements SignInMVPView {

    @Inject
    SignInPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this);
        presenter.initialView(this);
    }

    @Override
    protected boolean bindView() {
        return true;
    }

    @Override
    protected int addContextView() {
        return R.layout.activity_sign_in;
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

    @OnClick(R.id.tv_forgot_password)
    public void onClickForgotPassword() {
        GlobalFuntion.startActivity(this, ForgotPasswordActivity.class);
    }

    @OnClick(R.id.tv_sign_up)
    public void onClickSignUp() {
        GlobalFuntion.startActivity(this, VerifyMobileNumberActivity.class);
    }

    @OnClick(R.id.tv_sign_in)
    public void onClickSignIn() {
        GlobalFuntion.startActivity(this, MainActivity.class);
    }
}
