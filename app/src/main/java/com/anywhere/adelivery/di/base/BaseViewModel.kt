package com.anywhere.adelivery.di.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.anywhere.adelivery.data.model.entity.Response

abstract class BaseViewModel<T> : ViewModel() {

    val response: MutableLiveData<Response<T>> = MutableLiveData()

    val loadingStatus: MutableLiveData<Boolean> = MutableLiveData()

    abstract fun loadData()

}