package com.anywhere.adelivery.viewmodel

import android.annotation.SuppressLint
import com.anywhere.adelivery.data.model.entity.ApiListResponse
import com.anywhere.adelivery.data.model.entity.ConfirmationData
import com.anywhere.adelivery.data.model.entity.Response
import com.anywhere.adelivery.data.model.entity.Status
import com.anywhere.adelivery.data.repository.ConfirmationRepository
import com.anywhere.adelivery.data.request.ConfirmationRequest
import com.anywhere.adelivery.di.base.BaseViewModel
import com.anywhere.adelivery.utils.scheduler.BaseScheduler
import javax.inject.Inject

class ConfirmationViewModel @Inject constructor(
    private val scheduler: BaseScheduler,
    private val confirmationRepository: ConfirmationRepository
) : BaseViewModel<ApiListResponse<ConfirmationData>>() {

    lateinit var confirmationRequest: ConfirmationRequest
    @SuppressLint("CheckResult")
    override fun loadData() {
        confirmationRepository.doConfirmation(confirmationRequest)
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
            .doOnSubscribe { loadingStatus.value = true }
            .doAfterTerminate { loadingStatus.value = false }
            .subscribe({ result ->
                response.setValue(Response(Status.SUCCESS, result, null))
            }, { throwable ->
                response.setValue(Response(Status.ERROR, null, throwable))
            })
    }

    fun doConfirmation(confirmationRequest: ConfirmationRequest) {
        this.confirmationRequest = confirmationRequest
        loadData()
    }
}