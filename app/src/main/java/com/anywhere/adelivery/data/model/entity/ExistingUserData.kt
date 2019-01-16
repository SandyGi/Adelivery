package com.anywhere.adelivery.data.model.entity

import com.google.gson.annotations.SerializedName

data class ExistingUserData(
    @SerializedName("data") val createdUserData: CreatedUserData
)