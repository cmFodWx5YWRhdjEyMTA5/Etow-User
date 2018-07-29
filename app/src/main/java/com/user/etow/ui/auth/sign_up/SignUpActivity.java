package com.user.etow.ui.auth.sign_up;

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
import com.user.etow.ui.auth.sign_in.SignInActivity;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.ui.main.MainActivity;
import com.user.etow.utils.StringUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends BaseMVPDialogActivity implements SignUpMVPView {

    @Inject
    SignUpPresenter presenter;

    @BindView(R.id.edt_name)
    EditText edtName;

    @BindView(R.id.tv_mobile_number)
    TextView tvMobileNumber;

    @BindView(R.id.edt_email)
    EditText edtEmail;

    @BindView(R.id.edt_password)
    EditText edtPassword;

    @BindView(R.id.edt_confirm_password)
    EditText edtConfirmPassword;

    @BindView(R.id.tv_sign_up)
    TextView tvSignUp;

    private boolean mIsActiveSignUp;
    private String mPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this);
        presenter.initialView(this);

        getDataIntent();

        tvMobileNumber.setText(mPhoneNumber);
        setListener();
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPhoneNumber = bundle.getString(Constant.PHONE_NUMBER);
        }
    }

    @Override
    protected boolean bindView() {
        return true;
    }

    @Override
    protected int addContextView() {
        return R.layout.activity_sign_up;
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
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtil.isEmpty(s.toString().trim()) &&
                        !StringUtil.isEmpty(edtEmail.getText().toString().trim()) &&
                        !StringUtil.isEmpty(edtPassword.getText().toString().trim()) &&
                        !StringUtil.isEmpty(edtConfirmPassword.getText().toString().trim())) {
                    mIsActiveSignUp = true;
                    tvSignUp.setBackgroundResource(R.drawable.bg_black_corner);
                    tvSignUp.setTextColor(getResources().getColor(R.color.white));
                } else {
                    mIsActiveSignUp = false;
                    tvSignUp.setBackgroundResource(R.drawable.bg_grey_corner);
                    tvSignUp.setTextColor(getResources().getColor(R.color.textColorAccent));
                }
            }
        });

        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtil.isEmpty(s.toString().trim()) &&
                        !StringUtil.isEmpty(edtName.getText().toString().trim()) &&
                        !StringUtil.isEmpty(edtPassword.getText().toString().trim()) &&
                        !StringUtil.isEmpty(edtConfirmPassword.getText().toString().trim())) {
                    mIsActiveSignUp = true;
                    tvSignUp.setBackgroundResource(R.drawable.bg_black_corner);
                    tvSignUp.setTextColor(getResources().getColor(R.color.white));
                } else {
                    mIsActiveSignUp = false;
                    tvSignUp.setBackgroundResource(R.drawable.bg_grey_corner);
                    tvSignUp.setTextColor(getResources().getColor(R.color.textColorAccent));
                }
            }
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtil.isEmpty(s.toString().trim()) &&
                        !StringUtil.isEmpty(edtName.getText().toString().trim()) &&
                        !StringUtil.isEmpty(edtEmail.getText().toString().trim()) &&
                        !StringUtil.isEmpty(edtConfirmPassword.getText().toString().trim())) {
                    mIsActiveSignUp = true;
                    tvSignUp.setBackgroundResource(R.drawable.bg_black_corner);
                    tvSignUp.setTextColor(getResources().getColor(R.color.white));
                } else {
                    mIsActiveSignUp = false;
                    tvSignUp.setBackgroundResource(R.drawable.bg_grey_corner);
                    tvSignUp.setTextColor(getResources().getColor(R.color.textColorAccent));
                }
            }
        });

        edtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtil.isEmpty(s.toString().trim()) &&
                        !StringUtil.isEmpty(edtName.getText().toString().trim()) &&
                        !StringUtil.isEmpty(edtEmail.getText().toString().trim()) &&
                        !StringUtil.isEmpty(edtPassword.getText().toString().trim())) {
                    mIsActiveSignUp = true;
                    tvSignUp.setBackgroundResource(R.drawable.bg_black_corner);
                    tvSignUp.setTextColor(getResources().getColor(R.color.white));
                } else {
                    mIsActiveSignUp = false;
                    tvSignUp.setBackgroundResource(R.drawable.bg_grey_corner);
                    tvSignUp.setTextColor(getResources().getColor(R.color.textColorAccent));
                }
            }
        });
    }

    @OnClick(R.id.tv_sign_in)
    public void onClickSignIn() {
        GlobalFuntion.startActivity(this, SignInActivity.class);
        finishAffinity();
    }

    @OnClick(R.id.tv_sign_up)
    public void onClickSignUp() {

        if (mIsActiveSignUp) {
            if (!StringUtil.isValidEmail(edtEmail.getText().toString().trim())) {
                showAlert(getString(R.string.email_invalid));
            } else if (!edtPassword.getText().toString().trim()
                    .equals(edtConfirmPassword.getText().toString().trim())) {
                showAlert(getString(R.string.password_not_match));
            } else {
                String strFullName = edtName.getText().toString().trim();
                String strEmail = edtEmail.getText().toString().trim();
                String strPassword = edtPassword.getText().toString().trim();

                presenter.register(strFullName, strEmail, strPassword, mPhoneNumber);
            }
        }
    }

    @Override
    public void updateStatusRegister() {
        GlobalFuntion.startActivity(this, MainActivity.class);
        finishAffinity();
    }
}
