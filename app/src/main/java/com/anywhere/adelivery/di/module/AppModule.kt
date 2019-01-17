package com.anywhere.adelivery.di.module

import com.anywhere.adelivery.data.api.ApiInterface
import com.anywhere.adelivery.data.repository.ExistingUserRepository
import com.anywhere.adelivery.data.repository.CreateUserRepository
import com.anywhere.adelivery.data.repository.OfferRepository
import com.anywhere.adelivery.utils.scheduler.BaseScheduler
import com.anywhere.adelivery.utils.scheduler.SchedulerProvider
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [(ViewModelModule::class)])
class AppModule {
    @Provides
    @Singleton
    fun getApiClient(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun getOfferList(apiInterface: ApiInterface): OfferRepository {
        return OfferRepository(apiInterface)
    }

    @Provides
    @Singleton
    fun getExistingUser(apiInterface: ApiInterface): ExistingUserRepository {
        return ExistingUserRepository(apiInterface)
    }

    @Provides
    @Singleton
    fun createdUserDetail(apiInterface: ApiInterface): CreateUserRepository{
        return CreateUserRepository(apiInterface)
    }

    @Provides
    @Singleton
    fun provideScheduler(): BaseScheduler {
        return SchedulerProvider()
    }
}