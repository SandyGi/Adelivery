package com.anywhere.adelivery.viewmodel

import android.annotation.SuppressLint
import com.anywhere.adelivery.data.model.entity.ApiResponse
import com.anywhere.adelivery.data.model.entity.Response
import com.anywhere.adelivery.data.model.entity.Status
import com.anywhere.adelivery.data.model.entity.UserDetailData
import com.anywhere.adelivery.data.repository.UserDetailRepository
import com.anywhere.adelivery.data.request.CreatedUserDetailRequest
import com.anywhere.adelivery.di.base.BaseViewModel
import com.anywhere.adelivery.utils.scheduler.BaseScheduler
import javax.inject.Inject

class UserDetailViewModel @Inject constructor(
    private val scheduler: BaseScheduler,
    private val userDetailRepository: UserDetailRepository
) : BaseViewModel<ApiResponse>() {

    lateinit var createdUserDetailRequest: CreatedUserDetailRequest
    @SuppressLint("CheckResult")
    override fun loadData() {
        userDetailRepository.createdUserDetail(createdUserDetailRequest)
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

     fun createdUserDetail(createdUserDetailRequest: CreatedUserDetailRequest) {
        this.createdUserDetailRequest = createdUserDetailRequest
         loadData()
    }
}