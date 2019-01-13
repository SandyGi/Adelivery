package com.anywhere.adelivery.data.api

import com.anywhere.adelivery.data.model.entity.ApiListResponse
import com.anywhere.adelivery.data.model.entity.OfferData
import com.anywhere.adelivery.data.model.entity.ExistingUserData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    @GET("getOffers.php")
    fun fetchOffers(): Observable<ApiListResponse<OfferData>>

    @POST("userInfo.php")
    fun getExistingUser(): Observable<ApiListResponse<ExistingUserData>>
}