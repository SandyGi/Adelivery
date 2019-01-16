package com.anywhere.adelivery.data.model.entity

import com.google.gson.annotations.SerializedName

data class OTPData(
    @SerializedName("code") val code: String,
    @SerializedName("messageFromserver") val messageFromserver: String,
    @SerializedName("otp") val otp: String
)