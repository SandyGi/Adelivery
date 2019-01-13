package com.anywhere.adelivery.data.model.entity

import com.google.gson.annotations.SerializedName

data class OfferData(@SerializedName("offers")val offersList: List<Offers>)