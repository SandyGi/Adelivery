package com.anywhere.adelivery.data.model.entity

import com.google.gson.annotations.SerializedName

data class ApiListResponse<out T>(
    @SerializedName("errorCode") val errorCode: Int,
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("responseMessage") val responseMessage: String,
    @SerializedName("data") val data: T
)