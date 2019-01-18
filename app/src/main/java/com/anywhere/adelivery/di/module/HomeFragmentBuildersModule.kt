package com.anywhere.adelivery.di.module

import com.anywhere.adelivery.ui.fragment.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface HomeFragmentBuildersModule {

    @ContributesAndroidInjector
    fun contributeMyOrderFragment(): MyOrderFragment

    @ContributesAndroidInjector
    fun contributeOrderDetailFragment(): OrderDetailFragment

    @ContributesAndroidInjector
    fun contributeProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    fun contributeTermAndConditionFragment(): TermAndConditionFragment

    @ContributesAndroidInjector
    fun contributeContactUsFragment(): ContactUsFragment

    @ContributesAndroidInjector
    fun contributeScheduleDeliveryFragment(): ScheduleDeliveryFragment

    @ContributesAndroidInjector
    fun contributeConfirmationFragment(): ConfirmationFragment

    @ContributesAndroidInjector
    fun contributeHomeFragment(): HomeFragment
}