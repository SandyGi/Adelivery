package com.anywhere.adelivery.viewmodel

import android.annotation.SuppressLint
import com.anywhere.adelivery.data.model.entity.Response
import com.anywhere.adelivery.data.model.entity.Status
import com.anywhere.adelivery.data.model.entity.UploadImageResponse
import com.anywhere.adelivery.data.repository.UploadImageRepository
import com.anywhere.adelivery.di.base.BaseViewModel
import com.anywhere.adelivery.utils.scheduler.BaseScheduler
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UploadImageViewModel @Inject constructor(
    private val scheduler: BaseScheduler,
    private val uploadImageRepository: UploadImageRepository
) : BaseViewModel<UploadImageResponse>() {

    lateinit var filePart: MultipartBody.Part
    lateinit var orderId: RequestBody
    @SuppressLint("CheckResult")
    override fun loadData() {
        uploadImageRepository.uploadAttachment(orderId, filePart)
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
            .doOnSubscribe { loadingStatus.value = true }
            .doAfterTerminate { loadingStatus.value = false }
            .subscribe({ result ->
                response.setValue(Response(Status.SUCCESS, result, null))
            }, { throwable ->
                response.setValue(Response(Status.ERROR, null, throwable))
            })
    }

    fun uploadAttachment(orderId: RequestBody, filePart: MultipartBody.Part) {
        this.filePart = filePart
        this.orderId = orderId
        loadData()
    }
}