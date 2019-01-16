package com.anywhere.adelivery.viewmodel

import android.annotation.SuppressLint
import com.anywhere.adelivery.data.model.entity.ApiListResponse
import com.anywhere.adelivery.data.model.entity.DeliveryData
import com.anywhere.adelivery.data.model.entity.Response
import com.anywhere.adelivery.data.model.entity.Status
import com.anywhere.adelivery.data.repository.CreateDeliveryRepository
import com.anywhere.adelivery.data.request.DeliveryRequest
import com.anywhere.adelivery.di.base.BaseViewModel
import com.anywhere.adelivery.utils.scheduler.BaseScheduler
import javax.inject.Inject

class ProductDetailViewModel @Inject constructor(
    private val scheduler: BaseScheduler,
    private val createDeliveryRepository: CreateDeliveryRepository
) : BaseViewModel<ApiListResponse<DeliveryData>>() {

    lateinit var deliveryRequest: DeliveryRequest
    @SuppressLint("CheckResult")
    override fun loadData() {
        createDeliveryRepository.createdDeliveryDetail(deliveryRequest)
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

    fun createdDeliveryDetail(deliveryRequest: DeliveryRequest) {
        this.deliveryRequest = deliveryRequest
        loadData()
    }
}