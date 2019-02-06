package com.anywhere.adelivery.ui.fragment

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.anywhere.adelivery.AdeliveryApplication
import com.anywhere.adelivery.R
import com.anywhere.adelivery.data.model.entity.Order
import com.anywhere.adelivery.data.model.entity.Status
import com.anywhere.adelivery.ui.activity.HomeActivity
import com.anywhere.adelivery.ui.activity.ORDER_DETAIL_FRAGMENT
import com.anywhere.adelivery.ui.activity.SCHEDULE_DELIVERY_FRAGMENT
import com.anywhere.adelivery.ui.adapter.BaseAdapter
import com.anywhere.adelivery.utils.ORDER_ID
import com.anywhere.adelivery.viewmodel.OrderListViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_my_order.view.*
import kotlinx.android.synthetic.main.my_order_list.view.*
import java.util.*
import javax.inject.Inject

class MyOrderFragment : DaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var orderListViewModel: OrderListViewModel

    private var homeActivity = HomeActivity()
    private var dialog: ProgressDialog? = null

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

        orderListViewModel = ViewModelProviders.of(this, viewModelFactory).get(OrderListViewModel::class.java)
        observeProductDeliveryStatus(view)
        view.imgLogo.visibility =View.GONE
        view.txtMobileNumber.text = AdeliveryApplication.prefHelper!!.userId
        dialog = ProgressDialog(activity, R.style.MyTheme)
        dialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
//        dialog!!.setTitle("Loading")
        dialog!!.setMessage("Loading. Please wait...")
        dialog!!.setIndeterminate(true)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.show()

        orderListViewModel.getOrderList(AdeliveryApplication.prefHelper!!.userId)
        view.fabScheduleDelivery.setOnClickListener {
            homeActivity.displaySelectedScreen(SCHEDULE_DELIVERY_FRAGMENT, null)
        }
//        setOrderList(view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.title = activity!!.getString(R.string.str_my_order)
    }

    private fun setOrderList(view: View, orderList: List<Order>) {
        with(view.findViewById<RecyclerView>(R.id.rvOrderList)) {
            val linearLayoutManager = LinearLayoutManager(activity)
            layoutManager = linearLayoutManager
            itemAnimator = DefaultItemAnimator()

            adapter = object : BaseAdapter<Order, View>(activity!!, orderList as ArrayList<Order>) {
                override val layoutResId: Int
                    get() = R.layout.my_order_list

                override fun onBindData(model: Order, position: Int, dataBinding: View) {
                    dataBinding.txtSNO.text = "${position + 1}. "
                    dataBinding.txtOrderMobileNumber.text = model.orderId
                    dataBinding.txtOrderStatus.text = model.status
                }

                override fun onItemClick(model: Order, position: Int) {
                    var bundle = Bundle()
                    bundle.putString(ORDER_ID, model.orderId)
                    homeActivity.displaySelectedScreen(ORDER_DETAIL_FRAGMENT, bundle)
                }
            }
        }
    }

    private fun observeProductDeliveryStatus(view: View) {
        orderListViewModel.response.observe(this, Observer { response ->
            if (response != null && response.status == Status.SUCCESS) {
                setOrderList(view, response.data!!.data.orderList)
            } else {
                Toast.makeText(activity, "Server Side Error", Toast.LENGTH_SHORT).show()
            }
            dialog!!.dismiss()
        })
    }


}
