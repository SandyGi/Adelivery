package com.anywhere.adelivery.ui.activity

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View.GONE
import android.view.View.VISIBLE
import com.anywhere.adelivery.AdeliveryApplication
import com.anywhere.adelivery.R
import com.anywhere.adelivery.bsimagepicker.BSImagePicker
import com.anywhere.adelivery.listener.ChangeFragmentListener
import com.anywhere.adelivery.ui.fragment.ConfirmationFragment
import com.anywhere.adelivery.ui.fragment.MyDetailFragment
import com.anywhere.adelivery.ui.fragment.MyOrderFragment
import com.anywhere.adelivery.ui.fragment.ScheduleDeliveryFragment
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.app_header_layout.*
const val SCHEDULE_DELIVERY_FRAGMENT = 100
const val CONFIRMATION_FRAGMENT = 101

class RegistrationActivity : DaggerAppCompatActivity(), ChangeFragmentListener, BSImagePicker.OnMultiImageSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        displaySelectedScreen(R.id.nav_my_detail, null)
//        txtMobileNumber.text = AdeliveryApplication.prefHelper!!.userId
    }

    override fun onChangeFragmentListener(type: Int, bundle: Bundle) {
        displaySelectedScreen(type, bundle)
    }

    fun displaySelectedScreen(position: Int, bundle: Bundle?) {
        // update the main content by replacing fragments
        val fragment: Fragment = when (position) {
            R.id.nav_my_detail -> MyDetailFragment()
            R.id.nav_my_order -> MyOrderFragment()
            SCHEDULE_DELIVERY_FRAGMENT -> ScheduleDeliveryFragment()
            CONFIRMATION_FRAGMENT -> ConfirmationFragment()
            else -> MyDetailFragment()

        }
//        if (position != R.id.nav_my_detail) txtMobileNumber.visibility = VISIBLE else this.txtMobileNumber.visibility =
//                GONE

        val fragmentManager = supportFragmentManager // For AppCompat use getSupportFragmentManager

        if (bundle!=null){
            fragment.arguments = bundle
        }
        if (fragment is ScheduleDeliveryFragment) {
            fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, ScheduleDeliveryFragment::class.java.name).commit()
        } else {
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
        }

    }

    override fun onMultiImageSelected(uriList: MutableList<Uri>, tag: String?) {
        val fragment = supportFragmentManager.findFragmentByTag(ScheduleDeliveryFragment::class.java.name) as ScheduleDeliveryFragment

        fragment.onMultiImageSelected(uriList, tag)
    }
}
