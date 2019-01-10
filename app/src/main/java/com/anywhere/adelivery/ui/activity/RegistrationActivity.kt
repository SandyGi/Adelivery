package com.anywhere.adelivery.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View.GONE
import android.view.View.VISIBLE
import com.anywhere.adelivery.R
import com.anywhere.adelivery.ui.fragment.ConfirmationFragment
import com.anywhere.adelivery.ui.fragment.MyDetailFragment
import com.anywhere.adelivery.ui.fragment.MyOrderFragment
import com.anywhere.adelivery.ui.fragment.ScheduleDeliveryFragment
import kotlinx.android.synthetic.main.app_header_layout.*

class RegistrationActivity : AppCompatActivity() {

    val SUBMIT_FRAGMENT = 100
    val CONFIRMATION_FRAGMENT = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
//        setSupportActionBar(toolbar)
        displaySelectedScreen(R.id.nav_my_detail)
    }

    fun displaySelectedScreen(position: Int) {
        // update the main content by replacing fragments
        val fragment: Fragment = when (position) {
            R.id.nav_my_detail -> MyDetailFragment()
            R.id.nav_my_order -> MyOrderFragment()
            SUBMIT_FRAGMENT -> ScheduleDeliveryFragment()
            CONFIRMATION_FRAGMENT -> ConfirmationFragment()
            else -> MyDetailFragment()

        }
        if (position != R.id.nav_my_detail) txtMobileNumber.visibility = VISIBLE else this.txtMobileNumber.visibility =
                GONE

        val fragmentManager = supportFragmentManager // For AppCompat use getSupportFragmentManager

        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}
