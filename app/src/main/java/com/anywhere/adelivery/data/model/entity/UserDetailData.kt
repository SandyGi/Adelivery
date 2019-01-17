package com.anywhere.adelivery.data.model.entity

import com.google.gson.annotations.SerializedName

data class UserDetailData(
    @SerializedName("full_name") val full_name: String,
    @SerializedName("user_id") val user_id: String,
    @SerializedName("contact_number") val contact_number: String,
    @SerializedName("email") val email: String,
    @SerializedName("city") val city: String
)