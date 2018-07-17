package com.user.etow.data;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.data.networking.ThinkFitService;
import com.user.etow.models.response.CategoryResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class NetworkManager {

    private final ThinkFitService mThinkFitService;

    @Inject
    public NetworkManager(ThinkFitService thinkFitService) {
        this.mThinkFitService = thinkFitService;
    }

    /*public Observable<FoodResponse> getFood(String token, int page) {
        return mThinkFitService.getFood(token, page);
    }*/

    public Observable<CategoryResponse> getListCategory() {
        return mThinkFitService.getListCategory();
    }
}
