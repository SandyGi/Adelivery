package com.anywhere.adelivery.data.api

import com.anywhere.adelivery.data.model.entity.ApiListResponse
import com.anywhere.adelivery.data.model.entity.OfferData
import io.reactivex.Observable
import retrofit2.http.GET

interface ApiInterface{
    @GET("getOffers.php")
    fun fetchOffers(): Observable<ApiListResponse<OfferData>>

//    @GET("colors")
//    fun fetchColors(): Observable<ApiListResponse<Color>>
}