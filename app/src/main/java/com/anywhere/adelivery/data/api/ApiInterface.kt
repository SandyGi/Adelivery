package com.anywhere.adelivery.data.api

import com.anywhere.adelivery.data.model.entity.*
import com.anywhere.adelivery.data.request.CreatedUserDetailRequest
import com.anywhere.adelivery.data.request.ExistingUserRequest
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    @GET("getOffers.php")
    fun fetchOffers(): Observable<ApiListResponse<OfferData>>

    @POST("userInfo.php")
    fun getExistingUser(@Body existingUserRequest: ExistingUserRequest): Observable<ApiListResponse<ExistingUserData>>

    @POST("user/createUserDetails.php")
    fun createdUserDetail(@Body createdUserDetailRequest: CreatedUserDetailRequest): Observable<ApiResponse>
}