package com.anywhere.adelivery.utils

import com.anywhere.adelivery.data.model.OfferModel

class DummyData {

    companion object {

        public fun getOfferList(): ArrayList<OfferModel> {
            var offerList = ArrayList<OfferModel>()
            var offerModel1 = OfferModel(
                11,
                "100 MB Combo 3G / 4G Data Pack",
                "3G/4G Data: 100 MB, extra @Rs.0.04/10KB. Local & STD Calls: Unlimited. Roaming Outgoing: Unlimited. All SMS: 100 SMS. Not for Commercial Activity",
                "http://placehold.it/480x320&text=image1"
            )
            var offerModel2 = OfferModel(
                12,
                "ISD Pack 22",
                "Gulf @ Rs.0.1/sec. US & Canada @ Rs.0.01/sec. Nepal @ Rs.0.2/sec. UK @ Rs.0.01/sec",
                "http://placehold.it/480x320&text=image2"
            )
            var offerModel3 = OfferModel(
                13,
                "1 GB Combo 3G / 4G Data Pack",
                "3G/4G Data: 1 GB, extra @Rs.0.04/10KB. Local & STD Calls: Unlimited. Roaming Outgoing: Unlimited. All SMS (100 SMS/Day): 2800 SMS. Not for Commercial Activity",
                "http://placehold.it/480x680&text=image3"
            )
            var offerModel4 = OfferModel(
                14,
                "Rs. 280 Full Talktime Pack",
                "Talktime: Rs.280.0. Full TalkTime / Extra TalkTime",
                "http://placehold.it/480x320&text=image4"
            )
            var offerModel5 = OfferModel(
                15,
                "Rs. 90 Full Talktime Pack",
                "Local Airtel to Airtel: 2 SMS, Valid for 1 day. Promotional/Bonus Talktime: Rs.52.0, Valid for 5 days",
                "http://placehold.it/480x320&text=image5"
            )

            offerList.add(offerModel1)
            offerList.add(offerModel2)
            offerList.add(offerModel3)
            offerList.add(offerModel4)
            offerList.add(offerModel5)

            return offerList
        }
    }
}