package com.anywhere.adelivery.data.api

import com.anywhere.adelivery.data.model.entity.*
import com.anywhere.adelivery.data.request.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


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
    fun getOrderDetail(@Query("userId") userId: String, @Query("orderId") orderId: String): Observable<ApiListResponse<List<OrderDetail>>>

    @GET("user/getUserInfo.php")
    fun getUserDetail(@Query("userId") userId: String): Observable<ApiListResponse<UserDetailData>>

    @POST("order/canceOrder.php")
    fun cancelOrder(@Body cancelOrderRequest: CancelOrderRequest): Observable<ApiResponse>

    @POST("order/updateToDeliver.php")
    fun orderDelivered(@Body cancelOrderRequest: CancelOrderRequest): Observable<ApiResponse>

    @POST("order/upload_image.php")
    fun uploadProductImage()

    @Multipart
    @POST("order/upload_image.php")
    fun uploadAttachment(@Part("order_id") orderId: RequestBody, @Part filePart: MultipartBody.Part): Observable<UploadImageResponse>

}
