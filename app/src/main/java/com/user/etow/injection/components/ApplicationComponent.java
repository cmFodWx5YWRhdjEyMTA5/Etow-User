package com.user.etow.injection.components;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.user.etow.data.DataManager;
import com.user.etow.injection.ApplicationContext;
import com.user.etow.injection.modules.ApplicationModule;
import com.user.etow.ui.base.BaseActivity;
import com.user.etow.ui.base.BaseFragment;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    Application application();

    Retrofit retrofit();

    DataManager dataManager();

    Toast toast();

    void inject(BaseActivity baseActivity);

    void inject(BaseFragment fragment);
}
