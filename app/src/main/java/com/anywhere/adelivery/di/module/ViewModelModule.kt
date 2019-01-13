package com.anywhere.adelivery.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.anywhere.adelivery.di.keys.ViewModelKey
import com.anywhere.adelivery.ui.ViewModelFactory
import com.anywhere.adelivery.viewmodel.ExistingUserViewModel
import com.anywhere.adelivery.viewmodel.OfferViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule{
    @Binds
    @IntoMap
    @ViewModelKey(OfferViewModel::class)
    fun bindOfferViewModel(offerViewModel: OfferViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExistingUserViewModel::class)
    fun bindExitingViewModel(existingUserViewModel: ExistingUserViewModel) : ViewModel

    @Binds
    fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}