package com.anywhere.adelivery.data.repository

import com.anywhere.adelivery.data.api.ApiInterface
import com.anywhere.adelivery.data.model.entity.UploadImageResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

open class UploadImageRepository @Inject constructor(private val apiInterface: ApiInterface) {
    fun uploadAttachment(orderId: RequestBody, filePart: MultipartBody.Part): Observable<UploadImageResponse> {
        return apiInterface.uploadAttachment(orderId, filePart)
    }
}