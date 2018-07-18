package com.user.etow.ui.rate_trip;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.user.etow.R;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.ui.base.BaseMVPDialogActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RateTripActivity extends BaseMVPDialogActivity implements RateTripMVPView {

    @Inject
    RateTripPresenter presenter;

    @BindView(R.id.layout_thanks_service)
    LinearLayout layoutThanksService;

    @BindView(R.id.layout_rate_service)
    LinearLayout layoutRateService;

    @BindView(R.id.ratingbar)
    RatingBar ratingbar;

    @BindView(R.id.tv_rate_your_trip)
    TextView tvRateYourTrip;

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
        return R.layout.activity_rate_trip;
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

    @OnClick(R.id.tv_ok)
    public void onClickOk() {
        layoutThanksService.setVisibility(View.GONE);
        layoutRateService.setVisibility(View.VISIBLE);
    }

    private void setListener() {
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (ratingBar.getRating() > 0) {
                    tvRateYourTrip.setBackgroundResource(R.drawable.bg_black_corner);
                    tvRateYourTrip.setTextColor(getResources().getColor(R.color.white));
                } else {
                    tvRateYourTrip.setBackgroundResource(R.drawable.bg_grey_corner);
                    tvRateYourTrip.setTextColor(getResources().getColor(R.color.textColorAccent));
                }
            }
        });
    }
}
