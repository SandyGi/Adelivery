package com.anywhere.adelivery.data.repository

import com.anywhere.adelivery.data.api.ApiInterface
import com.anywhere.adelivery.data.model.entity.ApiListResponse
import com.anywhere.adelivery.data.model.entity.ExistingUserData
import com.anywhere.adelivery.data.request.ExistingUserRequest
import io.reactivex.Observable
import javax.inject.Inject

open class ExistingUserRepository @Inject constructor(private val apiInterface: ApiInterface) {
    fun getExistingUserData(existingUserRequest : ExistingUserRequest): Observable<ApiListResponse<ExistingUserData>> {
        return apiInterface.getExistingUser(existingUserRequest)
    }
}