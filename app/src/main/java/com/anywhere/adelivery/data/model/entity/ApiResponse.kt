package com.anywhere.adelivery.data.model.entity

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("errorCode") val errorCode: Int,
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("responseMessage") val responseMessage: String,
    @SerializedName("data") val data: String
)