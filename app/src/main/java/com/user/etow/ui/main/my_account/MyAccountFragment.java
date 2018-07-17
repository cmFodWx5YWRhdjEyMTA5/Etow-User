package com.user.etow.ui.main.my_account;

/*
 * ******************************************************************************
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/16
 * ******************************************************************************
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.user.etow.R;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.ui.auth.verify_mobile_number.VerifyMobileNumberActivity;
import com.user.etow.ui.base.BaseMVPFragmentWithDialog;
import com.user.etow.ui.main.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyAccountFragment extends BaseMVPFragmentWithDialog implements MyAccountMVPView {

    @Inject
    MyAccountPresenter presenter;

    @BindView(R.id.edt_name)
    EditText edtName;

    @BindView(R.id.tv_mobile_number)
    TextView tvMobileNumber;

    @BindView(R.id.edt_email)
    EditText edtEmail;

    @BindView(R.id.edt_password)
    EditText edtPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this, view);
        presenter.initialView(this);
        ((MainActivity)getActivity()).showAndHiddenItemToolbar(getString(R.string.my_account));

        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroyView();
    }

    @Override
    protected void initToolbar() {}

    @Override
    public void onErrorCallApi(int code) {
        GlobalFuntion.showMessageError(getActivity(), code);
    }

    private void initData() {
        edtName.setText("DangTin");
        tvMobileNumber.setText("+84 985757575");
        edtEmail.setText("dangtin@gmail.com");
        edtPassword.setText("********");
    }

    @OnClick(R.id.tv_edit)
    public void onClickEdit() {
        GlobalFuntion.startActivity(getActivity(), VerifyMobileNumberActivity.class);
    }

    @OnClick(R.id.tv_update)
    public void onClickUpdate() {

    }
}
