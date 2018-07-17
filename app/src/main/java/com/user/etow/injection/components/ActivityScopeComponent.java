package com.user.etow.injection.components;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.user.etow.injection.PerActivity;
import com.user.etow.injection.modules.ActivityModule;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class)
public interface ActivityScopeComponent {

    ActivityComponent activityComponent(ActivityModule module);
}
