package com.anywhere.adelivery.viewmodel

import android.annotation.SuppressLint
import com.anywhere.adelivery.data.model.entity.ExistingUserData
import com.anywhere.adelivery.data.model.entity.Response
import com.anywhere.adelivery.data.model.entity.Status
import com.anywhere.adelivery.data.repository.ExistingUserRepository
import com.anywhere.adelivery.di.base.BaseViewModel
import com.anywhere.adelivery.utils.scheduler.BaseScheduler
import javax.inject.Inject

class ExistingUserViewModel @Inject
constructor(
    private val scheduler: BaseScheduler,
    private val existingUserRepository: ExistingUserRepository
) :
    BaseViewModel<ExistingUserData>() {

    @SuppressLint("CheckResult")
    override fun loadData() {
        existingUserRepository.getExistingUserData()
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
            .doOnSubscribe { loadingStatus.value = true }
            .doAfterTerminate { loadingStatus.value = false }
            .subscribe({ result ->
                response.setValue(Response(Status.SUCCESS, result.data, null))
            }, { throwable ->
                response.setValue(Response(Status.ERROR, null, throwable))
            })
    }
}