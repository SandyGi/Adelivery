package com.anywhere.adelivery.data.model.entity

import com.google.gson.annotations.SerializedName

data class UploadImageResponse(
    @SerializedName("STATUS") val STATUS: Int,
    @SerializedName("MESSAGE") val MESSAGE: String
)