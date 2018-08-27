package com.user.etow.ui.auth.enter_otp;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.user.etow.R;
import com.user.etow.constant.Constant;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.messages.EditPhoneNumberSuccess;
import com.user.etow.ui.auth.sign_up.SignUpActivity;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

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
    private String mVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this);
        presenter.initialView(this);

        getDataIntent();

        tvMessage.setText(getString(R.string.have_sent_sms) + " " + mPhoneNumber);
        setListener();
        createCallback();
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPhoneNumber = bundle.getString(Constant.PHONE_NUMBER);
            mVerificationId = bundle.getString(Constant.VERIFICATION_ID);
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
            // presenter.verifyOTP(edtOtp.getText().toString().trim(), mPhoneNumber);
            showProgressDialog(true);
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, edtOtp.getText().toString().trim());
            signInWithPhoneAuthCredential(credential);
        }
    }

    @OnClick(R.id.tv_not_get_code)
    public void onClickDidNotGetTheCode() {
        // presenter.getOTP(mPhoneNumber);
        onClickSendAuthorizationCode(mPhoneNumber);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        showProgressDialog(false);
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            Log.e("------------", "-----Phone: " + user.getPhoneNumber());
                            getStatusVerifyOTP(user.getPhoneNumber());
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                showAlert(getString(R.string.msg_otp_incorrect));
                            } else {
                                showAlert(getString(R.string.otp_register_code_wrong));
                            }
                        }
                    }
                });
    }

    public void getStatusVerifyOTP(String phoneNumber) {
        if (mIsUpdate) {
            EventBus.getDefault().post(new EditPhoneNumberSuccess(phoneNumber));
            finish();
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.PHONE_NUMBER, phoneNumber);
            GlobalFuntion.startActivity(this, SignUpActivity.class, bundle);
        }
    }

    public void onClickSendAuthorizationCode(String phoneNumber) {
        mPhoneNumber = phoneNumber;
        showProgressDialog(true);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                callbacks);
    }

    private void createCallback() {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                showProgressDialog(false);
                // signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                showProgressDialog(false);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    showAlert(getString(R.string.otp_register_request_phone_error));
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    showAlert(getString(R.string.otp_register_request_send_many_time));
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                // Save verification ID and resending token so we can use them later
                showProgressDialog(false);
                mVerificationId = verificationId;
                showAlert(getString(R.string.otp_register_request_sent_phone));
            }
        };
    }
}
