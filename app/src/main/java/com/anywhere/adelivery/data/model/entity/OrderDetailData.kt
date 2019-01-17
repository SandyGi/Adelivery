package com.anywhere.adelivery.data.model.entity

import com.google.gson.annotations.SerializedName

data class OrderDetailData(@SerializedName("ordersDetails") val orderDetail: OrderDetail)