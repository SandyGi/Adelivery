package com.anywhere.adelivery.data.repository

import com.anywhere.adelivery.data.api.ApiInterface
import com.anywhere.adelivery.data.model.entity.ApiListResponse
import com.anywhere.adelivery.data.model.entity.ApiResponse
import com.anywhere.adelivery.data.model.entity.UserDetailData
import com.anywhere.adelivery.data.request.CreatedUserDetailRequest
import io.reactivex.Observable
import javax.inject.Inject

open class UserDetailRepository  @Inject constructor(private val apiInterface: ApiInterface) {
    fun createdUserDetail(createdUserDetailRequest: CreatedUserDetailRequest): Observable<ApiResponse> {
        return apiInterface.createdUserDetail(createdUserDetailRequest)
    }
}