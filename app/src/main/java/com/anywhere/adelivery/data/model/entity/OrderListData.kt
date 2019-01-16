package com.anywhere.adelivery.data.model.entity

import com.google.gson.annotations.SerializedName

data class OrderListData(@SerializedName("orders") val orderList: List<Order>)