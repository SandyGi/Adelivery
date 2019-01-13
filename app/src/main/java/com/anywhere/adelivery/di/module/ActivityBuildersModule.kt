package com.anywhere.adelivery.di.module

import com.anywhere.adelivery.ui.activity.OfferActivity
import com.anywhere.adelivery.ui.activity.RegistrationActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
interface ActivityBuildersModule {

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    fun contributeMainActivity(): RegistrationActivity

    @ContributesAndroidInjector
    fun contributeMainInjector(): OfferActivity
}