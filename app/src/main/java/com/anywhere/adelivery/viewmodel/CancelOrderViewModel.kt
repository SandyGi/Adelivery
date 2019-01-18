package com.anywhere.adelivery.viewmodel

import android.annotation.SuppressLint
import com.anywhere.adelivery.data.model.entity.ApiResponse
import com.anywhere.adelivery.data.model.entity.Response
import com.anywhere.adelivery.data.model.entity.Status
import com.anywhere.adelivery.data.repository.CancelOrderRepository
import com.anywhere.adelivery.data.repository.CreateUserRepository
import com.anywhere.adelivery.data.request.CancelOrderRequest
import com.anywhere.adelivery.data.request.CreatedUserDetailRequest
import com.anywhere.adelivery.di.base.BaseViewModel
import com.anywhere.adelivery.utils.scheduler.BaseScheduler
import javax.inject.Inject

class CancelOrderViewModel @Inject constructor(
    private val scheduler: BaseScheduler,
    private val cancelOrderRepository: CancelOrderRepository
) : BaseViewModel<ApiResponse>() {

    lateinit var cancelOrderRequest: CancelOrderRequest
    @SuppressLint("CheckResult")
    override fun loadData() {
        cancelOrderRepository.cancelOrder(cancelOrderRequest)
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

    fun createdUserDetail(cancelOrderRequest: CancelOrderRequest) {
        this.cancelOrderRequest = cancelOrderRequest
        loadData()
    }
}