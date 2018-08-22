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
import android.widget.ImageView;
import android.widget.TextView;

import com.user.etow.R;
import com.user.etow.constant.Constant;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.data.prefs.DataStoreManager;
import com.user.etow.models.Image;
import com.user.etow.ui.auth.verify_mobile_number.VerifyMobileNumberActivity;
import com.user.etow.ui.base.BaseMVPFragmentWithDialog;
import com.user.etow.ui.main.MainActivity;
import com.user.etow.utils.GlideUtils;
import com.user.etow.utils.StringUtil;
import com.user.etow.utils.Utils;

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

    @BindView(R.id.tv_email)
    TextView tvEmail;

    @BindView(R.id.edt_password)
    EditText edtPassword;

    @BindView(R.id.img_avatar)
    ImageView imgAvatar;

    private Image mImage;

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
        ((MainActivity) getActivity()).showAndHiddenItemToolbar(getString(R.string.my_account));

        initData();
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

    private void initData() {
        if (!StringUtil.isEmpty(DataStoreManager.getUser().getAvatar())) {
            GlideUtils.loadUrlAvatar(DataStoreManager.getUser().getAvatar(), imgAvatar);
        } else {
            imgAvatar.setImageResource(R.drawable.ic_avatar_default);
        }
        edtName.setText(DataStoreManager.getUser().getFull_name());
        tvMobileNumber.setText(DataStoreManager.getUser().getPhone());
        tvEmail.setText(DataStoreManager.getUser().getEmail());
    }

    @OnClick(R.id.tv_edit)
    public void onClickEdit() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.IS_UPDATE, true);
        GlobalFuntion.startActivity(getActivity(), VerifyMobileNumberActivity.class, bundle);
    }

    @OnClick(R.id.tv_update)
    public void onClickUpdate() {
        String strFullName = edtName.getText().toString().trim();
        String strPhone = tvMobileNumber.getText().toString().trim();
        String strEmail = tvEmail.getText().toString().trim();
        String strPassword = edtPassword.getText().toString().trim();
        if (StringUtil.isEmpty(strFullName)) {
            showAlert(getString(R.string.please_enter_full_name));
        } else if (StringUtil.isEmpty(strPassword)) {
            showAlert(getString(R.string.please_enter_password));
        } else {
            String strAvatar = "";
            if (mImage != null && mImage.getBitmap() != null) {
                strAvatar =  Utils.convertBitmapToBase64(mImage.getBitmap());
            }
            presenter.updateProfile(strFullName, strPhone, strEmail, strPassword, strAvatar);
        }
    }

    @OnClick(R.id.layout_avatar)
    public void onClickLayoutAvatar() {
        GlobalFuntion.pickImage(getActivity(), GlobalFuntion.PICK_IMAGE_AVATAR);
    }

    @Override
    public void updateStatusUpdateProfile() {
        initData();
    }

    @Override
    public void updatePhoneNumber(String phone) {
        tvMobileNumber.setText(phone);
    }

    @Override
    public void updateAvatar(Image image) {
        mImage = image;
        if (mImage != null && mImage.getBitmap() != null) {
            imgAvatar.setImageBitmap(mImage.getBitmap());
        }
    }
}
