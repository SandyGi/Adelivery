package com.anywhere.adelivery.data.api

import com.anywhere.adelivery.data.model.entity.*
import com.anywhere.adelivery.data.request.ConfirmationRequest
import com.anywhere.adelivery.data.request.CreatedUserDetailRequest
import com.anywhere.adelivery.data.request.DeliveryRequest
import com.anywhere.adelivery.data.request.ExistingUserRequest
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
    @GET("getOffers.php")
    fun fetchOffers(): Observable<ApiListResponse<OfferData>>

    @POST("userInfo.php")
    fun getExistingUser(@Body existingUserRequest: ExistingUserRequest): Observable<ApiListResponse<ExistingUserData>>

    @POST("user/createUserDetails.php")
    fun createdUserDetail(@Body createdUserDetailRequest: CreatedUserDetailRequest): Observable<ApiResponse>

    @POST("order/createDelivery.php")
    fun createdDeliveryDetail(@Body createDeliveryRequest: DeliveryRequest): Observable<ApiListResponse<DeliveryData>>

    @POST("order/confirmOrder.php")
    fun doConfirmation(@Body confirmationRequest: ConfirmationRequest): Observable<ApiListResponse<ConfirmationData>>

    @GET("order/getOrderList.php")
    fun getOrderList(@Query("userId") userId: String): Observable<ApiListResponse<OrderListData>>

    @GET("order/getOrderDetails.php")
    fun getOrderDetail(@Query("userId") userId: String, @Query("orderId") orderId: String): Observable<ApiListResponse<OrderDetailData>>


}
