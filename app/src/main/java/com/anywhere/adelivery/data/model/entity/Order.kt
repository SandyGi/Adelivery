package com.anywhere.adelivery.data.model.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Order(
    @SerializedName("orderId") val orderId: String,
    @SerializedName("status")val status: String,
    @SerializedName("uniqueCode") val uniqueCode: String,
    @SerializedName("createdBy")val createdBy: String,
    @SerializedName("createdDate")val createdDate: String,
    @SerializedName("createdTime")val createdTime: String
):Serializable