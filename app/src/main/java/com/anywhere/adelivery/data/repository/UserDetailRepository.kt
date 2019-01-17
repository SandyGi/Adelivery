package com.anywhere.adelivery.data.repository

import com.anywhere.adelivery.data.api.ApiInterface
import com.anywhere.adelivery.data.model.entity.ApiListResponse
import com.anywhere.adelivery.data.model.entity.UserDetailData
import io.reactivex.Observable
import javax.inject.Inject

open class UserDetailRepository @Inject constructor(private val apiInterface: ApiInterface) {
    fun getUserDetail(userId: String): Observable<ApiListResponse<UserDetailData>> {
        return apiInterface.getUserDetail(userId)
    }
}