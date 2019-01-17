package com.anywhere.adelivery.ui.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.anywhere.adelivery.R
import com.anywhere.adelivery.data.model.entity.Status
import com.anywhere.adelivery.utils.ORDER_ID
import com.anywhere.adelivery.viewmodel.OrderDetailViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class OrderDetailFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var orderDetailViewModel: OrderDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order_detail, container, false)
        orderDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(OrderDetailViewModel::class.java)
        observeOrderDetailStatus(view)
        orderDetailViewModel.getOrderDetail(arguments!!.getString(ORDER_ID))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.title = activity!!.getString(R.string.str_order_detail)
    }

    private fun observeOrderDetailStatus(view: View) {
        orderDetailViewModel.response.observe(this, Observer { response ->

            if (response != null && response.status == Status.SUCCESS) {

            } else {
                Toast.makeText(activity, "Server Side Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
