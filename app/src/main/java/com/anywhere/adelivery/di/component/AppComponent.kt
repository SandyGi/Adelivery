package com.anywhere.adelivery.di.component

import com.anywhere.adelivery.AdeliveryApplication
import com.anywhere.adelivery.di.module.ActivityBuildersModule
import com.anywhere.adelivery.di.module.AppModule
import com.anywhere.adelivery.di.module.NetworkModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, ActivityBuildersModule::class, AppModule::class, NetworkModule::class])
interface AppComponent : AndroidInjector<AdeliveryApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<AdeliveryApplication>()
}