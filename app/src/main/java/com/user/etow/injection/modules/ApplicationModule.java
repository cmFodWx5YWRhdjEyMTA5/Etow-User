package com.user.etow.injection.modules;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.user.etow.data.networking.ThinkFitService;
import com.user.etow.injection.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ApplicationModule {
    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    Toast provideToast(@ApplicationContext Context context) {
        return Toast.makeText(context, "", Toast.LENGTH_LONG);
    }

    @Provides
    @Singleton
    ThinkFitService networkService(Retrofit retrofit){
        return retrofit.create(ThinkFitService.class);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofitInstance() {
        return ThinkFitService.Creator.newRetrofitInstance();
    }
}
