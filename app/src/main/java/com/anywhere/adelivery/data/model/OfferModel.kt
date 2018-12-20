package com.anywhere.adelivery.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

@Entity(tableName = "offerData")
data class OfferModel(
    @Json(name = "offerId")
    @PrimaryKey
    @ColumnInfo(name = "offerId")
    val offerId: Int,

    @Json(name = "offerName")
    @ColumnInfo(name = "offerName")
    val offerName: String?,

    @Json(name = "offerDescription")
    @ColumnInfo(name = "offerDescription")
    val offerDescription: String?,

    @Json(name = "offerImage")
    @ColumnInfo(name = "offerImage")
    val offerImage: String?
) : Serializable