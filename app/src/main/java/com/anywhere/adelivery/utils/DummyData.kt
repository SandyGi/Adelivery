package com.anywhere.adelivery.utils

import com.anywhere.adelivery.data.model.MyOrderListModel
import com.anywhere.adelivery.data.model.OfferModel

class DummyData {

    companion object {

        public fun getOfferList(): ArrayList<OfferModel> {
            val offerList = ArrayList<OfferModel>()
            val offerModel1 = OfferModel(
                11,
                "100 MB Combo 3G / 4G Data Pack",
                "3G/4G Data: 100 MB, extra @Rs.0.04/10KB. Local & STD Calls: Unlimited. Roaming Outgoing: Unlimited. All SMS: 100 SMS. Not for Commercial Activity",
                "http://placehold.it/480x320&text=image1"
            )
            val offerModel2 = OfferModel(
                12,
                "ISD Pack 22, I think they didn't intend for wrap content to be supported as I don't think they thought it was a normal use case. To support it we have to re measure our selves after our children are measured so we can wrap content. I think they didn't intend for wrap content to be supported as I don't think they thought it was a normal use case. To support it we have to re measure our selves after our children are measured so we can wrap content.",
                "Gulf @ Rs.0.1/sec. US & Canada @ Rs.0.01/sec. Nepal @ Rs.0.2/sec. UK @ Rs.0.01/sec. I think they didn't intend for wrap content to be supported as I don't think they thought it was a normal use case. To support it we have to re measure our selves after our children are measured so we can wrap content.,I think they didn't intend for wrap content to be supported as I don't think they thought it was a normal use case. To support it we have to re measure our selves after our children are measured so we can wrap content. ,I think they didn't intend for wrap content to be supported as I don't think they thought it was a normal use case. To support it we have to re measure our selves after our children are measured so we can wrap content.,I think they didn't intend for wrap content to be supported as I don't think they thought it was a normal use case. To support it we have to re measure our selves after our children are measured so we can wrap content.",
                "http://placehold.it/480x320&text=image2"
            )
            val offerModel3 = OfferModel(
                13,
                "1 GB Combo 3G / 4G Data Pack",
                "3G/4G Data: 1 GB, extra @Rs.0.04/10KB. Local & STD Calls: Unlimited. Roaming Outgoing: Unlimited. All SMS (100 SMS/Day): 2800 SMS. Not for Commercial Activity",
                "http://placehold.it/480x680&text=image3"
            )
            val offerModel4 = OfferModel(
                14,
                "Rs. 280 Full Talktime Pack",
                "Talktime: Rs.280.0. Full TalkTime / Extra TalkTime. I think they didn't intend for wrap content to be supported as I don't think they thought it was a normal use case. To support it we have to re measure our selves after our children are measured so we can wrap content.. I think they didn't intend for wrap content to be supported as I don't think they thought it was a normal use case. To support it we have to re measure our selves after our children are measured so we can wrap content..  I think they didn't intend for wrap content to be supported as I don't think they thought it was a normal use case. To support it we have to re measure our selves after our children are measured so we can wrap content.",
                "http://placehold.it/480x320&text=image4"
            )
            val offerModel5 = OfferModel(
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

        fun dummyOrderList(): ArrayList<MyOrderListModel> {
            val myOrderList = ArrayList<MyOrderListModel>()
            val myOrderListModel1 = MyOrderListModel(1, "7985461356", "Delivered")
            val myOrderListModel2 = MyOrderListModel(2, "7985461356", "Cancel")
            val myOrderListModel3 = MyOrderListModel(3, "7985461356", "Scheduled")
            val myOrderListModel4 = MyOrderListModel(4, "7985461356", "Scheduled")
            val myOrderListModel5 = MyOrderListModel(5, "7985461356", "Delivered")
            val myOrderListModel6 = MyOrderListModel(6, "7985461356", "Cancel")
            val myOrderListModel7 = MyOrderListModel(7, "7985461356", "Scheduled")
            val myOrderListModel8 = MyOrderListModel(8, "7985461356", "Scheduled")
            val myOrderListModel9 = MyOrderListModel(9, "7985461356", "Cancel")
            val myOrderListModel10 = MyOrderListModel(10, "7985461356", "Delivered")
            val myOrderListModel11 = MyOrderListModel(11, "7985461356", "Scheduled")
            val myOrderListModel12 = MyOrderListModel(12, "7985461356", "Cancel")
            val myOrderListModel13 = MyOrderListModel(13, "7985461356", "Delivered")
            val myOrderListModel14 = MyOrderListModel(14, "7985461356", "Scheduled")
            val myOrderListModel15 = MyOrderListModel(15, "7985461356", "Scheduled")
            val myOrderListModel16 = MyOrderListModel(16, "7985461356", "Cancel")
            val myOrderListModel17 = MyOrderListModel(17, "7985461356", "Delivered")
            val myOrderListModel18 = MyOrderListModel(18, "7985461356", "Scheduled")
            val myOrderListModel19 = MyOrderListModel(19, "7985461356", "Cancel")
            val myOrderListModel20 = MyOrderListModel(20, "7985461356", "Delivered")
            val myOrderListModel21 = MyOrderListModel(21, "7985461356", "Scheduled")
            val myOrderListModel22 = MyOrderListModel(22, "7985461356", "Delivered")
            val myOrderListModel23 = MyOrderListModel(23, "7985461356", "Scheduled")
            val myOrderListModel24 = MyOrderListModel(24, "7985461356", "Cancel")

            myOrderList.add(myOrderListModel1)
            myOrderList.add(myOrderListModel2)
            myOrderList.add(myOrderListModel3)
            myOrderList.add(myOrderListModel4)
            myOrderList.add(myOrderListModel5)
            myOrderList.add(myOrderListModel6)
            myOrderList.add(myOrderListModel7)
            myOrderList.add(myOrderListModel8)
            myOrderList.add(myOrderListModel9)
            myOrderList.add(myOrderListModel10)
            myOrderList.add(myOrderListModel11)
            myOrderList.add(myOrderListModel12)
            myOrderList.add(myOrderListModel13)
            myOrderList.add(myOrderListModel14)
            myOrderList.add(myOrderListModel15)
            myOrderList.add(myOrderListModel16)
            myOrderList.add(myOrderListModel17)
            myOrderList.add(myOrderListModel18)
            myOrderList.add(myOrderListModel19)
            myOrderList.add(myOrderListModel20)
            myOrderList.add(myOrderListModel21)
            myOrderList.add(myOrderListModel22)
            myOrderList.add(myOrderListModel23)
            myOrderList.add(myOrderListModel24)

            return myOrderList
        }
    }
}