package com.user.etow;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.app.Application;
import android.content.Context;

import com.user.etow.data.prefs.DataStoreManager;
import com.user.etow.injection.components.ApplicationComponent;
import com.user.etow.injection.components.DaggerApplicationComponent;
import com.user.etow.injection.modules.ApplicationModule;

public class ETowApplication extends Application {

    private final Object lock = new Object();
    private ApplicationComponent mApplicationComponent;

    public static ETowApplication get(Context context) {
        return (ETowApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DataStoreManager.init(getApplicationContext());
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            synchronized (lock) {
                if (mApplicationComponent == null) {
                    mApplicationComponent = DaggerApplicationComponent.builder()
                            .applicationModule(new ApplicationModule(this))
                            .build();
                }
            }
        }
        return mApplicationComponent;
    }
}
