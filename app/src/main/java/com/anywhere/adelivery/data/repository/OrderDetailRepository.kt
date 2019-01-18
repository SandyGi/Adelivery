package com.anywhere.adelivery.data.repository

import com.anywhere.adelivery.AdeliveryApplication
import com.anywhere.adelivery.data.api.ApiInterface
import com.anywhere.adelivery.data.model.entity.*
import com.anywhere.adelivery.data.request.CreatedUserDetailRequest
import io.reactivex.Observable
import javax.inject.Inject

open class OrderDetailRepository  @Inject constructor(private val apiInterface: ApiInterface) {
    fun getOrderDetail(orderId : String): Observable<ApiListResponse<List<OrderDetail>>> {
        return apiInterface.getOrderDetail(AdeliveryApplication.prefHelper!!.userId, orderId)
    }
}