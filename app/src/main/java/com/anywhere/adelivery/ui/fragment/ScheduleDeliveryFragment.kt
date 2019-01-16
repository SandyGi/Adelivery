package com.anywhere.adelivery.ui.fragment

import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.anywhere.adelivery.AdeliveryApplication
import com.anywhere.adelivery.R
import com.anywhere.adelivery.data.model.entity.Status
import com.anywhere.adelivery.data.request.DeliveryRequest
import com.anywhere.adelivery.data.request.DropLocation
import com.anywhere.adelivery.data.request.PickUpLocation
import com.anywhere.adelivery.data.request.ProductDelivery
import com.anywhere.adelivery.ui.activity.RegistrationActivity
import com.anywhere.adelivery.utils.ORDER_ID
import com.anywhere.adelivery.utils.PreferencesManager
import com.anywhere.adelivery.viewmodel.ProductDetailViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_schedule_delivery.*
import kotlinx.android.synthetic.main.fragment_schedule_delivery.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class ScheduleDeliveryFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var productDetailViewModel: ProductDetailViewModel

    private var activityContext = RegistrationActivity()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.activityContext = context as RegistrationActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_schedule_delivery, container, false)
        productDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductDetailViewModel::class.java)
        observeProductDeliveryStatus()

        val calendar = Calendar.getInstance()
        val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy" //In which you need put here
            val sdf = SimpleDateFormat(myFormat, Locale.US)

            tiDeliveryDate.setText(sdf.format(calendar.time))
        }

        view.iconCalender.setOnClickListener {
            DatePickerDialog(
                activity, date,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        view.btnSubmit.setOnClickListener {
            productDetailViewModel.createdDeliveryDetail(getProductDetail(view))
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        activity!!.title = activity!!.getString(R.string.str_submit_detail)
    }

    private fun getProductDetail(view: View): DeliveryRequest {

        var pickUpLocation = PickUpLocation(view.tiEdtPickUpLocation.text.toString(), "18.510081", "73.807788")
        var dropLocation = DropLocation(view.tiEdtDropLocation.text.toString(), "18.515536", "73.928794")
        var productDelivery = ProductDelivery(
            view.tiAlternateMobileNo.text.toString(),
            view.tiDetailOfDelivery.text.toString(),
            "Pune",
            view.tiDeliveryDate.text.toString(),
            "COD",
            "400",
            pickUpLocation,
            dropLocation
        )
        return DeliveryRequest(
            AdeliveryApplication.prefHelper!!.userId,
            AdeliveryApplication.prefHelper!!.userId,
            productDelivery
        )
    }

    private fun observeProductDeliveryStatus() {
        productDetailViewModel.response.observe(this, Observer { response ->

            if (response != null && response.status == Status.SUCCESS) {
                var bundle = Bundle()
                bundle.putString(ORDER_ID, response.data!!.data.deliveryId)
                activityContext.displaySelectedScreen(activityContext.CONFIRMATION_FRAGMENT, bundle)
            } else {
                Toast.makeText(activityContext, "Server Side Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}// Required empty public constructor
