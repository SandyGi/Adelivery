package com.anywhere.adelivery

import com.anywhere.adelivery.di.component.DaggerAppComponent
import com.anywhere.adelivery.utils.PreferencesManager
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication


open class AdeliveryApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        return DaggerAppComponent.builder().create(this)
    }

    override fun onCreate() {
        AdeliveryApplication.prefHelper = PreferencesManager(applicationContext)
        super.onCreate()
    }

    companion object {
        var prefHelper: PreferencesManager? = null
    }
}