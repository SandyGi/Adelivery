package com.anywhere.adelivery.data.request

data class ProductDelivery(
    var alternateNumber: String,
    var detailOfDelivery: String,
    var city: String,
    var deliveryDate: String,
    var modeOfPayment: String,
    var paymentAmount: String,
    var pickupLocation: PickUpLocation,
    var dropLocation: DropLocation
)