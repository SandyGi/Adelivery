package com.anywhere.adelivery.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anywhere.adelivery.R
import com.anywhere.adelivery.data.model.MyOrderListModel
import com.anywhere.adelivery.ui.activity.HomeActivity
import com.anywhere.adelivery.ui.adapter.BaseAdapter
import com.anywhere.adelivery.utils.DummyData
import kotlinx.android.synthetic.main.fragment_my_order.*
import kotlinx.android.synthetic.main.my_order_list.view.*

class MyOrderFragment : Fragment() {

    private var homeActivity = HomeActivity()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.homeActivity = context as HomeActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_order, container, false)
        fabScheduleDelivery.setOnClickListener{

        }
        setOrderList(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.title = activity!!.getString(R.string.str_my_order)
    }

    private fun setOrderList(view: View) {
        with(view.findViewById<RecyclerView>(R.id.rvOrderList)) {
            val linearLayoutManager = LinearLayoutManager(activity)
            layoutManager = linearLayoutManager
            itemAnimator = DefaultItemAnimator()

            adapter = object : BaseAdapter<MyOrderListModel, View>(activity!!, DummyData.dummyOrderList()) {
                override val layoutResId: Int
                    get() = R.layout.my_order_list

                override fun onBindData(model: MyOrderListModel, position: Int, dataBinding: View) {
                    dataBinding.txtSNO.text = "${model.orderId}."
                    dataBinding.txtOrderMobileNumber.text = model.orderName
                    dataBinding.txtOrderStatus.text = model.orderStatus
                }

                override fun onItemClick(model: MyOrderListModel, position: Int) {
                    homeActivity.displaySelectedScreen(homeActivity.ORDER_DETAIL_FRAGMENT)
                }

            }
        }
    }


}// Required empty public constructor
