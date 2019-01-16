package com.anywhere.adelivery.di.module

import com.anywhere.adelivery.ui.activity.HomeActivity
import com.anywhere.adelivery.ui.activity.RegistrationActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
interface HomeActivityBuildersModule {

    @ContributesAndroidInjector(modules = [HomeFragmentBuildersModule::class])
    fun contributeMainActivity(): HomeActivity
}