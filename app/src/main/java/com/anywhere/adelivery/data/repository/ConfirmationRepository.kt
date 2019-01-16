package com.anywhere.adelivery.data.repository

import com.anywhere.adelivery.data.api.ApiInterface
import com.anywhere.adelivery.data.model.entity.ApiListResponse
import com.anywhere.adelivery.data.model.entity.ConfirmationData
import com.anywhere.adelivery.data.request.ConfirmationRequest
import io.reactivex.Observable
import javax.inject.Inject

open class ConfirmationRepository @Inject constructor(private val apiInterface: ApiInterface) {
    fun doConfirmation(confirmationRequest: ConfirmationRequest): Observable<ApiListResponse<ConfirmationData>> {
        return apiInterface.doConfirmation(confirmationRequest)
    }
}