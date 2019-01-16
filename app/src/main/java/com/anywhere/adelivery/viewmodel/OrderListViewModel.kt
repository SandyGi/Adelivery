package com.anywhere.adelivery.viewmodel

import android.annotation.SuppressLint
import com.anywhere.adelivery.data.model.entity.ApiListResponse
import com.anywhere.adelivery.data.model.entity.OrderListData
import com.anywhere.adelivery.data.model.entity.Response
import com.anywhere.adelivery.data.model.entity.Status
import com.anywhere.adelivery.data.repository.OrderListRepository
import com.anywhere.adelivery.data.request.OrderListRequest
import com.anywhere.adelivery.di.base.BaseViewModel
import com.anywhere.adelivery.utils.scheduler.BaseScheduler
import javax.inject.Inject

class OrderListViewModel @Inject constructor(
    private val scheduler: BaseScheduler,
    private val orderListRepository: OrderListRepository
) : BaseViewModel<ApiListResponse<OrderListData>>() {

    lateinit var userId: String
    @SuppressLint("CheckResult")
    override fun loadData() {
        orderListRepository.getOrderList(userId)
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

    fun getOrderList(userId: String) {
        this.userId = userId
        loadData()
    }
}