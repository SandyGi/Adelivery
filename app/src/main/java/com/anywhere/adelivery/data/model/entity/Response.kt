package com.anywhere.adelivery.data.model.entity

data class Response<out T>(
    val status: Int,
    val data: T?,
    val error: Throwable?
)