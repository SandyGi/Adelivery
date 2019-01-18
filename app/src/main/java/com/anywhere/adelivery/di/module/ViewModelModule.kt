package com.anywhere.adelivery.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.anywhere.adelivery.di.keys.ViewModelKey
import com.anywhere.adelivery.ui.ViewModelFactory
import com.anywhere.adelivery.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(OfferViewModel::class)
    fun bindOfferViewModel(offerViewModel: OfferViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExistingUserViewModel::class)
    fun bindExitingViewModel(existingUserViewModel: ExistingUserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateUserViewModel::class)
    fun bindCreateUserViewModel(createUserViewModel: CreateUserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductDetailViewModel::class)
    fun bindDeliveryViewModel(productDetailViewModel: ProductDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ConfirmationViewModel::class)
    fun bindConfirmationViewModel(confirmationViewModel: ConfirmationViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(UserDetailViewModel::class)
    fun bindUserDetailViewModel(userDetailViewModel: UserDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OrderListViewModel::class)
    fun bindOrderListViewModel(orderListViewModel: OrderListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OrderDetailViewModel::class)
    fun bindOrderDetailViewModel(orderDetailViewModel: OrderDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CancelOrderViewModel::class)
    fun bindCancelOrderViewModel(cancelOrderViewModel: CancelOrderViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OrderDeliveredViewModel::class)
    fun bindOrderDeliveredViewModel(orderDeliveredViewModel: OrderDeliveredViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}