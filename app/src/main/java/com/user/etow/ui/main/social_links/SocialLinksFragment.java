package com.user.etow.ui.main.social_links;

/*
 * ******************************************************************************
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/16
 * ******************************************************************************
 */

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.user.etow.R;
import com.user.etow.constant.GlobalFuntion;
import com.user.etow.ui.base.BaseMVPFragmentWithDialog;
import com.user.etow.ui.main.MainActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SocialLinksFragment extends BaseMVPFragmentWithDialog implements SocialLinksMVPView {

    String linkInstagram = "https://www.instagram.com/dangtin_myk";
    String linkFacebook = "https://www.facebook.com/dang.tin.7";
    String linkTwitter = "https://twitter.com/DangTin7";
    String facebookId = "100003676677725"; //https://findmyfbid.com/
    String twitterUserName = "DangTin7";


    @Inject
    SocialLinksPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_social_links, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivityComponent().inject(this);
        viewUnbind = ButterKnife.bind(this, view);
        presenter.initialView(this);
        ((MainActivity) getActivity()).showAndHiddenItemToolbar(getString(R.string.social_links));
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

    @OnClick(R.id.img_instagram)
    public void onClickInstagram() {
        Uri uri = Uri.parse(linkInstagram);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
        likeIng.setPackage("com.instagram.android");
        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(linkInstagram)));
        }
    }

    @OnClick(R.id.img_facebook)
    public void onClickFacebook() {
        Intent intent;
        try {
            getActivity().getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://profile/" + facebookId));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkFacebook)); //catches and opens a url to the desired page
        }
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.img_twitter)
    public void onClickTwitter() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitterUserName)));
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(linkTwitter)));
        }
    }
}
