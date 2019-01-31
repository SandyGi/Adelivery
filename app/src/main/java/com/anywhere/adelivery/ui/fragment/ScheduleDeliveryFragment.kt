package com.anywhere.adelivery.ui.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
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
import com.anywhere.adelivery.listener.ChangeFragmentListener
import com.anywhere.adelivery.ui.activity.CONFIRMATION_FRAGMENT
import com.anywhere.adelivery.ui.activity.MapsActivity
import com.anywhere.adelivery.utils.Constants
import com.anywhere.adelivery.utils.ORDER_ID
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

    private var mView: View? = null

    private val ADDRESS_REQUEST = 11
    private lateinit var changeFragmentListener: ChangeFragmentListener
    private var mSrcLatitude: Double? = null
    private var mSrcLongitude: Double? = null
    private var mDestLatitude: Double? = null
    private var mDestLongitude: Double? = null
    private var mTotalAmount = 0


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            changeFragmentListener = context as ChangeFragmentListener
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_schedule_delivery, container, false)
        this.mView = view
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
        view.iconPickUpLocation.setOnClickListener {
            var intent = Intent(activity, MapsActivity::class.java)
            startActivityForResult(intent, ADDRESS_REQUEST)
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADDRESS_REQUEST && resultCode == Activity.RESULT_OK) {

            mView!!.tiEdtPickUpLocation.setText(data!!.getStringExtra(Constants.EXTRA_PICK_UP_ADDRESS))
            mView!!.tiEdtDropLocation.setText(data.getStringExtra(Constants.EXTRA_DROP_ADDRESS))
            mTotalAmount = (data.getIntExtra(Constants.EXTRA_DISTANCE, 0)) * 4
            mSrcLatitude = data.getDoubleExtra(Constants.EXTRA_PICKUP_LAT, 0.toDouble())
            mSrcLongitude = data.getDoubleExtra(Constants.EXTRA_PICKUP_LONG, 0.toDouble())
            mDestLatitude = data.getDoubleExtra(Constants.EXTRA_DROP_LAT, 0.toDouble())
            mDestLongitude = data.getDoubleExtra(Constants.EXTRA_DROP_LONG, 0.toDouble())
        }



    }

    private fun getProductDetail(view: View): DeliveryRequest {

        var pickUpLocation = PickUpLocation(view.tiEdtPickUpLocation.text.toString(), "$mSrcLatitude", "$mSrcLongitude")
        var dropLocation = DropLocation(view.tiEdtDropLocation.text.toString(), "$mDestLatitude", "$mDestLongitude")
        var productDelivery = ProductDelivery(
            view.tiAlternateMobileNo.text.toString(),
            view.tiDetailOfDelivery.text.toString(),
            "",
            view.tiDeliveryDate.text.toString(),
            "COD",
            "$mTotalAmount",
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
                changeFragmentListener.onChangeFragmentListener(CONFIRMATION_FRAGMENT, bundle)
            } else {
                Toast.makeText(activity, "Server Side Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
