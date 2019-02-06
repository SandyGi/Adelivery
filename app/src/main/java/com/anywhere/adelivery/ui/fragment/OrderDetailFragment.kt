package com.anywhere.adelivery.ui.fragment

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.anywhere.adelivery.AdeliveryApplication
import com.anywhere.adelivery.R
import com.anywhere.adelivery.data.model.entity.Status
import com.anywhere.adelivery.data.request.CancelOrderRequest
import com.anywhere.adelivery.utils.CommonMethod
import com.anywhere.adelivery.utils.ORDER_ID
import com.anywhere.adelivery.viewmodel.CancelOrderViewModel
import com.anywhere.adelivery.viewmodel.OrderDeliveredViewModel
import com.anywhere.adelivery.viewmodel.OrderDetailViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_order_detail.view.*
import javax.inject.Inject

class OrderDetailFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var orderDetailViewModel: OrderDetailViewModel
    private lateinit var cancelOrderViewModel: CancelOrderViewModel
    private lateinit var orderDeliveredViewModel: OrderDeliveredViewModel
    private lateinit var uId: String
    private var dialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order_detail, container, false)
        orderDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(OrderDetailViewModel::class.java)
        cancelOrderViewModel = ViewModelProviders.of(this, viewModelFactory).get(CancelOrderViewModel::class.java)
        orderDeliveredViewModel = ViewModelProviders.of(this, viewModelFactory).get(OrderDeliveredViewModel::class.java)

        view.imgLogo.visibility =View.GONE
        view.txtMobileNumber.text = AdeliveryApplication.prefHelper!!.userId
        observeOrderDetailStatus(view)
        observeCancelOrderStatus(view)
        observeOrderDeliveredStatus(view)
        dialog = ProgressDialog(activity, R.style.MyTheme)
        dialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
//        dialog!!.setTitle("Loading")
        dialog!!.setMessage("Loading. Please wait...")
        dialog!!.setIndeterminate(true)
        dialog!!.setCanceledOnTouchOutside(false)

        dialog!!.show()
        orderDetailViewModel.getOrderDetail(arguments!!.getString(ORDER_ID))
        view.btnCancel.setOnClickListener {
            dialog!!.show()
            val cancelOrderRequest =
                CancelOrderRequest(arguments!!.getString(ORDER_ID), AdeliveryApplication.prefHelper!!.userId, uId)
            cancelOrderViewModel.createdUserDetail(cancelOrderRequest)
        }

        view.btnOrderDelivered.setOnClickListener {
            dialog!!.show()
            val cancelOrderRequest =
                CancelOrderRequest(arguments!!.getString(ORDER_ID), AdeliveryApplication.prefHelper!!.userId, uId)
            orderDeliveredViewModel.orderDelivered(cancelOrderRequest)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.title = activity!!.getString(R.string.str_order_detail)
    }

    private fun observeOrderDetailStatus(view: View) {
        orderDetailViewModel.response.observe(this, Observer { response ->

            if (response != null && response.status == Status.SUCCESS) {

                var orderDetailData = response.data!!.data[0]
                view.txtPickUpAddress.text = orderDetailData.pickupLocation
                view.txtDropAddress.text = orderDetailData.dropLocation

                view.txtDeliveryDate.text = orderDetailData.delivery_exp_date
//                        changeDateFormat(orderDetailData.delivery_exp_date, "yyyy-MM-dd", "dd/MM/yyyy")

                view.txtAmountToPay.text = orderDetailData.Payment_amt
                uId = orderDetailData.uniqueCode
                if (orderDetailData.orderStatus.equals("Cancel")) {
                    view.btnCancel.isEnabled = false
                    view.btnOrderDelivered.isEnabled = false
                }
                if (orderDetailData.orderStatus.equals("Delivered")) {
                    view.btnOrderDelivered.isEnabled = false
                    view.btnCancel.isEnabled = false
                }

            } else {
                Toast.makeText(activity, response!!.data!!.responseMessage, Toast.LENGTH_SHORT).show()
            }
            dialog!!.dismiss()
        })
    }

    private fun observeCancelOrderStatus(view: View) {
        cancelOrderViewModel.response.observe(this, Observer { response ->

            if (response != null && response.status == Status.SUCCESS) {
                if (response.data!!.data.equals("Done")) {
                    view.btnCancel.isEnabled = false
                    view.btnOrderDelivered.isEnabled = false
                    CommonMethod.showCustomToast(activity!!.applicationContext, response.data.responseMessage)
                }
            } else {
                CommonMethod.showCustomToast(activity!!.applicationContext, response!!.data!!.responseMessage)
            }
            dialog!!.dismiss()
        })
    }

    private fun observeOrderDeliveredStatus(view: View) {
        orderDeliveredViewModel.response.observe(this, Observer { response ->

            if (response != null && response.status == Status.SUCCESS) {
                if (response.data!!.data.equals("Done")) {
                    view.btnOrderDelivered.isEnabled = false
                    view.btnCancel.isEnabled = false
                    CommonMethod.showCustomToast(activity!!.applicationContext, response.data.responseMessage)
                }
            } else {
                CommonMethod.showCustomToast(activity!!.applicationContext, response!!.data!!.responseMessage)
            }
            dialog!!.dismiss()
        })
    }
}
