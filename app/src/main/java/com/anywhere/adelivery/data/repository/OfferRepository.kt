package com.anywhere.adelivery.data.repository

import com.anywhere.adelivery.data.api.ApiInterface
import com.anywhere.adelivery.data.model.entity.ApiListResponse
import com.anywhere.adelivery.data.model.entity.OfferData
import io.reactivex.Observable
import javax.inject.Inject

open class OfferRepository @Inject constructor(private val apiInterface: ApiInterface) {
    fun getOffers(): Observable<ApiListResponse<OfferData>> {
        return apiInterface.fetchOffers()
    }
}