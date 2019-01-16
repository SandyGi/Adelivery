package com.anywhere.adelivery.data.repository

import com.anywhere.adelivery.data.api.ApiInterface
import com.anywhere.adelivery.data.model.entity.*
import com.anywhere.adelivery.data.request.CreatedUserDetailRequest
import com.anywhere.adelivery.data.request.DeliveryRequest
import io.reactivex.Observable
import javax.inject.Inject

open class CreateDeliveryRepository  @Inject constructor(private val apiInterface: ApiInterface) {
    fun createdDeliveryDetail(deliveryRequest: DeliveryRequest): Observable<ApiListResponse<DeliveryData>> {
        return apiInterface.createdDeliveryDetail(deliveryRequest)
    }
}