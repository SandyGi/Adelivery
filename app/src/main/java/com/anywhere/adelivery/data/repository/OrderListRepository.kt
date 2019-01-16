package com.anywhere.adelivery.data.repository

import com.anywhere.adelivery.data.api.ApiInterface
import com.anywhere.adelivery.data.model.entity.ApiListResponse
import com.anywhere.adelivery.data.model.entity.ConfirmationData
import com.anywhere.adelivery.data.model.entity.OrderListData
import com.anywhere.adelivery.data.request.ConfirmationRequest
import com.anywhere.adelivery.data.request.OrderListRequest
import io.reactivex.Observable
import javax.inject.Inject

open class OrderListRepository @Inject constructor(private val apiInterface: ApiInterface) {
    fun getOrderList(userId: String): Observable<ApiListResponse<OrderListData>> {
        return apiInterface.getOrderList(userId)
    }
}