package com.anywhere.adelivery.viewmodel

import android.annotation.SuppressLint
import com.anywhere.adelivery.data.model.entity.*
import com.anywhere.adelivery.data.repository.ConfirmationRepository
import com.anywhere.adelivery.data.repository.OrderDetailRepository
import com.anywhere.adelivery.data.request.ConfirmationRequest
import com.anywhere.adelivery.di.base.BaseViewModel
import com.anywhere.adelivery.utils.scheduler.BaseScheduler
import javax.inject.Inject

class OrderDetailViewModel @Inject constructor(
    private val scheduler: BaseScheduler,
    private val orderDetailRepository: OrderDetailRepository
) : BaseViewModel<ApiListResponse<List<OrderDetail>>>() {

    lateinit var orderId: String
    @SuppressLint("CheckResult")
    override fun loadData() {
        orderDetailRepository.getOrderDetail(orderId)
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

    fun getOrderDetail(orderId : String) {
        this.orderId = orderId
        loadData()
    }
}