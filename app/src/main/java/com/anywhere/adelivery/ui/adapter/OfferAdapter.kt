package com.anywhere.adelivery.ui.adapter

import android.content.Context
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anywhere.adelivery.R
import com.anywhere.adelivery.data.model.entity.Offers
import kotlinx.android.synthetic.main.offer_list_layout.view.*

class OfferAdapter(val context: Context, offerList: List<Offers>) : PagerAdapter() {

    private var offerList: List<Offers>
    private var layoutInflater: LayoutInflater? = null
    private var mContext: Context? = null

    init {
        this.mContext = context
        this.offerList = offerList
        layoutInflater = LayoutInflater.from(mContext)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, p1: Any): Boolean {
        return view.equals(p1)
    }

    override fun getCount(): Int {
        return offerList.size
    }

    override fun saveState(): Parcelable? {
        return null
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view = layoutInflater!!.inflate(R.layout.offer_list_layout, container, false)
        view.txtOfferDescription.text = offerList[position].description
        view.txtOfferName.text = offerList[position].offerName
//        Glide.with(mContext!!).load(offerList!![position].offerImage).into(view.imgOffer)
        container.addView(view, 0)
        return view
    }
}