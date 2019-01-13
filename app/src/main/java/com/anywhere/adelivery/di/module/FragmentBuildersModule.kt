package com.anywhere.adelivery.di.module

import com.anywhere.adelivery.ui.fragment.ConfirmationFragment
import com.anywhere.adelivery.ui.fragment.MyDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentBuildersModule {

    @ContributesAndroidInjector
    fun contributeListUsersFragment(): MyDetailFragment

    @ContributesAndroidInjector
    fun contributeListColorsFragment(): ConfirmationFragment

}