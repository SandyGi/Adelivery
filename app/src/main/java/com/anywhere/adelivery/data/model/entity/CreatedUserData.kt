package com.anywhere.adelivery.data.model.entity

import com.google.gson.annotations.SerializedName

data class CreatedUserData(
    @SerializedName("user_exist") val userExist: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("isUserDetailAvailable") val isUserDetailAvailable: String
)