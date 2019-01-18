package com.anywhere.adelivery.data.repository

import com.anywhere.adelivery.data.api.ApiInterface
import com.anywhere.adelivery.data.model.entity.ApiResponse
import com.anywhere.adelivery.data.request.CancelOrderRequest
import com.anywhere.adelivery.data.request.CreatedUserDetailRequest
import io.reactivex.Observable
import javax.inject.Inject

open class CancelOrderRepository  @Inject constructor(private val apiInterface: ApiInterface) {
    fun cancelOrder(cancelOrderRequest: CancelOrderRequest): Observable<ApiResponse> {
        return apiInterface.cancelOrder(cancelOrderRequest)
    }
}