package com.anywhere.adelivery.data.model.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class OrderDetail(
    @SerializedName("orderId") val orderId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("city") val city: String,
    @SerializedName("orderStatus") val orderStatus: String,
    @SerializedName("uniqueCode") val uniqueCode: String,
    @SerializedName("payment_mode") val payment_mode: String,
    @SerializedName("Payment_amt")val Payment_amt: String,
    @SerializedName("alt_contact_number") val alt_contact_number: String,
    @SerializedName("delivery_detail")val delivery_detail: String,
    @SerializedName("delivery_exp_date")val delivery_exp_date: String,
    @SerializedName("pickupLocation")val pickupLocation: String,
    @SerializedName("dropLocation")val dropLocation: String,
    @SerializedName("executivename")val executivename: String,
    @SerializedName("executivenumber")val executivenumber: String
):Serializable