package com.anywhere.adelivery.utils.scheduler

import io.reactivex.Scheduler
import io.reactivex.annotations.NonNull

interface BaseScheduler {

    @NonNull
    fun io(): Scheduler

    @NonNull
    fun ui(): Scheduler

}