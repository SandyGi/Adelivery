package com.anywhere.adelivery.di.module

import com.anywhere.adelivery.ui.fragment.ConfirmationFragment
import com.anywhere.adelivery.ui.fragment.MyDetailFragment
import com.anywhere.adelivery.ui.fragment.ScheduleDeliveryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentBuildersModule {

    @ContributesAndroidInjector
    fun contributeMyDetailFragment(): MyDetailFragment

    @ContributesAndroidInjector
    fun contributeScheduleDeliveryFragment(): ScheduleDeliveryFragment

    @ContributesAndroidInjector
    fun contributeConfirmationFragment(): ConfirmationFragment

}