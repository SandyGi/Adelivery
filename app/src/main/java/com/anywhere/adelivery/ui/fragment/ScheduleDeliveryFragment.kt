package com.anywhere.adelivery.ui.fragment

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.anywhere.adelivery.AdeliveryApplication
import com.anywhere.adelivery.R
import com.anywhere.adelivery.bsimagepicker.BSImagePicker
import com.anywhere.adelivery.data.model.entity.Status
import com.anywhere.adelivery.data.request.DeliveryRequest
import com.anywhere.adelivery.data.request.DropLocation
import com.anywhere.adelivery.data.request.PickUpLocation
import com.anywhere.adelivery.data.request.ProductDelivery
import com.anywhere.adelivery.listener.ChangeFragmentListener
import com.anywhere.adelivery.listener.PermissionCallback
import com.anywhere.adelivery.ui.activity.CONFIRMATION_FRAGMENT
import com.anywhere.adelivery.ui.activity.MapsActivity
import com.anywhere.adelivery.ui.adapter.BaseAdapter
import com.anywhere.adelivery.utils.*
import com.anywhere.adelivery.viewmodel.ProductDetailViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_schedule_delivery.*
import kotlinx.android.synthetic.main.fragment_schedule_delivery.view.*
import kotlinx.android.synthetic.main.product_image_list.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class ScheduleDeliveryFragment : DaggerFragment(), PermissionCallback {

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
    private var mTotalAmount: Double? = null


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

        observeProductDeliveryStatus(view)
        PermissionChecker.init(activity!!)

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
        view.btnPickImage.setOnClickListener {
            val pickerDialog = BSImagePicker.Builder("com.anywhere.adelivery.fileprovider")
                .setMaximumDisplayingImages(Integer.MAX_VALUE)
                .isMultiSelect
                .setMinimumMultiSelectCount(1)
                .setMaximumMultiSelectCount(10)
                .build()
            pickerDialog.show(requireFragmentManager(), "picker")
        }
        view.btnSubmit.setOnClickListener {
            if (!view.tiEdtPickUpLocation.text.toString().equals("")) {
                if (!view.tiEdtDropLocation.text.toString().equals("")) {
                    if (mTotalAmount!!.toInt() >= 0) {
                        if (!view.tiDetailOfDelivery.text.toString().equals("")) {
                            if (!view.tiDeliveryDate.text.toString().equals("")) {
                                productDetailViewModel.createdDeliveryDetail(getProductDetail(view))
                            } else {
                                CommonMethod.showCustomToast(activity!!, "Please select deliverable date.")
                            }
                        } else {
                            CommonMethod.showCustomToast(activity!!, "Please enter product detail.")
                        }

                    } else {
                        CommonMethod.showCustomToast(activity!!, "Please enter valid pick drop location.")
                    }
                } else {
                    CommonMethod.showCustomToast(activity!!, "Please enter drop location")
                }
            } else {
                CommonMethod.showCustomToast(activity!!, "Please enter pick up location.")
            }

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

            mSrcLatitude = data.getDoubleExtra(Constants.EXTRA_PICKUP_LAT, 0.toDouble())
            mSrcLongitude = data.getDoubleExtra(Constants.EXTRA_PICKUP_LONG, 0.toDouble())
            mDestLatitude = data.getDoubleExtra(Constants.EXTRA_DROP_LAT, 0.toDouble())
            mDestLongitude = data.getDoubleExtra(Constants.EXTRA_DROP_LONG, 0.toDouble())
            mTotalAmount = CommonMethod.distance(mSrcLatitude!!, mSrcLongitude!!, mDestLatitude!!, mDestLongitude!!)
            Log.e("Total Amount", "${mTotalAmount!! * 8}")
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

    private fun observeProductDeliveryStatus(view: View) {
        productDetailViewModel.response.observe(this, Observer { response ->


            if (response != null && response.status == Status.SUCCESS) {
                val bundle = Bundle()
                bundle.putString(Constants.EXTRA_PICK_UP_ADDRESS, view.tiEdtPickUpLocation.text.toString())
                bundle.putString(Constants.EXTRA_DROP_ADDRESS, view.tiEdtDropLocation.text.toString())
                bundle.putString(ORDER_ID, response.data!!.data.deliveryId)
                changeFragmentListener.onChangeFragmentListener(CONFIRMATION_FRAGMENT, bundle)
            } else {
                Toast.makeText(activity, response!!.data!!.responseMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (!PermissionChecker.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) && !PermissionChecker.checkPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) && !PermissionChecker.checkPermission(Manifest.permission.CAMERA)
        ) run {
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
            )
        }

    }

    private fun requestPermissions(permissions: Array<String>) {

        if (PermissionChecker.getPermissionStatus(activity, permissions[0]) == PermissionChecker.DENIED) {
            CommonMethod.showCustomToast(activity!!, "Go to setting and enable permissions.")
        } else {
            CommonMethodJava.permissionChecker(activity!!, permissions, this@ScheduleDeliveryFragment)

        }
    }

    override fun permissionGranted() {
//        TODO("not implemented") To change body of created functions use File | Settings | File Templates.
    }

    override fun permissionRefused() {
        onResume()
    }

     fun onMultiImageSelected(uriList: MutableList<Uri>?, tag: String?) {
         for (i in 0 until uriList!!.size) {
            Log.e("Multi Image fragment", uriList[i].toString())
             setOrderList(view, uriList)
        }
    }
    private fun setOrderList(view: View?, productImgList: List<Uri>) {
        with(view!!.findViewById<RecyclerView>(R.id.rvProductImage)) {
            val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, true)
            layoutManager = linearLayoutManager
            itemAnimator = DefaultItemAnimator()

            adapter = object : BaseAdapter<Uri, View>(activity!!, productImgList as ArrayList<Uri>) {
                override val layoutResId: Int
                    get() = R.layout.product_image_list

                override fun onBindData(model: Uri, position: Int, dataBinding: View) {
                    dataBinding.imgProductImg.setImageURI(model)
                }

                override fun onItemClick(model: Uri, position: Int) {

                }
            }
        }
    }
}
