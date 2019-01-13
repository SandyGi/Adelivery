package com.anywhere.adelivery.di.module

import com.anywhere.adelivery.ui.activity.OfferActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
interface OfferActivityBuildersModule {

//    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
//    fun contributeMainActivity(): RegistrationActivity

    @ContributesAndroidInjector
    fun contributeMainInjector(): OfferActivity
}