package com.anywhere.adelivery.di.module

import com.anywhere.adelivery.ui.activity.HomeActivity
import com.anywhere.adelivery.ui.activity.OfferActivity
import com.anywhere.adelivery.ui.activity.RegistrationActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
interface ActivityBuildersModule {

    @ContributesAndroidInjector(modules = [RegistrationFragmentBuildersModule::class])
    fun contributeRegistrationActivity(): RegistrationActivity

    @ContributesAndroidInjector
    fun contributeOfferActivity(): OfferActivity

    @ContributesAndroidInjector(modules = [HomeFragmentBuildersModule::class])
    fun contributeHomeActivity(): HomeActivity
}