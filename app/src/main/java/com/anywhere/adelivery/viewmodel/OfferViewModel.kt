package com.anywhere.adelivery.viewmodel

import android.annotation.SuppressLint
import com.anywhere.adelivery.data.model.entity.OfferData
import com.anywhere.adelivery.data.model.entity.Offers
import com.anywhere.adelivery.data.model.entity.Response
import com.anywhere.adelivery.data.model.entity.Status
import com.anywhere.adelivery.data.repository.OfferRepository
import com.anywhere.adelivery.di.base.BaseViewModel
import com.anywhere.adelivery.utils.scheduler.BaseScheduler
import javax.inject.Inject

class OfferViewModel
@Inject constructor(
    private val scheduler: BaseScheduler,
    private val offerRepository: OfferRepository
) : BaseViewModel<List<Offers>>() {

    @SuppressLint("CheckResult")
    override fun loadData() {
        offerRepository.getOffers().subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
            .doOnSubscribe { loadingStatus.value = true }
            .doAfterTerminate { loadingStatus.value = false }
            .subscribe({ result ->
                response.setValue(Response(Status.SUCCESS, result.data.offersList, null))
            }, { throwable ->
                response.setValue(Response(Status.ERROR, null, throwable))
            })
    }
}