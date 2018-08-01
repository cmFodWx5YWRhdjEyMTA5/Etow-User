package com.user.etow.ui.main;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.user.etow.R;
import com.user.etow.constant.Constant;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.messages.SelectAvatarSuccess;
import com.user.etow.models.Image;
import com.user.etow.ui.auth.sign_in.SignInActivity;
import com.user.etow.ui.base.BaseMVPDialogActivity;
import com.user.etow.ui.main.get_in_touch.GetInTouchFragment;
import com.user.etow.ui.main.home.HomeFragment;
import com.user.etow.ui.main.my_account.MyAccountFragment;
import com.user.etow.ui.main.my_bookings.MyBookingsFragment;
import com.user.etow.ui.main.social_links.SocialLinksFragment;
import com.user.etow.ui.main.term_and_condition.TermAndConditionFragment;
import com.user.etow.ui.widget.image.ImagePicker;
import com.user.etow.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseMVPDialogActivity implements MainMVPView {

    @Inject
    MainPresenter presenter;

    @BindView(R.id.drawer_layout)
    public DrawerLayout mDrawerLayout;

    @BindView(R.id.img_back)
    ImageView imgBack;

    @BindView(R.id.tv_title_toolbar)
    TextView tvTitleToolbar;

    @BindView(R.id.tv_title_header)
    TextView tvTitleHeader;

    private boolean mTabCompleted = true;
    private boolean mCheckGoToUpcomingTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this);
        presenter.initialView(this);

        // set menu
        imgBack.setImageResource(R.drawable.ic_close_black);
        tvTitleToolbar.setText(getString(R.string.menu));

        getDataIntent();
        setListenerDrawer();
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            mCheckGoToUpcomingTrip = bundle.getBoolean(Constant.GO_TO_UPCOMING_TRIPS);
        if (mCheckGoToUpcomingTrip) {
            mTabCompleted = false;
            replaceFragment(new MyBookingsFragment(), MyBookingsFragment.class.getName());
        } else {
            replaceFragment(new HomeFragment(), HomeFragment.class.getName());
        }
    }

    @Override
    protected boolean bindView() {
        return true;
    }

    @Override
    protected int addContextView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GlobalFuntion.getCurrentLocation(this, mLocationManager);
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

    private void setListenerDrawer() {
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                GlobalFuntion.hideSoftKeyboard(MainActivity.this);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            showDialogLogout();
        }
    }

    private void showDialogLogout() {
        MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                .title(getString(R.string.app_name))
                .content(getString(R.string.msg_exit_app))
                .positiveText(getString(R.string.action_ok))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                })
                .negativeText(getString(R.string.action_cancel))
                .cancelable(false)
                .show();
    }

    public void replaceFragment(Fragment fragment, String tag) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, tag);
        ft.commit();
    }

    public void showAndHiddenItemToolbar(String title) {
        tvTitleHeader.setText(title);
    }

    @OnClick({R.id.img_back, R.id.img_menu, R.id.tv_menu_home, R.id.tv_menu_my_account,
            R.id.tv_menu_my_bookings, R.id.tv_menu_get_in_touch, R.id.tv_menu_term_and_condition,
            R.id.tv_social_links, R.id.tv_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.img_menu:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.tv_menu_home:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                replaceFragment(new HomeFragment(), HomeFragment.class.getName());
                break;

            case R.id.tv_menu_my_account:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                replaceFragment(new MyAccountFragment(), MyAccountFragment.class.getName());
                break;

            case R.id.tv_menu_my_bookings:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                replaceFragment(new MyBookingsFragment(), MyBookingsFragment.class.getName());
                break;

            case R.id.tv_menu_get_in_touch:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                replaceFragment(new GetInTouchFragment(), GetInTouchFragment.class.getName());
                break;

            case R.id.tv_menu_term_and_condition:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                replaceFragment(new TermAndConditionFragment(), TermAndConditionFragment.class.getName());
                break;

            case R.id.tv_social_links:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                replaceFragment(new SocialLinksFragment(), SocialLinksFragment.class.getName());
                break;

            case R.id.tv_logout:
                presenter.logout();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GlobalFuntion.PICK_IMAGE_AVATAR) {
            Image image = ImagePicker.getImageFromResult(this, resultCode, data);
            EventBus.getDefault().post(new SelectAvatarSuccess(image));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Utils.callPhoneNumber(this, getString(R.string.phone_number_call_us));
                } else {
                    Log.e("Permission", "Permission not Granted");
                }
                break;
        }
    }

    @Override
    public void logout() {
        GlobalFuntion.startActivity(this, SignInActivity.class);
        finishAffinity();
    }

    public boolean isTabCompleted() {
        return mTabCompleted;
    }
}
